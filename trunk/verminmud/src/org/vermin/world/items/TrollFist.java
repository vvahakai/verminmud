/* TrollFist.java
	12.9.2003	MV
	
	An item representing a troll fist as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class TrollFist extends DefaultWieldableImpl {
	
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		return Damage.build().crushing(10).dmg();
	}

	public int getDefensiveValue() {
      return 0;
   }
	public boolean isVisible() {
		return false;
	}	
	public String getName() {
		return "fist";
	}
}
