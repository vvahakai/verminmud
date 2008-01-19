package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class IceBlastSkill extends Offensive {
   public IceBlastSkill() {
     spCost = 44;
     rounds = 2+Dice.random(2);
     spellWords = "arDum occultum";
     damageType = Damage.Type.COLD;
     damage = 172;
     name = "ice blast";
	 skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.LOCAL };
   }}
