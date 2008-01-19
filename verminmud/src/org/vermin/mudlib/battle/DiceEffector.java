package org.vermin.mudlib.battle;

import org.vermin.mudlib.*;

public class DiceEffector implements Effector {

	private int effectiveness;

	public DiceEffector(int e) {
		effectiveness = e;
	}

	public int getEffectiveness() {
		return effectiveness;
	}

	public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
		return Dice.random();
	}

}
