package org.vermin.util;

public class Arrays {

	public static boolean contains(Object[] haystack, Object needle) {
		if(haystack == null)
			return false;
		for(Object o : haystack)
			if( (o==null && needle==null) ||
				 o.equals(needle) )
				return true;
		return false;
	}

}
