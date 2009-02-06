/*
 * Created on Jul 17, 2004
 */
package org.vermin.world.races;

import static org.vermin.mudlib.battle.GoreProperty.HAS_EXOSKELETON;
import static org.vermin.mudlib.battle.GoreProperty.HAS_INTERNAL_ORGANS;
import static org.vermin.mudlib.battle.GoreProperty.IS_INSECT;
import static org.vermin.mudlib.battle.GoreProperty.VOICE_SHRIEKS;

import java.util.EnumSet;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.InsectJaw;


/**
 * @author Jaakko Pohjamo
 */
public class AntRace extends DefaultRaceImpl implements Singleton {
	
	private static AntRace _instance = null;

	protected String name = "ant";
	private int size = 10;
	private int physicalstr = 18;
	private int mentalstr = 10;
	private int physicalcon = 10;
	private int mentalcon = 10;
	private int physicaldex = 3;
	private int mentaldex = 10;
	private int physicalcha = 2;
	private int mentalcha = 10;

	public int getMinimumVisibleIllumination() {
		return 35;
	}
	public String getName() {
		return "ant";
	}
	/* hit location name by percent */
	public String getHitLocation(int pos) {
		if(pos < 10)	{ return "head"; }
		if(pos < 12)	{ return "jaw"; }
		if(pos < 13)	{ return "left eye cluster"; }
		if(pos < 14)	{ return "right eye cluster"; }
		if(pos < 15)	{ return "left antenna"; }		
		if(pos < 16)	{ return "right antenna"; }		
		if(pos < 47)	{ return "front body section"; }
		if(pos < 60)	{ return "middle body section"; }
		if(pos < 70)	{ return "left foreleg"; }
		if(pos < 75)	{ return "right foreleg"; }
		if(pos < 80)	{ return "left middle-leg"; }
		if(pos < 85)	{ return "right middle-leg"; }
		if(pos < 93)	{ return "left backleg"; }
		if(pos <= 100)	{ return "right backleg"; }
		return "invisible antenna";
	
	}
	
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(HAS_EXOSKELETON, HAS_INTERNAL_ORGANS, IS_INSECT, VOICE_SHRIEKS);
	}
	
	/* slot by percent */
	public Slot getSlotForLocation(int pos) {
		return null;
	}	

	
	/* indexed limb name 0 to n */
	public String getLimbName(int limb) {
		switch(limb) {
			case 0: return "jaw";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb) {
		if (limb == 0)
			return new InsectJaw();
		return null;
	}

	public int getLimbCount() {
		return 1;
	}
	
	public synchronized static Race getInstance() {
		if(_instance == null) {
			_instance = new AntRace();
			_instance.start();
		}
		
		return _instance;
	}
	
	public int getBaseHpRegen() {
		return 15;
	}
	
	public int getBaseSpRegen() {
		return 1;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getExpRate() {
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
