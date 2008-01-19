package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.ProjectileAttack;
import org.vermin.mudlib.battle.Reaction;

public class Riposte {
   
   public static boolean mayRiposte(Living defender, Attack attack) {
	   if(defender.provides(LivingProperty.NO_SKILLS)) {
		   return false;
	   }
	   if(attack instanceof ProjectileAttack)
		   return false;
	   else
		   return true;
   }
   
   public static void riposte(Living defender, Living attacker, Reaction reaction) {
      if((Dice.random() + defender.getPhysicalDexterity())/2 < defender.getSkill("riposte") ) {
         reaction.attackerMessage = reaction.attackerMessage+".. and performs a quick counter strike.";
         reaction.targetMessage = reaction.targetMessage+".. and perform a quick counter strike.";
         reaction.spectatorMessage = reaction.spectatorMessage+".. and performs a quick counter strike.";
         reaction.counterAttack = new Attack();
         reaction.counterAttack.attacker = defender;
         reaction.counterAttack.weapon = defender.getWieldedItems(true)[0];
         Damage [] baseDamage = reaction.counterAttack.weapon.getHitDamage(attacker);
         reaction.counterAttack.damage = new Damage[baseDamage.length];
         
         for(int i=0; i<reaction.counterAttack.damage.length; i++) {
                reaction.counterAttack.damage[i] = new Damage();
                reaction.counterAttack.damage[i].type = baseDamage[i].type;
                reaction.counterAttack.damage[i].damage = baseDamage[i].damage;
                
                reaction.counterAttack.damage[i].damage *= defender.getPhysicalStrength();   
         }
         
      } else if(defender.checkSkill("riposte") > 0) {
         reaction.attackerMessage = reaction.attackerMessage+".. and tries to riposte but fails.";
         reaction.targetMessage = reaction.targetMessage+".. and try to riposte, but fail.";
         reaction.spectatorMessage = reaction.spectatorMessage+".. and tries to riposte but fails.";
      }
   }
}
