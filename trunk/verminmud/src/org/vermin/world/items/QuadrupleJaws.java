package org.vermin.world.items;

import org.vermin.mudlib.*;

public class QuadrupleJaws extends DefaultWieldableImpl {
	
   private static Damage[] dmg = null;
	
   public boolean isWeapon() { return true; }
	
   static {
      dmg = new Damage[2];
      dmg[0] = new Damage();
      dmg[0].type = Damage.Type.PIERCING;
      dmg[0].damage = 8;
      dmg[1] = new Damage();
      dmg[1].type = Damage.Type.CRUSHING;
      dmg[1].damage = 8;
   }  
   
   public Damage[] getHitDamage(Living target) {
      return dmg;
   }
   
   public String getObjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
            return "bite";
         case CRUSHING:
         switch(Dice.random(3)) {
            case 1:
               return "rend";
            case 2:
               return "crush";
            case 3:
               return "rip";
         }
      }
      
      return "congratulate";
   }
   
   public String getSubjectHitVerb(Damage.Type damageType) {
      switch(damageType) {
         case PIERCING:
            return "bites";
         case CRUSHING:
         switch(Dice.random(3)) {
            case 1:
               return "rends";
            case 2:
               return "crushes";
            case 3:
               return "rips";
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
      return "jaws";
   }
}
