/* SnakeTooth.java
	22.9.2003	MV & VV
	
	An item representing a snake tooth as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class SnakeTooth extends DefaultWieldableImpl
{
	
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target)
	{
		if(dmg == null)
		{
			dmg = new Damage[2];
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.PIERCING;
			dmg[0].damage = 6;
			dmg[1] = new Damage();
			dmg[1].type = Damage.Type.POISON;
			dmg[1].damage = 35;
		}
		
		return dmg;
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
