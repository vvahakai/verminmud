package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class AcidBlastSkill extends Offensive {
   public AcidBlastSkill() {
     spCost = 246;
     rounds = 4;
     spellWords = "gurgle gob blah";
     damageType = Damage.Type.ACID;
     damage = 812;
     name = "acid blast";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.LOCAL };

   }}
