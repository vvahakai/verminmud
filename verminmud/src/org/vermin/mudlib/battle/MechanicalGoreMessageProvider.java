/*
 * Created on 2.9.2006
 */
package org.vermin.mudlib.battle;

import static org.vermin.mudlib.battle.GoreProperty.IS_MECHANICAL;

import java.util.EnumSet;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.util.Print;

public class MechanicalGoreMessageProvider extends AbstractGoreMessageProvider {

	   private static MechanicalGoreMessageProvider _instance = new MechanicalGoreMessageProvider();
	   
	   protected MechanicalGoreMessageProvider() {
	      super(EnumSet.of(IS_MECHANICAL));
	   }
	   
	   public static MechanicalGoreMessageProvider getInstance() {
		      return _instance;
	   }
	   
	   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
		   
	      Damage damage = attack.getMaxDamage();
	      int maxHp = Math.max(who.getMaxHp(), 1);
		   int percentage = (int) ((damage.damage * 100) / maxHp);
	      String location = who.getRace().getHitLocation(hitLocation);
	      String hit = attack.weapon.getHitNoun(damage.type);
	      
	      
	      if(percentage < 1)
	            return "The "+hit+" glances off "+who.getName()+"'s surface, causing only minor scratches.";
	      else if(percentage < 2)
	            return "The "+hit+" scrapes "+who.getName()+"'s "+location+", leaving a small dent.";
	      else if(percentage < 4)
	            return Print.capitalize(who.getName())+"'s "+location+" is left slightly bent by the "+hit+".";
	      else if(percentage < 6)
	            return "Some parts fly on to the ground as the "+hit+" connects with "+who.getName()+"'s "+location+".";
	      else if(percentage < 14)
	            return "A shower of sparks fly from a major hole left by the vicious "+hit+" to "+who.getName()+"'s "+location+".";
	      else if(percentage < 22)
	            return Print.capitalize(who.getName())+" gears groan as the powerful "+hit+" wrecks its structure.";
	      else if(percentage < 37)
	            return "The "+attack.weapon.getName()+" rips a gaping hole to "+who.getName()+"'s "+location+", causing parts to fly out.";
	      else if(percentage < 60)
	    	  return Print.capitalize(who.getName())+" stutters and groans as the "+attack.weapon.getName()+" pierces into its internal workings.";
	      else if(percentage < 80)
	            return "Flying gears and fluid fill the area as the "+attack.weapon.getName()+" shatters "+who.getName()+"'s "+location+".";
	      else if(percentage < 100)
	            return "The "+attack.weapon.getName()+" brutally rips "+who.getName()+" apart, causing a shower of sparks and shattered parts.";
	      else
	            return "The "+attack.weapon.getName()+" "+hit+" destroys "+who.getName()+" into spare parts in a single, mighty blow.";
	 	}

	public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
		
	      Living attacker = attack.attacker;
	      Damage damage = attack.getMaxDamage();
	      int maxHp = Math.max(who.getMaxHp(), 1);
	      int percentage = (int) ((damage.damage * 100) / maxHp);
	      String location = who.getRace().getHitLocation(hitLocation);
	      String hit = attack.weapon.getHitNoun(damage.type);
	      
	      if(percentage < 1)
	            return "The "+hit+" glances off your surface, only a minor scratch.";
	      else if(percentage < 2)
	            return "The "+hit+" scrapes your "+location+", leaving a small dent.";
	      else if(percentage < 4)
	            return "Your "+location+" is left slightly bent by the "+hit+".";
	      else if(percentage < 6)
	            return "Some of your parts fly on to the ground as the "+hit+" connects with your "+location+".";
	      else if(percentage < 14)
	            return "A shower of sparks fly from a major hole left by the vicious "+hit+" to your "+location+".";
	      else if(percentage < 37)
	            return "The "+attack.weapon.getName()+" rips a gaping hole to your "+location+", causing parts to fly out.";
	      else if(percentage < 60)
	    	    return "You stutter and groan as the "+attack.weapon.getName()+" pierces into your internal workings.";
	      else if(percentage < 80)
	            return "Your gears and fluid fill the area as the "+attack.weapon.getName()+" shatters your "+location+".";
	      else if(percentage < 100)
	            return "The "+attack.weapon.getName()+" brutally rips you apart, causing a shower of sparks and shattered parts.";
	      else
	            return "The "+attack.weapon.getName()+" "+hit+" destroys you into spare parts in a single, mighty blow.";
	}
}
