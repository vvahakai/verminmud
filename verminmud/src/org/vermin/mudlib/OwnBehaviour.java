package org.vermin.mudlib;

/**
 * A behaviour that has a specific owner which may be referenced.
 * The owner must be set before calling any of the behaviour hooks.
 */
public interface OwnBehaviour extends Behaviour {

	/**
	 * Set the owner whose behaviour this is.
	 * 
	 * @param actor the actor
	 */
	public void setOwner(Living owner);
	
	/**
	 * 
	 * @return
	 */
	public Living getOwner();
}
