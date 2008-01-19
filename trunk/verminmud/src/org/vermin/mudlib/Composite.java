/*
 * Created on 5.2.2005
 *
 */
package org.vermin.mudlib;

/**
 * @author tadex
 *
 */
public interface Composite<T> {
    
    /** 
     * Add a child to this object. 
     * The actual meaning of this operation is
     * implementation specific and may do different
     * things to different types of objects added.
     * 
     * @param child The child to add.
     */
    public void add(T child);
    
    /** 
     * Remove a child object.
     * 
     * @param child The child to remove. 
     */
    public void remove(T child);
    
    /**
     * Check if this object contains the given object.
     *
     * @param child the object to check
     * @return true if this object contains the given object, false otherwise
     */
    public boolean contains(T child);

}
