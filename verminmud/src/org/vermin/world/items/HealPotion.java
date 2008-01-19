/*
 * Created on 2.4.2005
 */
package org.vermin.world.items;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;

/**
 * @author Matti V�h�kainu / Vermin ry
 *
 */
public class HealPotion extends Potion {
	
	public HealPotion(ReagentItem ri) {
		colour = "light red";
		addAlias("potion");
		addAlias("light red potion");
	}
	
	public int getNutritionValue() {
		return 0;
	}
	
	public boolean tryConsume(Living l) {
		return true;
	}
	
	public void consume(Living l) {
		l.addHp(50+Dice.random(30));
		l.remove(this);
		l.notice("You quaff the "+colour+" potion.");
	}
	
	
}
