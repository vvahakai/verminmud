package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class ElectricitySkill extends Offensive {
   public ElectricitySkill() {
     spCost = 52;
     rounds = 3;
     spellWords = "tesla tosla tumps";
     damageType = Damage.Type.ELECTRIC;
     damage = 210;
     name = "electricity";
	skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ELECTRIC, SkillTypes.LOCAL };

   }}
