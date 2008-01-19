/* BirdRace.java
	12.9.2003   VV&MV
	
	Bird race implementation.
*/

package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.BirdBeak;
import org.vermin.world.items.BirdTalon;

public class BirdRace extends DefaultRaceImpl implements Singleton
{
	private static BirdTalon hand = new BirdTalon();

	private static BirdTalon hand2 = new BirdTalon();

	private static BirdBeak beak = new BirdBeak();
	
	protected static BirdRace _instance = null;


	private int size = 10;

	private int physicalstr = 10;

	private int mentalstr = 10;

	private int physicalcon = 7;

	private int mentalcon = 10;

	private int physicaldex = 15;

	private int mentaldex = 10;

	private int physicalcha = 10;

	private int mentalcha = 10;


	public BirdRace() {
		super();
	}
	
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(GoreProperty.HAS_BLOOD, GoreProperty.HAS_INTERNAL_ORGANS,
				GoreProperty.HAS_BONES, GoreProperty.IS_FLYING);
	}
	
	public EnumSet<LivingProperty> getRaceProperties() {
		return EnumSet.of(LivingProperty.FLIGHT);
	}

	public String getName() {
		return "bird";
	}
	public int getLifeAlignment() {
		return 0;
	}
	public int getProgressAlignment() {
		return 0;
	}
	
	Slot[] slots = new Slot[0];	
	public int getMinimumVisibleIllumination() {
		return 35;
	}

	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 10)	{ return "head"; }
		if(pos < 12)	{ return "beak"; }
		if(pos < 47)	{ return "chest"; }
		if(pos < 60)	{ return "left wing"; }						
		if(pos < 73)	{ return "right wing"; }	
		if(pos < 83)	{ return "left leg"; }
		if(pos < 93)	{ return "right leg"; }										
		if(pos <= 100)	{ return "tail"; }				
		return "invisible antenna";
	
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
			case 0: return "right talon";
			case 1: return "left talon";	
			case 2: return "beak";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		if (limb == 0)
			return BirdRace.hand;

		if (limb == 1)
			return BirdRace.hand2;

		if (limb == 2)
			return BirdRace.beak;
		
		return BirdRace.beak;
	}

	public int getLimbCount()
	{
		return 3;
	}
	
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new BirdRace();
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
