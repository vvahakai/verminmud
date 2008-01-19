/* Tickable.java 
 * 6.3.2003 Tatu Tarvainen / Council 4
 *
 * An interface for objects that are perioidically polled.
 */
package org.vermin.driver;

public interface Tickable<Q extends Queue> {

	/**
	 * Tick (update this object).
	 *	Returns boolean indicating if this object wants to be
	 *	ticked again for this queue number.
	 *
	 * @return true if object wants to be ticked again, false otherwise
	 */
	public boolean tick(Q queue);
	
}
