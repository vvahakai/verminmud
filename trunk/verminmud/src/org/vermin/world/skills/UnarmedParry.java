package org.vermin.world.skills;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.AttackPrevented;
import org.vermin.mudlib.battle.FailedAttack;
import org.vermin.mudlib.battle.Message;

/**
 * This class contains utility methods
 * for calculating whether an unarmed parry was succesful
 * or not.
 */
public class UnarmedParry {

   /**
    * Returns whether a Living is eligible for
    * a parry attempt.
    *
    * @param parryee  the living checking for parry eligibility 
    *
    * @return boolean indicating eligibility
    */
   public static boolean mayParry(Living parryee) {
      return true;
   }
   
   /**
    * Makes a parry attempt against an attack.
    *
    * @param defender  the Living making a parry attempt
    * @param attack  the attack the defender is attempting to parry
	 * @param factor scale parry success rate with this value
    *
    * @return a Message describing the outcome of the attempt
    */
   public static Message parry(Living defender, Attack attack, double factor) {
      Message result = null;

      if(attack instanceof FailedAttack) { // Don't bother to parry failed attacks
         return result;
      }

      int parriesPerTenThousand = (int) (factor * defender.getSkill("parry")*10);
      
      Item parryWeapon = getRandomLimb(defender);
      
      parriesPerTenThousand += defender.getPhysicalDexterity()*3 - attack.attacker.getPhysicalDexterity()*3;
      parriesPerTenThousand += defender.getPhysicalDexterity()*4; 
      parriesPerTenThousand -= attack.hitLocation*5;
      
      boolean success = Dice.random(10000) < parriesPerTenThousand;
      
      if(success) {
         result = new AttackPrevented();
         ((AttackPrevented) result).attackerMessage = defender.getName()+" skillfully parries your hit.";
         ((AttackPrevented) result).targetMessage = "You deftly parry the blow with your "+parryWeapon.getName()+".";
         ((AttackPrevented) result).spectatorMessage = defender.getName()+" skillfully parries "+attack.attacker.getName()+"'s blow with "+defender.getPossessive()+" "+parryWeapon.getName()+".";
      } else {
			return null;
      }
      
      return result;
   }
   
   private static Item getRandomLimb(Living defender) {

		int limb = Dice.random(defender.getRace().getLimbCount()) - 1;
		return defender.getRace().getLimb(limb);
	}
   
}
