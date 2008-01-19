package org.vermin.world.items;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;

public class ManaPotion extends Potion {
	
	public ManaPotion(ReagentItem ri) {
		colour = "light blue";
		addAlias("potion");
		addAlias("light blue potion");
	}
	
	public int getNutritionValue() {
		return 0;
	}
	
	public boolean tryConsume(Living l) {
		return true;
	}
	
	public void consume(Living l) {
		l.addSp(50+Dice.random(30));
		l.remove(this);
		l.notice("You quaff the "+colour+" potion.");
	}
}
