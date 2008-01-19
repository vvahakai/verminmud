/*
 * Created on 7.1.2006
 */
package org.vermin.world.items;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultWieldableImpl;
import org.vermin.mudlib.Living;

public class Piston extends DefaultWieldableImpl {
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	static {
	   dmg = new Damage[1];
	   dmg[0] = new Damage();
	   dmg[0].type = Damage.Type.PIERCING;
	   dmg[0].damage = 30;
	}  
   
	public Damage[] getHitDamage(Living target) {
	   return dmg;
	}
   
	public int getDefensiveValue() {
	   return 0;
	}
  
	public boolean isVisible() {
		return false;
	}
	
	public String getName() {
	   return "piston";
	}
}
