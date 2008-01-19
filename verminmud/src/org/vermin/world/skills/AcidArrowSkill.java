package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class AcidArrowSkill extends Offensive {
   public AcidArrowSkill() {
     spCost = 52;
     rounds = 3;
     spellWords = "acidos sodom";
     damageType = Damage.Type.ACID;
     damage = 210;
     name = "acid arrow";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.LOCAL };

   }}
