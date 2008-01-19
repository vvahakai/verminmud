package org.vermin.world.items;

import org.vermin.mudlib.*;

public class QuadrupleHorns extends DefaultWieldableImpl {
	
   private static Damage[] dmg = null;
	
   public boolean isWeapon() { return true; }
	
   static {
      dmg = new Damage[1];
      dmg[0] = new Damage();
      dmg[0].type = Damage.Type.PIERCING;
      dmg[0].damage = 16;
   }  
   
   public Damage[] getHitDamage(Living target) {
      return dmg;
   }
   
   public String getObjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
         switch(Dice.random(3)) {
            case 1:
               return "pierce";
            case 2:
               return "impale";
            case 3:
               return "gore";
         }
      }
      
      return "congratulate";
   }
   
   public String getSubjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
         switch(Dice.random(3)) {
            case 1:
               return "pierces";
            case 2:
               return "impales";
            case 3:
               return "gores";
         }
      }
      
      return "congratulates";
   }
   
   public int getDefensiveValue() {
      return 0;
   }
	public boolean isVisible() {
		return false;
	}   
   public String getName() {
      return "horns";
   }
}
