package org.vermin.world.skills;

import java.util.HashMap;

import org.vermin.driver.Driver;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Modifier;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.mudlib.Tick;

public class ForgetSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };
	
	private class ForgetModifier implements Modifier, Tickable {
		private int ticks;
		private Living caster;
		private int strength;
		private String skill;

		public Object[] getArguments() {
			return new String[] { skill };
		}

		public ModifierTypes getType() {
			return ModifierTypes.SKILL;
		}

		public void deActivate() {
			ticks =0;
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

		public boolean tick(Queue q) {
			ticks -= 1;
			return isActive();
		}

		public ForgetModifier(String skill, int t, Living who, int str) {
			this.skill=skill;
			ticks = t;
			caster = who;
			strength = str;
			Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		}
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Ermm?";

	public String getName() {
		return "forget";
	}

	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target) &&
			(target instanceof Player);
	}

	public int getCost(SkillUsageContext suc) { return 511; }

   public int getTickCount() {
      return 6 + Dice.random(2);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		if(!(suc.getTarget() instanceof Player))
			return;
		
		Player target = (Player) suc.getTarget();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0)
		{
			who.notice("You chant '"+spellWords+"' and make " + target.getName() + " forgetful.");
			target.notice(who.getName() + "'s spell makes you feel forgetful.");
				
			HashMap skills = target.getSkillObject().getSkills();
			
			int length = Dice.random(who.getMentalDexterity());
			
			for(String name : target.getSkillObject().getSkills().keySet()) {
				SkillObject.SkillEntry se = (SkillObject.SkillEntry) skills.get(name);
				ForgetModifier fm = new ForgetModifier(name, length, who, se.percent);
				target.addModifier(fm);	
			}
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
