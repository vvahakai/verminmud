package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class FlamestrikeSkill extends Offensive {
   public FlamestrikeSkill() {
     spCost = 60;
     rounds = 3;
     spellWords = "iFer ogam daalet";
     damageType = Damage.Type.FIRE;
     damage = 150;
     name = "flamestrike";
	 skillTypes = new SkillType[] { SkillTypes.DIVINE, SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.LOCAL };
   }
   	protected int getDamage(Living who, Living tgt) {
		return Math.max(who.getSkill("flamestrike")*2, damage);
	}   
   }
