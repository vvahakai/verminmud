package org.vermin.mudlib;

import org.vermin.mudlib.battle.Message;
import org.vermin.mudlib.battle.Order;

/**
 * Object representing a combat style.
 * 
 * @author Jaakko Pohjamo
 * @version 1.0
 */
public interface BattleStyle {

	/**
	 * This is called when someone attacks the
	 * owner of this battle style.
	 *
	 * @param attacks an array of attacks
	 * @return reactions to the attacks
	 */
	public Message[] accept(Message[] attacks);

   /**
    * This method is called in every attack skill use.
    * It should return whether this style is usable in the
    * current status of the Living.
    * (eg. correct weapons wielded etc.)
    * 
    * @return Can this style be used.
    */
   public boolean tryUse();

	/**
	 * Use this battle style.
	 * This is meant to be called on every battle tick.
	 *
	 * @return true if an opponent was found, false otherwise
	 */
	public boolean use();

	/**
	 * Get the owner of this battle style.
	 *
	 * @return the owner
	 */
	public Living getOwner();

	/**
	 * Sets the owner of this battle style.
	 *
	 * @param owner  the owner
	 */
	public void setOwner(Living owner);
	
	/**
	 * Set the current target.
	 */
	public void setTarget(Living name);
	  
	/**
	 * Returns the name of this battlestyle.
	 */
	public String getName();

	/**
	 * Returns the defensive hit modifier.
	 * The modifier represents how many percent the attacker's
	 * hit chance is decreased.
	 * Scale 0 - 100. A value of zero means that this battlestyle
	 * has no defensive bonus.
	 *
	 * @return the hit modifier
	 */
	public int calculateHitModifier();

	/**
	 * Check if the owner succeeds in tumbling a physical attack.
	 *
	 * @return true if tumble succeeds, false otherwise
	 */
	public boolean canTumbleSkill();

	/**
	 * Check if the owner succeeds in tumbling a magical attack.
	 *
	 * @return true if tumble succeeds, false otherwise
	 */
	public boolean canTumbleSpell();

	/**
	 * Get message for tumbling a spell.
	 */
	public String getAttackerTumbleSpellMessage();
	/**
	 * Get message for tumbling a spell.
	 */
	public String getOwnerTumbleSpellMessage();
	/**
	 * Get message for tumbling a skill.
	 */
	public String getAttackerTumbleSkillMessage();
	/**
	 * Get message for tumbling a skill.
	 */
	public String getOwnerTumbleSkillMessage();

	/**
	 * Create affliction.
	 * This method is used to create a possibly specialized
	 * affliction for the given type and amount.
	 * Afflictions are added to the victim for damagetypes that
	 * don't directly subtract hit points.
	 *
	 * @param type the type of the affliction
	 * @param amount the amount of damage taken
	 * @return the affliction
	 */
	public Affliction createAffliction(Affliction.Type type, int amount);

	/**
	 * 
	 */
	public void onBattleStop();
	
	/**
	 * Set a standing battle order. 
	 * The order will be executed on the next battle tick.
	 * 
	 * @param order the battle order
	 */
	public void setOrder(Order order);
	
	/**
	 *  Returns a verbal description of the owner's proficiency with his/her wielded weapons.
	 * @return the description of proficiency
	 */
	public String describeWeaponProficiency();
	
	/**
	 *  Calculates the number of times the owner will attack with given weapon
	 * @param weapon the weapon to calculate attacks for
	 * @return number of attacks per round, can be under 1
	 */
	public float calculateWeaponSpeed(Wieldable weapon);
}
