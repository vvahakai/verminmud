/*
 * Created on 7.1.2006
 */
package org.vermin.world.races;

import static org.vermin.mudlib.battle.GoreProperty.IS_MAGICAL;
import static org.vermin.mudlib.battle.GoreProperty.IS_MECHANICAL;

import java.util.EnumSet;

import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.Piston;

public class AutomatonRace extends DefaultRaceImpl {

	private static AutomatonRace _instance = null;

	private int size = 50;
	private int physicalstr = 6;
	private int mentalstr = 10;
	private int physicalcon = 15;
	private int mentalcon = 10;
	private int physicaldex = 6;
	private int mentaldex = 10;
	private int physicalcha = 2;
	private int mentalcha = 10;

	public int getMinimumVisibleIllumination() {
		return 35;
	}

	public String getName() {
		return "automaton";
	}
	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 10)	{ return "head"; }
		if(pos < 12)	{ return "jaw"; }
		if(pos < 13)	{ return "left visual sensor"; }
		if(pos < 14)	{ return "right visual sensor"; }
		if(pos < 20)    { return "cabling"; }
		if(pos < 47)	{ return "hull"; }
		if(pos < 60)	{ return "left piston"; }
		if(pos < 73)	{ return "right piston"; }
		if(pos < 83)	{ return "left leg"; }
		if(pos < 93)	{ return "right leg"; }
		if(pos <= 100)	{ return "lower hull"; }
		return "invisible antenna";
	
	}
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(IS_MECHANICAL, IS_MAGICAL);
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
			case 0: return "left piston";
			case 1: return "right piston";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		if (limb == 0)
			return new Piston();

		if (limb == 1)
			return new Piston();
		
		return null;
	}

	public int getLimbCount()
	{
		return 2;
	}
	
	public synchronized static Race getInstance() {
		if(_instance == null) {
			_instance = new AutomatonRace();
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
