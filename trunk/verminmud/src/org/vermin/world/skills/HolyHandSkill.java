package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class HolyHandSkill extends Offensive {
	public HolyHandSkill() {
		spCost = 153+Dice.random(25);
		rounds = 3;
		skillTypes = new SkillType[] { SkillTypes.TEMPLAROFFENSIVE, SkillTypes.DIVINE, SkillTypes.OFFENSIVE, SkillTypes.LOCAL };
		damageType = Damage.Type.MAGICAL;
		damage = 312+Dice.random(40);
		name = "holy hand";
		spellWords = "Dath halin trall!";
	}
}