package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class CreateCompleteVacuumSkill extends AreaOffensive {
   public CreateCompleteVacuumSkill() {
     spCost = 321;
     rounds = 8;
     spellWords = "Aksefn feiue ASPHYXIATION!";
     damageType = Damage.Type.ASPHYXIATION;
     damage = 1322;
     name = "create complete vacuum";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ASPHYXIATION, SkillTypes.AREA };

   }}
			