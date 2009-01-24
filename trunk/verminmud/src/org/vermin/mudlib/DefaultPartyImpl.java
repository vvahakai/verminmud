package org.vermin.mudlib;

import java.util.*;

import static org.vermin.mudlib.BattleGroup.NodeType.*;
import org.vermin.mudlib.battle.AbstractBattleGroup;
import org.vermin.util.Print;
import org.vermin.util.RingBuffer;

public class DefaultPartyImpl implements Party {

	private static int[][] canonicalEntryPositions = new int[][] {
		{ 0, 0 },
		{ 0, 1 },
		{ 1, 0 },
		{ 2, 0 },
		{ 1, 1 },
		{ 2, 0 },
		{ 1, 2 },
		{ 2, 1 },
		{ 2, 2 }
	};
		
	private RingBuffer<String> messages = new RingBuffer<String>(25);
		
	// Name of the party	
	private String name;

	// Party members
	private Map<Living,PartyMembership> members = Collections.synchronizedMap(new HashMap<Living, PartyMembership>());

	// Current party leader
	private Living leader;

	// `Ringbuffer' of the last 20 kills
	private String[] kills = new String[20];
	private int lastKillIndex = 0;

	// Timestamp of when the party was created
	private long createdAt;

	// Total experience gained by this party
	private long totalExperience;

	private PartyBattleGroup group = new PartyBattleGroup();

	// positions[row][column]
	private BattleGroup[][] positions = new BattleGroup[16][16];

	private int memberNumber;

	public DefaultPartyImpl(String name) {
		this.name = name;
		createdAt = System.currentTimeMillis();
	}

	public BattleGroup[][] getPositions() {
		return (BattleGroup[][]) positions.clone();
	}

	public synchronized void setPositions(BattleGroup[][] newPositions) {
		positions = newPositions;
	}

	/**
	 * Returns the party members in the order of their joining.
	 *
	 * @return a list of party members
	 */
	public List<Living> membersInJoinOrder() {

		ArrayList<Living> al = new ArrayList<Living>(members());
		Collections.sort(al, new Comparator<Living>() {
				public int compare(Living m1, Living m2) {
					if(members.get(m1).number < members.get(m2).number)
						return -1;
					else 
						return 1;
				}});
		
		return al;
	}

	/**
	 * Returns the battle position of the given member.
	 * The first value in the array is the row and the
	 * second value is the column.
	 *
	 * If the member is not in this party, null is returned.
	 */
	public int[] getPosition(Living member) {

		for(int row=0; row<positions.length; row++) {
			for(int col=0; col<positions[row].length; col++) {
				if(positions[row][col] != null && containsLiving(member, positions[row][col]))
					return new int[] { row, col };
			}
		}
		return null;
	}

	private boolean containsLiving(Living check, BattleGroup bg) {
		if(bg.getLeafValue() == check)
			return true;

		Iterator children = bg.children();
		while(children.hasNext()) {
			BattleGroup child = (BattleGroup) children.next();
			if(containsLiving(check, child))
				return true;
		}
		return false;
	}

	/**
	 * Returns the current duration of the party.
	 *
	 * @returns party duration in milliseconds
	 */
	public long getDuration() {
		return System.currentTimeMillis() - createdAt;
	}

	/**
	 * Returns the amount of total experience gained by this party.
	 *
	 * @returns the amount of experience
	 */
	public long getTotalExperience() {
		return totalExperience;
	}

	/**
	 * Set the party leader.
	 *
	 * @param newLeader the new leader
	 */
	public synchronized void setLeader(Living newLeader) {
		World.log("TRYING TO SET LEADER: "+newLeader+" (member? "+isMember(newLeader)+")");
		if(!isMember(newLeader))
			throw new IllegalArgumentException("The new leader is not a member of the party.");

		this.leader = newLeader;
	}

	public int getTotalLevel() {
		int totalLevel = 0;
		Iterator<Living> it = members.keySet().iterator();
		while(it.hasNext()) {
			Living l = it.next();
			if(l instanceof Player)
				totalLevel += ((Player) l).getLevel();
		}
		return totalLevel;
	}
		
