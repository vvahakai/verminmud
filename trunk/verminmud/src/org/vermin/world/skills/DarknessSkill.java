/* DarknessSkill.java
 * 
 * Conjures a ball of darkness that follows the caster.
 */
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.driver.Driver;


public class DarknessSkill extends BaseSkill {

	public SkillType[] getTypes() {
		return new SkillType[] { SkillTypes.SELF };
	}

	public String getName() {
		return "darkness";
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

			who.notice("You chant 'Noir Noir' and create a ball of darkness.");
			who.getRoom().notice(who, who.getName()+" chants 'Noir Noir' and creates a ball of darkness.");

			who.addModifier(new DarknessModifier(20, who) );

		} else {
			who.notice("You chant 'Noir Noir' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants 'Noir Noir'.");
		}
		
	}
	
	private static class DarknessModifier implements Modifier, Tickable {
		
		private int ticks;
		private Living bearer;
		
		DarknessModifier(int t, Living bearer) {
			this.bearer = bearer;
			ticks = t;
			Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		}
		public boolean tick(Queue q) {
			ticks--;
			boolean continuing = ticks > 0;
			if(!continuing) {
				bearer.getRoom().notice(bearer, bearer.getName()+ "s ball of darkness dissappears.");
				bearer.notice("Your ball of darkness dissappears.");
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
			return -50;
		}
		public String getDescription() {
			return "";
		}
	};
			
}
