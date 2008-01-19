/* FimirRace.java
	26.3.2002   VV
	
	Fimir race implementation.
*/

package org.vermin.world.races;

import org.vermin.mudlib.*;
import org.vermin.world.items.*;

public class FimirRace extends PlayerRace {
	private static FimirHand lHand = new FimirHand();
	private static FimirHand rHand = new FimirHand();
	private static FimirTail tail = new FimirTail();
	
	protected static FimirRace _instance = null;

	private String name = new String("Fimir");

	Slot[] slots = new Slot[] { new Slot(Slot.HEAD, "head"),
								new Slot(Slot.NECK, "neck"),
								new Slot(Slot.ARM, "left arm"),
								new Slot(Slot.HAND, "left hand"),
								new Slot(Slot.ARM, "right arm"),
								new Slot(Slot.HAND, "right hand"),
								new Slot(Slot.TORSO, "torso"),
								new Slot(Slot.LEG, "left leg"),
								new Slot(Slot.FOOT, "left foot"),
								new Slot(Slot.LEG, "right leg"),
								new Slot(Slot.FOOT, "right foot"),
								new Slot(Slot.TAIL, "tail"),
								new Slot(Slot.FINGER, "left hand finger"),
								new Slot(Slot.FINGER, "right hand finger"),
								new Slot(Slot.AMULET, "amulet"),
								new Slot(Slot.BELT, "belt"),
								new Slot(Slot.CLOAK, "cloak"),
								new Slot(Slot.WRIST, "left wrist"),
								new Slot(Slot.WRIST, "right wrist")
								};	
	
    public String getName() {
        return name;
     }

    public Slot[] getSlots() {
    	return slots;
    }    
    
	public int getMinimumVisibleIllumination() {
		return 10;
	}

	public int getMaximumVisibleIllumination() {
		return 50;
	}

	public int getIntrinsicSkill(String skillname) {
		if(skillname.equals("darkness")) { return 50; }
		else return 0;
	}

	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 2)		{ return "forehead"; }
		if(pos < 4)		{ return "eye"; }
		if(pos < 5)		{ return "nose"; }	
		if(pos < 6)		{ return "jaw"; }
		if(pos < 7)		{ return "neck"; }
		if(pos < 8)		{ return "left shoulder"; }
		if(pos < 10)	{ return "upper left arm"; }						
		if(pos < 11)	{ return "left elbow"; }
		if(pos < 13)	{ return "lower left arm"; }
		if(pos < 14)	{ return "left wrist"; }
		if(pos < 17)	{ return "left hand"; }						
		if(pos < 18)	{ return "right shoulder"; }
		if(pos < 20)	{ return "upper right arm"; }						
		if(pos < 21)	{ return "right elbow"; }
		if(pos < 23)	{ return "lower right arm"; }
		if(pos < 24)	{ return "right wrist"; }
		if(pos < 27)	{ return "right hand"; }
		if(pos < 44)	{ return "chest"; }
		if(pos < 58)	{ return "stomach"; }
		if(pos < 63)	{ return "tail"; }				
		if(pos < 66)	{ return "groin"; }
		if(pos < 70)	{ return "left hip"; }
		if(pos < 75)	{ return "left thigh"; }
		if(pos < 76)	{ return "left knee"; }						
		if(pos < 79)	{ return "left leg"; }
		if(pos < 80)	{ return "left ankle"; }
		if(pos < 82)	{ return "left foot"; }
		if(pos < 85)	{ return "right hip"; }						
		if(pos < 91)	{ return "right thigh"; }						
		if(pos < 92)	{ return "right knee"; }						
		if(pos < 97)	{ return "right leg"; }						
		if(pos < 98)	{ return "right ankle"; }						
		if(pos <= 100)	{ return "right foot"; }	
		return "invisible antenna";
	
	}
	
	/* slot by percent */
	public Slot getSlotForLocation(int pos)
	{
		if(pos < 6)		{ return slots[0]; }
		if(pos < 7)		{ return slots[1]; }
		if(pos < 13)	{ return slots[2]; }
		if(pos < 17)	{ return slots[3]; }						
		if(pos < 23)	{ return slots[4]; }
		if(pos < 27)	{ return slots[5]; }
		if(pos < 61)	{ return slots[6]; }
		if(pos < 63)	{ return slots[11]; }
		if(pos < 66)	{ return slots[6]; }											
		if(pos < 79)	{ return slots[7]; }
		if(pos < 82)	{ return slots[8]; }				
		if(pos < 97)	{ return slots[9]; }												
		if(pos <= 100)	{ return slots[10]; }	
		return slots[0];
	}	

	
	/* indexed limb name 0 to n */
	public String getLimbName(int limb)
	{
		switch(limb)
		{
			case 0: return "right hand";
			case 1: return "left hand";
			case 2: return "tail";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		switch(limb) {
		case 0:
			return lHand;
		case 1:
			return rHand;
		case 2:
			return tail;
		}
		return new FimirTail();
	}

	public int getLimbCount()
	{
		return 3;
	}
	
	public static Race getInstance()
	{
		return (Race) World.get("races/fimir");
	}
	
	public int getBaseHpRegen()
	{
		return 60;
	}
	
	public int getBaseSpRegen()
	{
		return 25;
	}
	
	public int getSize()
	{
		return 60;
	}
	
	public int getExpRate()
	{
		return 90;
	}

	/* Max stats for this race */
	public int getMaxPhysicalStrength() { return 65; }
	public int getMaxMentalStrength() { return 15; }			// Channeling ability && spmax
	public int getMaxPhysicalConstitution() { return 70; }	
	public int getMaxMentalConstitution() { return 25; }		// Willpower && spres
	public int getMaxPhysicalDexterity() { return 45; }
	public int getMaxMentalDexterity() { return 35; }			// Int && learning ability
	public int getMaxPhysicalCharisma() { return 30; }			// Beauty
	public int getMaxMentalCharisma() { return 20; }		

	/* Statcosts for this race (0-10) */
	public int getPhysicalStrengthCost() { return 3; }
	public int getMentalStrengthCost() { return 8; }
	public int getPhysicalConstitutionCost() { return 3; }
	public int getMentalConstitutionCost() { return 7; }
	public int getPhysicalDexterityCost() { return 5; }
	public int getMentalDexterityCost() { return 6; }
	public int getPhysicalCharismaCost() { return 7; }
	public int getMentalCharismaCost() { return 8; }

}
