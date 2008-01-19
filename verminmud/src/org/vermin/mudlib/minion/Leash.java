package org.vermin.mudlib.minion;

import java.util.HashSet;

import org.vermin.mudlib.*;

public class Leash extends DefaultItemImpl {

	protected static HashSet<DefaultMinionImpl> minions = new HashSet<DefaultMinionImpl>();
	
	public Leash() {
		name = "_minion_leash";			
	}
	
	public boolean isVisible() {
		return false;
	}

	public boolean tryDrop(Living who, MObject where) {
		return false;
	}
	
	public boolean tryTake(Living who) {
		return false;
	}
	
	public void addMinion(DefaultMinionImpl minion) {
		minions.add(minion);
	}
	
	public void removeMinion(Minion minion) {
		minions.remove(minion);
	}

	public void followMaster(Exit e, Room oldRoom) {
		for(Minion minion : minions) {
			if(minion.getParent() == oldRoom && minion.isFollowing())
				minion.move(e);
		}
	}
	
	public boolean follow(String target) {
		boolean found = false;
		for(Minion minion : minions) {
			if(minion.isAlias(target) && minion.getParent() == this.getParent().getParent()) {
				minion.setFollowing(true);
				found = true;
			}
		}
		return found;		
	}
	
	public boolean unfollow(String target) {
		boolean found = false;
		for(Minion minion : minions) {
			if(minion.isAlias(target) && minion.getParent() == this.getParent().getParent()) {
				minion.setFollowing(false);
				found = true;
			}
		}
		return found;		
	}	
	
    public boolean order(String target, String command, String action) {
		boolean found = false;
		for(Minion minion : minions) {
			if(minion.isAlias(target)) {
				minion.performCommand(command, action);
				found = true;
			}
		}
		return found;
    }

	public static int getMinionSize() {
		return minions.size();
	}
	
	public boolean minions(Player actor) {
		if(minions.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("Your current minions ("+minions.size()+")\n");
			for(Minion minion : minions) {
				sb.append(minion.getDescription()+"\n");
			}
			actor.notice(sb.toString());
			return true;
		}
		else actor.notice("You don't have any minions.");
		return false;
	}

	public boolean name(Living target, String name) {
		for(Minion minion : minions) {
			if(minion == target) {
				minion.setNickname(name);
				return true;
			}
		}
		return false;	
	}
	
	public boolean moveToFront(Living target) {
		for(Minion minion : minions) {
			if(minion == target) {
				minion.moveToFront();
				return true;
			}
		}
		return false;	
	}
	
	public boolean listCommands(Living target) {
		for(Minion minion : minions) {
			if(minion == target) {
				minion.listCommands();
				return true;
			}
		}
		return false;	
	}	
	
	public void start() {
		for(Minion minion : minions) {
			minion.start();
		}
	}
	
}
