package org.vermin.mudlib.skills;

import org.vermin.driver.AbstractPropertyProvider;
import org.vermin.driver.PropertyProvider;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Tick;
import org.vermin.mudlib.World;

/**
 * Hover spell provides the HOVER LivingProperty for the target
 * for 600 regen ticks.
 */
public class Hover extends BaseSkill {

	@Override
	public void use(SkillUsageContext suc) {
		int s = suc.getSkillSuccess();
		Living who = suc.getActor();
		
		if(s <= 0) {
			who.notice("You chant 'amnis pendere' but your spell just fizzles.");
		} else {
			if(suc.getTarget() == who)
				who.notice("You chant 'amnis pendere' and start hovering slightly above the ground.");
			else {
				who.notice("You chant 'amnis pendere' and "+suc.getTargetName()+" starts hovering slightly above the ground.");
				((Living)suc.getTarget()).notice("You start hovering slightly above the ground.");
			}
			
			final PropertyProvider<LivingProperty> provider = new AbstractPropertyProvider<LivingProperty>(){
				public boolean provides(LivingProperty property) {
					return property==LivingProperty.HOVER;
				}
			};
			final Living target = (Living)suc.getTarget();
			
			target.addProvider(provider);
			World.withDelay(Tick.REGEN, s, new Runnable(){
				public void run() {
					target.removeProvider(provider);
					target.notice("You stop hovering and descend to the ground.");
				}});
		}
	}

	@Override
	public String getName() {
		return "hover";
	}

	@Override
	public int getTickCount() {
		return 4 + Dice.random(4);
	}

	private static SkillType[] types = new SkillType[] {
		SkillTypes.ARCANE, SkillTypes.LOCAL
	};
	
	@Override
	public SkillType[] getTypes() {
		return types;
	}

	@Override
	public boolean tryUse(SkillUsageContext suc) {
		if(!(suc.getTarget() instanceof Living)) {
			suc.getActor().notice("You can't make that hover.");
			return false;
		}
		return true;
	}
	
}
