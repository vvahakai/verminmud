/*
 * Created on 14.8.2004
 *
 */
package org.vermin.mudlib;

import java.util.*;

import static org.vermin.mudlib.BattleGroup.NodeType.*;
import org.vermin.mudlib.battle.AbstractBattleGroup;

/**
 * A generic branch battlegroup which may contain
 * other branches and leaves as children.
 */
public class BranchBattleGroup extends AbstractBattleGroup {

	protected ArrayList<BattleGroup> children = new ArrayList<BattleGroup>();

	
	public BattleGroup.NodeType getType() {
		return BRANCH;
	}

	protected Random r = new Random();

	public void doBattle() {
		for(BattleGroup bg : children)
			bg.doBattle();
	}

	public String getName() {
		return "Branch battle group ("+children.size()+" children).";
	}

	public Living getCombatant(Room room) {
		ArrayList<Living> al = new ArrayList<Living>();
		
//		World.log("Finding combatant from BranchBattleGroup...");

		for(BattleGroup bg : children) {
			Living combatant = bg.getCombatant(room);
			if(combatant != null)
				al.add(combatant);
		}

		if(al.size() == 0) {
			return null;
		}

//		World.log("Found.... combatants: " +al.size()+ ", children: "+children.size()+".");

		return al.get(r.nextInt(al.size()));
	}

	public boolean isCombatant(Room r, Living l) {
		for(BattleGroup bg : children) {
			if(bg.isCombatant(r, l))
				return true;
		}
		return false;
	}

	public void onDeath(Living who) {
		// if this is the personal battle group of the living who died,
		// clear the hostile set.
		if(who.getPersonalBattleGroup() == this)
			hostiles.clear();

		for(int i=0;i<children.size();i++) {
			children.get(i).onDeath(who);
		}

	}	
	
	public void wrapChild(BattleGroup child, BattleGroup branch) {

		if(branch.getType() != BRANCH)
			World.log(" WARNING: wrapping with non-branch...");
			//throw new IllegalArgumentException("Wrapping group is not a branch.");

		int found = -1;
		for(int i=0; i<children.size(); i++) {
			if(children.get(i) == child) {
				found = i;
				break;
			}
		}
		if(found == -1)
			throw new IllegalStateException("Unable to wrap child: no such child.");

		children.set(found, branch);
		branch.parentChanged(this);
		branch.addChild(child);
	}


	public void wrapAll(BattleGroup branch) {
		if(branch.getType() != BRANCH)
			throw new IllegalArgumentException("Wrapping group is not a branch.");

		for(BattleGroup bg : children) {
			branch.addChild(bg);
		}
		children.clear();
		addChild(branch);
	}

	public void unwrap(BattleGroup branch) {
		
		if(branch.getType() != BRANCH)
			World.log(" WARNING: unwrapping with non-branch...");
		//throw new IllegalArgumentException("Wrapping group is not a branch.");

		int found = -1;
		for(int i=0; i<children.size(); i++) {
			if(children.get(i) == branch) {
				found = i;
				break;
			}
		}
		if(found == -1)
			throw new IllegalStateException("Unable to unwrap branch: no such branch.");
		
		children.remove(branch);
		Iterator it = branch.children();
		while(it.hasNext()) {
			BattleGroup child = (BattleGroup) it.next();
			addChild(child);
		}
	}

	public void addChild(BattleGroup child) {
		children.add(child);
		child.parentChanged(this);
	}

	public void removeChild(BattleGroup child) {
		children.remove(child);
	}

	public boolean contains(BattleGroup child) {
		for(BattleGroup bg : children) {
			if(bg == child || bg.contains(child))
				return true;
		}
		return false;
	}

	public boolean containsName(String name) {
		for(BattleGroup bg : children) {
			if(bg.getName().equals(name) || bg.containsName(name))
				return true;
		}
		return false;
	}

	public Iterator<BattleGroup> children() {
		return children.iterator();
	}
}
