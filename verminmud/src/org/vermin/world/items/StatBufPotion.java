package org.vermin.world.items;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Reagent;

public class StatBufPotion extends Potion {

	Reagent reagent;
	
	public StatBufPotion(ReagentItem ri) {
		colour = "clear";
		addAlias("potion");
		addAlias("clear potion");
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
