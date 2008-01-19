/*
 * Created on 5.2.2005
 *
 */
package org.vermin.mudlib;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @author tadex
 *
 */
public interface Container extends Composite<MObject>, MObject {

    public static final Pattern INDEXED_CONTAINER_ACCESS =
        Pattern.compile("(.+)\\s+(\\d+)\\s*$");

    /** 
     * Find object by type (as returned by getType()). The 
     * returned Iterator doesn't need to be in any particular 
     * order.
     * 
     * @param type 
     * @return <code>Iterator</code> to the found objects
     */
    public Iterator findByType(Types type);

    /**
     * Find an object by name (or alias). 
     * If there are many objects that have name as an alias, 
     * one of them is returned, but not necessarily the same
     * every time.
     * 
     * If the name ends with a number, it is considered to be an index
     * and the indexed version will be automatically called.
     *
     * If this object doesn't contain an object that responds to
     * the given alias, <code>null</code> will be returned.
     * 
     * @param name the name of the object
     * @return the contained object or null
     */
    public MObject findByName(String name);
    
    /**
     * Find the index:th object by name.
     * If there are many items with the same name, the
     * index can be used to determine which of the objects
     * is returned.
     * 
     * @param name the name of the object
     * @param index a 1-based index of the object
     * @return the contained object or null
     */
    public MObject findByName(String name, int index);  
    
    /**
     * Find an object by name an type.
     * This method is the same as <code>findByName</code> except
     * that it only considers objects of the given type.
     * 
     * @param name the name of the object
     * @param type the type of the object
     * @return the contained object or null
     */
    public MObject findByNameAndType(String name, Types type);
    
    /**
     * Find the index:th object by name and type. 
     * Indexed version of <code>findByNameAndType</code>.
     * 
     * @param name the name of the object
     * @param index a 1-based index of the object
     * @param type the type of the object
     */
    public MObject findByNameAndType(String name, int index, Types type); 
    
    /**
     * Checks if the given <code>MObject</code> can be added to this container
     * @param obj the <code>MObject</code> that should be added
     * @return true if <code>MObject</code> can be added.
     */
    public boolean tryAdd(MObject obj);

    /**
     * Checks if the given <code>MObject</code> can be removed from this container
     * @param obj the <code>MObject</code> that should be removed
     * @return true if <code>MObject</code> can be removed.
     */    
    public boolean tryRemove(MObject obj);
    

}
