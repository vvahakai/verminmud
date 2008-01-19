/*
 * Created on 19.8.2005
 */
package org.vermin.wicca.web;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.AttackPrevented;
import org.vermin.mudlib.battle.DamageTaken;
import org.vermin.mudlib.battle.Death;
import org.vermin.mudlib.battle.FailedAttack;
import org.vermin.mudlib.battle.Reaction;

public class Battle extends AbstractWebComponent implements
		org.vermin.wicca.remote.Battle {

	public Battle(WebClientOutput output) {
		super(output);
	}
	
	public void end() {
		write((short) 4, (byte) 3, "");
	}

	public void die(String who) {
		write((short) 4, (byte) 4, who);
	}
	
	public void attack(Attack a, Reaction r, Living subject) {
		
		int potentialDamage = 0;
		if(!(a instanceof FailedAttack)) {
			potentialDamage = a.getMaxDamage().damage;
		}
		int perc = (potentialDamage * 100) / subject.getMaxHp();
		String aType = null;
		String rType = null;
		if(a instanceof FailedAttack) {
			aType = "fail";
		} else {
			aType = "hit";
		}

		if(r == null) {
			rType = "none";
		} else if(r instanceof DamageTaken) {
			rType = "take";
		} else if (r instanceof AttackPrevented) {
			rType = "prevent";
		} else if (r instanceof Death) {
			rType = "death";
		}

		write((short) 4, (byte) 1, Integer.toString(perc), aType, rType);

	}

	public void defend(Attack a, Reaction r, Living subject) {
	
		int potentialDamage = 0;
		if(!(a instanceof FailedAttack)) {
			potentialDamage = a.getMaxDamage().damage;
		}
		int perc = (potentialDamage * 100) / subject.getMaxHp();
		String aType = null;
		String rType = null;
		if (a instanceof FailedAttack) {
			aType = "fail";
		} else {
			aType = "hit";
		}

		if(r == null) {
			rType = "none";
		} else if(r instanceof DamageTaken) {
			rType = "take";
		} else if (r instanceof AttackPrevented) {
			rType = "prevent";
		} else if (r instanceof Death) {
			rType = "death";
		}


		write((short) 4, (byte) 2, Integer.toString(perc), aType, rType);
	}

}
