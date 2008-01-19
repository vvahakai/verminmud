package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.driver.*;
public class MentalRegenerationSkill extends BaseSkill {
   public MentalRegenerationSkill() {
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
	   Living who = suc.getActor();
	   MObject target = suc.getTarget();
	   int s = suc.getSkillSuccess();

	   if(s < 0) {
		   who.notice("You chant 'regenex disiplem' but your spell just fizzles.");
		   who.getRoom().notice(who, who.getName()+" chants 'regenex disimplem' but "+who.getPossessive()+" spell just fizzles.");
		   return;
	   }
	   else {
		   who.notice("You chant 'regenex disiplem'.");
		   who.getRoom().notice(who, who.getName()+"chants 'regenex disiplem'."); 
	   }
	   Player p = (Player)target;
	   p.addModifier(new SpRegenModifier(Math.min((Math.max(0,s)*(75+(who.getMentalDexterity()/4)))/100, 45), Math.min(Math.max(0,s/2), 20)));
	   p.notice("Your mental regeneration has been enhanced.");
	   who.notice("You enhanced "+p.getName()+"'s mental regeneration.");
   }
   public int getTickCount() {
	   return 15;
   }
   public int getCost() {
	   return 50;
   }
   public String getName() {
	   return "MentalRegeneration";
   }
	   private class SpRegenModifier implements Modifier, Tickable {
		   private int ticks;
		   private int amount;
		   public boolean isActive() {
			   return ticks > 0;
		   }
		   public int modify(MObject target) {
			   return amount;
		   }
		   public String getDescription() {
			   return null;
		   }
		   public boolean tick(Queue q) {
			   ticks -= 1;
			   return isActive();
			   }
			public ModifierTypes getType() {
				return ModifierTypes.SPREGEN;
			}
			public Object[] getArguments() {
				return new Object[0];
			}
			public void deActivate() {
				ticks=0;
			}

		   public SpRegenModifier(int t, int a) {
			   ticks = t;
			   amount = a;
			   Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		   }

	   };

}
