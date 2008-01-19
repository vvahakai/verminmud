/**
 * DefaultCompositePropertyProvider.java
 * 23.10.2004 Tatu Tarvainen
 */
package org.vermin.driver;

import java.util.*;

/**
 * Provides a default implementation of a composite property provider.
 */
public class DefaultCompositePropertyProvider<K extends Enum<K>> 
	extends AbstractPropertyProvider<K> 
	implements CompositePropertyProvider<K> {


	private List<PropertyProvider<K>> providers = new LinkedList();


	public boolean provides(K property) {
		for(PropertyProvider<K> pp : providers)
			if(pp.provides(property))
				return true;

		return false;
	}

	public void addProvider(PropertyProvider<K> provider) {
		providers.add(provider);
	}

	public void removeProvider(PropertyProvider<K> provider) {
		providers.remove(provider);
	}

}

