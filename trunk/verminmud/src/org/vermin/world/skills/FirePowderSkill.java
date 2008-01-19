package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class FirePowderSkill extends Offensive {
   public FirePowderSkill() {
     spCost = 40;
     rounds = 2;
     spellWords = "das bist eine powder bomb";
     damageType = Damage.Type.FIRE;
     damage = 100+Dice.random(20);
     name = "fire powder";
	 skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.LOCAL, SkillTypes.ARCANE };
   }
   	protected int getDamage(Living who, Living tgt) {
		return Math.max(who.getSkill("fire powder"), damage);
	}   
   }
