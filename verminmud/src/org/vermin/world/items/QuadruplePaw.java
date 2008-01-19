package org.vermin.world.items;

import org.vermin.mudlib.*;

public class QuadruplePaw extends DefaultWieldableImpl {
	
   private static Damage[] dmg = null;
	
   public boolean isWeapon() { return true; }
	
   static {
      dmg = new Damage[2];
      dmg[0] = new Damage();
      dmg[0].type = Damage.Type.PIERCING;
      dmg[0].damage = 8;
      dmg[1] = new Damage();
      dmg[1].type = Damage.Type.SLASHING;
      dmg[1].damage = 8;
   }  
   
   public Damage[] getHitDamage(Living who) {
      return dmg;
   }
   
   public String getObjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
            return "pierce";
         case SLASHING:
         switch(Dice.random(3)) {
            case 1:
               return "slash";
            case 2:
               return "strike";
            case 3:
               return "swipe";
         }
      }
      
      return "congratulate";
   }
   
   public String getSubjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
            return "pierces";
         case SLASHING:
         switch(Dice.random(3)) {
            case 1:
               return "slashes";
            case 2:
               return "strikes";
            case 3:
               return "swipes";
         }
      }
      
      return "congratulates (damagetype: "+damageType+")";
   }
   
   public int getDefensiveValue() {
      return 0;
   }
	public boolean isVisible() {
		return false;
	}   
   public String getName() {
      return "paw";
   }
}
