/* SingleBattleGroup.java
 * 1.8.2003 Tatu Tarvainen / Council 4
 *
 * One man battle group.
 */
package org.vermin.mudlib;

import java.util.*;
import static org.vermin.mudlib.BattleGroup.NodeType.*;
import org.vermin.mudlib.battle.AbstractBattleGroup;

public class LeafBattleGroup extends AbstractBattleGroup {

	protected Living owner;

	public LeafBattleGroup() {}

	public LeafBattleGroup(Living owner) {
		this.owner = owner;
	}

	public BattleGroup.NodeType getType() {
		return LEAF;
	}

	public void doBattle() {
		owner.doBattle();
	}

	public String getName() {
		return owner.getName()+ " (leaf)";
	}

	public Living getLeafValue() {
		return owner;
	}

	public Living getCombatant(Room room) {
		//World.log("OWNER: "+owner);
		//World.log("LeafBattleGroup("+owner.getName()+"): room: "+owner.getRoom()+", isDead: "+owner.isDead());

		//if(owner.getRoom() == null)
//			throw new IllegalArgumentException("Room must not be null");
		
		if(room.contains(owner) && !owner.isDead())
			return owner;
		else
			return null;
	}

	public boolean isCombatant(Room r, Living l) {
		boolean comb = !owner.isDead() && l==owner && r==owner.getRoom();
		// World.log(getName()+": isCombatant("+r+", "+l+") == "+comb);
		return comb;
	}

	public void addHostileGroup(BattleGroup g) {
		super.addHostileGroup(g);
		owner.doBattle();
	}

	public void onDeath(Living who) {
		if(who == owner)
			hostiles.clear();
	}

	public boolean contains(BattleGroup bg) {
		return false;
	}

	public boolean containsName(String name) {
		return false;
	}
	
	public Iterator children() {
		return new Iterator() {
				public boolean hasNext() {
					return false;
				}
				public Object next() {
					throw new NoSuchElementException();
				}
				public void remove() {}
			};
	}

	public void wrapChild(BattleGroup child, BattleGroup branch) {
		throw new UnsupportedOperationException("Leaf doesn't support wrapChild");
	}
	public void wrapAll(BattleGroup branch) {
		throw new UnsupportedOperationException("Leaf doesn't support wrapAll");
	}
	public void unwrap(BattleGroup branch) {
		throw new UnsupportedOperationException("Leaf doesn't support unwrap");
	}

	public void addChild(BattleGroup child) {
		throw new UnsupportedOperationException("Leaf doesn't support addChild");
	}
	public void removeChild(BattleGroup child) {
		throw new UnsupportedOperationException("Leaf doesn't support removeChild");
	}
}
