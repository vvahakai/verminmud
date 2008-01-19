/*
 * Created on 2.4.2005
 */
package org.vermin.world.items;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.PoisonAffliction;
import org.vermin.mudlib.Damage.Type;

/**
 * @author Matti V�h�kainu / Vermin ry
 *
 */
public class PoisonPotion extends Potion {

	public PoisonPotion(ReagentItem ri) {
		colour = "green";
		addAlias("potion");
		addAlias("green potion");		
	}
	
	public int getNutritionValue() {
		return 1;
	}
	
	public boolean tryConsume(Living l) {
		return true;
	}
	
	public void consume(Living l) {
		l.remove(this);
		l.notice("You quaff the "+colour+" potion.");
		l.subHp(new Damage(Damage.Type.POISON, 50+Dice.random(50)));
		l.addAffliction(new PoisonAffliction(null, 50+Dice.random(50)));
	}

	public Damage[] getProjectileDamage() {
		return new Damage[] {new Damage(Damage.Type.POISON, 100+Dice.random(100))};
	}

	public String getObjectHitVerb(Type type) {
		return "hit";
	}

	public String getSubjectHitVerb(Type type) {
		return "hits";
	}

	public String getHitNoun(Type type) {
		return "hit";
	}

	public int getDefensiveValue() {
		return 0;
	}

	public void onWield(Living who) {	
	}

	public void onUnwield(Living who) {
	}

	public float getSpeed() {
		return 1;
	}
}
