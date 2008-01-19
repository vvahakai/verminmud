package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class GoldenArrowSkill extends Offensive {
   public GoldenArrowSkill() {
     spCost = 52;
     rounds = 3;
     spellWords = "Pwqmk masb eyihna GoLd!";
     damageType = Damage.Type.MAGICAL;
     damage = 210;
     name = "golden arrow";
	skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ACID, SkillTypes.LOCAL };
   }}
