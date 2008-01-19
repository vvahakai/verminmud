/**
 * Persistent.java 
 * 1.8.2003 Tatu Tarvainen / Council 4
 *
 * Interface for prototype/instance control.
 */
package org.vermin.driver;

public interface Persistent {

	/**
	 * This is called whenever the object is loaded from storage.
	 */
	public void start();

	/**
	 * Anonymous instances (those that will not be saved) 
	 * will have their anonymous flag set to true.
	 *
	 * @param anonymous the flag value to set
	 */
	public void setAnonymous(boolean anonymous);

	/**
	 * Returns true if this object is anonymous.
	 */
	public boolean isAnonymous();

	/**
	 * Get the id of this persistent object.
	 * Id strings are used to get references to 
     * non-anonymous instances.
	 */
	public String getId();

    /** 
     * Set the unique identity of this object.
     * The id is used by the persistence system
     * and set when the object is loaded.
     * 
     * @param id the identity.
     */
    public void setId(String id);

	/**
	 * Save this instance (if it is not anonymous).
	 */
	public void save();
}
