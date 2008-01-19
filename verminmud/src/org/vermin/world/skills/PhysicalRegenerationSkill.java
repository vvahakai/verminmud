package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import java.util.Vector;
import org.vermin.driver.*;
public class PhysicalRegenerationSkill extends BaseSkill {
   public PhysicalRegenerationSkill() {
   }

   public boolean tryUse(Living who, MObject target) {
	   if (hasLivingTarget(who, target) && target instanceof Player)
	   { 
		   return true;
	   }
   		return false;
   }

   protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL };
   public SkillType[] getTypes() {
	   return skillTypes;
   }
  	public void use(SkillUsageContext suc) {
		use(suc.getActor(), suc.getTarget(), new Vector(), suc.getSkillSuccess());
	}
   public void use(Living who, MObject target, Vector params, int s) {
	   if(s > 0) {
		   who.notice("You chant 'regenex absurum' but your spell just fizzles.");
		   who.getRoom().notice(who, who.getName()+" chants 'regenex absurum' but "+who.getPossessive()+" spell just fizzles.");
		   return;
	   }
	   else {
		   who.notice("You chant 'regenex absurum'.");
		   who.getRoom().notice(who, who.getName()+"chants 'regenex absurum'."); 
	   }
	   Player p = (Player)target;
	   p.addModifier(new HpRegenModifier(Math.min(((who.getSkill("physical regeneration")/2)*(75+((who.getMentalDexterity())/4)))/100, 45), Math.min(who.getSkill("physical regeneration")/4, 20)));
	   p.notice("Your physical regeneration has been enhanced.");
	   who.notice("You enhanced "+p.getName()+"'s physical regeneration.");
   }
   public int getTickCount() {
	   return 15;
   }
   public int getCost() {
	   return 50;
   }
   public String getName() {
	   return "PhysicalRegeneration";
   }
	   private class HpRegenModifier implements Modifier, Tickable {
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
				return ModifierTypes.HPREGEN;
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
		   public HpRegenModifier(int t, int a) {
			   ticks = t;
			   amount = a;
			   Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		   }

	   };

}
