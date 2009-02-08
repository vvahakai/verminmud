/* Dice.java
	19.1.2002	TT & VV
	
	Throws a d<n>
*/
package org.vermin.mudlib;
import java.util.List;
import java.util.Random;

/**
 * Utilities for getting random numbers and elements.
 */
public class Dice {

	public final static Random rng = new Random();

	public static int random() {
		return random(100);
	}
	
	/**
	 * Returns a random int in the range 1-n.
	 * 
	 * N must not be negative, if it is, this
	 * returns 0.
	 * 
	 * N is inclusive.
	 * 
	 * @param n the maximum int to return
	 * @return  int in the range 1-n
	 */
	public static int random(int n) {
		if(n <= 0) {
			return 0;
		}
		return 1+rng.nextInt(n);
	}	
	
	public static <T> T randomElement(T[] elements) {
		if(elements == null)
			throw new IllegalArgumentException("Cannot get random element from null array");
		return elements[rng.nextInt(elements.length)];
	}
	
	public static <T> T randomElement(List<T> elements) {
		if(elements == null)
			throw new IllegalArgumentException("Cannot get random element from null list");
		return elements.get(rng.nextInt(elements.size()));
	}
}
