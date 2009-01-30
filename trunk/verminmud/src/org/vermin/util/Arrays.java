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
		if(as.length == 0) return;
		T first = as[0];
		for(int i=0; i<as.length; i++) 
			as[i] = as[as.length-1-i];
		as[as.length-1] = first;
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
