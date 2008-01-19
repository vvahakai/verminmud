/**
 * All party commands.
 */
package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Print;
import org.vermin.wicca.Session;

import java.text.NumberFormat;
import java.util.*;

import org.vermin.driver.Driver;
import java.io.*;

import static org.vermin.util.Print.*;

public class PartyCommand extends RegexCommand {

	private static NumberFormat shareNumberFormat = NumberFormat.getInstance();
	static {
		shareNumberFormat.setMaximumFractionDigits(2);
		shareNumberFormat.setMinimumFractionDigits(2);
	}

	public static Party getPartyItem(Living actor) {
		return actor.getParty();
	}

	public String[] getDispatchConfiguration() {
		return new String[] {
				"party => usage(actor)",
				"party create (.+) => create(actor, 1)",
				"party say (.+) => say(actor, 1)",
				"party invite (.+) => invite(actor, living 1)",
				"party leave => leave(actor)",
				"party follow => toggleFollow(actor)",
				"party leader (.+) => setLeader(actor, living 1)",
				"party shares => showShares(actor)",
				"party kills => showKills(actor)",
				"party kick (.+) => kick(actor, living 1)",
				"party name (.+) => setName(actor, 1)",
				"party place => showPlaces(actor)",
				"party place (\\w+) (\\d+) (\\d+) => setPlace(actor, living 1, 2, 3)",
				"party place (\\d+) (\\d+) => setPlace(actor, 1, 2)",
				"party dice => dice(actor)", "party join => join(actor)",
				"party status => status(actor)",
				"party disband => disband(actor)",
				"party who (.+) => who(actor, player 1)",
				"party report => report(actor)"
		};
	}

	public void usage(Living actor) {
		actor.notice("See `help party' for a list of party commands.");
	}

	public void report(Living actor) {
		Party pi = actor.getParty();
		String notice = actor.getName()+" reports '"+Report.makeReportNotice(actor)+"'";
		for(Living l : pi.members()) {
			l.notice(notice);
		}
	}
	
	public void create(Player actor, String name) {
		Party pi = actor.getParty();
		if (pi != null) {
			actor.notice("You are already in a party.");
			return;
		}

		pi = new DefaultPartyImpl(name);
		actor.setParty(pi);

		pi.addMember(actor);
		pi.setLeader(actor);
		actor.notice("You have created a new party called '" + name + "'.");

	}

	
	public void say(Player actor, String what) {
		Party pi = actor.getParty();
		if (!checkParty(pi, actor))
			return;

		if(what.trim().equalsIgnoreCase("last")) {
			// show last msgs
			for(String msg : pi.getLastMessages())
				actor.notice(msg);
		} else {
			pi.say(actor, what);
		}
	}

	public void invite(Player actor, Living target) {

		if(target == null) {
			actor.notice("Invite who?");
			return;
		}
		
		if (target.getParty() != null) {
			actor.notice(target.getName() + " is already in a party.");
			return;
		}

		Party pi = actor.getParty();
		if (!checkLeader(pi, actor))
			return;

		if (pi.isMember(target)) {
			actor.notice(target.getName()
					+ " is already a member of your party.");
			return;
		}
		actor.notice("You invite " + target.getName() + " to your party.");
		PartyInvitation inv = new PartyInvitation(pi);
		Driver.getInstance().getTickService().addTick(inv, Tick.REGEN);
		target.add(inv);
		target.notice(actor.getName() + " has invited you to party '"
				+ pi.getPartyName() + "'.");
	}

	private static class PartyInvitation extends SpecialItem {
		public PartyInvitation(Party pi) {
			super("_party_invitation_" + pi.getPartyName());
			this.party = pi;
		}

		public Party party;

		public int ticks = 4;

		public boolean done = false;

		public boolean tick(short queue) {
			if (done)
				return false;

			ticks--;
			if (ticks < 0) {
				Living invitee = (Living) getParent();
				invitee.remove(this);
				invitee.notice("Your party invitation to '"
						+ party.getPartyName() + "' has expired.");
				return false;
			}
			return true;
		}
	}

	public void leave(Player actor) {
		Party pi = actor.getParty();
		if (!checkParty(pi, actor))
			return;

		actor.notice("You have left the party.");
		pi.removeMember(actor);
		
		pi.notice(actor.getName() + " has left the party.");

		
		
	}

	public void toggleFollow(Player actor) {
		Party pi = actor.getParty();
		if (!checkParty(pi, actor))
			return;

		pi.toggleFollow(actor);
		actor.notice("You " + (pi.isFollowing(actor) ? "start" : "stop")
				+ " following the party leader.");
	}

