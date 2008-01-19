package org.vermin.driver;

/**
 * A very generic factory interface.
 * 
 * @author tadex
 *
 */
public interface Factory {
    /**
     * Create an instance of what ever it is that
     * this factory creates.
     * 
     * @param args arguments, if any
     * @return the created instance
     * @throws IllegalArgumentException if the arguments are illegal
     */
	public Object create(Object ... args) throws IllegalArgumentException;
}
