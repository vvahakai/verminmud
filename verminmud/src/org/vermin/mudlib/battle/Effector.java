package org.vermin.mudlib.battle;

import org.vermin.mudlib.*;

public interface Effector {

	/**
	 * Returns the effectiveness of this effector.
	 * Higher value means that this effector has more
	 * signifigance. The effectiveness values are
	 * relative to the total effectiveness of all
	 * effectors in use.
	 *
	 * If the effectiveness is zero (0) then this effector
	 * will not be scaled by the total effectiveness, but added
	 * as a bonus (unscaled).
	 *
	 * @return the effectiveness
	 */
	public int getEffectiveness();

	/**
	 * Calculate the effect of this effector.
	 *
	 * @param actor the actor
	 * @param target the object of aggression
	 * @param weapon the used weapon
	 */
	public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type damageType);

}
