/* FimirHand.java
	22.10.2004	VV
	
	An item representing a fimir hand as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class FimirHand extends DefaultWieldableImpl {
	
	private static final String OBJECT_HIT_VERBOSE = "wildly shred using your bare $weapon$";
	private static final String SUBJECT_HIT_VERBOSE = "wildly shreds using $his$ bare $weapon$";
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		Damage[] dmg = new Damage[1];
		dmg[0] = new Damage();
		dmg[0].type = Damage.Type.CRUSHING;
		dmg[0].damage = 9;
		return dmg;
	}

	public String getObjectHitVerbose(int hit)
	{
		return OBJECT_HIT_VERBOSE;
	}
	
	public String getSubjectHitVerbose(int hit)
	{
		return SUBJECT_HIT_VERBOSE;
	}
	
	public boolean isVisible() {
		return false;
	}
	
	public String getName() { return "hand"; }
}
