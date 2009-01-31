/* Mapper.java
	13.2.2002	Tatu Tarvainen / Council 4
	
	Interface that map generators must implement.
*/

package org.vermin.mudlib;

public interface Mapper {

	/**
	 * Return a default map view.
	 * 
	 * @param currentPosition the room id 
	 * @return a map representation (containing multiple lines)
	 */
	public String getMap(String currentPosition);
	
	/**
	 * Get a larger map view.
	 * 
	 * @param currentPosition the room id
	 * @return
	 */
	public String getLargeMap(String currentPosition);

	/**
	 * Get a small map view. The small map is used when looking in a room.
	 * 
	 * @param currentPosition the room id
	 * @return
	 */
	public String getSmallMap(String currentPosition);
	
	/**
	 * Render a description of the map symbols for the player.
	 * 
	 * @param who
	 */
	public void showLegend(Player who);

	/**
	 * Control whether or not to show special locations.
	 * 
	 * @param suppress
	 */
	public void setSuppressSpecials(boolean suppress);
	
	/**
	 * Check if special locations are shown.
	 * @return
	 */
	public boolean isSuppressSpecials();

}
