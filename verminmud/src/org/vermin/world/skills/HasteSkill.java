package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.driver.*;

public class HasteSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL };
	
	private class HasteModifier implements Modifier, Tickable { 
		private int ticks;
		private Living caster;
		private Living target;
		private int strength;
		private Stat stat;

		public boolean isActive() {
			return ticks > 0;
		}
		
		public int modify(MObject target) {
			return strength;
		}
		
		public String getDescription() {
			return null;
		}
		
		public boolean tryDrop(Living who, MObject where) { return false; }
		
		public boolean tick(Queue q) {
			ticks -= 1;
			if(!isActive())
				target.notice("You no longer feel hasted");
			return isActive();
		}
		
		public Object[] getArguments() {
			return new Object[] { stat };
		}

		public ModifierTypes getType() {
			return ModifierTypes.STAT;
		}
		public void deActivate() {
			ticks=0;
		}
		public HasteModifier(Stat stat, int t, Living who, int str, Living target) {
			this.stat = stat;
			ticks = t;
			caster = who;
			this.target = target;
			strength = str;
			Driver.getInstance().getTickService().addTick(this, Tick.BATTLE);
		}
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Faster ooOOoo FasterrRaAAAAAHH OO OOh YES YES!";

	public String getName() {
		return "haste";
	}

	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}

	public int getCost(SkillUsageContext suc) { return 100; }

   public int getTickCount() {
      return 8 + Dice.random(2);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Living target = (Living) suc.getTarget();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0)
		{
				who.notice("You chant '"+spellWords+"' and haste " + target.getName() + ".");
				target.notice(who.getName() + "'s spell make you feel hasted.");
		
				HasteModifier hm = new HasteModifier(Stat.PHYS_DEX, success*who.getMentalDexterity()/10, who, who.getMentalDexterity()/10, target);
				
				target.addModifier(hm);			
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
