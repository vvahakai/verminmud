/* SnakeTooth.java
	22.9.2003	MV & VV
	
	An item representing a snake tooth as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class SnakeTooth extends DefaultWieldableImpl{

	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		return Damage.build().piercing(6).poison(35).dmg();
	}
	
	public String getObjectHitVerb(Damage.Type damageType) {
		return "bite";
	}
		   
	public String getSubjectHitVerb(Damage.Type damageType) {
		switch(Dice.random(3)) {
		case 1:
			return "rends";
		case 2:
			return "bites";
		case 3:
			return "rips";
		}
		return "congratulates";	
	}	
	public boolean isVisible() {
		return false;
	}
	public String getName() { return "teeth"; }
}
