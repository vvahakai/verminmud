/* LargeFishTooth.java
	25.10.2003	MV 
	
	An item representing a large fish tooth as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class LargeFishTooth extends DefaultWieldableImpl
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
			dmg[0].damage = 20;
			dmg[1] = new Damage();
			dmg[1].type = Damage.Type.CRUSHING;
			dmg[1].damage = 20;
		}
		
		return dmg;
	}
	public boolean isVisible() {
		return false;
	}
	   public String getObjectHitVerb(Damage.Type damageType) {
		      switch(damageType) {
		         case PIERCING:
		            return "bite";
		         case CRUSHING:
		         switch(Dice.random(3)) {
		            case 1:
		               return "rend";
		            case 2:
		               return "crush";
		            case 3:
		               return "rip";
		         }
		      }
		      
		      return "congratulate";
		   }
		   
		   public String getSubjectHitVerb(Damage.Type damageType) {
		      switch(damageType) {
		         case PIERCING:
		            return "bites";
		         case CRUSHING:
		         switch(Dice.random(3)) {
		            case 1:
		               return "rends";
		            case 2:
		               return "crushes";
		            case 3:
		               return "rips";
		         }
		      }
		      
		      return "congratulates";
		   }
	public String getName() { return "teeth"; }
}
