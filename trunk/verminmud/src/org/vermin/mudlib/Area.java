/*
 * Created on 3.2.2005
 *
 */
package org.vermin.mudlib;

/**
 * Models a greater geographical area which contains
 * rooms.
 * 
 * @author tadex
 *
 */
public interface Area {

	/**
	 * Returns the name of this area.
	 * 
	 * @return the name of this area
	 */
	public String getName();
	
    /**
     * Returns the mapper for this area.
     * The mapper may be null if the area is unmapped.
     * 
     * @return the mapper or null
     */
    public Mapper getMapper();
    
    /**
     * Returns the Weather object describing the
     * weather in the rooms in this area.
     * 
     * @return  the weather or null
     */
    public Weather getWeather();
    
    /**
     * Notifies this area that the passed MObject
     * has been removed from this area.
     * Currently only used to provide SpawningRules
     * with information of their spawned things
     * being removed / unloaded.
     * 
     * @param l the MObject removed from 
     */
    public void removed(MObject l);
    
    /**
     * Allows a room to notify an Area that this would
     * be a good time to spawn some monsters.
     * 
     * @param r
     */
    public void spawn(Room r);
    
    /**
     * Sets the parent area this area is a subarea of.
     * 
     * @param a  the parent area
     */
    public void setParent(Area a);
    
    /**
     * Gets the parent area of this area, or null if
     * this is the toplevel area.
     * 
     * @return  the parent area or null if this area is toplevel
     */
    public Area getParent();
    
    /**
     * Adds a spawning rule for this area.
     * 
     * @param r  the spawning rule to add
     */
    public void addSpawningRule(SpawningRule r);
    
    /**
     * Set mapper for this area.
     */
    public void setMapper(Mapper m);
}