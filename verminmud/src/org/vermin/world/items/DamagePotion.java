/*
 * Created on 2.4.2005
 *
 */
package org.vermin.world.items;


import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.Damage.Type;

/**
 * @author Matti V�h�kainu / Vermin ry
 *
 */
public class DamagePotion extends Potion implements Wieldable {

	public DamagePotion(ReagentItem ri) {
		colour = "yellow";
		addAlias("potion");
		addAlias("yellow potion");		
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
	}

	public Damage[] getProjectileDamage(Living target) {
		return new Damage[] {new Damage(Damage.Type.FIRE, 100+Dice.random(50))};
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
