/**
 * Alignment.java 
 * 14.1.2005 TT
 */
package org.vermin.mudlib;

/**
 * Defines an object that has a position in the
 * metaphysical axes.
 */
public interface Alignment {

	/**
	 * Returns the alignment on the life - unlife axis
	 * ranging from -10 000 to 10 000.
	 * A value of 10 000 is fully life and a value
	 * of -10 000 is fully unlife.
	 *
	 * @return the life - unlife alignment
	 */
	public int getLifeAlignment();

	/**
	 * Returns the alignment on the progress - stagnation axis
	 * ranging from -10 000 to 10 000.
	 * A value of 10 000 is fully progress and a value
	 * of -10 000 is fully stagnation.
	 */
	public int getProgressAlignment();


}
