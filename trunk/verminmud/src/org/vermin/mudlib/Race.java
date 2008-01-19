/* Race.java
	19.1.2002	TT & VV
	
	Defines the interface for race objects.
*/
package org.vermin.mudlib;

import java.util.EnumSet;

import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.GoreMessageProvider;
import org.vermin.mudlib.battle.GoreProperty;

/**
 * Interaface <code>Race</code>.
 *
 * @author <a href="mailto:ttarvain@mail.student.oulu.fi">Tatu Tarvainen</a>
 * @version 1.0
 */
public interface Race extends Alignment {

	/**
	 * Describes a hit location.
	 * Smaller hit location number means better hit location.
	 * 0 = best hit location, 100 = worst hit location
	 *
	 * @param pos the hit location (0 - 100)
	 * @return the name of the location
	 */
	public String getHitLocation(int pos);
	
	/**
	 * Returns the name of the race.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns the name of the given limb.
	 *
	 * @param limb the limb number
	 * @return the name of the limb
	 */
	public String getLimbName(int limb);

	/**
	 * Returns the given limb.
	 *
	 * @param limb the limb number
	 * @return the limb item
	 */
	public Item getLimb(int limb);

	/**
	 * Returns the number of limbs this race has.
	 *
	 * @return the number of limbs
	 */
	public int getLimbCount();
	
   /**
	 * Returns an array of armor slots.
	 *
	 * @return an array of <code>Slot</code>s
	 */
	public Slot[] getSlots();
   
   /* Returns the slot associated with given
    * hit location. (or null if no slot)
    */
   /**
	 * Returns the slot associated with the given
	 * hit location.
	 *
	 * @param hitLoc the desired hit location (0 - 100)
	 * @return the <code>Slot</code> or <code>null</code> if there is
	 *         no slot for the location
	 */
	public Slot getSlotForLocation(int hitLoc);


	/* NOTE TO IMPLEMENTORS:
	 * Although it is not defined here, you must implement
	 * a method with the following signature:
	 * 	public static Race getInstance()
	 * which must return a valid Race instance for that class.
	 */
	
	/* max stats + regenit + skillmax + skillcost +
	 * exprate  ja niin päin pois. 
	 */

	/**
	 * Returns the maximum amount of physical strength
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxPhysicalStrength();

	/**
	 * Returns the maximum amount of mental strength
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxMentalStrength();

	/**
	 * Returns the maximum amount of physical constitution
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxPhysicalConstitution();

	/**
	 * Returns the maximum amount of mental constitution
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxMentalConstitution();

	/**
	 * Returns the maximum amount of physical charisma
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxPhysicalCharisma();

	/**
	 * Returns the maximum amount of mental charisma
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxMentalCharisma();

	/**
	 * Returns the maximum amount of physical dexterity
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxPhysicalDexterity();

	/**
	 * Returns the maximum amount of mental dexterity
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
	public int getMaxMentalDexterity();

	/**
	 * Returns the cost of one point of physical strength.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
	public int getPhysicalStrengthCost();

	/**
	 * Returns the cost of one point of mental strength.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
	public int getMentalStrengthCost();

	/**
	 * Returns the cost of one point of physical constitution.
	 *
	 * @return a <code>int</code>
	 */
	public int getPhysicalConstitutionCost();

	/**
	 * Returns the cost of one point of mental constitution.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
	public int getMentalConstitutionCost();

	/**
	 * Returns the cost of one point of physical charisma.
	 *
	 * @return a <code>int</code>  from 0 to 10
	 */
	public int getPhysicalCharismaCost();

	/**
	 * Returns the cost of one point of mental charisma.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
	public int getMentalCharismaCost();

	/**
	 * Returns the cost of one point of physical dexterity.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
	public int getPhysicalDexterityCost();

	/**
	 * Returns the cost of one point of mental dexterity.
	 *
	 * @return a <code>int</code> from 0 to 10 
	 */
	public int getMentalDexterityCost();

	/**
	 * Returns the base hit point regeneration rate.
	 *
	 * @return an <code>int</code> value
	 */

	public int getBaseHpRegen();
	/**
	 * Returns the base spell point regeneration rate.
	 *
	 * @return an <code>int</code> value
	 */
	public int getBaseSpRegen();
	
	/**
	 * Returns the size.
	 *
	 * @return a <code>int</code> value
	 */
	public int getSize();

	/**
	 * Returns the experience rate percentage.
	 *
	 * @return a <code>int</code> value
	 */
	public int getExpRate();
	
	/**
	 * Create a gore message for attacker.
	 * @param who the <code>Living</code> who got hurt
	 * @param attack the attack producing the damage
	 * @param hitLocation the hit location of the damage
	 * @return a <code>String</code> value
	 */
	public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation);
        
	/**
	 * Create a gore message for sujbect.
	 * @param who the <code>Living</code> who got hurt
	 * @param attack the attack producing the damage
	 * @param hitLocation the hit location of the damage
	 * @return a <code>String</code> value
	 */
	public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation);

	/**
	 * Returns the available genders for this race.
	 *
	 * @return an array of <code>String</code> values
	 */
	public String[] getGenders();

	/**
	 * Returns the minimum illumination level this Race's creatures
	 * can see in.
	 *
	 * @return the illumination level
	 */
	public int getMinimumVisibleIllumination();

	/**
	 * Returns the maximum illumination level this Race's creatures
	 * can see in.
	 *
	 * @return the illumination level
	 */
    public int getMaximumVisibleIllumination();

	/**
	 * Returns a skill value of an intrinsic skill if such exists.
	 *
	 * @param skillname the name of the intrinsic skill
	 * @return the skill value
	 */
	public int getIntrinsicSkill(String skillname);	

	/**
	 * Get intrinsic race properties.
	 */
	public EnumSet<LivingProperty> getRaceProperties();
	
	/**
	 * Get race gore message flags.
	 * @see GoreMessageProvider org.vermin.battle.GoreMessageProvider
	 */
	
	public EnumSet<GoreProperty> getGoreFlags();
}
