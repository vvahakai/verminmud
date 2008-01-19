package org.vermin.mudlib.battle;

import java.util.EnumSet;
import static org.vermin.mudlib.battle.GoreProperty.*;

import org.vermin.mudlib.Living; 
import org.vermin.mudlib.Damage;
import org.vermin.util.Print;

public class MammalGoreMessageProvider extends AbstractGoreMessageProvider {
   
   private static MammalGoreMessageProvider _instance = new MammalGoreMessageProvider();
   
   protected MammalGoreMessageProvider() {
      super(EnumSet.of(HAS_BLOOD, HAS_BONES, HAS_INTERNAL_ORGANS));
   }
   
   public static MammalGoreMessageProvider getInstance() {
      return _instance;
   }
      
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
      int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);
      
      if(percentage < 1)
			return Print.capitalize(who.getName())+" hardly notices your "+attack.weapon.getHitNoun(damage.type)+".";
		else if(percentage < 2)
	   	return "Your "+attack.weapon.getName()+" bounces off "+who.getName()+"'s "+location+", causing minor damage.";
      else if(percentage < 3)
         return "The "+attack.weapon.getName()+" leaves a small bruise on "+who.getName()+"'s "+location+".";
		else if(percentage < 4)
		   return "A few scratches are left on "+who.getName()+"'s "+location+" by your "+attack.weapon.getHitNoun(damage.type)+".";
		else if(percentage < 5)
		   return Print.capitalize(who.getName())+" flinches as your blow lands on "+who.getPossessive()+" "+location+".";
      else if(percentage < 6)
			return "The "+attack.weapon.getObjectHitVerb(damage.type)+" connects with "+who.getName()+"'s "+location+" causing a small flesh wound.";
		else if(percentage < 7)
		   return "Your "+attack.weapon.getHitNoun(damage.type)+" at the "+who.getRace().getName()+"'s "+location+" leaves a nasty bleeding wound.";
      else if(percentage < 9)
         return Print.capitalize(who.getName())+" grunts as the "+attack.weapon.getHitNoun(damage.type)+" rips through "+who.getPossessive()+" skin.";
      else if(percentage < 12)
         return "Skin and muscle is torn apart by the powerful "+attack.weapon.getHitNoun(damage.type)+" to "+who.getName()+"'s "+location+".";
      else if(percentage < 15)
         return "You drive the "+attack.weapon.getName()+" to "+who.getName()+"'s "+location+", severing tendons and major blood vessels.";
      else if(percentage < 20)
         return Print.capitalize(who.getName())+" cries out in pain as the swift "+attack.weapon.getHitNoun(damage.type)+" connects with "+who.getName()+"'s "+location+", penetrating to the bone.";
      else if(percentage < 40)
         return "The powerful "+attack.weapon.getName()+" "+attack.weapon.getHitNoun(damage.type)+" snaps several large bones and leaves "+who.getName()+"'s "+location+" bleeding.";
      else if(percentage < 60)
         return "A mass of blood and internal organs fall out as you connect a mighty "+attack.weapon.getHitNoun(damage.type)+" with your "+attack.weapon.getName()+".";
      else if(percentage < 90)
         return Print.capitalize(who.getName())+"'s "+location+" is reduced to a bloody mess of organs and broken bones by the devastating "+attack.weapon.getHitNoun(damage.type)+".";
      else
         return "Your incredible "+attack.weapon.getHitNoun(damage.type)+" turns "+who.getName()+"'s body into a broken mass of guts and blood.";
     }
  
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      Living attacker = attack.attacker;
      
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
      int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);

		if(percentage < 1)
		  return "You hardly notice "+attacker.getPossessive()+" "+attack.weapon.getHitNoun(damage.type)+".";
	   else if(percentage < 2)
		  return Print.capitalize(attacker.getPossessive())+" "+attack.weapon.getName()+" bounces off your "+location+", causing minor damage.";
		else if(percentage < 3)
         return "The "+attack.weapon.getName()+" leaves a small bruise on your "+location+".";
		else if(percentage < 4)
		   return "A few scratches are left on your "+location+" by "+attacker.getPossessive()+" "+attack.weapon.getHitNoun(damage.type)+".";
		else if(percentage < 5)
		   return "You flinch as "+attacker.getPossessive()+" blow lands on your "+location+".";
      else if(percentage < 6)
         return "The "+attack.weapon.getHitNoun(damage.type)+" connects with your "+location+" causing a small flesh wound.";
		else if(percentage < 7)
	 	  return Print.capitalize(attacker.getName())+"'s "+attack.weapon.getHitNoun(damage.type)+" at your "+location+" leaves a nasty bleeding wound.";
      else if(percentage < 9)
         return "You grunt as "+attacker.getPossessive()+" "+attack.weapon.getHitNoun(damage.type)+" slices through your skin.";
      else if(percentage < 12)
         return "Skin and muscle is torn apart by the "+attacker.getRace().getName()+"'s powerful "+attack.weapon.getHitNoun(damage.type)+" to your "+location+".";
      else if(percentage < 15)
         return Print.capitalize(attacker.getName())+" drives "+attacker.getPossessive()+" "+attack.weapon.getName()+" to your "+location+", severing tendons and major blood vessels.";
      else if(percentage < 20)
         return "You cry out in pain as the "+attacker.getRace().getName()+"'s swift "+attack.weapon.getHitNoun(damage.type)+" connects to your "+location+", penetrating to the bone.";
      else if(percentage < 40)
         return Print.capitalize(attacker.getPossessive())+" powerful "+attack.weapon.getName()+" "+attack.weapon.getObjectHitVerb(damage.type)+" snaps several large bones and leaves your "+location+" bleeding.";
      else if(percentage < 60)
         return "A mass of your blood and internal organs fall out as the "+attacker.getRace().getName()+" connects a mighty "+attack.weapon.getHitNoun(damage.type)+" with "+attacker.getPossessive()+".";
      else if(percentage < 90)
         return "Your "+location+" is reduced into a bloody mess of organs and broken bones by the devastating "+attack.weapon.getHitNoun(damage.type)+".";
      else
         return Print.capitalize(attacker.getName())+"'s incredible "+attack.weapon.getHitNoun(damage.type)+" turns your body into a broken mass of guts and blood.";
   }
   
}
