package org.vermin.mudlib.battle;

import org.vermin.mudlib.*;

public class SkillCheckEffector implements Effector {

	private int effectiveness;
	private String skill;

	public SkillCheckEffector(int e, String s) {
		effectiveness = e;
		skill = s;
	}

	public int getEffectiveness() {
		return effectiveness;
	}

	public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
		return actor.getSkill(skill);
	}

}
