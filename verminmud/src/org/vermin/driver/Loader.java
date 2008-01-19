/**
 * Loader.java
 * 13.7.2003 Tatu Tarvainen / Council 4
 *
 * Interface for object loaders.
 */
package org.vermin.driver;

public interface Loader {

	/**
	 * Load an object prototype from the
	 * given path.
	 * If this loader can't load won't handle the given
	 * path, it must return null.
	 * If the load fails, LoadException must
	 * be thrown.
	 *
	 * @param path loader specific location string
	 * @return the loaded prototype
	 */
	public Prototype load(String path) throws Exception;

	/**
	 * Shorthand for Loader.load(path).get()
	 */
	public Object get(String path) throws Exception;

	/**
	 * Check if the given object is loaded and in memory.
	 *
	 * @param path the path of the object
	 * @return true if loaded, false otherwise
	 */
	public boolean isLoaded(String path);

	/**
	 * Unload an object. This will remove the object
	 * from the cache and a subsequent call to load
	 * for this path will reload the object.
	 *
	 * @param path the path of the object to unload
	 */
	public void unload(String path);

}
