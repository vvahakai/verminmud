package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class SummonLightningSkill extends AreaOffensive {
   public SummonLightningSkill() {
     spCost = 321;
     rounds = 8;
     spellWords = "Aksefn feiue ELECTRIC!";
     damageType = Damage.Type.ELECTRIC;
     damage = 1322;
     name = "summon lightning";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ELECTRIC, SkillTypes.AREA };

   }}
			