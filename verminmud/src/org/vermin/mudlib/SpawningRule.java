/*
 * Created on 19.2.2005
 */
package org.vermin.mudlib;

/**
 * @author Jaakko Pohjamo
 */
public interface SpawningRule {
	
	/**
	 * Gives this spawning rule a chance to
	 * spawn new monsters into a Room.
	 * 
	 * @param room  the Room to spawn into
	 */
	public void spawn(Room room);
	
	/**
	 * Notifies this spawning rule that a MObject
	 * has been removed from an area it is
	 * associated with. Note that the MObject was
	 * not necessarily spawned by this rule.
	 * 
	 * @param what  the MObject which was removed
	 */
	public void unspawn(MObject what);
}
