/**
 * Maybe.java
 * 24.2.2004 Tatu Tarvainen
 *
 * A reference to an object that may or may not be loaded.
 */
package org.vermin.driver;

import org.vermin.mudlib.World;

public class Maybe<T> {

    private String id;
    private boolean onDemand = false;

    public Maybe() {}

    public Maybe(String id) {
	this.id = id;
    }
	
    public T get() {
        // FIXME: MOVE THIS TO MUDLIB
    	return onDemand || isLoaded() ? (T) World.get(id) : null;
    }

    public boolean isLoaded() {
		return World.isLoaded(id);
    }

    public String getId() {
		return id;
    }

    public int hashCode() {
    	return id.hashCode();
    }

    public boolean equals(Object other) {
    	return other instanceof Maybe && id.equals(((Maybe) other).getId());
    }
}
