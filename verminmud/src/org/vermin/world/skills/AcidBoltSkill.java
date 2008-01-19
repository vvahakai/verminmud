package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class AcidBoltSkill extends Offensive {
   public AcidBoltSkill() {
     spCost = 120;
     rounds = 3;
     spellWords = "holoba moloba moo";
     damageType = Damage.Type.ACID;
     damage = 421;
     name = "acid bolt";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.LOCAL };

   }}
