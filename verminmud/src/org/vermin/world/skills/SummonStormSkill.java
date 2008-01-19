package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class SummonStormSkill extends AreaOffensive {
   public SummonStormSkill() {
     spCost = 132;
     rounds = 4;
     spellWords = "Sama electric 'Ah'Ra'Sa'H'rhas amsfas!";
     damageType = Damage.Type.ELECTRIC;
     damage = 421;
     name = "summon storm";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ELECTRIC, SkillTypes.AREA };

   }}
			