/* UndeadRace.java
	23.2.2002
	
	Undead facade race.
*/

package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.GoreProperty;

public class UndeadRace implements Race
{

	private Race old;
	
	private String race;
	private EnumSet<GoreProperty> goreFlags;
    private EnumSet<LivingProperty> raceProperties;
    
	public UndeadRace(Race hasBegun) {
	
		old = hasBegun;
	
		switch(Dice.random(8)-1) {
			case 0: race = "skeleton"; break;
			case 1: race = "zombie"; break;
			case 2: race = "mummy"; break;
			case 3: race = "ghoul"; break;
			case 4: race = "wight"; break;
			case 5: race = "ghost"; break;
			case 6: race = "wraith"; break;
			case 7: race = "liche"; break;
			default: race = "naskuli";
		}
		
		goreFlags = EnumSet.copyOf(old.getGoreFlags());
        goreFlags.add(GoreProperty.IS_UNDEAD);

        raceProperties = EnumSet.copyOf(old.getRaceProperties());
        raceProperties.add(LivingProperty.DOES_NOT_BREATHE);
        raceProperties.add(LivingProperty.MINDLESS);
        raceProperties.add(LivingProperty.UNDEAD);

	}
	public int getLifeAlignment() {
		return 0;
	}
	public int getProgressAlignment() {
		return 0;
	}
	
	public int getIntrinsicSkill(String name) {
		return 0;
	}

	public int getMaximumVisibleIllumination() {
		return 100;
	}

	public EnumSet<LivingProperty> getRaceProperties() {
        return raceProperties;
	}


	public EnumSet<GoreProperty> getGoreFlags() {
		return goreFlags;
	}
	public int getMinimumVisibleIllumination() {
		return old.getMinimumVisibleIllumination();
	}

	public String getLimbName(int limb)
	{
		return old.getLimbName(limb);
	}
	
	public String getHitLocation(int pos)
	{
		return old.getHitLocation(pos);
	}
	
	public String getName() 
	{ 
		return race;
	}
	
	public Item getLimb(int limb)
	{
		return old.getLimb(limb);
	}
	
	public int getLimbCount()
	{
		return old.getLimbCount();
	}
	
	public int getBaseHpRegen()
	{
		return 0;
	}
						
	public int getBaseSpRegen()
	{
		return 0;
	}
	
	public int getSize()
	{
		return old.getSize();
	}
	
	public int getExpRate()
	{
		return 0;
	}

	public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation)
	{
		return old.getAttackerGoreMessage(who, attack, hitLocation);
	}

	public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation)
	{
		return old.getSubjectGoreMessage(who, attack, hitLocation);
	}

   public Slot[] getSlots() {
      return old.getSlots();
   }
   public Slot getSlotForLocation(int slot) {
      return old.getSlotForLocation(slot);
   }
   
	public String[] getGenders() {
		return old.getGenders();
	}

	/* Max stats for this race */
	public int getMaxPhysicalStrength() { return old.getMaxPhysicalStrength(); }
	public int getMaxMentalStrength() { return old.getMaxMentalStrength(); }
	public int getMaxPhysicalConstitution() { return old.getMaxPhysicalConstitution(); }
	public int getMaxMentalConstitution() { return old.getMaxMentalConstitution(); }
	public int getMaxPhysicalCharisma() { return old.getMaxPhysicalCharisma(); }
	public int getMaxMentalCharisma() { return old.getMaxMentalCharisma(); }
	public int getMaxPhysicalDexterity() { return old.getMaxPhysicalDexterity(); }
	public int getMaxMentalDexterity() { return old.getMaxMentalDexterity(); }

	/* Statcosts for this race (0-10) */
	public int getPhysicalStrengthCost() { return old.getPhysicalStrengthCost(); }
	public int getMentalStrengthCost() { return old.getMentalStrengthCost(); }
	public int getPhysicalConstitutionCost() { return old.getPhysicalConstitutionCost(); }
	public int getMentalConstitutionCost() { return old.getMentalConstitutionCost(); }
	public int getPhysicalCharismaCost() { return old.getPhysicalCharismaCost(); }
	public int getMentalCharismaCost() { return old.getMentalCharismaCost(); }
	public int getPhysicalDexterityCost() { return old.getPhysicalDexterityCost(); }
	public int getMentalDexterityCost() { return old.getMentalDexterityCost(); }
		
	
}
