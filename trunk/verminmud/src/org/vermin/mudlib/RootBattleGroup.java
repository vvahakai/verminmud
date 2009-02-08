
package org.vermin.mudlib;

import java.util.*;

import static org.vermin.mudlib.BattleGroup.NodeType.*;
import org.vermin.mudlib.battle.AbstractBattleGroup;
/**
 * Root battle group of a Living that may only contain one child
 * group, which is the personal battle group or a party battle group.
 */
public class RootBattleGroup extends AbstractBattleGroup {


	private BattleGroup child = null;

	private Living owner;

	public RootBattleGroup(Living owner) {
		this.owner = owner;
	}

	public BattleGroup.NodeType getType() {
		return ROOT;
	}
	
	public void addHostileGroup(BattleGroup bg) {

		if(bg == null ||
			bg.contains(owner.getPersonalBattleGroup()))
			return; // refuse to add a battlegroup the owner is a part of (eg. party)

		super.addHostileGroup(bg);
	}
	
	public void wrapAll(BattleGroup branch) {
		wrapChild(child, branch);
	}

	public void addChild(BattleGroup child) {
		if(this.child != null) {
			throw new IllegalStateException("Root battle group already has a child.");
		}
		this.child = child;
	}

	public void removeChild(BattleGroup child) {
		if(this.child != child)
			throw new IllegalStateException("Unable to remove child: no such child.");
		this.child = null;
	}

	public void wrapChild(BattleGroup child, BattleGroup branch) {
		if(this.child != child)
			throw new IllegalStateException("Unable to wrap child: no such child.");

		if(branch.getType() != BRANCH)
			throw new IllegalArgumentException("Wrapping group is not a branch.");

		branch.addChild(child);
		this.child = branch;
	}

	public void unwrap(BattleGroup branch) {
		
		if(branch.getType() != BRANCH)
			throw new IllegalArgumentException("Wrapping group is not a branch.");

		ArrayList<BattleGroup> al = new ArrayList<BattleGroup>();
		Iterator<BattleGroup> it = branch.children();
		while(it.hasNext()) al.add(it.next());

		if(al.size() > 1) {
			child = new BranchBattleGroup();
			for(BattleGroup bg : al)
				child.addChild(bg);
		} else {
			child = al.get(0);
		}
	}

	public boolean contains(BattleGroup ch) {
		return ch == child || child.contains(ch);
	}

	public boolean containsName(String name) {
		return child.getName().equals(name) || child.containsName(name);
	}

	public Iterator children() {
		final boolean[] fetched = new boolean[] { false };
		return new Iterator() {
				public boolean hasNext() {
					return !fetched[0];
				}
				public Object next() {
					if(fetched[0]) throw new NoSuchElementException();
					fetched[0] = true;
					return child;
				}
				public void remove() {}
			};
	}
				
	public String getName() {
		return owner.getName()+" (root)";
	}

	public Living getCombatant(Room room) {
		return child.getCombatant(room);
	}

	public boolean isCombatant(Room r, Living l) {
		return child.isCombatant(r, l);
	}

	public void onDeath(Living who) {
		child.onDeath(who);
	}
		
	public void doBattle() {
		child.doBattle();
	}


}