	/**
	 * Add experience when this party kills something.
	 * The experience is split between members according
	 * to their level.
	 * If the room parameter is not null, the experience
	 * is awarded only to those members, who are
	 * in the given room.
	 *
	 * @param exp the amount of experience gained
	 * @param room the room the experience was gained in or null
	 */
	public void addExperience(long exp, Room room) {

		totalExperience += exp;
		int totalLevel = getTotalLevel();
		for(Living l : members()) {
			if(l instanceof Player) {
				if(room == null || l.getRoom() == room) {
					Player p = (Player) l;
					long prevExp = p.getExperience();
					long expG = p.getLevel()*exp / totalLevel;
					p.addExperience(expG);
					if(p.getPreference("showexp").equals("on")) {
						long expDelta = p.getExperience() - prevExp;
						p.notice("The kill gains you "+expDelta+" experience.");
					}
					members.get(p).experienceGained += expG;
				}
			}
		}
	}

	/**
	 * Get the share of the given member as a value between 0.0 - 1.0.
	 */
	public double getShare(Living m) {
		if(m instanceof Player)
			return ((double) ((Player) m).getLevel()) / ((double) getTotalLevel());
		else
			return 0.0;
	}

	/**
	 * Returns the current party leader.
	 *
	 * @returns the leader
	 */
	public Living getLeader() {
		return leader;
	}

	/**
	 * Add a member to this party.
	 * Also sets the battle group of the new member.
	 *
	 * @param member the new member
	 */
	public synchronized void addMember(Living member) {
		PartyMembership pm = new PartyMembership(memberNumber++);
		pm.follow = false;
		members.put(member, pm);
		member.getBattleGroup().wrapChild(member.getPersonalBattleGroup(), group);
		member.setParty(this);
		
		for(int posIndex = 0; posIndex < canonicalEntryPositions.length; posIndex++) {
			int row = canonicalEntryPositions[posIndex][0];
			int col = canonicalEntryPositions[posIndex][1];
			if(positions[row][col] == null) {
				// Free position
				positions[row][col] = member.getPersonalBattleGroup();
				break;
			}
		}
	}

	/**
	 * Remove a member from this party.
	 * Also sets the removed member's battle group to
	 * a single battle group.
	 *
	 * @param member the member to remove
	 */
	public synchronized void removeMember(Living member) {
		
		Living nextInCommand = null;
		if(leader == member) {
			for (Living mbr : members()) {
				if (mbr != member) {
					nextInCommand = mbr;
					break;
				}
			}
		}
		
		members.remove(member);
		member.getBattleGroup().removeChild(group);
		int[] pos = getPosition(member);
		member.getBattleGroup().addChild(positions[pos[0]][pos[1]]);
		positions[pos[0]][pos[1]] = null;
		member.setParty(null);
		
		if(nextInCommand != null) {
			setLeader(nextInCommand);
			notice(Print.capitalize(nextInCommand.getName())+" is the new party leader.");
		}
	}
	
	/**
	 * Returns an unordered collection of the members.
	 *
	 * @returns a collection of party members
	 */
	public Collection<Living> members() {
		return members.keySet();
	}
	
	/**
	 * Returns the current amount of party members.
	 *
	 * @return the member count > 0
	 */
	public int getMemberCount() {
		return members.size();
	}

	/**
	 * Check if the someone is a member of this party.
	 *
	 * @param member the member candidate
	 * @returns true if candidate is a member, false otherwise
	 */
	public boolean isMember(Living member) {
		return members.containsKey(member);
	}

	/**
	 * Returns the current party name.
	 *
	 * @returns the party name
	 */
	public String getPartyName() {
		return name;
	}

	/**
	 * Set a new party name.
	 *
	 * @param name the new name
	 */
	public void setPartyName(String name) {
		this.name = name;
	}
	
	/**
	 * Toggle the `follow leader' flag for a given member.
	 *
	 * @param member the party member
	 */
	public void toggleFollow(Living member) {
		PartyMembership pm = members.get(member);
		if(pm == null)
			throw new IllegalArgumentException("The given member is not a member.");
		pm.follow = !pm.follow;
	}

	/**
	 * Returns the `follow leader' flag for a given member.
	 *
	 * @param member the party member
	 * @returns true if the member is following leader, false otherwise
	 */
	public boolean isFollowing(Living member) {
		PartyMembership pm = members.get(member);
		if(pm == null)
			throw new IllegalArgumentException("The given member is not a member.");
		return pm.follow;
	}