	public void setLeader(Player actor, Living newleader) {
		
		if(newleader == null) {
			actor.notice("Make who the leader?");
			return;
		}
			
		
		World.log("SET LEADER: "+newleader);
		Party pi = actor.getParty();
		if (!checkLeader(pi, actor))
			return;

		if (!pi.isMember(newleader)) {
			actor.notice(newleader.getName()
					+ " is not a member of your party.");
			return;
		}
		pi.setLeader(newleader);
	}

	public void showShares(Player actor) {
		Party pi = actor.getParty();
		double totalLevel = (double) pi.getTotalLevel();
		for (Living l : pi.members()) {
			if (l instanceof Player) {
				actor.notice(l.getName() + ": "
						+ shareNumberFormat.format((100.0 * pi.getShare(l)))
						+ "%");
			}
		}
	}

	public void showKills(Player actor) {
		if(actor.getParty() == null) {
			actor.notice("You are not in a party.");
			return;
		}
		ArrayList<String> al = actor.getParty().lastKills();
		actor.notice("Last " + al.size() + " kills:");
		for (String s : al)
			actor.notice(s);

	}

	public void kick(Player actor, Living memberToKick) {
		
		if(memberToKick == null) {
			actor.notice("Kick who?");
			return;
		}
		
		Party pi = actor.getParty();
		if (!checkLeader(pi, actor))
			return;

		if (!pi.isMember(memberToKick)) {
			actor.notice(memberToKick.getName()
					+ " is not a member of your party.");
			return;
		}
		pi.removeMember(memberToKick);
		memberToKick.notice("You have been kicked from the '"
				+ pi.getPartyName() + "' party.");
		String msg = memberToKick.getName() + " was kicked from the party.";
		for (Living member : pi.members()) {
			if (member != actor)
				member.notice(msg);
		}
		actor.notice("You have kicked " + memberToKick.getName()
				+ " from the party.");
	}

	public void setName(Player actor, String newname) {
		Party pi = actor.getParty();
		if (!checkLeader(pi, actor))
			return;

		pi.setPartyName(newname);
		String msg = "The party is now called '" + pi.getPartyName() + "'.";
		for (Living member : pi.members())
			member.notice(msg);
	}

	public void showPlaces(Player actor) {
		Party pi = actor.getParty();

		HashMap<String, Living> positions = new HashMap();

		int maxRow = 0, maxCol = 0;
		for (Living m : pi.members()) {
			int[] pos = pi.getPosition(m);

			maxRow = pos[0] > maxRow ? pos[0] : maxRow;
			maxCol = pos[1] > maxCol ? pos[1] : maxCol;

			positions.put(pos[0] + "," + pos[1], m);
		}

		Formatter fmt = new Formatter(new StringWriter());
		for (int row = 0; row < maxRow + 1; row++) {

			fmt.format("Row%d: ", row + 1);

			for (int col = 0; col < maxCol + 1; col++) {
				Living member = positions.get(row + "," + col);
				if (member == null)
					fmt.format("          ");
				else
					fmt.format("%8s  ", clamp(8, member.getName()));
			}
			fmt.format("\n");
		}
		actor.notice(fmt.toString());
	}

	public void setPlace(Player actor, int row, int col) {
		actor.getParty().setPlace(actor, actor, row, col);
	}

	public void setPlace(Player actor, Player member, int row, int column) {
		actor.getParty().setPlace(actor, member, row, column);
	}

	public void dice(Player actor) {
		Party pi = actor.getParty();
		if (!checkParty(pi, actor))
			return;

		Vector dices = new Vector();
		for (Living member : pi.members()) {
			dices.add(new Object[] { member.getName(),
					new Integer(Dice.random()) });
		}

		Collections.sort(dices, new Comparator() {
			public int compare(Object o1, Object o2) {
				int i1 = ((Integer) ((Object[]) o1)[1]).intValue();
				int i2 = ((Integer) ((Object[]) o2)[1]).intValue();
				return i1 < i2 ? -1 : (i1 > i2 ? 1 : 0);
			}
		});

		for (int i = 0; i < dices.size(); i++) {
			Object[] d = (Object[]) dices.get(i);

			String msg = "   " + d[0] + ": " + d[1];
			for (Living member : pi.members())
				member.notice(msg);
		}
	}

	public void join(Player actor) {
		Iterator en = actor.findByType(Types.TYPE_ITEM);
		PartyInvitation pi = null;

		while (en.hasNext()) {
			Item item = (Item) en.next();
			if (item instanceof PartyInvitation) {
				pi = (PartyInvitation) item;
				break;
			}
		}

		if (pi == null)
			actor.notice("You have no outstanding invitations.");
		else {
			if (actor.getParty() != null) {
				actor.notice("You are already in a party.");
			} else {
				pi.party.addMember(actor);
				actor.notice("You are now a member of '"
						+ pi.party.getPartyName() + "'.");
				
				pi.party.notice(Print.capitalize(actor.getName())+ " has joined the party.", actor);
			}
			actor.remove(pi);
			pi.done = true;
		}
	}

