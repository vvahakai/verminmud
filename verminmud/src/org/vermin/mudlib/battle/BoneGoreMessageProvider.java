package org.vermin.mudlib.battle;

import java.util.EnumSet;
import static org.vermin.mudlib.battle.GoreProperty.*;

import org.vermin.mudlib.Living; 
import org.vermin.mudlib.Damage;
import org.vermin.util.Print;

public class BoneGoreMessageProvider extends AbstractGoreMessageProvider {
   
   private static BoneGoreMessageProvider _instance = new BoneGoreMessageProvider();
   
   protected BoneGoreMessageProvider() {
      super(EnumSet.of(HAS_BONES));
   }
   
   public static BoneGoreMessageProvider getInstance() {
      return _instance;
   }
      
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
	   int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);
      
      if(percentage < 3)
            return "The "+attack.weapon.getHitNoun(damage.type)+" scrapes "+who.getName()+"'s "+location+", causing minor fractures.";
      else if(percentage < 6)
            return "The "+attack.weapon.getHitNoun(damage.type)+" marks "+who.getName()+"'s "+location+" with a small fracture.";
      else if(percentage < 9)
            return Print.capitalize(who.getName())+"'s "+location+" is left dislocated by the skilled "+attack.weapon.getHitNoun(damage.type)+".";
      else if(percentage < 12)
            return "Bones snap as the "+attack.weapon.getHitNoun(damage.type)+" connects with "+who.getName()+"'s "+location+".";
      else if(percentage < 15)
            return "Some bones are left broken by the vicious "+attack.weapon.getHitNoun(damage.type)+" to "+who.getName()+"'s "+location+".";
      else if(percentage < 20)
            return Print.capitalize(who.getName())+" stumbles as the cunning "+attack.weapon.getHitNoun(damage.type)+" breaks through bones.";
      else if(percentage < 40)
            return "The "+attack.weapon.getName()+" breaks a large wound to "+who.getName()+"'s "+location+", causing a bones to splinter.";
      else if(percentage < 60)
            return "Shattered bone fragments fill the air as the "+attack.weapon.getName()+" tears a large wound into "+who.getName()+"'s "+location+".";
      else if(percentage < 90)
            return Print.capitalize(who.getName())+"'s bones pulverize around the "+attack.weapon.getName()+", covering you and the ground with a pieces of bone.";
      else if(percentage < 100)
            return "You brutally rip "+who.getName()+"'s "+location+" asunder, causing "+who.getPossessive()+" bones to turn to dust.";
      else
            return "You hew "+who.getName()+" down with a single mighty blow, leaving a thick cloud of dust lingering in the air.";
       
     }
  
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      Living attacker = attack.attacker;
      Damage damage = attack.getMaxDamage();
      int maxHp = Math.max(who.getMaxHp(), 1);
      int percentage = (int) ((damage.damage * 100) / maxHp);
      String location = who.getRace().getHitLocation(hitLocation);
      
      if(percentage < 3)
            return "The "+attack.weapon.getHitNoun(damage.type)+" scrapes your "+location+", causing minor fractures.";
      else if(percentage < 6)
            return "The "+attack.weapon.getHitNoun(damage.type)+" marks your "+location+" with a small fracture.";
      else if(percentage < 9)
            return "Your "+location+" is left dislocated by the "+attacker.getRace().getName()+"'s "+attack.weapon.getHitNoun(damage.type)+".";
      else if(percentage < 12)
            return "Your bones snap as the "+attack.weapon.getHitNoun(damage.type)+" connects with your "+location+".";
      else if(percentage < 15)
            return "Some of your bones are left broken by "+attacker.getPossessive()+" vicious "+attack.weapon.getHitNoun(damage.type)+" to your "+location+".";
      else if(percentage < 20)
            return "You stumble as "+attacker.getName()+"'s cunning "+attack.weapon.getHitNoun(damage.type)+" breaks through bones.";
      else if(percentage < 40)
            return Print.capitalize(attacker.getPossessive())+" "+attack.weapon.getName()+" breaks a large wound to your "+location+", causing bones to splinter.";
      else if(percentage < 60)
            return "Shattered bone fragments fill the air as the "+attack.weapon.getName()+" tears a large wound into your "+location+".";
      else if(percentage < 90)
            return "Your bones pulverize around the "+attacker.getRace().getName()+"'s "+attack.weapon.getName()+", covering "+who.getPronoun()+" and the ground with pieces of bone.";
      else if(percentage < 100)
            return Print.capitalize(attacker.getName())+" "+attack.weapon.getHitNoun(damage.type)+" brutally rips your "+location+" asunder, causing your bones to turn to dust.";
      else
            return Print.capitalize(attacker.getName())+" hews you down with a single mighty blow, leaving a thick cloud of dust lingering in the air.";
   }
}
