/*
 * Created on 23.10.2004
 */
package org.vermin.mudlib;

import org.vermin.driver.AbstractPropertyProvider;
import org.vermin.util.Print;

/**
 * @author Jaakko Pohjamo
 */
public class PoisonAffliction extends AbstractPropertyProvider<LivingProperty>  implements Affliction {

	private Living sufferer;
	private transient Living cause;
	private int amount;
	private int damagePerTick;
	
	public PoisonAffliction() {
	}
	
	public PoisonAffliction(Living cause, int amount) {
		this.cause = cause;
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}

	public Living getSufferer() {
		return sufferer;
	}

	public Living getCause() {
		return cause;
	}
	public void onRegen() {
		
		if(amount < 0) {
			end();
		}
		
		generateMessages();
		sufferer.subHp(new Damage(Damage.Type.POISON, damagePerTick*2), cause);
		if(sufferer.isDead()) {
			end();
			return;
		}
		
		amount -= damagePerTick*2;
		if(amount < 0) {
			sufferer.notice("You feel better as your body overcomes the poison.");
			end();
		}
	}
	
	public void onBattle() {
		if(amount < 0) {
			end();
		}

		generateMessages();
		sufferer.subHp(new Damage(Damage.Type.POISON, damagePerTick*2), cause);
		if(sufferer.isDead()) {
			end();
			return;
		}
		amount -= damagePerTick;
		if(amount < 0) {
			sufferer.notice("You feel better as your body overcomes the poison.");
			end();
		}
	}

	public void start(Living who) {
		sufferer = who;
		damagePerTick = amount / ((sufferer.getPhysicalConstitution() / 10)+3);
		damagePerTick++;
		
		if(calculatePercent() < 10) {
			who.notice("You feel slightly ill.");
		} else if(calculatePercent() < 30) {
			who.notice("You feel sick.");
		} else if(calculatePercent() < 70) {
			who.notice("You feel very sick.");
		} else {
			who.notice("You feel the deathly grip of poison!");
		}
	}

	public void end() {
		sufferer.removeAffliction(this);
	}

	public Type getType() {
		return Affliction.Type.POISON;
	}

	public boolean provides(LivingProperty property) {
		if(property == LivingProperty.NO_REGENERATION && amount > 0) {
			return true;
		}
		
		return false;
	}

	private int calculatePercent() {
		return (int) ((double)amount*100f/(double)sufferer.getMaxHp());
	}
	
	private void generateMessages() {
		if(sufferer.isDead()) {
			return;
		}
		if(calculatePercent() < 10) {
			sufferer.notice("Your stomach turns as the poison takes effect.");
			sufferer.getRoom().notice(sufferer, Print.capitalize(sufferer.getName())+" looks slightly ill.");
		} else if(calculatePercent() < 20) {
			sufferer.notice("You gringe in pain as the poison courses through your veins.");
			sufferer.getRoom().notice(sufferer, Print.capitalize(sufferer.getName())+" gringes in pain.");
		} else if(calculatePercent() < 50) {
			sufferer.notice("The pain caused by the poison causes you to bend in two.");
			sufferer.getRoom().notice(sufferer, Print.capitalize(sufferer.getName())+" suddenly doubles up in pain.");
		} else if(calculatePercent() < 70) {
			sufferer.notice("The horrible pain caused by the poison makes you throw up.");
			sufferer.getRoom().notice(sufferer, Print.capitalize(sufferer.getName())+" turns green and throws up.");
		} else if(calculatePercent() < 90) {
			sufferer.notice("Green foam and blood drips from your mouth as the poison takes effect.");
			sufferer.getRoom().notice(sufferer, "Green foam and blood drips from "+sufferer.getName()+"'s mouth.");
		} else {
			sufferer.notice("Your vision goes black and you fall on your knees. You are deathly sick.");
			sufferer.getRoom().notice(sufferer, Print.capitalize(sufferer.getName())+" drops on "+sufferer.getPossessive()+" knees and almost loses "+sufferer.getPossessive()+" consciousness.");
		}
	}
}
