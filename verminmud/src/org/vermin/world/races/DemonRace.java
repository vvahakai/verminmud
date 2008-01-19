/* TrollRace.java
	13.9.2003 MV
	
	Troll race implementation.
*/

package org.vermin.world.races;

import org.vermin.driver.Singleton;
import java.util.EnumSet;


import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.DemonClaw;
import org.vermin.world.items.DemonTail;


public class DemonRace extends DefaultRaceImpl implements Singleton
{
	private static DemonClaw claw = new DemonClaw();
	
	private static DemonClaw claw2 = new DemonClaw();
	
	private static DemonTail tail = new DemonTail();
	
	protected static DemonRace _instance = null;

	private String name = new String("demon");
	
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
	
	public EnumSet<LivingProperty> getRaceProperties() {
		return EnumSet.of(LivingProperty.UNDEAD, LivingProperty.ALLERGY_LIGHT, LivingProperty.DOES_NOT_BREATHE);
	}
	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(GoreProperty.HAS_BLOOD, GoreProperty.HAS_INTERNAL_ORGANS,
				GoreProperty.HAS_BONES);
	}
	public int getMinimumVisibleIllumination() {
		return 10;
	}

	/* hit location name by percent */
	public String getHitLocation(int pos)
	{
		if(pos < 2)		{ return "forehead"; }
		if(pos < 3)		{ return "left eye"; }
		if(pos < 4)		{ return "right eye"; }
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
		if(pos < 47)	{ return "chest"; }
		if(pos < 58)	{ return "stomach"; }
		if(pos < 64)	{ return "tail"; }
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
		if(pos < 58)	{ return slots[6]; }
		if(pos < 64)	{ return slots[11]; }
		if(pos < 79)	{ return slots[7]; }
		if(pos < 82)	{ return slots[8]; }				
		if(pos < 97)	{ return slots[9]; }												
		if(pos <= 100)	{ return slots[10]; }	
		return slots[0];
	}	

   /* Returns array of armour slots for this
    * race.
    */
   public Slot[] getSlots()
   {
		return slots;
   }
   
	
	public String getName() { return this.name; }
	
	/* indexed limb name 0 to n */
	public String getLimbName(int limb)
	{
		switch(limb)
		{
			case 0: return "right claw";
			case 1: return "left claw";
			case 2: return "tail";
			default: return "left butt cheek";
		}
	}
	
		
	@Override
	public int getLifeAlignment() {
		return -2000;
	}

	public Item getLimb(int limb)
	{
		if (limb == 0)
			return DemonRace.claw;

		if (limb == 1)
			return DemonRace.claw2;

		if (limb == 2)
			return DemonRace.tail;
		
		return DemonRace.tail;
	}
	public int getLimbCount()
	{
		return 3;
	}
	
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new DemonRace();
			_instance.start();
		}
		
		return _instance;
	}
	
	public int getBaseHpRegen()
	{
		return 75;
	}
	
	public int getBaseSpRegen()
	{
		return 60;
	}
	
	public int getSize()
	{
		return 75;
	}
	
	public int getExpRate()
	{
		return 72;
	}

	public String[] getGenders() { return new String[] { "male", "female" }; }

	/* Max stats for this race */
	public int getMaxPhysicalStrength() { return 80; }
	public int getMaxMentalStrength() { return 60; }
	public int getMaxPhysicalConstitution() { return 65; }
	public int getMaxMentalConstitution() { return 55; }
	public int getMaxPhysicalCharisma() { return 22; }
	public int getMaxMentalCharisma() { return 34; }
	public int getMaxPhysicalDexterity() { return 45; }
	public int getMaxMentalDexterity() { return 66; }

	/* Statcosts for this race (0-10) */
	public int getPhysicalStrengthCost() { return 3; }
	public int getMentalStrengthCost() { return 5; }
	public int getPhysicalConstitutionCost() { return 4; }
	public int getMentalConstitutionCost() { return 6; }
	public int getPhysicalCharismaCost() { return 8; }
	public int getMentalCharismaCost() { return 6; }
	public int getPhysicalDexterityCost() { return 6; }
	public int getMentalDexterityCost() { return 5; }

}
