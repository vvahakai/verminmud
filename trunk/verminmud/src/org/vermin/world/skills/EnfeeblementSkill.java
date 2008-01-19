package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.driver.*;

public class EnfeeblementSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };
	
	private class EnfeeblementModifier implements Modifier, Tickable { 
		   private int ticks;
		   private Living caster;
		   private int strength;

		public Object[] getArguments() {
			return new Object[] { Stat.PHYS_DEX };
		}

		public void deActivate() {
			ticks=0;
		}
		public ModifierTypes getType() {
			return ModifierTypes.STAT;
		}

		   public boolean isActive() {
			   return ticks > 0;
		   }

		   public int modify(MObject target) {
			   return -strength;
		   }

		   public String getDescription() {
			   return null;
		   }

		   public boolean tryDrop(Living who, MObject where) { return false; }

		   public boolean tick(Queue q) {
			   ticks -= 1;
			   return isActive();
		   }

		   public EnfeeblementModifier(int t, Living who, int str) {
			   ticks = t;
			   caster = who;
			   strength = str;
			   Driver.getInstance().getTickService().addTick(this, Tick.BATTLE);
		   }
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Slooov as a snaaill";

	public String getName() {
		return "enfeeblement";
	}

	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}

	public int getCost(SkillUsageContext suc) { return 120; }

   public int getTickCount() {
      return 4 + Dice.random(1);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Living target = (Living) suc.getTarget();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0)
		{
				who.notice("You chant '"+spellWords+"' and enfeeble " + target.getName() + ".");
				target.notice(who.getName() + "'s spell make you feel enfeebled.");
		
				EnfeeblementModifier em = new EnfeeblementModifier(success*who.getMentalDexterity()/10, who, who.getMentalDexterity()/10);
				
				target.addModifier(em);			
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
