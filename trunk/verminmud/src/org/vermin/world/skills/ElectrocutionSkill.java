package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class ElectrocutionSkill extends Offensive {
   public ElectrocutionSkill() {
     spCost = 246;
     rounds = 4;
     spellWords = "raah poks muuntaja";
     damageType = Damage.Type.ELECTRIC;
     damage = 812;
     name = "electrocution";
	skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ELECTRIC, SkillTypes.LOCAL };

   }}
