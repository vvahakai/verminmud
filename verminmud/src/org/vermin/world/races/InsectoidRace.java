/*
 * Created on Jul 17, 2004
 */
package org.vermin.world.races;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.*;
import java.util.EnumSet;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.InsectClaw;
import org.vermin.world.items.InsectStinger;

import static org.vermin.mudlib.battle.GoreProperty.*;

public class InsectoidRace extends DefaultRaceImpl implements Singleton {
	
	protected static InsectoidRace _instance = null;

	private int size = 10;
	private int physicalstr = 6;
	private int mentalstr = 10;
	private int physicalcon = 15;
	private int mentalcon = 10;
	private int physicaldex = 6;
	private int mentaldex = 10;
	private int physicalcha = 2;
	private int mentalcha = 10;

	Slot[] slots = new Slot[0];	
	public int getMinimumVisibleIllumination() {
		return 35;
	}

	public String getName() {
		return "insect";
	}
	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 10)	{ return "head"; }
		if(pos < 12)	{ return "jaw"; }
		if(pos < 13)	{ return "left eye cluster"; }
		if(pos < 14)	{ return "right eye cluster"; }
		if(pos < 47)	{ return "carapace"; }
		if(pos < 60)	{ return "left claw"; }
		if(pos < 73)	{ return "right claw"; }
		if(pos < 83)	{ return "left leg"; }
		if(pos < 93)	{ return "right leg"; }
		if(pos <= 100)	{ return "stinger"; }
		return "invisible antenna";
	
	}
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(HAS_EXOSKELETON, HAS_INTERNAL_ORGANS, IS_INSECT, VOICE_SHRIEKS);
	}
	
	/* slot by percent */
	public Slot getSlotForLocation(int pos)
	{
		return null;
	}	

	
	/* indexed limb name 0 to n */
	public String getLimbName(int limb)
	{
		switch(limb)
		{
			case 0: return "right claw";
			case 1: return "left claw";
			case 2: return "stinger";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		if (limb == 0)
			return new InsectClaw();

		if (limb == 1)
			return new InsectClaw();

		if (limb == 2)
			return new InsectStinger();
		
		return null;
	}

	public int getLimbCount()
	{
		return 3;
	}
	
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new InsectoidRace();
			_instance.start();
		}
		
		return _instance;
	}
	
	public int getBaseHpRegen()
	{
		return 15;
	}
	
	public int getBaseSpRegen()
	{
		return 1;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public int getExpRate()
	{
		return 90;
	}

	/* Max stats for this race */
	public int getMaxPhysicalStrength() { return this.physicalstr; }
	public int getMaxMentalStrength() { return this.mentalstr; }			// Channeling ability && spmax
	public int getMaxPhysicalConstitution() { return this.physicalcon; }	
	public int getMaxMentalConstitution() { return this.mentalcon; }		// Willpower && spres
	public int getMaxPhysicalDexterity() { return this.physicaldex; }
	public int getMaxMentalDexterity() { return this.mentaldex; }			// Int && learning ability
	public int getMaxPhysicalCharisma() { return this.physicalcha; }			// Beauty
	public int getMaxMentalCharisma() { return this.mentalcha; }		

	/* Statcosts for this race (0-10) */
	public int getPhysicalStrengthCost() { return 5; }
	public int getMentalStrengthCost() { return 6; }
	public int getPhysicalConstitutionCost() { return 5; }
	public int getMentalConstitutionCost() { return 10; }
	public int getPhysicalDexterityCost() { return 3; }
	public int getMentalDexterityCost() { return 5; }
	public int getPhysicalCharismaCost() { return 7; }
	public int getMentalCharismaCost() { return 8; }

}
