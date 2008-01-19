package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
public class CauseSeriousWoundsSkill extends Offensive {
   public CauseSeriousWoundsSkill() {
     spCost = 24;
     rounds = 2;
     spellWords = "Hrsta abtum";
     damageType = Damage.Type.PHYSICAL;
     damage = 72;
     name = "cause serious wounds";
   }
   	protected int getDamage(Living who, Living tgt) {
		return Math.max(who.getSkill("cause serious wounds"), damage);
	}   
   }
