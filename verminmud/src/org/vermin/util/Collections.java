package org.vermin.util;

import java.util.Map;

public class Collections {

	public static Object get(Map m, Object key, Object dfl) {
		Object ret = m.get(key);
		if(ret == null)
			return dfl;
		return ret;
	}
	
}
