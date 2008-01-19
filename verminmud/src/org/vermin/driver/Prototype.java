/* Prototype.java
 * 6.3.2003 Tatu Tarvainen / Council 4
 *
 */
package org.vermin.driver;

import java.io.File;


public interface Prototype {

	public void setId(String id);

	/* The file this prototype was loaded from */
	public File getSource();
	
	/* Is this prototype unique? */
	public boolean isUnique();

	/* Name of the prototype */
	public String getName();
	
	/**
	 * Gets the unique instance.
	 */
	public Object get();

	/**
	 * Get a named instance.
	 */
	public Object get(String name);

	/**
	 * Create a new instance. It will be named with
	 * an increasing index.
	 */
	public Object create();

	/**
	 * Create a named instance. The name must not
	 * be a number (it might clash with the 'anonymous' 
	 * instances).
	 */
	public Object create(String name);

}
