/* HumanHand.java
	19.1.2002	TT & VV
	
	An item representing a human hand as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class HumanHand extends DefaultWieldableImpl {
	
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		if(dmg == null) {
			dmg = new Damage[1];
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.CRUSHING;
			dmg[0].damage = 7;
		}
		
		return dmg;
	}

	public boolean isVisible() {
		return false;
	}
	
	public int getDefensiveValue() {
      return 0;
   }
	
	public String getName() {
		return "fist";
	}
}
