/*
 * Created on 6.1.2006
 */
package org.vermin.world.items;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultWieldableImpl;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;

public class MonsterBody extends DefaultWieldableImpl {
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	static {
	   dmg = new Damage[1];
	   dmg[0] = new Damage();
	   dmg[0].type = Damage.Type.CRUSHING;
	   dmg[0].damage = 25;
	}  
   
	public Damage[] getHitDamage(Living target) {
	   return dmg;
	}
	public String getObjectHitVerb(Damage.Type type) {
		if(Dice.random(2) == 1) {
			return "crush";
		} else {
			return "bonk";
		}
	}
	
	public String getSubjectHitVerb(Damage.Type type) {
		if(Dice.random(2) == 1) {
			return "crushes";
		} else {
			return "bonks";
		}
	}
	public int getDefensiveValue() {
	   return 0;
	}
	public boolean isVisible() {
		return false;
	}
	
	public String getName() {
	   return "body";
	}
}
