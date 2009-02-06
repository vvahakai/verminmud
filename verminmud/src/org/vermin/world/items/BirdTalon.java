/* BirdTalon.java
	12.9.2003	MV & VV
	
	An item representing a bird talon as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class BirdTalon extends DefaultWieldableImpl {
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		Damage[] dmg = new Damage[1];
		dmg[0] = new Damage();
		dmg[0].type = Damage.Type.SLASHING;
		dmg[0].damage = 6;
		return dmg;
	}

	public boolean isVisible() {
		return false;
	}
	
	public String getName() { return "talon"; }
}
