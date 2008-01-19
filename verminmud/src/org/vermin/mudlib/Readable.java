/**
 * Readable.java
 * 1.8.2003 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

public interface Readable extends Item {

	public boolean tryRead(Living l);

	public void read(Living l);
}

