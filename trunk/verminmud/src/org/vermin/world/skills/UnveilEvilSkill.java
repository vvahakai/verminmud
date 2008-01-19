package org.vermin.world.skills;

import java.util.Vector;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.util.Print;

import static org.vermin.mudlib.LivingProperty.*;

public class UnveilEvilSkill extends BaseSkill {

	protected SkillType[] skilltypes = {SkillTypes.ARCANE, SkillTypes.LOCAL}; 

	public SkillType[] getTypes() {
		return skilltypes;
	}
	
	public String getName() {
		return "unveil evil";
	}

	public int getCost(SkillUsageContext suc) {
		return 40;
	}

	public boolean tryUse(Living who, Living target) {
		if(hasLivingTarget(who, target)) {
			return true;
			}
			else return false;		
	}
	
	
	public int getTickCount() {
		return 2+Dice.random(2);
	}
	
	public void use(SkillUsageContext suc) {
		use(suc.getActor(), suc.getTarget(), new Vector(), suc.getSkillSuccess());

	}

	public void use(Living actor, MObject target, Vector vector, int skillSuccess) {
		if(target instanceof Living) {
			Living enemy = (Living) target;
			
		int evilness = enemy.getLifeAlignment();
		boolean undead;
		if(enemy.provides(UNDEAD)){
			undead = true;
		}
		else undead = false;
		String name = Print.capitalize(target.getName()); 
		if(skillSuccess>0) {
			
			if(evilness<-5000) {
				if (!undead) actor.notice(name+" is extremely evil on your standards.");
				if (undead) actor.notice(name+" is extremely evil on your standards. "+name+" is undead.");
			}
			if(evilness<0 && evilness>=-5000) {
				if (!undead) actor.notice(name+" is evil on your standards.");
				if (undead) actor.notice(name+" is evil on your standards. "+name+" is undead.");
			}
			if(evilness>=0 && evilness<5000) {
				if (!undead)	actor.notice(name+" is good on your standards.");
				if (undead) actor.notice(name+" is good on your standards. "+name+" is undead.");
			}
			if(evilness>5000) {
				if (!undead) actor.notice(name+" is extremely good on your standards.");
				if (undead) actor.notice(name+" is extremely good on your standards. "+name+" is undead.");
			}
		}
		else actor.notice("You fail the skill.");
	}
	}
}
