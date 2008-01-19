/* Room.java
	5.1.2002	Tatu Tarvainen / Council 4
	
	Defines the interface that all rooms must implement.
*/
package org.vermin.mudlib;

import org.vermin.driver.PropertyProvider;


/**
 * Interface definition for rooms.
 * Rooms are containers that can contain
 * <code>Exit</code>s (the exits leading out of the room,
 * <code>Living</code>s (the people in the room)
 * and <code>Item</code>s (stuff on the ground).
 */
public interface Room 
    extends MObject, Container,
            Purse, PropertyProvider<RoomProperty> {

    /**
     * Returns the larger geographical area this room
     * is a part of.
     */
    public Area getArea();
    
    /**
     * Sets the area of this room.
     */
    public void setArea(Area area);
    
	/**
	 * Returns a string name for the location this room is in.
	 * This can be used to designate smaller subareas.
	 * If the location is not known specified, returns null.
	 *
	 * @returns the area location, or null
	 */
	public String getLocation();

	/**
	 * Enter the room.
	 *
	 * @param l the target <code>Living</code>
	 * @param from the target direction
	 */
	public void enter(Living l, Exit from);

	/**
	 * Enter the room by unspecified means 
	 * (like teleportation or starting location).
	 *
	 * @param l the target <code>Living</code>
	 */
	public void enter(Living l);

	
	/**
	 * Leave the room.
	 *
	 * @param l the target <code>Living</code>
	 * @param to the target direction
	 */
	public void leave(Living l, Exit to);

	/**
	 * Notify that someone has taken something.
	 *
	 * @param l the target <code>Living</code>
	 * @param what an <code>Item</code> value
	 */
	public void takes(Living l, Item what);

	/**
	 * Notify that someone has dropped something.
	 *
	 * @param l the target <code>Living</code>
	 * @param what the target that was dropped
	 */
	public void drops(Living l, Item what);

	/**
	 * Wield something.
	 *
	 * @param l the target <code>Living</code>
	 * @param what the item that was wielded
	 */
	public void wield(Living l, Item what);
	
	/**
	 * Unwield something.
	 *
	 * @param l the target <code>Living</code>
	 * @param what the item that was unwielded
	 */
	public void unwield(Living l, Item what);
	
	/**
	 * Notify that the life of a Living has been
	 * extinguished in this room.
	 * 
	 * @param victim  the unfortunate dead
	 * @param killer  the successful aggressor, or null
	 *                if something else caused the death
	 */
	public void dies(Living victim, Living killer);

	/**
	 * Say something.
	 *
	 * @param who the speaker
	 * @param what the said text
	 */
	public void say(Living who, String what);
	
	/**
	 * Send a notice to all <code>Living</code>s in this room,
	 * or optionally exclude a single living.
	 *
	 * @param who the living excluded from this notice, or <code>null</code>
	 * 				if everyone should receive the notification
	 * @param what the notice text
	 */
	public void notice(Living who, String what);

	/**
	 * Send a notice to all <code>Living</code>s in this room,
	 * except the ones listed in <code>excluded</code>.
	 *
	 * @param what  the message
	 * @param excluded  livings excluded
	 */
	public void noticeExcluding(String what, Living...excluded);
	
	/**
	 * Send a notice to all <code>Living</code>s in this room about
	 * an ask event.
	 * 
	 */
	public void asks(Living asker, Living target, String subject);
	
   /**
    * Get an extended description of an observable feature in this room,
    * such as a painting on the wall etc.
    * 
    * @param what  a string specifying the feature examined
    * @return  the description of the specified feature, or <code>null</code>
    *          if the feature was not recognized
    */
   public String getExtendedDescription(String what);
   
   /**
	 * Returns a boolean indicating whether this room is outdoors.
	 *
	 * @return <code>true</code> if the room is outdoors, <code>false</code> otherwise.
	 */
	public boolean isOutdoor();

   /**
	 * Returns the amount of water. A value of 100 
	 * means that the room is underwater.
	 *
	 * @return a <code>byte</code> from 0 to 100
	 */
	public int getWaterLevel();

	/**
	 * Explore this room.
	 * This method should call World-utilities 
	 * to determine if the room has been explored and
	 * for setting the exploration status.
	 * Basic exploration experience is added by the World-utilities.
	 *
	 * @param explorer the explorer
	 */
	public void explore(Living explorer);

	/**
	 * Check if the given direction is blocked.
	 * Blocked directions cannot be moved towards or
	 * peered at.
	 *
	 * @param dir the direction to check
	 * @return true if the direction is blocked, false otherwise
	 */
	public boolean isBlocked(String dir);

	/**
	 * Notification that a living has ticked in the room.
	 *
	 * @param who the living who ticked
	 * @param type the tick type
	 */
	public void ticked(Living who, short type);

	/**
	 * Check if flying is required to be in this room.
	 *
	 * @return true if yes, false otherwise
	 */
	public boolean requiresAviation();

	/**
	 * Check if this room can be teleported to or from.
	 *
	 * @param who the actor who is teleporting
	 * @return true if teleportation is possible, false otherwise
	 */
	public boolean mayTeleport(Living who);

	/**
	 * Returns the amount of vegetation in the room ranging
	 * from 0 to 100. Zero vegetations means that there is
	 * no plant life in the room. A vegetation value of 100
	 * means that the amount of vegetation is comparable to
	 * a dense rainforest.
	 *
	 * @return the amount of vegetation
	 */
	public int getVegetation();
    
    /**
     * Returns an array of exits in this room.
     * If there are no exits in this room, returns
     * an array of zero length.
     * 
     * @return an array of exits
     */
    public Exit[] getExits();
    
    /**
     * Returns the exit leading to the given direction.
     * If there is no exit to that direction, returns null.
     * 
     * @return the exit or null
     */
    public Exit getExitTo(String direction);

}
