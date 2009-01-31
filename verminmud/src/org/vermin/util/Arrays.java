package org.vermin.util;

import java.util.Collection;

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

	/**
	 * Fill collection from array.
	 * 
	 * @param c
	 * @param as
	 */
	public static <T> void fill(Collection<T> c, T[] as) {
		for(T a : as) c.add(a);
	}

	/**
	 * Reverse an array in place.
	 * Modifies the input array.
	 * 
	 * @param as
	 */
	public static <T> void reverse(T[] as) {
		if(as.length < 2) return;
		int first = 0;
		int last = as.length-1;
		boolean odd = as.length%2 == 1;
		
		while( first < last ) {
			T tmp = as[first];
			as[first] = as[last];
			as[last] = tmp;
			first++; last--;
		}
	}
	
	/**
	 * Reverse an array into a new array.
	 * 
	 * @param as
	 * @return 
	 */
	public static <T> T[] reverseCopy(T[] as) {
		T[] copy = as.clone();
		reverse(copy);
		return copy;
	}
}
