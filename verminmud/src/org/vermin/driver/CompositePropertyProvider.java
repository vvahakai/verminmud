/**
 * CompositePropertyProvider.java
 * 23.10.2004 Tatu Tarvainen
 */
package org.vermin.driver;

/**
 * Interface for composite property providers.
 */
public interface CompositePropertyProvider<K extends Enum<K>> extends PropertyProvider<K> {

	/**
	 * Add a nested provider that should be checked when deciding whether this
	 * provider provides a given property.
	 *
	 * @param provider the <code>PropertyProvider</code> to add
	 */
	public void addProvider(PropertyProvider<K> provider);

	/**
	 * Remove a nested provider.
	 *
	 * @param provider the <code>PropertyProvider</code> to remove
	 */
	public void removeProvider(PropertyProvider<K> provider);
}
