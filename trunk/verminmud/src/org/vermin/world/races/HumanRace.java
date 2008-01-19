/* HumanRace.java
	19.1.2002 TT & VV
	
	Human race implementation.
*/

package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.PlayerRace;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.World;
import org.vermin.mudlib.battle.GoreMessageProvider;
import org.vermin.mudlib.battle.GoreMessageProviderFactory;
import org.vermin.world.items.HumanHand;

import static org.vermin.mudlib.battle.GoreProperty.HAS_BLOOD;
import static org.vermin.mudlib.battle.GoreProperty.HAS_BONES;
import static org.vermin.mudlib.battle.GoreProperty.HAS_INTERNAL_ORGANS;
import static org.vermin.mudlib.battle.GoreProperty.VOICE_SPEAKS;

public class HumanRace extends PlayerRace {
	private static HumanHand hand = null;
	
	protected static HumanRace _instance = null;
   
   public static final GoreMessageProvider msg = GoreMessageProviderFactory.getInstance().findProvider(EnumSet.of(HAS_BLOOD, HAS_BONES, HAS_INTERNAL_ORGANS, VOICE_SPEAKS));

	private String name = new String("human");
	

	String[] genders = new String[] { "male", "female" };
	public int getLifeAlignment() {
		return 0;
	}
	public int getProgressAlignment() {
		return 0;
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
		if(pos < 62)	{ return "stomach"; }						
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
		if(pos < 62)	{ return slots[6]; }											
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
			case 0: return "right hand";
			case 1: return "left hand";
			default: return "left butt cheek";
		}
	}
	
		
	public Item getLimb(int limb)
	{
		return new HumanHand();
	}

	public int getLimbCount()
	{
		return 2;
	}
	
	public static Race getInstance()
	{
		return (Race) World.get("races/human");
	}
	
	public int getBaseHpRegen()
	{
		return 50;
	}
	
	public int getBaseSpRegen()
	{
		return 50;
	}
	
	public int getSize()
	{
		return 50;
	}
	
	public int getExpRate()
	{
		return 100;
	}
	
	
	private String hitType(Damage.Type type)
	{
		switch(type)
		{
			case CRUSHING: return "crushing";
			case PIERCING: return "piercing";
			case CHOPPING: return "chopping";
			case SLASHING: return "slashing";
			default: return "spanking";
		}
	}
	
	public String[] getGenders() {
		return genders;
	}

	public int getMinimumVisibleIllumination() {
		return 35;
	}

	/* Max stats for this race */
	public int getMaxPhysicalStrength() { return 50; }
	public int getMaxMentalStrength() { return 50; }
	public int getMaxPhysicalConstitution() { return 50; }
	public int getMaxMentalConstitution() { return 50; }
	public int getMaxPhysicalCharisma() { return 50; }
	public int getMaxMentalCharisma() { return 50; }
	public int getMaxPhysicalDexterity() { return 50; }
	public int getMaxMentalDexterity() { return 50; }

	/* Statcosts for this race (0-10) */
	public int getPhysicalStrengthCost() { return 5; }
	public int getMentalStrengthCost() { return 5; }
	public int getPhysicalConstitutionCost() { return 5; }
	public int getMentalConstitutionCost() { return 5; }
	public int getPhysicalCharismaCost() { return 5; }
	public int getMentalCharismaCost() { return 5; }
	public int getPhysicalDexterityCost() { return 5; }
	public int getMentalDexterityCost() { return 5; }

}
