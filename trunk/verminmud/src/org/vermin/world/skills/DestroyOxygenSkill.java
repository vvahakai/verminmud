package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class DestroyOxygenSkill extends AreaOffensive {
   public DestroyOxygenSkill() {
     spCost = 132;
     rounds = 4;
     spellWords = "Sama asphyxiation 'Ah'Ra'Sa'H'rhas amsfas!";
     damageType = Damage.Type.ASPHYXIATION;
     damage = 421;
     name = "destroy oxygen";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ASPHYXIATION, SkillTypes.AREA };

   }}
			