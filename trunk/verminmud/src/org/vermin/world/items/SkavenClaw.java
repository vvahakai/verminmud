/* SkavenClaw.java
	19.1.2002	TT & VV
	
	An item representing a skaven claw as a melee weapon.
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class SkavenClaw extends DefaultWieldableImpl
{
	
	private static final String OBJECT_HIT_VERBOSE = "wildly shred using your bare $weapon$";
	private static final String SUBJECT_HIT_VERBOSE = "wildly shreds using $his$ bare $weapon$";
	
	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }
	
	public Damage[] getHitDamage(Living target)
	{
		if(dmg == null)
		{
			dmg = new Damage[1];
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.SLASHING;
			dmg[0].damage = 8;
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
	public String getName() { return "claw"; }
}
