package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class FireStormSkill extends AreaOffensive {

	public FireStormSkill() {
	     spCost = 321;
	     rounds = 8;
	     spellWords = "Khemir Flez FIRE!";
	     damageType = Damage.Type.FIRE;
	     damage = 1322;
	     name = "fire storm";
		  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.AREA };

	}

}
