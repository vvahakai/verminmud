/*
 * Created on 6.1.2006
 */
package org.vermin.mudlib.battle;

import java.util.EnumSet;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.util.Print;

public class SpeakGoreMessageProvider extends AbstractGoreMessageProvider {

   private static SpeakGoreMessageProvider _instance = new SpeakGoreMessageProvider();
	   
   protected SpeakGoreMessageProvider() {
      super(EnumSet.of(GoreProperty.VOICE_SPEAKS));
   }
   
   public static SpeakGoreMessageProvider getInstance() {
      return _instance;
   }
	   
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
	      Damage damage = attack.getMaxDamage();
	      int maxHp = Math.max(who.getMaxHp(), 1);
		   int percentage = (int) ((damage.damage * 100) / maxHp);
	      String location = who.getRace().getHitLocation(hitLocation);
	      Living attacker = attack.attacker;
	      
	      if(percentage < 3)
	            return "The "+attack.weapon.getHitNoun(damage.type)+" strikes "+who.getName()+"'s "+location+", making "+who.getPronoun()+" grunt.";
	      else if(percentage < 6)
	            return Print.capitalize(who.getName())+" curses as the "+attack.weapon.getHitNoun(damage.type)+" connects with "+who.getPossessive()+" "+location+".";
	      else if(percentage < 9)
	            return Print.capitalize(who.getName())+" screams: 'Have at you, "+attacker.getName()+"'.";
	      else if(percentage < 12)
	            return Print.capitalize(who.getName())+" yells in rage as you strike "+who.getPossessive()+" "+location+".";
	      else if(percentage < 15)
	            return Print.capitalize(who.getName())+" yells: 'I will get you for that, "+attacker.getName()+"'.";
	      else if(percentage < 20)
	            return "You connect with "+Print.capitalize(who.getName())+"'s "+location+", making "+who.getPronoun()+" screams obscenities.";
	      else if(percentage < 40)
	            return Print.capitalize(who.getName())+" pleads: '"+attacker.getName()+" can't we handle this by talking?'";
	      else if(percentage < 60)
	            return Print.capitalize(who.getName())+" cries in horror: 'Oh no, i'm going to die!'";
	      else if(percentage < 95)
	            return Print.capitalize(who.getName())+" tries to beg for mercy, but your vicious blow to "+who.getPossessive()+" "+location+" silences "+who.getPronoun()+".";
	      else
	            return Print.capitalize(who.getName())+" quietly gurgles "+who.getPossessive()+" last prayers.";
	       
	     }
	  
	   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
	      Living attacker = attack.attacker;
	      Damage damage = attack.getMaxDamage();
	      int maxHp = Math.max(who.getMaxHp(), 1);
	      int percentage = (int) ((damage.damage * 100) / maxHp);
	      String location = who.getRace().getHitLocation(hitLocation);

	      if(percentage < 3)
	            return "The "+attack.weapon.getHitNoun(damage.type)+" strikes your "+location+", making you grunt.";
	      else if(percentage < 6)
	            return "You curse as the "+attack.weapon.getHitNoun(damage.type)+" connects with your "+location+".";
	      else if(percentage < 9)
	            return "You scream at "+attacker.getName()+" in pain.";
	      else if(percentage < 12)
	            return "You yell in rage as "+Print.capitalize(who.getName())+" strikes your "+location+".";
	      else if(percentage < 15)
	            return "You yell at "+attacker.getName()+" in agony.";
	      else if(percentage < 20)
	            return Print.capitalize(attacker.getName())+" connects with "+attacker.getPossessive()+" "+attack.weapon.getHitNoun(damage.type)+", making you screams obscenities.";
	      else if(percentage < 75)
	            return "You break down and plead for "+Print.capitalize(attacker.getName())+"'s mercy.";
	      else if(percentage < 90)
	            return "You try to beg for mercy, but "+who.getName()+"'s vicious blow to your "+location+" silences you.";
	      else
	            return "You quietly gurgle your last prayers.";	      
	   }
}
