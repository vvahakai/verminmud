package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
public class CauseSmallWoundsSkill extends Offensive {
   public CauseSmallWoundsSkill() {
     spCost = 12;
     rounds = 1;
     spellWords = "Hrsta ytwhz";
     damageType = Damage.Type.PHYSICAL;
     damage = 32;
     name = "cause small wounds";
   }
   	protected int getDamage(Living who, Living tgt) {
		return Math.max(who.getSkill("cause small wounds")/2, damage);
	}   
   }
