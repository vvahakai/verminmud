package org.vermin.mudlib.battle;

import java.util.EnumSet;
import static org.vermin.mudlib.battle.GoreProperty.*;

import org.vermin.mudlib.Living; 
import org.vermin.mudlib.Damage;
import org.vermin.util.Print;

public class BloodGoreMessageProvider extends AbstractGoreMessageProvider {
   
   private static BloodGoreMessageProvider _instance = new BloodGoreMessageProvider();
   
   protected BloodGoreMessageProvider() {
      super(EnumSet.of(HAS_BLOOD));
   }
   
   public static BloodGoreMessageProvider getInstance() {
      return _instance;
   }
      
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
	   int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);
      
      if(percentage < 3)
            return "The "+attack.weapon.getHitNoun(damage.type)+" scrapes "+who.getName()+"'s "+location+", causing minor bleeding.";
      else if(percentage < 6)
            return "The "+attack.weapon.getHitNoun(damage.type)+" marks "+who.getName()+"'s "+location+" with a small bleeding wound.";
      else if(percentage < 9)
            return Print.capitalize(who.getName())+"'s "+location+" is left bleeding by the skilled "+attack.weapon.getHitNoun(damage.type)+".";
      else if(percentage < 12)
            return "Blood sprinkles on the ground as the "+attack.weapon.getHitNoun(damage.type)+" connects with "+who.getName()+"'s "+location+".";
      else if(percentage < 15)
            return "Blood squirts from a major wound left by the vicious "+attack.weapon.getHitNoun(damage.type)+" to "+who.getName()+"'s "+location+".";
      else if(percentage < 20)
            return Print.capitalize(who.getName())+" stumbles as the cunning "+attack.weapon.getHitNoun(damage.type)+" opens a blood surging wound.";
      else if(percentage < 40)
            return "The "+attack.weapon.getName()+" rips a gaping wound to "+who.getName()+"'s "+location+", causing a torrent of blood to gush out.";
      else if(percentage < 60)
            return "A rain of blood fills the air as the "+attack.weapon.getName()+" tears a large cavity into "+who.getName()+"'s "+location+".";
      else if(percentage < 90)
            return Print.capitalize(who.getName())+" crumples around the "+attack.weapon.getName()+", covering you and the ground with a flow of blood.";
      else if(percentage < 100)
            return Print.capitalize(attack.weapon.getName())+" brutally rips "+who.getName()+"'s "+location+" open, causing "+who.getPossessive()+" body to empty in a deluge of blood.";
      else
            return "The "+attack.weapon.getHitNoun(damage.type)+" hews "+who.getName()+" down with a single mighty blow, leaving a thick mist of blood lingering in the air.";
       
     }
  
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      Living attacker = attack.attacker;
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
      int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);
      
      if(percentage < 3)
            return "The "+attack.weapon.getHitNoun(damage.type)+" scrapes your "+location+", causing minor bleeding.";
      else if(percentage < 6)
            return "The "+attack.weapon.getHitNoun(damage.type)+" marks your "+location+" with a small bleeding wound.";
      else if(percentage < 9)
            return "Your "+location+" is left bleeding by the "+attacker.getRace().getName()+"'s "+attack.weapon.getHitNoun(damage.type)+".";
      else if(percentage < 12)
            return "Blood sprinkles on the ground as the "+attack.weapon.getHitNoun(damage.type)+" connects with your "+location+".";
      else if(percentage < 15)
            return "Blood squirts from a major wound left by "+attacker.getPossessive()+" vicious "+attack.weapon.getHitNoun(damage.type)+" to your "+location+".";
      else if(percentage < 20)
            return "You stumble as "+attacker.getName()+"'s cunning "+attack.weapon.getHitNoun(damage.type)+" opens a blood surging wound.";
      else if(percentage < 40)
            return Print.capitalize(attacker.getPossessive())+" "+attack.weapon.getName()+" rips a gaping wound to your "+location+", causing a torrent of blood to gush out.";
      else if(percentage < 60)
            return "A rain of blood fills the air as the "+attack.weapon.getName()+" tears a large cavity into your "+location+".";
      else if(percentage < 90)
            return "You crumple around the "+attacker.getRace().getName()+"'s "+attack.weapon.getName()+", covering "+who.getPronoun()+" and the ground with a flow of blood.";
      else if(percentage < 100)
            return Print.capitalize(attacker.getName())+" "+attack.weapon.getHitNoun(damage.type)+" brutally rips your "+location+" open, causing your body to empty in a deluge of blood.";
      else
            return Print.capitalize(attacker.getName())+" hews you down with a single mighty blow, leaving a thick mist of blood lingering in the air.";
   }
}
