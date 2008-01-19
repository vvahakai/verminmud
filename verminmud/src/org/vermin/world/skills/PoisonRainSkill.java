package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class PoisonRainSkill extends AreaOffensive {
   public PoisonRainSkill() {
     spCost = 132;
     rounds = 4;
     spellWords = "Sama poison 'Ah'Ra'Sa'H'rhas amsfas!";
     damageType = Damage.Type.POISON;
     damage = 421;
     name = "poison rain";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.POISON, SkillTypes.AREA };

   }}
			