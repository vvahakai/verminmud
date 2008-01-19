package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.world.skills.AreaOffensive;

public class PurgatorySkill extends AreaOffensive {
   public PurgatorySkill() {
     spCost = 450;
     rounds = 3+Dice.random(2);
     spellWords = "Fiat iustitia, pereat monstruosus!";
     damageType = Damage.Type.MAGICAL;
     damage = 1500+Dice.random(150);
     name = "purgatory";
	  skillTypes = new SkillType[] { SkillTypes.TEMPLAROFFENSIVE, SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.MAGICAL, SkillTypes.AREA };

   }}
