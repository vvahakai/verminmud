/*
 * Created on 29.1.2005
 */
package org.vermin.world.races;

import org.vermin.driver.Factory;

/**
 * @author Jaakko Pohjamo
 */
public class QuadrupleFactory implements Factory {
	
	public enum QuadrupleOption {
		/**
		 * Indicates the Quadruple should have hooves.
		 */
		HOOVES,
		/**
		 * Indicates the Quadruple should have paws.
		 */	
		PAWS,
		/**
		 * Indicates the Quadruple should have horns.
		 */
		HORNS,
		/**
		 * Indicates the Quadruple has jaws that can be
		 * used as a weapon.
		 */
		JAWS,
		/**
		 * Indicates the Quadruple has a tail that can be
		 * used as a weapon.
		 */
		TAIL
	}
	
	protected static QuadrupleFactory _instance = new QuadrupleFactory();
	
	protected QuadrupleFactory() {
		
	}
	
	public static QuadrupleFactory getInstance() {
		return _instance;
	}
	/**
	 * Creates a new instance of QuadrupleRace with the specified
	 * QuadrupleFactory.QuadrupleOptions.
	 */
	public Object create(Object ...  args) throws IllegalArgumentException {
		return QuadrupleRace.getInstance(args);
	}

}