	public void status(Player actor) {

		Party pi = actor.getParty();
		if (pi == null) {
			actor.notice("You are not in a party.");
			return;
		}

		CharArrayWriter caw = new CharArrayWriter();
		PrintWriter ps = new PrintWriter(caw);

		long dur = pi.getDuration();
		long mins = dur / 60000;

		long rate = mins == 0 ? pi.getTotalExperience() : pi
				.getTotalExperience()
				/ mins;

		ps
				.printf(",-------------------.----------------.----------.------------.----------------.\n");
		ps
				.printf(
						"| %-17s | Time: %02d:%02d.%02d | Xp: %-4s | Rate: %-4s | Rate/mbr: %-4s |\n",
						clamp(17, pi.getPartyName()), // party name
						dur / (1000 * 60 * 60), (dur / (1000 * 60)) % 60,
						(dur - (mins * 60000)) / 1000, // party duration
						shortExp(pi.getTotalExperience()), shortExp(rate), // xp /
																			// min
						shortExp(rate / pi.getMemberCount()));

		ps
				.printf("+-------------------+-------+-------------+-------------+-------+-------------+\n");
		ps
				.printf("| POS | NAME        | FLAGS |     HP      |     SP      | LEVEL | EXPERIENCE  |\n");
		ps
				.printf("+-----+-------------+-------+-------------+-------------+-------+-------------+\n");

		for (Living m : pi.membersInJoinOrder()) {

			int[] pos = pi.getPosition(m);

			ps
					.printf(
							"| %d,%d | %-11s |  %c%c%c%c | %11s | %11s | %5s | %11d |\n",
							pos[0], pos[1], // pos
							clamp(11, m.getName()), m == pi.getLeader() ? 'L'
									: ' ', // leader?
							isIdle(m) ? 'I' : ' ', // idle?
							pi.isFollowing(m) ? 'F' : ' ', // following?
							m.isDead() ? 'D' : ' ', // dead?

							"(" + m.getHp() + "/" + m.getMaxHp() + ")", // hp/maxhp
							"(" + m.getSp() + "/" + m.getMaxSp() + ")", // sp/maxsp
							m instanceof Player ? "" + ((Player) m).getLevel()
									: "N/A", // level
							pi.getExperienceGained(m));

		}

		ps
				.printf("`-----`-------------`-------`-------------`-------------`-------`-------------'\n");
		actor.notice(caw.toString());

	}

	private String shortExp(long exp) {
		if (exp >= 1000000) {
			Formatter fmt = new Formatter(new StringWriter());
			double d = exp / 1000000.0;
			fmt.format("%.1fM", d);
			return fmt.toString();
		} else if (exp >= 10000) {
			double d = exp / 10000.0;
			int k = (int) Math.round(exp / 1000.0);
			return Integer.toString(k) + "K";
		} else {
			return Long.toString(exp);
		}
	}

	public boolean isIdle(Living l) {
		Session c = World.getSession(l.getName());
		if (c.getIdleTime() < 60000)
			return false;
		else
			return true;
	}

	public void disband(Player actor) {
		Party pi = actor.getParty();
		if (!checkLeader(pi, actor))
			return;

		actor.notice("You have disbanded the party.");

		String msg = actor.getName() + " has disbanded the party.";
		ArrayList<Living> al = new ArrayList();
		for (Living member : pi.members())
			al.add(member);

		for (Living member : al) {
			if (actor != member)
				member.notice(msg);
			pi.removeMember(member);
		}
	}

	public void who(Player actor, Player target) {
		if (target == null) {
			actor.notice("No such player in the game.");
		} else {
			actor.notice("Party information on " + target.getName() + ":");
			Party p = target.getParty();
			for (Living living : p.membersInJoinOrder()) {
				if (living instanceof Player) {
					Player pl = (Player) living;
					actor.notice("( " + pl.getLevel() + " )  "
							+ Print.capitalize(living.getName()));
				}
			}
		}
	}

	private boolean checkParty(Party pi, Living actor) {
		if (pi == null) {
			actor.notice("You are not in a party.");
			return false;
		}
		return true;
	}

	private boolean checkLeader(Party pi, Living actor) {
		if (!checkParty(pi, actor))
			return false;

		if (pi.getLeader() != actor) {
			actor.notice("You are not the party leader.");
			return false;
		}

		return true;
	}
}
