package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.driver.*;
public class BlessSkill extends BaseSkill {
   public BlessSkill() {
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
		Player p = (Player) suc.getTarget();
	   if(s < 0) {
		   who.notice("You try to bless "+p.getName()+", but fail miserably.");
		   return;
	   }
	   else {
		   who.notice("You successfully bless "+p.getName()+".");
	   }
	   p.addModifier(new SkillModifier("critical", 20+Dice.random(who.getSkill("bless")/5), 20+Dice.random(who.getSkill("bless")/5)));
	   p.addModifier(new SkillModifier("dodge", 20+Dice.random(who.getSkill("bless")/5), 20+Dice.random(who.getSkill("bless")/5)));	   
	   p.notice("You have been blessed.");
   }
   public int getTickCount() {
	   return 15;
   }
   public int getSpellCost() {
	   return 0;
   }
   public String getName() {
	   return "bless";
   }
	   private class SkillModifier implements Modifier, Tickable {
		   private int ticks;
		   private int amount;
			private String skill;
		   public boolean isActive() {
			   return ticks > 0;
		   }
		   public int modify(MObject target) {
			   return amount;
		   }
		   public String getDescription() {
			   return null;
		   }
			public Object[] getArguments() {
				return new String[] { skill };
			}
			public ModifierTypes getType() {
				return ModifierTypes.SKILL;
			}
			public void deActivate() {
				ticks=0;
			}
		   public boolean tick(Queue q) {
			   ticks -= 1;
			   return isActive();
			   }
		   public SkillModifier(String s, int t, int a) {
				skill = s;
			   ticks = t;
			   amount = a;
			   Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		   }

	   };

}
