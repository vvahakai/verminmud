package org.vermin.mudlib.outworld;

/**
 * Interface for objects that provide outerworld map data.
 * 
 * @author tadex
 */
public interface MapSource {
	
	/**
	 * Returns the minimum layer index of the map.
	 * 
	 * @return the minimum layer index
	 */
	public int getMinimumLayer();
	
	/**
	 * Returns the maximum layer index of the map.
	 *
	 * @return the maximum layer index
	 */
	public int getMaximumLayer();
	
	/**
	 * Returns map entry type as an array.
	 * type[0] = map legend character
	 * type[1] = special code
	 */
	public byte[] getType(int x, int y, int layer);
	
	/**
	 * Returns the width of the map in rooms
	 */
	public int getWidth();
	
	/**
	 * Returns the height of the map in rooms
	 */
	public int getHeight();
	
	/**
	 * Returns whether this mapsource has a bit-mask
	 * for directional exits.
	 */
	public boolean hasMaskedExits();
	
	/**
	 * Returns the exit bit-mask for the given location.
	 */
	public int getExits(int x, int y, int layer);
}
