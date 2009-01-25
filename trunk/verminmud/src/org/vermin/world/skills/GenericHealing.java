package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import java.util.Iterator;

/* base class for generic healing spells */
public class GenericHealing extends BaseSkill {

	protected String name = "FIXME!";
	protected int spCost = 1;
	protected int rounds = 1;
	protected String spellWords = "FIXME!";
	protected int healAmount = 1;
	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.DIVINE, SkillTypes.HEALING, SkillTypes.LOCAL };
	protected boolean MentalDexEffect = false;
	protected boolean MentalStrEffect = false;

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public String getName() { return name; }

	public int getCost(SkillUsageContext suc) { return spCost; }
	
	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target) &&
			targetIsValid(who, target);
	}
	
	public boolean targetIsValid(Living who, MObject target) {
		if(target instanceof Living) {
			Living tgt = (Living) target;

			if(tgt.provides(LivingProperty.CONSTRUCT)) {
				who.notice("You cannot heal mechanical beings.");
				return false;
			}
			
			if(tgt.provides(LivingProperty.SWARM)) {
				who.notice("You cannot heal swarms.");
				return false;
			}
			
			if(tgt.provides(LivingProperty.UNDEAD)) {
				who.notice("Undead cannot be healed.");
				return false;
			}
			return true;
		}
		return false;
	}
	
	public int getTickCount() {
		return rounds;
	}

	protected int getHealingAmount(Living who, Living tgt) {
		int amount = healAmount;
		int success = who.checkSkill("mastery of healing");
		if(success > 0)
		{
			who.notice("Your supreme grandmastery of the healing arts allows you to heal your target better.\n");
			amount += amount * success / 300;
		}
		if(MentalDexEffect)
		{
			amount = amount * (75 + who.getMentalDexterity()/4) / 100;
		}
		if(MentalStrEffect)
		{
			amount = amount * (75 + who.getMentalStrength()/4) / 100;
		}
		return amount;
	}

	protected String getAmountDescription(Living tgt, int hitpoints) {
			int ratio = hitpoints / tgt.getMaxHp() * 100;
			if(ratio < 10) return "slightly ";
			if(ratio < 10) return "";	
			else return "critically ";
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		if(!tryUse(who, target))
			return;
		
		Living tgt = (Living) target;
		
		if(success <= 0) {
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		} else {

			int hitpoints = getHealingAmount(who,tgt);
			int healAmt = Math.min(tgt.getMaxHp() - tgt.getHp(), hitpoints);
			tgt.addHp(hitpoints);
			
			if(tgt instanceof Player && who instanceof Player && tgt != who) {
				Player p = (Player) who;
				long prevExp = p.getExperience();
				who.addExperience(healAmt * 10);
				if(p.getPreference("showexp").equals("on")) {
					long expDelta = p.getExperience() - prevExp;
					p.notice("The heal gains you "+expDelta+" experience.");
				}
			
			}
			
			String amountDescription = getAmountDescription(tgt, hitpoints);

			who.notice("You chant '"+spellWords+"' and "+amountDescription+"heal "+tgt.getName()+" with your "+name+".");
			tgt.notice(who.getName()+" chants '"+spellWords+"' and "+amountDescription+"heals you with "+who.getPossessive()+" "+name+".");
			String msg = who.getName()+" chants '"+spellWords+"' and heals "+tgt.getName()+" with "+who.getPossessive()+" "+name+".";
			
			Iterator en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
		}
	}
}
