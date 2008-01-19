package org.vermin.mudlib.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.*;

public class MonkSkills {

   public static boolean mayRedirectDamage(Living defender) {
		if(defender.checkSkill("pendulum movement") > 0)
			return true;
		else
			return false;
   }
   
   public static void redirectDamage(Living defender, Living attacker, DamageTaken reaction) {
      if((Math.max(0, defender.checkSkill("redirect damage")) + defender.getPhysicalDexterity())/2 > Dice.random()) {

			Damage[] dmg = reaction.damage;
			for(int i=0; i<dmg.length; i++)
				dmg[i].damage = dmg[i].damage * Dice.random(defender.getSkill("redirect damage")) / 100;

         reaction.attackerMessage = reaction.attackerMessage+".. and redirects damage.";
         reaction.targetMessage = reaction.targetMessage+".. and redirects damage.";
         reaction.spectatorMessage = reaction.spectatorMessage+".. and redirects damage.";
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
			
      } else if(Dice.random() < defender.getSkill("redirect damage")) {
         reaction.attackerMessage = reaction.attackerMessage+".. and does a pendulum movement but fails to redirect damage.";
         reaction.targetMessage = reaction.targetMessage+".. and does a pendulun movement but fails to redirect damage.";
         reaction.spectatorMessage = reaction.spectatorMessage+".. and does a pendulun movement but fails to redirect damage.";
      }
   }
	
	public static int chiFlow(Living actor) {
		
		if(actor.checkSkill("chi flow") > 0) {
			return (int)
				(actor.getPhysicalDexterity() +
				 actor.getMentalStrength() +
				 actor.getSkill("chi flow")*5 +
				 actor.getMentalDexterity()) / 8;

		} else return 0;
	}

	public static void pressurePointManipulation(Living actor, Living target) {
		String style = actor.getBattleStyle().getName();
		if((style.equals("dragon") || style.equals("mantis") || style.equals("tiger")) && 
			actor.checkSkill("pressure point manipulation") > 0) {

			Damage d = new Damage();
			d.type = Damage.Type.STUN;
			d.damage = (100 * actor.getMentalDexterity()) / (20 + Dice.random(50));
			target.subHp(d, actor);
			actor.notice("Your accurate blow hits a critical nerve ending causing a stunning effect.");
		}
	}
}
