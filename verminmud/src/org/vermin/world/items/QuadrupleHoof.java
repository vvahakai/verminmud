package org.vermin.world.items;

import org.vermin.mudlib.*;

public class QuadrupleHoof extends DefaultWieldableImpl {
	
   private static Damage[] dmg = null;
	
   public boolean isWeapon() { return true; }
	
   static {
      dmg = new Damage[1];
      dmg[0] = new Damage();
      dmg[0].type = Damage.Type.CRUSHING;
      dmg[0].damage = 12;
   }  
   
   public Damage[] getHitDamage(Living target) {
      return dmg;
   }
   
   public String getObjectHitVerb(Damage.Type damageType) {
      return "kick";
   }
   
   public String getSubjectHitVerb(Damage.Type damageType) {
      return "kicks";
   }
   
   public int getDefensiveValue() {
      return 0;
   }
	public boolean isVisible() {
		return false;
	}  
   public String getName() {
      return "hoof";
   }
}
