/* TickEntry.java
	14.1.2002	Tatu Tarvainen / Council 4
	
	Entry in the tick queue.
*/

package org.vermin.driver;

public class TickEntry {
	
	/* when was the last tick time (in milliseconds since Jan 1 1970) */
	public long last = 0;
	
	/* the tickable to tick */
	public Tickable obj = null;
	
	public TickEntry(Tickable who) {
		obj = who;
		last = System.currentTimeMillis();
	}
	
}
