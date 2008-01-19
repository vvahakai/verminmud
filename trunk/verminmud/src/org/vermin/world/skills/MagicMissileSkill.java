package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class MagicMissileSkill extends Offensive {
   public MagicMissileSkill() {
     spCost = 15;
     rounds = 1;
     spellWords = "Deenina roBBafoBBa!";
     damageType = Damage.Type.MAGICAL;
     damage = 50;
     name = "magic missile";
	 skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.LOCAL };
   }}
