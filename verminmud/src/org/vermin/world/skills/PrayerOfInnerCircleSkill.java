package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import java.util.Vector;
import org.vermin.driver.*;
public class PrayerOfInnerCircleSkill extends BaseSkill {
   public PrayerOfInnerCircleSkill() {
   }

   public boolean tryUse(Living who, MObject target) {
	   if (hasLivingTarget(who, target) && target instanceof Player)
	   { 
		   return true;
	   }
   		return false;
   }

   protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL, SkillTypes.DIVINE };
   public SkillType[] getTypes() {
	   return skillTypes;
   }
  	public void use(SkillUsageContext suc) {
		use(suc.getActor(), suc.getTarget(), new Vector(), suc.getSkillSuccess());
	}
   public void use(Living who, MObject target, Vector params, int s) {
	   if(s > 0) {
		   who.notice("You chant 'ex hoopee namax' but your spell just fizzles.");
		   who.getRoom().notice(who, who.getName()+" chants 'ex hoopee namax' but "+who.getPossessive()+" spell just fizzles.");
		   return;
	   }
	   else {
		   who.notice("You chant 'ex hoopee namax'.");
		   who.getRoom().notice(who, who.getName()+"chants 'ex hoopee namax'."); 
	   }
	   Player p = (Player)target;
	   p.addModifier(new MaxHpModifier(Math.min(Math.max(1,who.checkSkill("prayer of inner circle"))*2, 100), Math.min(Math.max(1,who.checkSkill("physical regeneration"))*4, 200)));
	   p.notice("Your hardiness has been enhanced.");
	   who.notice("You enhanced "+p.getName()+"'s hardiness.");
   }
   public int getTickCount() {
	   return 10+Dice.random(5);
   }
   public int getCost() {
	   return 150;
   }
   public String getName() {
	   return "prayer of inner circle";
   }
	   private class MaxHpModifier implements Modifier, Tickable {
		   private int ticks;
		   private int amount;
		   public boolean isActive() {
			   return ticks > 0;
		   }
		   public int modify(MObject target) {
			   return amount;
		   } // Math.min(((who.getSkill("physical regeneration")/2)*(75+((who.getMentalDexterity())/4)))/100, 45)
		   public String getDescription() {
			   return null;
		   }

			public ModifierTypes getType() {
				return ModifierTypes.MAXHP;
			}
			public Object[] getArguments() {
				return new Object[0];
			}
			public void deActivate() {
				ticks=0;
			}
		   public boolean tick(Queue q) {
			   ticks -= 1;
			   return isActive();
			   }
		   public MaxHpModifier(int t, int a) {
			   ticks = t;
			   amount = a;
			   Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		   }

	   };

}
