/*
 * Created on 6.1.2006
 */
package org.vermin.world.items;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultWieldableImpl;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;

public class ScorchingWeapon extends DefaultWieldableImpl {
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	static {
	   dmg = new Damage[1];
	   dmg[0] = new Damage();
	   dmg[0].type = Damage.Type.FIRE;
	   dmg[0].damage = 40;
	}  
   
	public Damage[] getHitDamage(Living target) {
	   return dmg;
	}
	public String getObjectHitVerb(Damage.Type type) {
		if(Dice.random(2) == 1) {
			return "flare";
		} else {
			return "burn";
		}
	}
	
	public String getSubjectHitVerb(Damage.Type type) {
		if(Dice.random(2) == 1) {
			return "flares";
		} else {
			return "burns";
		}
	}
	public int getDefensiveValue() {
	   return 0;
	}
	public boolean isVisible() {
		return false;
	}
	
	public String getName() {
	   return "flames";
	}
}
