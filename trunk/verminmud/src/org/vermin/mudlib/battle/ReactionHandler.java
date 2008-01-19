package org.vermin.mudlib.battle;

public interface ReactionHandler {

	/**
	 * Participate in the reaction handling chain.
	 * The current reaction is given as a parameter.
	 * The returned reaction is given to the next
	 * handler in the chain.
	 *
	 * If the reaction is not handled in any way,
	 * the handler must return the reaction given
	 * as a parameter.
	 *
	 * @param attack the attack to react to
	 * @param reaction the current reaction
	 * @return the new reaction
	 */
	public Reaction handleReaction(Attack attack, Reaction reaction);

}
