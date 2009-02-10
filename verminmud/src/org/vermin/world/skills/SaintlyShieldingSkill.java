package org.vermin.world.skills;

import org.vermin.driver.Driver;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Modifier;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Stat;
import org.vermin.mudlib.Tick;
import org.vermin.mudlib.skills.BaseSkill;

public class SaintlyShieldingSkill extends BaseSkill {

	@Override
	public String getName() {
		return "saintly shielding";
	}

	@Override
	public int getTickCount() {
		return 5+Dice.random(1);
	}
	
	private String spellWords = "Sanctum shieldum";

	
	public int getCost() {
		return 102+Dice.random(13);
	}

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL };
	public SkillType[] getTypes() {
		return skillTypes;
	}

	@Override
	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Living target = (Living) suc.getTarget();
		Room room = who.getRoom();

		if(suc.getSkillSuccess() > 0) {
			room.notice(who, who.getName()+" chants '"+spellWords+"' and successfully shields "+target.getName()+".");
			target.notice(who.getName()+" chants '"+spellWords+"' and successfully shields you.");
			who.notice("You chant '"+spellWords+"' and successfully shield "+target.getName()+".");
			SaintlyShieldingModifier ssmod = new SaintlyShieldingModifier(who.getSkill("saintly shielding")/3, who, 100);
		
		}
		else {
			room.notice(who, who.getName()+" fails spell.");
			who.notice("You fail the spell.");
		}
	}
	public boolean tryUse(Living who, Living target) {
		return hasLivingTarget(who, target) && isTargetInRoom(who, target);
	}
	
	private class SaintlyShieldingModifier implements Modifier, Tickable {
		private int ticks;
		private int amount;
		private Living caster;
		
		public void deActivate() {
			ticks = 0;	
		}
		public Object[] getArguments() {
			return new Object[] {Stat.PHYS_CON};
		}
		public String getDescription() {
			return null;
		}
		public ModifierTypes getType() {
			return ModifierTypes.STAT;
		}
		public boolean isActive() {
			return ticks > 0;
		}
		public int modify(MObject target) {
			return amount;
		}
		public boolean tick(Queue queue) {
			ticks -= 1;
			return isActive();
		}
		public SaintlyShieldingModifier(int t, Living who, int str) {
			ticks = t;
			caster = who;
			amount = str;
			Driver.getInstance().getTickService().addTick(this, Tick.BATTLE);
		}
		
	}

}
