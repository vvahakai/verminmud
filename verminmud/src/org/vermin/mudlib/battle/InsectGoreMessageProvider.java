/*
 * Created on Jul 17, 2004
 */
package org.vermin.mudlib.battle;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.battle.GoreProperty.*;
import org.vermin.util.Print;

import java.util.EnumSet;

/**
 * @author Jaakko Pohjamo
 */
public class InsectGoreMessageProvider extends AbstractGoreMessageProvider {

   
	private static InsectGoreMessageProvider _instance = new InsectGoreMessageProvider();
   
	protected InsectGoreMessageProvider() {
	   super(EnumSet.of(IS_INSECT, HAS_EXOSKELETON));
	}
   
	public static InsectGoreMessageProvider getInstance() {
	   return _instance;
	}
      
	public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
	   Damage damage = attack.getMaxDamage();
	   int maxHp = Math.max(who.getMaxHp(), 1);
	   int percentage = (int) ((damage.damage * 100) / maxHp);
	   String location = who.getRace().getHitLocation(hitLocation);
      
		if(percentage < 1)
			return "Your "+attack.weapon.getName()+" bounces off "+who.getName()+"'s tough shell, causing only minor scratches.";
		else if(percentage < 2)
			return Print.capitalize(who.getName())+" scuttles nimbly, avoiding your "+attack.weapon.getObjectHitVerb(damage.type)+" almost completely.";
		else if(percentage < 3)
			return "A few marks are left on "+who.getName()+"'s chitinous "+location+" by your "+attack.weapon.getObjectHitVerb(damage.type)+".";
		else if(percentage < 4)
			return "The "+attack.weapon.getName()+" lands on "+who.getName()+"'s "+location+" with a loud crunch, but the exoskeleton seems to hold.";
		else if(percentage < 5)
			return Print.capitalize(who.getName())+"'s numerous legs twitch in pain as your blow lands on "+who.getPossessive()+" "+location+".";
		else if(percentage < 6)
			return "The "+attack.weapon.getObjectHitVerb(damage.type)+" connects with "+who.getName()+"'s "+location+" causing a crack to appear on "+who.getPossessive()+" shell.";
		else if(percentage < 7)
			return "Your "+attack.weapon.getHitNoun(damage.type)+" at the "+who.getRace().getName()+", leaving "+who.getPossessive()+" "+location+" oozing green fluids.";
		else if(percentage < 9)
			return Print.capitalize(who.getName())+" quivers in pain as the "+attack.weapon.getObjectHitVerb(damage.type)+" rips through "+who.getPossessive()+" exoskeleton at "+location+".";
			
		else if(percentage < 12)
			return Print.capitalize(who.getName())+" drops from "+who.getPossessive()+" legs as your "+attack.weapon.getObjectHitVerb(damage.type)+" easilly penetrates "+who.getPossessive()+" tough carapace.";
		else if(percentage < 15)
			return "You drive the "+attack.weapon.getName()+" to "+who.getName()+"'s "+location+", causing a major squirt of viscous fluids to fly trough the air.";
		else if(percentage < 20)
			return Print.capitalize(who.getName())+" wraps "+who.getPossessive()+" legs around "+who.getPronoun()+" in agony as the swift "+attack.weapon.getObjectHitVerb(damage.type)+" crunches trough "+who.getPossessive()+" shell.";
		else if(percentage < 40)
			return "Chitinous exoskeleton at "+who.getName()+"'s "+location+" is ripped apart by the powerful "+attack.weapon.getObjectHitVerb(damage.type)+".";
		else if(percentage < 60)
			return "The powerful "+attack.weapon.getName()+" "+attack.weapon.getObjectHitVerb(damage.type)+" breaks "+who.getName()+"'s exoskeleton open, exposing the soft innards inside.";
		else if(percentage < 90)
			return Print.capitalize(who.getName())+"'s "+location+" is smashed into a mess of slime and chitin pieces by the ferocious "+attack.weapon.getObjectHitVerb(damage.type)+".";
		else
			return "Your devastating "+attack.weapon.getObjectHitVerb(damage.type)+" sends the crushed body of "+who.getName()+" flying trough the air, leaving a trail of sticky innards on the ground.";
	  }
  
	public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
		Living attacker = attack.attacker;

		Damage damage = attack.getMaxDamage();
		int maxHp = Math.max(who.getMaxHp(), 1);
		int percentage = (int) ((damage.damage * 100) / maxHp);
		String location = who.getRace().getHitLocation(hitLocation);
		return "You are an insect, and just suffered "+percentage+"% of damage. ";
/*
		if(percentage < 1)
			return "You hardly notice "+attacker.getPossessive()+"s "+attack.weapon.getObjectHitVerb(damage.type)+".";
		else if(percentage < 2)
			return Print.capitalize(attacker.getPossessive())+"s "+attack.weapon.getName()+" bounces off your "+location+", causing minor damage.";
		else if(percentage < 3)
		  return "The "+attack.weapon.getName()+" leaves a small bruise on your "+location+".";
		else if(percentage < 4)
			return "A few scratches are left on your "+location+" by "+attacker.getPossessive()+"s "+attack.weapon.getHitNoun(damage.type)+".";
		else if(percentage < 5)
			return "You flinch as "+attacker.getPossessive()+"s blow lands on your "+location+".";
		else if(percentage < 6)
		  return "The "+attack.weapon.getHitNoun(damage.type)+" connects with your "+location+" causing a small flesh wound.";
		else if(percentage < 7)
		   return "Your "+attack.weapon.getObjectHitVerb(damage.type)+" at the "+who.getRace().getName()+"s "+location+" leaves a nasty bleeding wound.";
		else if(percentage < 9)
		  return "You grunt as "+attacker.getPossessive()+" "+attack.weapon.getObjectHitVerb(damage.type)+" slices through your skin.";
		else if(percentage < 12)
		  return "Skin and muscle is torn apart by the "+attacker.getRace().getName()+"s powerful "+attack.weapon.getObjectHitVerb(damage.type)+" to your "+location+".";
		else if(percentage < 15)
		  return Print.capitalize(attacker.getName())+" drives "+attacker.getPossessive()+" "+attack.weapon.getName()+" to your "+location+", severing tendons and major blood vessels.";
		else if(percentage < 20)
		  return "You cry out in pain as the "+attacker.getRace().getName()+"s swift "+attack.weapon.getObjectHitVerb(damage.type)+" connects to your "+location+", penetrating to the bone.";
		else if(percentage < 40)
		  return Print.capitalize(attacker.getPossessive())+" powerful "+attack.weapon.getName()+" "+attack.weapon.getObjectHitVerb(damage.type)+" snaps several large bones and leaves your "+location+" bleeding.";
		else if(percentage < 60)
		  return "A mass of your blood and internal organs fall out as the "+attacker.getRace().getName()+" connects a mighty "+attack.weapon.getObjectHitVerb(damage.type)+" with "+attacker.getPossessive()+".";
		else if(percentage < 90)
		  return "Your "+location+" is reduced into a bloody mess of organs and broken bones by the devastating "+attack.weapon.getObjectHitVerb(damage.type)+".";
		else
		  return Print.capitalize(attacker.getName())+"s incredible "+attack.weapon.getObjectHitVerb(damage.type)+" turns your body into a broken mass of guts and blood.";
		  */
	}
   
}
