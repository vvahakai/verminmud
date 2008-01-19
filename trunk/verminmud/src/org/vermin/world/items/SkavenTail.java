/* SkavenTail.java
	19.1.2002	TT & VV
	
	An item representing a skaven tail as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class SkavenTail extends DefaultWieldableImpl
{
	
	private static final String OBJECT_HIT_VERBOSE = "sneakily swing your $weapon$";
	private static final String SUBJECT_HIT_VERBOSE = "sneakily swings $his$ $weapon$";

	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target)
	{
		if(dmg == null)
		{
			dmg = new Damage[1];
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.SLASHING;
			dmg[0].damage = 1;
		}
		
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
