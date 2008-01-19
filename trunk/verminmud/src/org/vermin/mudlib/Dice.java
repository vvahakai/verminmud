/* Dice.java
	19.1.2002	TT & VV
	
	Throws a d<n>
*/
package org.vermin.mudlib;
import java.util.Random;

public class Dice
{

	public static Random rng = new Random();

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
		/*
		int ret = 1;
		ret += ((Math.random()*(n-1)));
		return ret;
		*/
	}	
}
