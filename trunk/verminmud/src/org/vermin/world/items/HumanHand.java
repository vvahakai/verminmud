/* HumanHand.java
	19.1.2002	TT & VV
	
	An item representing a human hand as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class HumanHand extends DefaultWieldableImpl {
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		return Damage.build().crushing(7).dmg();
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
