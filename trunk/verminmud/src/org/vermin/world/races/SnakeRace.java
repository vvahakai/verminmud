/* SnakeRace.java
	21.9.2003   VV&MV
	
	Snake race implementation.
*/

package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.SnakeTooth;

public class SnakeRace extends DefaultRaceImpl implements Singleton
{
	private static SnakeTooth hand = new SnakeTooth();

	protected static SnakeRace _instance = null;

	private int size = 15;

	private int physicalstr = 15;

	private int mentalstr = 15;

	private int physicalcon = 10;

	private int mentalcon = 10;

	private int physicaldex = 20;

	private int mentaldex = 10;

	private int physicalcha = 10;

	private int mentalcha = 10;

	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(GoreProperty.HAS_BLOOD, GoreProperty.HAS_INTERNAL_ORGANS,
				GoreProperty.HAS_SCALES);
	}
	
	Slot[] slots = new Slot[0];	
	public String getName() {
		return "snake";
	}
	public int getMinimumVisibleIllumination() {
		return 35;
	}

	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 10)	{ return "head"; }
		if(pos < 80)   { return "body"; }
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
			case 0: return "teeth";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		if (limb == 0)
			return SnakeRace.hand;
		
		return SnakeRace.hand;
	}

	public int getLimbCount()
	{
		return 1;
	}
	
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new SnakeRace();
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
