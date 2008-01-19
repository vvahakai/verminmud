/*
 * Created on 6.1.2006
 */
package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.ScorchingWeapon;

public class SunRace extends DefaultRaceImpl implements Singleton {
	protected static SunRace _instance = null;

	private String name = new String("sun");
	private int size = 45;
	private int physicalstr = 6;
	private int mentalstr = 35;
	private int physicalcon = 20;
	private int mentalcon = 20;
	private int physicaldex = 3;
	private int mentaldex = 40;
	private int physicalcha = 20;
	private int mentalcha = 30;

	Slot[] slots = new Slot[0];	
	public int getMinimumVisibleIllumination() {
		return 35;
	}

	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 10)	{ return "core"; }
		if(pos < 15)	{ return "sunspots"; }
		if(pos < 45)	{ return "surface"; }
		if(pos < 85)	{ return "flare"; }
		if(pos <= 100)	{ return "corona"; }
		return "invisible antenna";
	
	}
	
	public String getName() {
		return "sun";
	}
	
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(GoreProperty.VOICE_SPEAKS);
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
			case 0: return "surface";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		if (limb == 0)
			return new ScorchingWeapon();
		return null;
	}

	public int getLimbCount()
	{
		return 1;
	}
	
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new SunRace();
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
		return 15;
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
