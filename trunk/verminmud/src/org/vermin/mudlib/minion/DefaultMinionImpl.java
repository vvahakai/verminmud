/**
 * DefaultFamiliarImpl.java
 *
 */
package org.vermin.mudlib.minion;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;

import org.vermin.mudlib.BattleGroup;
import org.vermin.mudlib.Commander;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.OutputCapture;
import org.vermin.mudlib.Types;
import org.vermin.util.Print;


public class DefaultMinionImpl extends DefaultMonster implements Minion {

	protected Living master = null;
	protected boolean following = true;
	protected HashSet<String> commands;
	protected transient MinionBattleGroup gbg;
	protected String nickname = null;
	
	//TODO: implement minion experience 
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#setFollowing(boolean)
	 */
	public void setFollowing(boolean f) {
		following = f;
	}
	
	public String getDescription() {
		if(nickname != null)
			return description+" called "+nickname;
		else
			return description;
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#setNickname(java.lang.String)
	 */
	public void setNickname(String nickname) {
		if(nickname != null) {
			aliases.remove(nickname);
		}
		this.nickname = nickname;
		addAlias(nickname);
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#getNickname()
	 */
	public String getNickname() {
		return nickname;
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#getMaster()
	 */
	public Living getMaster() {
		return master;
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#isFollowing()
	 */
	public boolean isFollowing() {
		return following;
	}
	
	public BattleGroup getBattleGroup() {
		return master.getBattleGroup();
	}
	
	public BattleGroup getPersonalBattleGroup() {
		return gbg;
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#addCommand(java.lang.String)
	 */
	public void addCommand(String command) {
		commands.add(command);
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.minion.Minion#performCommand(java.lang.String, java.lang.String)
	 */
	public boolean performCommand(String command, String params) {
		for(String cmd : commands) {
			if(cmd.equals(command)) {
				if(Commander.getCommand(command) != null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(out);
					Commander.getCommand(command).action(OutputCapture.withOutputTo(this, ps), command+" "+params);
					if(nickname != null)
						master.notice(Print.capitalize(nickname)+": "+out.toString());
					else 
						master.notice("Minion: "+out.toString());
				}
			}
		}
		return false;
	}
	
	public void start() {
		// This will configure the minion to stand infront of the master
		// TODO: maybe we should implement minions able to stand beside or behind the master
		
		master.getParent().add(this);
		
		gbg = new MinionBattleGroup();
		gbg.setMinion(this);
		gbg.addChild(getLeafBattleGroup());

		BattleGroup foundBattleGroup = master.getLeafBattleGroup();

		// Finding the child battlegroup of personal group that contains this player's leaf battlegroup
		Iterator it = master.getPersonalBattleGroup().children();
		while(it.hasNext()) {
			BattleGroup bg = (BattleGroup) it.next();
			if(bg.contains(master.getLeafBattleGroup())) {
				foundBattleGroup = bg;
				break;
			}
		}

		if(foundBattleGroup != null) {
			master.getPersonalBattleGroup().wrapChild(foundBattleGroup, gbg);
		}
		super.start();
	}
	
	public void moveToFront() {
		synchronized(this.getParent()) {
			gbg.getParent().unwrap(gbg);
			BattleGroup foundBattleGroup = master.getLeafBattleGroup();
	
			// Finding the child battlegroup of personal group that contains this player's leaf battlegroup
			Iterator it = master.getPersonalBattleGroup().children();
			while(it.hasNext()) {
				BattleGroup bg = (BattleGroup) it.next();
				if(bg.contains(master.getLeafBattleGroup())) {
					foundBattleGroup = bg;
					break;
				}
			}
	
			if(foundBattleGroup != null) {
				master.getPersonalBattleGroup().wrapChild(foundBattleGroup, gbg);
			}
		}
	}
	
	public void listCommands() {
		StringBuffer sb = new StringBuffer();
		sb.append("Available commands:\n  follow\n  unfollow\n  front\n  list commands\n");
		for(String cmd : commands) {
			sb.append("  "+cmd+"\n");
		}
		master.notice(sb.toString());
	}
	
	public DefaultMinionImpl() {
		super();
	}
	
	public DefaultMinionImpl(Living master) {
		super();
		this.commands = new HashSet<String>();
		this.master = master;
		
		
		//this.rootBattleGroup.wrapAll(master.getPersonalBattleGroup());
		//master.getPersonalBattleGroup().addChild(this.getPersonalBattleGroup());
	}
	
	public void dies(Living vic, Living killer) {
		super.dies(vic, killer);
		if(vic == this) {
			gbg.getParent().unwrap(gbg);
			Leash l = (Leash) master.findByNameAndType("_minion_leash", Types.ITEM);
			if(l != null) {
				l.removeMinion(this);
			}
		}
	}

	
	
}
