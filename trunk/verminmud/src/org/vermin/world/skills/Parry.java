package org.vermin.world.skills;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.battle.*;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.WeaponType;

/**
 * This class contains utility methods
 * for calculating whether a parry was succesful
 * or not.
 */
public class Parry {

   /**
    * Returns whether a Living is eligible for
    * a parry attempt.
    *
    * @param parryee  the living checking for parry eligibility 
    *
    * @return boolean indicating eligibility
    */
   public static boolean mayParry(Living parryee) {
	   if(parryee.provides(LivingProperty.NO_SKILLS)) {
		   return false;
	   }
	   
      Wieldable[] weapons = parryee.getWieldedItems(true);

      for(int i=0; i<weapons.length; i++) {
         // Lisää if(!isStunned) kun on sellasia tehtynä
         if(mayParryWith(weapons[i])) {
               return true;
         }
      }
      
      return false;
   }
   
   /**
    * Makes a parry attempt against an attack.
    *
    * @param defender  the Living making a parry attempt
    * @param attack  the attack the defender is attempting to parry
	 * @param factor scale parry success rate with this value
    *
    * @return an AttackPrevented on success, null on failure
    */
   public static Reaction parry(Living defender, Attack attack, double factor) {
      Reaction result = null;

      if(attack instanceof FailedAttack) { // Don't bother to parry failed attacks
         return result;
      }

	  if(attack instanceof ProjectileAttack)
		  return null; // can't parry projectiles
	  
      int parriesPerTenThousand = (int) (factor * defender.getSkill("parry")*10);
      
      Wieldable parryWeapon = findBestDefensiveWieldable(defender);
      
      parriesPerTenThousand += defender.getPhysicalDexterity()*3 - attack.attacker.getPhysicalDexterity()*3;
      parriesPerTenThousand += parryWeapon.getDefensiveValue()*4; 
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
   
   private static Wieldable findBestDefensiveWieldable(Living defender) {
      Wieldable[] weapons = defender.getWieldedItems(true);
      Wieldable weapon = weapons[0];
      
      for(int i=0; i<weapons.length; i++) {
         if(mayParryWith(weapons[i])) {
            if(weapons[i].getDefensiveValue() > weapon.getDefensiveValue())
               weapon = weapons[i];
         }
      }
      
      return weapon;
   }
   
   private static boolean mayParryWith(Wieldable weapon) {
      return weapon.getWeaponType() == WeaponType.SWORD ||
             weapon.getWeaponType() == WeaponType.AXE ||
             weapon.getWeaponType() == WeaponType.BLUDGEON ||
             weapon.getWeaponType() == WeaponType.SHIELD ||
             weapon.getWeaponType() == WeaponType.DAGGER;
   }
}
