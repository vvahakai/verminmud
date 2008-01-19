/*
 * Created on 2.4.2005
 */
package org.vermin.world.items;

import org.vermin.mudlib.DefaultDrinkableImpl;

/**
 * @author Matti V�h�kainu / Vermin ry
 *
 */
public class Potion extends DefaultDrinkableImpl {
	
	public String getDescription() {
		return "a "+colour+" potion";
	}
	
	public String getName() {
		return "potion";
	}
	
	public String getLongDescription() {
		return "a "+colour+" drinkable potion";
	}
	
	protected String colour;

}
