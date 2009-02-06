/* DemonTail.java
	29.1.2005	MV
	
	An item representing a demon tail as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class DemonTail extends DefaultWieldableImpl {
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target)	{
		Damage[] dmg = new Damage[1];
		dmg[0] = new Damage();
		dmg[0].type = Damage.Type.CRUSHING;
		dmg[0].damage = 6;

		return dmg;
	}
	
	public boolean isVisible() {
		return false;
	}
	
	public String getName() { return "tail"; }
}
