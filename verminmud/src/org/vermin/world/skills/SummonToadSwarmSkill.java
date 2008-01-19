package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class SummonToadSwarmSkill extends AreaOffensive {
   public SummonToadSwarmSkill() {
     spCost = 321;
     rounds = 8;
     spellWords = "Aksefn feiue POISON!";
     damageType = Damage.Type.POISON;
     damage = 1322;
     name = "summon toad swarm";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.POISON, SkillTypes.AREA };

   }}
			