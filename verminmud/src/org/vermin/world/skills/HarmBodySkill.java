package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillUsageContext;
public class HarmBodySkill extends Offensive {
   public HarmBodySkill() {
     rounds = 8;
	 spellWords = "ydoB mraH";
     damageType = Damage.Type.PHYSICAL;
     name = "harm body";
   }
   
   public int getSkillCost(SkillUsageContext suc) {
	   return (int) (((double)suc.getActor().getMaxSp())*0.8);
   }
   
   protected int getDamage(Living who, Living target) {
	   return target.getHp()/4;
   }
}
