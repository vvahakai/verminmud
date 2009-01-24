/*
 * Created on 1.9.2006
 */
package org.vermin.world.skills;

import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;

import org.vermin.driver.AbstractPropertyProvider;
import org.vermin.driver.PropertyProvider;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.Tick;
import org.vermin.mudlib.World;

/**
 * Hover spell provides the HOVER LivingProperty for the target
 * for 600 regen ticks.
 */
public class NightVision extends BaseSkill {

	@Override
	public void use(SkillUsageContext suc) {
		int s = suc.getSkillSuccess();
		Living who = suc.getActor();
		
		if(s <= 0) {
			who.notice("You chant 'jdhua orckuf' but your spell just fizzles.");
		} else {
			if(suc.getTarget() == who)
				who.notice("You chant 'jdhua orckuf'. You feel a flash behind your eyes, and your night vision improves.");
			else {
				who.notice("You chant 'jdhua orckuf'. "+suc.getTargetName()+"'s eyes flash brightly.");
				((Living)suc.getTarget()).notice("You feel a flash behind your eyes, and your night vision improves.");
			}
			
			final PropertyProvider<LivingProperty> provider = new AbstractPropertyProvider<LivingProperty>(){
				public boolean provides(LivingProperty property) {
					return property==LivingProperty.INFRAVISION;
				}
			};
			final Living target = (Living)suc.getTarget();
			
			target.addProvider(provider);
			World.withDelay(Tick.REGEN, s, new Runnable(){
				public void run() {
					target.removeProvider(provider);
					target.notice("You feel a dark flash behind your eyes, and your vision darkens to normal.");
				}});
		}
	}

	@Override
	public String getName() {
		return "night vision";
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
			suc.getActor().notice("You cannot give night vision to that.");
			return false;
		}
		return true;
	}
	
}
