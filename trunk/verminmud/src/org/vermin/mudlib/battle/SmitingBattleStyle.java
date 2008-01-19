package org.vermin.mudlib.battle;

import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.WeaponType;
import org.vermin.mudlib.Wieldable;

public class SmitingBattleStyle extends DefaultBattleStyle {

	public SmitingBattleStyle(Living owner) {
		super(owner);
	}

	public SmitingBattleStyle() {}
	
	public String getName() {
		return "smiting";
	}
	
	public Effector[] getHitEffectors() {
		return new Effector[] {
			SKILL(5, "combat discipline",true),
			SKILL(0, "bludgeon mastery",true),
			STAT(5, PHYS_DEX, true)
		};
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(6, PHYS_STR, true),
			STAT(3, PHYS_DEX, true),
			WEAPONDAM(0),
			SKILL(0, "blungeon mastery",true),
			SKILL(5, "combat discipline",true),
			DICE(3)
		};
	}

	public Effector[] getDefensiveHitEffectors() {
		return new Effector[] {

			STAT(3, PHYS_DEX, true),
			SKILL(5, "combat discipline",true),
			DICE(2)
		};
	}
	
	  public boolean tryUse(Living target) {
			Wieldable[] wielded = owner.getWieldedItems(false);
			if(wielded == null)
				return false;

	        boolean haveBludgeon = false;

			for(int i=0; i<wielded.length; i++) {
				if(wielded[i] == null) continue;
				if(wielded[i].getWeaponType() == WeaponType.BLUDGEON)
					haveBludgeon = true;
			}
			if(haveBludgeon)
				return true;
			else {
				owner.notice("&3;You revert to uneducated fighting.");
				return false;
			}
		}
	
}
