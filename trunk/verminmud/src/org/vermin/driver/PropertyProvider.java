/**
 * PropertyProvider.java
 * 23.10.2004 Tatu Tarvainen
 */
package org.vermin.driver;

/**
 * A simple interface for objects that provide
 * properties of the specialized enumerated type.
 */
public interface PropertyProvider<K extends Enum<K>> {

	/**
	 * Check if this provider provides the specified
	 * properties.
	 * The reason for separating the <code>first</code> and 
	 * <code>rest</code> arguments is that his way the compiler
	 * catches attempts to call <code>provides</code> without
	 * any arguments.
	 *
	 * @param first the first property to check for
	 * @param rest the rest of the properties to check for
	 * @return true if all the properties are provided by this provider, false otherwise
	 */
	public boolean provides(K first, K ... rest);

	/**
	 * Check that this provider provides the specified
	 * property.
	 *
	 * @param property the property to check for
	 * @return true if the property is provided, false otherwise
	 */
	public boolean provides(K property);
    
    /**
     * Check if this provider provides any of the specified
     * properties.
     * 
     * @param first the first property
     * @param rest the rest of the properties to check
     * @return true if any of the properties is provided, false otherwise
     */
    public boolean providesAny(K first, K ... rest);
}
