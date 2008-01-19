/**
 * AbstractPropertyProvider.java
 * 23.10.2004 Tatu Tarvainen
 */
package org.vermin.driver;

public abstract class AbstractPropertyProvider<K extends Enum<K>> implements PropertyProvider<K> {

    public boolean providesAny(K first, K ... rest) {
        if(provides(first))
            return true;
        
        for(K p : rest) {
            if(provides(p))
                return true;
        }
        
        return false;
    }

	public boolean provides(K first, K ... rest) {
		if(!provides(first))
			return false;

		for(K p : rest)
			if(!provides(p))
				return false;

		return true;
	}


}
