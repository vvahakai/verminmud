/* LightSkill.java
 * 
 * Conjures a ball of light that follows the caster.
 */
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.driver.Driver;


public class LightSkill extends BaseSkill {

	public SkillType[] getTypes() {
		return new SkillType[] { SkillTypes.SELF, SkillTypes.ARCANE };
	}

	public String getName() {
		return "light";
	}

	/* Try to use this skill. */
	public boolean tryUse(Living who, MObject target) {
		return true;
	}

   public int getTickCount() {
      return 4;
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		if(success > 0) {

			who.notice("You chant 'lux pux' and create a ball of light.");
			who.getRoom().notice(who, who.getName()+" chants 'lux pux' and creates a ball of light.");

			who.addModifier(new LightModifier(who.getMentalStrength()*2, who) );

		} else {
			who.notice("You chant 'lux pux' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants 'lux pux'.");
		}
		
	}
	
	public static class LightModifier implements Modifier, Tickable {
		
		private int ticks;
		private Living enlightened;
		
		public LightModifier() {}
		
		public LightModifier(int t, Living enlightened) {
			this.enlightened = enlightened;
			ticks = t;
			Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		}
		public boolean tick(Queue q) {
			ticks--;
			boolean continuing = ticks > 0;
			if(!continuing) {
				enlightened.getRoom().notice(enlightened, enlightened.getName()+ "s ball of light dissappears.");
				enlightened.notice("Your ball of light dissappears.");
			}
			return continuing;
		}

		public ModifierTypes getType() {
			return ModifierTypes.LIGHT;
		}
		public Object[] getArguments() {
			return new Object[0];
		}
		public void deActivate() {
			ticks = 0;
		}

		public boolean isActive() {
			return ticks > 0;
		}
		public int modify(MObject target) {
			return 50;
		}
		public String getDescription() {
			return "";
		}
	};
			
}
