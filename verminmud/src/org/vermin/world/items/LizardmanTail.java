/* LizardmanTail.java
	13.9.2003	MV
	
	An item representing a lizardman tail as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class LizardmanTail extends DefaultWieldableImpl {
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		return Damage.build().crushing(5).dmg();
	}
	public boolean isVisible() {
		return false;
	}
	public String getName() { return "tail"; }
}
