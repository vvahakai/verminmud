/**
 * Edible.java 
 * 1.8.2003 Tatu Tarvainen / Council 4
 *
 * Edible items.
 */
package org.vermin.mudlib;

public interface Edible extends Item {
	
	public int getNutritionValue();

	/* Check that the given living can consume this */
	public boolean tryConsume(Living l);

	/* Hook for consuming */
	public void consume(Living l);

}
