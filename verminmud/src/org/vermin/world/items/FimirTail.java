/* FimirTail.java
	22.10.2004	VV
	
	An item representing a fimir tail as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class FimirTail extends DefaultWieldableImpl {
	
	private static final String OBJECT_HIT_VERBOSE = "sneakily swing your $weapon$";
	private static final String SUBJECT_HIT_VERBOSE = "sneakily swings $his$ $weapon$";
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target) {
		Damage[] dmg = new Damage[1];
		dmg[0] = new Damage();
		dmg[0].type = Damage.Type.CRUSHING;
		dmg[0].damage = 15;
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
	
	public String getName() { return "tail"; }
	
}
