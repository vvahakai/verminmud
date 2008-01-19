package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class FaerieFireSkill extends Offensive {
   public FaerieFireSkill() {
     spCost = 82;
     rounds = 3;
     spellWords = "hinuri hanuri hompsis";
     damageType = Damage.Type.MAGICAL;
     damage = 10;
     name = "faerie fire";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.LOCAL };

   }}
