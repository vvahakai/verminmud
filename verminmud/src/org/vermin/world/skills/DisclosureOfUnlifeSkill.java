package org.vermin.world.skills;

import static org.vermin.mudlib.LivingProperty.UNDEAD;

import java.util.Iterator;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.util.Print;

public class DisclosureOfUnlifeSkill extends BaseSkill {

	public String getName() {
		return "disclosure of unlife";
	}

	public int getTickCount() {
		return 3+Dice.random(4);
	}
	public int getCost(SkillUsageContext suc) {
		return 100;
	}

	public SkillType[] getTypes() {
		return skilltypes;
	}
	protected SkillType[] skilltypes = {SkillTypes.ARCANE, SkillTypes.LOCAL, SkillTypes.AREA}; 

	public void use(SkillUsageContext suc) {
		use(suc.getActor(), suc.getSkillSuccess());
	}
	
	public void use(Living actor, int skillSuccess) {
		Iterator en = actor.getRoom().findByType(Types.TYPE_LIVING);
		if(skillSuccess>0) {
		while(en.hasNext()) {
			Living l = (Living) en.next();
			if(l != actor && !actor.getBattleGroup().contains(l.getLeafBattleGroup())) {
				int evilness = l.getLifeAlignment();
				boolean undead;
				if(l.provides(UNDEAD)){
					undead = true;
				}
				else undead = false;
				String name = Print.capitalize(l.getName()); 
					
					if(evilness<-5000) {
						if (!undead) actor.notice(name+" is extremely evil on your standards.\n\n");
						if (undead) actor.notice(name+" is extremely evil on your standards. "+name+" is undead.\n\n");
					}
					if(evilness<0 && evilness>=-5000) {
						if(!undead)	actor.notice(name+" is evil on your standards.\n\n");
						if (undead) actor.notice(name+" is evil on your standards. "+name+" is undead.\n\n");
					}
					if(evilness>=0 && evilness<5000) {
						if(!undead)	actor.notice(name+" is good on your standards.\n\n");
						if (undead) actor.notice(name+" is good on your standards. "+name+" is undead.\n\n");
					}
					if(evilness>5000) {
						if(!undead)	actor.notice(name+" is extremely good on your standards.\n\n");
						if (undead) actor.notice(name+" is extremely good on your standards."+ name+" is undead.\n\n");
					}
				}
			}
		}
		else actor.notice("You fail the skill.");
	}
}
