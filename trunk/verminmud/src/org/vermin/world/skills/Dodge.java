package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.AttackPrevented;
import org.vermin.mudlib.battle.FailedAttack;
import org.vermin.mudlib.battle.Reaction;
import org.vermin.util.Print;
/**
 * This class contains utility methods
 * for calculating whether a dodge was succesful
 * or not.
 */
public class Dodge {

   /**
    * Returns whether a Living is eligible for
    * a dodge attempt.
    *
    * @param dodger  the living checking for dodge eligibility 
    *
    * @return boolean indicating eligibility
    */
   public static boolean mayDodge(Living dodger) {
	   if(dodger.provides(LivingProperty.NO_SKILLS)) {
		   return false;
	   }
      return true;
   }
   
   /**
    * Makes a dodge attempt against an attack.
    *
    * @param defender  the Living making a dodge attempt
    * @param attack  the attack the defender is attempting to dodge
    * @param factor  scale the success rate with this factor
    *
    * @return an AttackPrevented on success, null on failure
    */
   public static Reaction dodge(Living defender, Attack attack, double factor) {
      Reaction result = null;

      if(attack instanceof FailedAttack) { // Don't bother to dodge failed attacks
         return result;
      }

      int dodgesPerTenThousand = (int) (factor * defender.getSkill("dodge")*8);
      
      dodgesPerTenThousand += defender.getPhysicalDexterity()*6 - attack.attacker.getPhysicalDexterity()*3;
      dodgesPerTenThousand -= attack.hitLocation*5;
      dodgesPerTenThousand -= (250 - defender.getSize()*5);
      
      int weightModifier = calculateWeightModifier(defender);
      dodgesPerTenThousand -= weightModifier;
      
      int diceThrow = Dice.random(10000);
    
      boolean success = diceThrow < dodgesPerTenThousand;
      
      if(success) {
         result = new AttackPrevented();
         ((AttackPrevented) result).attackerMessage = Print.capitalize(defender.getName())+" nimbly dodges your hit.";
         ((AttackPrevented) result).targetMessage = "You nimbly dodge the blow.";
         ((AttackPrevented) result).spectatorMessage = Print.capitalize(defender.getName())+" nimbly dodges "+attack.attacker.getName()+"'s blow.";
      } else {
			return null;
      }
      
      return result;
   }
   
   private static int calculateWeightModifier(Living defender) {
	  if(defender == null) { return 0; }
      Iterator inventory = defender.findByType(Types.TYPE_ITEM);
      if(inventory == null) { return 0; }
      
      int weight = 0;
      while(inventory.hasNext()) {
         Item item = (Item) inventory.next();
			//System.out.println("Dodge item: "+item+", material: "+item.getMaterial());
         if(item.getMaterial() == null) { continue; }
         weight += item.getSize() * item.getMaterial().getWeight();
      }
      
      int modifier = (weight/1000) - defender.getPhysicalStrength();
      
      if(modifier > 0) {
         return modifier*10;
      } else {
         return 0;
      }
   }
}
