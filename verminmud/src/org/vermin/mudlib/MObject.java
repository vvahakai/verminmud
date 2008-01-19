/* file name  : org.Mud/MObject.java
 * authors    : Tatu Tarvainen
 *
 * modifications:
 *
 */

package org.vermin.mudlib;

import org.vermin.driver.Persistent;
import org.vermin.driver.Tickable;

/** 
 * The basic Mud interface that <b>all</b> game object must
 * implement.
 * 
 * @author Tatu Tarvainen
 * @version 1.0
 */
public interface MObject
    extends ActionHandler<MObject>, Tickable, Persistent, Cloneable,
        Description {
	
	
    /** 
     * Return the type of this object.
     * 
     * @return The type constant.
     */
    public Types getType();
	
	/** 
	 * Add a name alias for this object. Aliases are alternative
	 * names that can also be used in commands and composite methods
	 * to refer to an object.
	 * 
	 * @param alias   The alias to add. 
	 */
	public void addAlias(String alias);

    /** 
     * Set the short description of this object.
     * 
     * @param name The new description. 
     */
    public void setDescription(String name);

    /** 
     * Set the long description of this object.
     * 
     * @param name The new description. 
     */
    public void setLongDescription(String name);
	
	/** 
	 * Called after the object has been created or loaded 
     * from a persistent source. This method must perform 
     * any necessary calculations that must be done to make
     * the object usable.
	 */
	public void start();
	
 
   /**
    * Get a clone of this object. 
    * May return null if this object can not be cloned. 
    */
   public MObject getClone();

	/**
	 * Get the parent of this object.
     * The parent is the object that contains this object.
	 * If this object has no parent, null is returned.
     * 
	 * @return the parent or null
	 */
	public Container getParent();

	/**
	 * Set the parent of this object.
	 *
	 * @param parent the parent
	 */
	public void setParent(Container parent);

   /**
	 * Returns the illumination of this object.
	 * Normal items should have an illumination of zero (0).
	 * Containers like Rooms should sum the illumination of all 
	 * contained items. Containers like bags should not.
	 *
	 * A value of 75 or more is considered to be daylight.
	 * A value of under 35 is considered to be dark (below the 
	 * minimum visible illumination level of most races).
	 * 
	 * @return an <code>int</code> from 0 to 100
	 */
	public int getIllumination();

	/**
	 * Add modifier.
	 * @param m the modifier
	 */
	public void addModifier(Modifier m);

}
