package org.vermin.mudlib;

import org.vermin.driver.AbstractPropertyProvider;
import static org.vermin.mudlib.LivingProperty.*;

/**
 * Implements a stun affliction. Stun may prevent
 * skill usage, fighting and moving, depending
 * on severity.
 */
public class StunAffliction extends AbstractPropertyProvider<LivingProperty> implements Affliction {

	private Living sufferer;
	private Living cause;
	private int amount;
	private int percent;
	private boolean firstTick;

	public StunAffliction() {
	}
	
	public StunAffliction(Living cause, int amount) {
		this.cause = cause;
		this.amount = amount;
		this.firstTick = true;
	}


	public Living getSufferer() {
		return sufferer;
	}

	public Living getCause() {
		return cause;
	}

	public void onRegen() {
		
		if(provides(IMMOBILIZED)) {
			sufferer.notice("&B;You are knocked out.&;");
		} else if(provides(NO_BATTLE)) {
			sufferer.notice("&B;You are stunned.&;");
		} else if(provides(NO_SKILLS)) {
			sufferer.notice("&B;You are dazed.&;");
		}
		
		if(sufferer.isDead()) {
			end();
		}
	}

	public void onBattle() {
		double fraction = (double)sufferer.getMaxHp()/100d;

		if(firstTick) {
			if(sufferer.getSkill("iron will")/3 > Dice.random()) {
				sufferer.notice("Your will of iron allows you to continue fighting regardless of the stunning blow.");
				sufferer.checkSkill("iron will");
				end();
				return;
			}
			firstTick = false;
		}
		
		// totally ad-hoc stun regen formula!
		amount -= ((sufferer.getPhysicalConstitution()/10d * fraction) + (sufferer.getMentalConstitution()/10d * 3d));
		update();
		if(amount < 0) {
			end();
			return;
		}
		
		if(sufferer.inBattle()) {
			if(provides(IMMOBILIZED)) {
				sufferer.notice("&B;You are knocked out.&;");
			} else if(provides(NO_BATTLE)) {
				sufferer.notice("&B;You are stunned.&;");
			} else if(provides(NO_SKILLS)) {
				sufferer.notice("&B;You are dazed.&;");
			}
		}
	}

	private void update() {
		percent = (int) ((double)amount*100f/(double)sufferer.getMaxHp());
	}

	public void start(Living who) {
		sufferer = who;
		
		update();

		if(provides(NO_SKILLS))  {
			if(who instanceof Player) {
				Player p = (Player) who;
				SkillObject so = p.getSkillObject();
				if(so.skillActive()) {
					who.notice("Your concentration shatters.");
					p.getSkillObject().stopSkill();
				}
			}
		} else { // this stun is too weak to have any kind of effect
			sufferer.removeAffliction(this);
		}
	}

	public void end() {
		sufferer.removeAffliction(this);
		if(!sufferer.isDead()) {
			sufferer.notice("You recover from the stun.");
			sufferer.getRoom().notice(sufferer, sufferer.getName()+" breaks out of "+sufferer.getPossessive()+" stun.");
		}
	}


	public Type getType() {
		return Affliction.Type.STUN;
	}

	public int getAmount() {
		return amount;
	}

	public boolean provides(LivingProperty property) {
		update();
		
		switch(property) {
		  case IMMOBILIZED:
			  return percent >= 50;

		  case NO_BATTLE:
			  return percent >= 20;

		  case NO_SKILLS:
			  return percent >= 10;

		  default:
			  return false;
		}
	}
	

}
