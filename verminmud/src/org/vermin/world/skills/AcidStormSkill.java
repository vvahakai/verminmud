package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class AcidStormSkill extends AreaOffensive {
   public AcidStormSkill() {
     spCost = 321;
     rounds = 8;
     spellWords = "Aksefn feiue ACID!";
     damageType = Damage.Type.ACID;
     damage = 1322;
     name = "acid storm";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.AREA };

   }}
			