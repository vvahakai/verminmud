/* Exit.java
	13.1.2002	Tatu Tarvainen / Council 4
	
	Defines interface for room exits.
*/
package org.vermin.mudlib;

public interface Exit extends MObject {

	/**
	 * Try to move through this exit from the specified room.
	 * If the movement fails, this method should print 
	 * a descriptive message why the attempt failed.
	 * 
	 * @param who the actor who tries to move
	 * @param roomId the room where movement is attempted from
	 * @return true if movement can be done, false otherwise
	 */
	public boolean tryMove(Living who, String roomId);
	
	/**
	 * Get the id of the target room when moving from the specified room. 
	 *
	 * @param roomId the id of the room
	 * @return the id of the target room 
	 */
	public String getTarget(String roomId);

	/**
	 * Get the direction string for moving from the
	 * given room. The direction is a human readable
	 * description (eg. "w", "out").
	 *
	 * @param roomId the id of the room
	 * @return the direction
	 */
	public String getDirection(String roomId);
	
	/**
	 * Gets the direction string used to arrive through
	 * this exit to the specified room. The direction is
	 * a human readable description (eg. "w", "out").
	 * 
	 * @param roomId the id of the room
	 * @return the direction
	 */
	
	public String getArrivalDirection(String roomId);
	
	/**
	 * Get the message for a successfull pass attempt
	 * from the given room. 
	 * If there is no message, this method should return null.
	 *
	 * @param roomId the id of the room
	 * @return the message or null
	 */
	public String getPassMessage(String roomId);
	
	/**
	 * Check if this exit is obvious in the given room for the 
	 * given actor. This method should be called for every look
	 * operation as the obviousness status can change.
	 * 
	 * @param who the actor who is examining the room
	 * @param roomId the room being examined
	 */
	public boolean isObvious(Living who, String roomId);
	

}
