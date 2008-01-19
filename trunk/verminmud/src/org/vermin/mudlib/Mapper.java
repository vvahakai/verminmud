/* Mapper.java
	13.2.2002	Tatu Tarvainen / Council 4
	
	Interface that map generators must implement.
*/

package org.vermin.mudlib;

public interface Mapper {

	public String getMap(String currentPosition);
	
	public String getLargeMap(String currentPosition);

	public void showLegend(Player who);

	public void setSuppressSpecials(boolean suppress);
	public boolean isSuppressSpecials();

}
