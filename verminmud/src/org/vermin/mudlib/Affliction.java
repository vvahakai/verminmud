package org.vermin.mudlib;

import org.vermin.driver.PropertyProvider;

/**
 * af�flic�tion 
 * n.
 *   1. A condition of pain, suffering, or distress. 
 *   2. A cause of pain, suffering, or distress. 
 */
public interface Affliction extends PropertyProvider<LivingProperty> {

	public enum Type {

		/**
		 * Stun affliction causes the victim to temporarily lose 
		 * some mobility. The severity ranges from no skill usage to 
		 * no battle and finally to total immobility.
		 *
		 * The stun duration and severity is calculated from the 
		 * stun damage received and the stats of the sufferer.
		 * The damage scale is the same as in normal damage, so that
		 * a value equal to or higher than the maximum hitpoints of
		 * the sufferer means worst possible stun effect.
		 */
		STUN,

		/**
		 * Poison affliction causes the victim to lose hitpoints
		 * in every battle and regeneration tick. The poison
		 * causes more damage in battle than in rest.
		 *
		 * The amount of hitpoints lost per round and the duration
		 * of the effect are calculated from the damage amount and
		 * the stats of the sufferer. The damage scale is the same
		 * as in normal damage, so that a value equal to or higher
		 * than the maximum hitpoints of the sufferer should
		 * be lethal if unresisted or untended.
		 */
		POISON,

		/**
		 * Web affliction causes total immobilization, but may be
		 * broken by a successful strength test.
		 */
		WEB,
		
		/** drunken effect not yet available */
		ALCOHOL
	};

	/**
	 * Returns the current amount of this affliction.
	 */
	public int getAmount();

	/**
	 * The living who is suffering this affliction.
	 *
	 * @return the sufferer of this afffliction
	 */
	public Living getSufferer();

	/**
	 * Gets the cause of this affliction, if it is the
	 * result of a hit.
	 *
	 * @return the cause of the affliction, or null if not applicable
	 */
	public Living getCause();

	/**
	 * Called on a regen tick.
	 */
	public void onRegen();

	/**
	 * Called on a battle tick.
	 */
	public void onBattle();


	/**
	 * Called when the affliction is added to a living.
	 */
	public void start(Living who);

	/**
	 * Called when the affliction ends.
	 */
	public void end();


	/**
	 * Returns the type of this affliction.
	 */
	public Type getType();

	// TODO: cure?
}
