package org.vermin.world.items;

import org.vermin.mudlib.*;

public class QuadrupleTail extends DefaultWieldableImpl {
	
   private static Damage[] dmg = null;
	
   public boolean isWeapon() { return true; }
	
   static {
      dmg = new Damage[1];
      dmg[0] = new Damage();
      dmg[0].type = Damage.Type.CRUSHING;
      dmg[0].damage = 4;
   }  
   
   public Damage[] getHitDamage(Living target) {
      return dmg;
   }
   
   public String getObjectHitVerb(Damage.Type damageType) {
      return "swish";
   }
   
   public String getSubjectHitVerb(Damage.Type damageType) {
      return "swishes";
   }
   
   public int getDefensiveValue() {
      return 0;
   }
	public boolean isVisible() {
		return false;
	}   
   public String getName() {
      return "tail";
   }
}
