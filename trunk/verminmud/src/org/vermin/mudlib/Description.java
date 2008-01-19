/*
 * Created on 3.2.2005
 *
 */
package org.vermin.mudlib;

/**
 * An interface for things that are presented in human
 * readable form.
 * 
 * @author tadex
 *
 */
public interface Description {
    /** 
     * Get the name of this object. The name is used in
     * commands and composite methods to refer to this object.
     * 
     * @return  The name of the object. 
     */
    public String getName();
    
    /** 
     * Set the name of this object.
     * 
     * @param name The new name. 
     */
    public void setName(String name);
    
    /** 
     * Get the short description of this object. The description
     * is the default printed representation of the object. All
     * listings use this.
     * 
     * @return  The short description. 
     */
    public String getDescription();

    /** 
     * Get the long description of this object. The long description
     * is used when examining objects in detail (eg. looking at them).
     * 
     * @return  The long description of this object. 
     */
    public String getLongDescription();

    
    /** 
     * Determine if a string is an alias for this object.
     * 
     * @param alias   The string to compare. 
     * @return        true if the string is an alias, false otherwise. 
     */
    public boolean isAlias(String alias);
}
