/* BattleGroup.java
 * 1.8.2003 Tatu Tarvainen / Council 4
 *
 * Handles group fighting.
 */

package org.vermin.mudlib;

import java.util.*;

/**
 * Composite tree structure for battle groups.
 */
public interface BattleGroup {

	public enum NodeType { ROOT, BRANCH, LEAF };

	public NodeType getType();

	public String getName();

	/* These methods are meant to be called by a member
	 * of this battle group to find an opponent
	 * (from any hostile group)
	 */
	public Living getOpponent(Room room, String name);
	public Living getOpponent(Room room);

	/**
	 * Check if this BattleGroup is hostile with the
	 * given creatures BattleGroup.
	 */
	public boolean isOpponent(Living l);

	/* These methods are called by other BattleGroups
	 * to find opponents.
	 */
	public Living getCombatant(Room room);

	/* ADD THIS:
		getCombatant(room, preferred combatant);
	*/

	/**
	 * Mark the given group as hostile to this group.
	 */
	public void addHostileGroup(BattleGroup bg);

	/**
	 * Returns an unmodifiable collection of the
	 * combatants currently in the given room.
	 */
	//public Collection<Living> getCombatants(Room r);
	
	/**
	 * Returns an unmodifiable collection of the
	 * groups that are hostile to this group.
	 */ 
	public Collection<BattleGroup> hostileGroups();
	
	/**
	 * Notify all interested parties that battle
	 * needs to be done. Should be called from 
	 * addHostileGroup as necessary.
	 * All branches should call their children in order
	 * for the notification to reach actual <code>Living</code>s.
	 */
	public void doBattle();

	/**
	 * Get the leaf value, which is a Living.
	 * Non leaves must return null.
	 */
	public Living getLeafValue();

	/**
	 * Check if a living is combatant in the given room (eg. in the first row).
	 *
	 * @param r the room to check
	 * @param l the living to check
	 * @return true if the living is combatant, false otherwise
	 */
	public boolean isCombatant(Room r, Living l);

	/**
	 * Remove the battlegroup from the set of hostile groups.
	 *
	 * @param bg the hostile group to remove
	 */
	public void removeHostileGroup(BattleGroup bg);

	/**
	 * Notify that a member has died.
	 *
	 * @param who the member who died
	 */
	public void onDeath(Living who);

	/**
	 * Clear all hostile groups recursively.
	 */
	public void clearHostiles();
	

	/**
	 * Add a child group to this node.
	 */
	public void addChild(BattleGroup child);

	/**
	 * Remove a child group.
	 */
	public void removeChild(BattleGroup child);

	/**
	 * Wrap a child with the given branch.
	 * Wrapping is done by first removing the
	 * child node and adding the wrapping branch.
	 * The removed child is then added as a child to the
	 * wrapping branch.
	 *
	 * If the wrapping battlegroup is not a branch,
	 * IllegalArgumentException will be thrown.
	 */
	public void wrapChild(BattleGroup child, BattleGroup branch);

	/**
	 * Wrap all children with the given branch.
	 * Wrapping is done by first removing the
	 * child nodes and adding the wrapping branch.
	 * The removed children are then added as children to the
	 * wrapping branch.
	 *
	 * If the wrapping battlegroup is not a branch,
	 * IllegalArgumentException will be thrown.
	 */
	public void wrapAll(BattleGroup branch);

	/**
	 * Remove the given wrapping branch.
	 * The wrapping branch is removed from this branch
	 * and all children of the removed branch will be 
	 * added as children to this branch.
	 */
	public void unwrap(BattleGroup branch);

	/**
	 * Check if the subtree starting from this node
	 * contains the given group.
	 */
	public boolean contains(BattleGroup group);

	/**
	 * Check if the subtree starting from this node
	 * contains a group a by the given name.
	 */
	public boolean containsName(String name);

	/**
	 * Returns an iterator of all direct children of this branch.
	 */
	public Iterator<BattleGroup> children();

	/**
	 * Notify this Battlegroup that it's parent has changed
	 */
	public void parentChanged(BattleGroup group);

	/**
	 * Recursively search for the parent battlegroup of the given battlegroup.
	 *
	 * @param child the battlegroup to search for
	 * @return the parent of the given battlegroup
	 */
	public BattleGroup searchParent(BattleGroup child);
}
