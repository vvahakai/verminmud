/* LizardmanHand.java
	13.9.2003	MV
	
	An item representing a lizardman hand as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class LizardmanHand extends DefaultWieldableImpl {
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		return Damage.build().crushing(8).dmg();
	}
	public boolean isVisible() {
		return false;
	}
	public String getName() { return "fist"; }
}
