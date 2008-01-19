/*
 * Created on 30.3.2005
 *
 */
package org.vermin.world.skills;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.races.GolemRace;


/**
 * @author Matti V�h�kainu
 *
 */
public class GolemRepairSkill extends BaseSkill {
	
	public String getName() {
		return "golem repair";
	}
	
	public int getTickCount() {
		return 3+Dice.random(3);
	}
	
	public SkillType [] getTypes() {
		return new SkillType [] { SkillTypes.HEALING, SkillTypes.LOCAL };
	}
	
	public boolean tryUse(SkillUsageContext suc, MObject golem) {
		
		if (golem instanceof Living) {
			
			Living golemi = (Living) golem;
			if (golemi.inBattle()) {
				suc.getActor().notice("You cannot repair golem while it is fighting.");
				return false;
			}
		}
		if (!suc.getActor().getRoom().contains(golem)) {
			suc.getActor().notice("There is no golem in the room.");
			return false;
		}
		else if(!suc.getActor().getRoom().isOutdoor()) {
			suc.getActor().notice("You cannot use this skill indoors.");
			return false;
		}
		
		
		else return true;

	}
	
	public void use(SkillUsageContext suc) {
		
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		Living tgt = (Living) target;
		int success = suc.getSkillSuccess();
		int gather = Math.max(1, suc.getActor().checkSkill("gather reagents"));
		int mineralogy = Math.max(1, suc.getActor().checkSkill("mineralogy"));
		
		if(!(tgt.getRace() instanceof GolemRace)) {
			who.notice("You cannot repair that.");
			return;
		}
			
		if ((success + gather + mineralogy) > 100) {
			tgt.addHp((gather + mineralogy + success) * 3);
			who.notice("You succesfully repair your golem.");
		}
		else {
			if (gather <= mineralogy) 
				who.notice("You are unable to find reagents to repair your golem.");
			else
				who.notice("You fail to repair your golem with all the reagents.");			
		}

	}

}
