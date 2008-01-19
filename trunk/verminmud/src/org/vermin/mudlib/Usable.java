/**
 * Usable.java
 * 1.8.2003 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

public interface Usable extends Item {
	
	public boolean tryUse(Living l);
	public void use(Living l);

}
