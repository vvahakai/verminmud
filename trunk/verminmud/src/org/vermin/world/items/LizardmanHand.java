/* LizardmanHand.java
	13.9.2003	MV
	
	An item representing a lizardman hand as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class LizardmanHand extends DefaultWieldableImpl
{
	
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target)
	{
		if(dmg == null)
		{
			dmg = new Damage[1];
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.CRUSHING;
			dmg[0].damage = 8;
		}
		
		return dmg;
	}
	public boolean isVisible() {
		return false;
	}
	public String getName() { return "fist"; }
}