	/**
	 * Implements a battle group for the party.
	 * This battle group takes party combat places into account.
	 */
	public class PartyBattleGroup extends AbstractBattleGroup {
		public PartyBattleGroup() {}
		public String getName() {
			return "Party: "+getPartyName()+ " ("+members.size()+" members)";
		}

		public BattleGroup.NodeType getType() {
			return org.vermin.mudlib.BattleGroup.NodeType.BRANCH;
		}
		
		public void doBattle() {
			for(int row=0; row<16; row++)
				for(int col=0; col<16; col++)
					if(positions[row][col] != null)
						positions[row][col].doBattle();
		}

		public Iterator<BattleGroup> children() {
			ArrayList<BattleGroup> al = new ArrayList<BattleGroup>();

			for(int row=0; row<16;row++)
				for(int col=0; col<16;col++)
					if(positions[row][col] != null)
						al.add(positions[row][col]);
			return al.iterator();
		}

		public boolean contains(BattleGroup bg) {
			for(int row=0; row<16;row++)
				for(int col=0; col<16;col++)
					if(positions[row][col] != null) {
						if(positions[row][col] == bg || positions[row][col].contains(bg))
							return true;
					}
			return false;
		}

		public boolean containsName(String name) {
			for(int row=0; row<16;row++)
				for(int col=0; col<16;col++)
					if(positions[row][col] != null) {
						if(positions[row][col].getName().equals(name) || positions[row][col].containsName(name))
							return true;
					}
			return false;
		}

		private int[] getPosition(BattleGroup bg) {
			for(int row=0; row<16;row++)
				for(int col=0; col<16;col++)
					if(positions[row][col] != null) {
						if(positions[row][col] == bg || positions[row][col].contains(bg))
							return new int[] { row, col };
					}
			return null;
		}

		public synchronized void wrapChild(BattleGroup child, BattleGroup branch) {
			if(branch.getType() != BRANCH)
				throw new IllegalArgumentException("Wrapping group is not a branch.");

			int[] pos = getPosition(child);
			if(pos == null)
				throw new IllegalStateException("Unable to wrap child: no such child.");

			positions[pos[0]][pos[1]] = branch;
			branch.addChild(child);
			branch.parentChanged(this);
		}

		public void wrapAll(BattleGroup branch) {
			throw new UnsupportedOperationException("Party wrapAll is not supported, sorry.");
			/* possible implementation model:
			 * use a field to indicate that allwrap is in effect, but don't
			 * change positions array.
			 * all methods should then check if allwrap is in effect and delegate to that
			 * where applicable.
			 */
		}

		public void removeChild(BattleGroup child) {
			throw new UnsupportedOperationException("Party removeChild is not supported, sorry.");
		}
		public void addChild(BattleGroup child) {
			child.parentChanged(this);
		}

		public synchronized void unwrap(BattleGroup branch) {
			
			int[] pos = getPosition(branch);

			ArrayList<BattleGroup> al = new ArrayList<BattleGroup>();
			Iterator it = branch.children();
			while(it.hasNext()) al.add((BattleGroup) it.next());

			if(al.size() > 1) {
				BranchBattleGroup bg = new BranchBattleGroup();
				for(BattleGroup b : al) bg.addChild(b);
				positions[pos[0]][pos[1]] = bg;
			} else {
				positions[pos[0]][pos[1]] = al.get(0);
			}

		}
		
		/* These methods are called by other BattleGroups
		 * to find opponents.
		 */
		public Living getCombatant(Room room) {
			ArrayList<Living> al = new ArrayList<Living>();

			for(int col=0; col<16; col++) {
				for(int row=0; row<16; row++) {
					if(positions[row][col] != null) {
						Living combatant = positions[row][col].getCombatant(room);
						if(combatant != null) {
							al.add(combatant);
							break; 
						}
					}
				}
			}
			
			if(al.isEmpty())
				return null;

			return al.get(r.nextInt(al.size()));
		}

		/* Check if a living is combatant (eg. in the first row) */
		public boolean isCombatant(Room r, Living l) {

			// for all first row occupants, check
			// their personal battle groups for the given living

			for(int col=0; col<16; col++) {
				// find the first living in the row
				for(int row=0; row<16; row++) {
					if(positions[row][col] != null) {
						if(positions[row][col].isCombatant(r, l))
							return true;
						
						if(positions[row][col].getCombatant(r) != null)
							break;
					}
				}
			}
			return false;
		}
	}

