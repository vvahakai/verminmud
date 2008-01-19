/*
 * Created on 4.12.2004
 */
package org.vermin.mudlib;
import org.vermin.driver.AbstractPropertyProvider;
/**
 * @author Jaakko Pohjamo
 */
public class WebAffliction	extends AbstractPropertyProvider<LivingProperty> implements Affliction {

	private Living sufferer;
	private transient Living cause;
	private int strength;
	
	public WebAffliction() {
	}
	
	public WebAffliction(Living cause, int strength) {
		this.cause = cause;
		this.strength = strength;
	}
	
	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#getAmount()
	 */
	public int getAmount() {
		return strength;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#getSufferer()
	 */
	public Living getSufferer() {
		return sufferer;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#getCause()
	 */
	public Living getCause() {
		return cause;
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#onRegen()
	 */
	public void onRegen() {
		if(Dice.random() + sufferer.getPhysicalStrength() > strength) {
			sufferer.notice("You break free of the sticky webs holding you.");
			sufferer.getRoom().notice(sufferer, sufferer.getName()+" breaks free of the sticky webs holding "+sufferer.getPronoun()+".");
			end();
		} else {
			sufferer.notice("You struggle against the sticky webs holding you.");
			sufferer.getRoom().notice(sufferer, sufferer.getName()+" struggles against the webs holding "+sufferer.getPronoun()+".");
		}

		strength -= 10;
		if(strength < 0) {
			sufferer.notice("The webs holding you wither and fall on the ground.");
			end();
		}
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#onBattle()
	 */
	public void onBattle() {
		if(Dice.random() + sufferer.getPhysicalStrength() > strength) {
			sufferer.notice("You break free of the sticky webs holding you.");
			sufferer.getRoom().notice(sufferer, sufferer.getName()+" breaks free of the sticky webs holding "+sufferer.getPronoun()+".");
			end();
		} else {
			sufferer.notice("You struggle against the sticky webs holding you.");
			sufferer.getRoom().notice(sufferer, sufferer.getName()+" struggles against the webs holding "+sufferer.getPronoun()+".");
		}
		
		strength -= 10;
		if(strength < 0) {
			strength = 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#start(org.vermin.mudlib.Living)
	 */
	public void start(Living who) {
		sufferer = who;
		if(strength < 50) {
			who.notice("Some webs cover you.");
		} else if(strength < 100) {
			who.notice("You are covered in strong webs.");
		} else if(strength < 150) {
			who.notice("You are completely entangled!");
		} else {
			who.notice("The webs around you feel as strong as steel wires!");
		}
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#end()
	 */
	public void end() {
		sufferer.removeAffliction(this);
	}

	/* (non-Javadoc)
	 * @see org.vermin.mudlib.Affliction#getType()
	 */
	public Type getType() {
		return Affliction.Type.WEB;
	}

	/* (non-Javadoc)
	 * @see org.vermin.driver.PropertyProvider#provides(null)
	 */
	public boolean provides(LivingProperty property) {
		if(property == LivingProperty.NO_SKILLS ||
			 property == LivingProperty.NO_BATTLE ||
			 property == LivingProperty.IMMOBILIZED) {
			return true;
		}
		
		return false;
	}
	

}
