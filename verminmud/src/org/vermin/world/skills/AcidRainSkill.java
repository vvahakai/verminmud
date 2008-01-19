package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class AcidRainSkill extends AreaOffensive {
   public AcidRainSkill() {
     spCost = 132;
     rounds = 4;
     spellWords = "Sama acid 'Ah'Ra'Sa'H'rhas amsfas!";
     damageType = Damage.Type.ACID;
     damage = 421;
     name = "acid rain";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.AREA };

   }}
