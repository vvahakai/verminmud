package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.mudlib.commands.*;

public class PrayerSkill extends BaseSkill {
   public PrayerSkill() {
   }

   public boolean tryUse(Living who, MObject target) {
	   if (hasLivingTarget(who, target) && target instanceof Player)
	   {
		if (((Player)target).isDead())
			return true;
		who.notice(target.getName()+" is still alive.");
	   } //add accept thingie
   			return false;
   }
   protected SkillType[] skillTypes = new SkillType[] { SkillTypes.REMOTE, SkillTypes.RESURRECT };
   public SkillType[] getTypes() {
	   return skillTypes;
   }

   public void use(SkillUsageContext suc) {
	   Living who = suc.getActor();
	   MObject target = suc.getTarget();
	   int s = suc.getSkillSuccess();

	   if(s < 0) {
		   who.notice("Your prayer has gone unaswered.");
		   return;
	   }
	   Player p = (Player)target;
	   p.getRoom().remove(p);
	   who.getRoom().add(p);
	   p.setExperience((long) (p.getExperience()*0.30));
	   p.notice(who.getName()+"'s prayer has granted you a new life.");
	   Look l = (Look) Commander.getInstance().get("look");
	   l.look(p, true);
	   who.notice("Your prayer has been aswered.");
  	   p.setHp(Math.min(p.getMaxHp(), 20));

   }
   public int getTickCount() {
	   return 15;
   }
   public int getSpellCost() {
	   return 0;
   }
   public String getName() {
	   return "prayer";
   }

}