	/* Change party member's position */
	public synchronized void setPlace(Living actor, Living member, int row, int col) {
		
		if(member.getBattleGroup().getOpponent(member.getRoom()) != null) {
			actor.notice("You cannot change the place of a member who is in battle.");
			return;
		}
		
		for(Living mbr : members()) {
			if(positions[row][col] != null && positions[row][col].contains(mbr.getLeafBattleGroup())) {
				if(mbr.getBattleGroup().getOpponent(mbr.getRoom()) != null) {
					actor.notice("You cannot change the place of a member who is in battle.");
					return;
				}
			}
		}
		
		
		int maxRowCol = (int) (Math.sqrt(getMemberCount())+1.5); 
		
		if(maxRowCol > 15) maxRowCol = 15;
		
		if(row < 0 || row > maxRowCol) {
			actor.notice("The row must be between 1 and "+(maxRowCol+1)+".");
			return;
		}
		if(col < 0 || col > maxRowCol) {
			actor.notice("The column must be between 1 and "+(maxRowCol+1)+".");
			return;
		}
		int[] curPos = getPosition(member);
		BattleGroup actorGroup = positions[curPos[0]][curPos[1]];

		if(row==curPos[0] && col==curPos[1]) {
			actor.notice((member==actor?"You are":member.getName()+" is") +" already in that position.");
			return;
		}

		// if the place is occupied, swap places
		if(positions[row][col] != null) {
			BattleGroup occupant = positions[row][col];
			String occupantName = whoIsAt(occupant);
				
			positions[row][col] = actorGroup;
			positions[curPos[0]][curPos[1]] = occupant;
			notice(member.getName()+" changes places with "+occupantName+".");
		} else {
			// try to find the leftmost position on the row
			int c = col;
			while(c > 0 && (positions[row][c-1] == null || positions[row][c-1] == actorGroup))
				c--;
			positions[curPos[0]][curPos[1]] = null;
			positions[row][c] = actorGroup;

			// optimize the from and to rows to start from the leftmost position
			optimizeRow(row);
			optimizeRow(curPos[0]);

			int[] newPos = getPosition(member);
			notice(member.getName()+" moves to row "+newPos[0]+", column "+newPos[1]+".");
		}
	}

	private String whoIsAt(BattleGroup occupant) {
		String occupantName = "";
		for(Living mbr : members()) {
			if(occupant.contains(mbr.getLeafBattleGroup())) {
				occupantName = mbr.getName();
				break;
			}
		}
		return occupantName;
	}

	private void optimizeRow(int row) {
		// optimize the from and to rows to start from the leftmost position
		int empty = -1;
		for(int s=0; s<15; s++) {
			if(positions[row][s] == null)
				empty = s;
			else if(empty != -1) {
				positions[row][empty] = positions[row][s];
				positions[row][s] = null;
				notice(whoIsAt(positions[row][empty])+" moves to row "+row+", column "+empty+".");
				empty = s;
			}
		}
	}

	public void notice(String msg) {
		for(Living l : members()) l.notice(msg);
	}
	
	public void notice(String msg, Living excluded) {
		for(Living l : members()) {
			if(l != excluded) {
				l.notice(msg);
			}
		}
	}
	

	public void addKill(Living killed) {
		lastKillIndex++;
		kills[lastKillIndex%20] = killed.getDescription()+": "+Long.toString(killed.getExperienceWorth());
	}

	public ArrayList<String> lastKills() {
		ArrayList<String> al = new ArrayList<String>();
		int ind = lastKillIndex+1;
		for(int i=0; i<20; i++) {
			if(kills[ind%20] != null)
				al.add(kills[ind%20]);
			ind++;
		}
		return al;
	}

	/* Party membership information */
	public static class PartyMembership {
		public PartyMembership() {}
		public PartyMembership(int n) { number = n; }
		public boolean follow;
		public int number;
		public long experienceGained = 0;

	}

	public long getExperienceGained(Living member) {
		return members.get(member).experienceGained;
	}
	
	public Iterable<String> getLastMessages() {
		return messages;
	}
	public void say(Living actor, String what) {
		String msg = actor.getName() + " &B;[party]&;: " + what;
		messages.add(msg);
		notice(msg);
	}	
}
