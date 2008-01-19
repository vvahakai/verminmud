package org.vermin.mudlib.battle;

//import org.vermin.mudlib.skills.TemplarSkills;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;
import org.vermin.mudlib.WeaponType;
import org.vermin.mudlib.Wieldable;

public class StandGroundBattleStyle extends SwordAndShieldBattleStyle {

	public StandGroundBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public StandGroundBattleStyle() {}

	public String getName() {
		return "stand ground";
	}

	public Effector[] getHitEffectors() {
		return new Effector[] {
			SKILL(5, "combat discipline",true),
			STAT(5, PHYS_DEX, true)
		};
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(4, PHYS_STR, true),
			STAT(3, PHYS_DEX, true),
			SKILL(6, "sword and shield techniques",true),
			SKILL(5, "combat discipline",true),
			SKILL(0, "stand ground",true),
			DICE(2)
		};
	}
	
	public Effector[] getDefensiveHitEffectors() {
		return new Effector[] {

			STAT(3, PHYS_DEX, true),
			SKILL(6, "sword and shield techniques",true),
			SKILL(5, "combat discipline",true),
			SKILL(0, "stand ground",true),
			DICE(2)
		};
	}
	
   public boolean tryUse() {
		Wieldable[] wielded = owner.getWieldedItems(false);
		if(wielded == null)
			return false;

		for(int i=0; i<wielded.length; i++) {
			if(wielded[i] == null) continue;
			if(wielded[i].getWeaponType() == WeaponType.SHIELD)
				return true;
		}
		owner.notice("&3;You revert to uneducated fighting.");
		return false;

	}

}
