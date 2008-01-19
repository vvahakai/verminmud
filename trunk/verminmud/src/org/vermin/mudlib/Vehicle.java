/**
 * Vehicle.java
 * 1.8.2004 Tatu Tarvainen
 */
package org.vermin.mudlib;

/**
 * Defines the interface for vehicles.
 * Vehicles are transportation devices, which <code>Living</code>s can ride and
 * optionally control.
 *
 * Vehicles may allow multiple passengers (like boats) or
 * they can be for a single passenger only (for example, a horse).
 *
 * Vehicles should replace the passengers <code>BattleGroup</code> so
 * that the passenger can fall of.
 *
 * @author Tatu Tarvainen
 */
public interface Vehicle extends MObject {


	/**
	 * Get the verb used to describe movement in this vehicle.
	 * Used for displaying movement in rooms, for example using
	 * a horse could display "Erno rides north".
	 *
	 * @return the movement verb
	 */
	public String getMoveVerb();


	/**
	 * Check if this vehicle can float on water (a boat or raft, for example).
	 */
	public boolean canFloat();

	/**
	 * Check if this vehicle can fly.
	 */
	public boolean canFly();

	/**
	 * Check if this vehicle is submersible (can move underwater).
	 */
	public boolean isSubmersible();

	/**
	 * Check if the given actor can use (mount) this vehicle.
	 *
	 * @param who the actor
	 * @return true if mounting can be done, false otherwise
	 */
	public boolean tryMount(Living who);

	/**
	 * Check if the given actor can stop using (dismount) this vehicle.
	 *
	 * @param who the actor
	 * @return true if dismounting can be done, false otherwise
	 */
	public boolean tryDismount(Living who);

	/**
	 * Notification that the given actor has mounted the vehicle.
	 *
	 * @param who the actor who mounted
	 */
	public void onMount(Living who);

	/**
	 * Notification that the given actor has dismounted the vehicle.
	 * TODO: How to notify that someone has been knocked of the vehicle or teleported away?
	 *
	 * @param who the actor who dismounted
	 */
	public void onDismount(Living who);

	/**
	 * Try to move through the given exit.
	 * Called after the Exit's tryMove has succeeded.
	 *
	 * @return true if movement is possible, false otherwise
	 */
	public boolean tryMove(Living who, Exit exit);

	/**
	 * Notification of movement. This is called before
	 * the current room is changed on the actor.
	 *
	 * This method can be used to decrease sustenance points (like rowing a boat).
	 */
	public void onMove(Living who, Exit exit);

	/**
	 * Check if this vehicle is for a single person only.
	 * This is significant, because vehicles like horses should
	 * teleport with the user.
	 *
	 * @return true if personal vehicle, false otherwise
	 */
	public boolean isPersonal();
}

