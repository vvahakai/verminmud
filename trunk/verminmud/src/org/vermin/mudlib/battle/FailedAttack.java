package org.vermin.mudlib.battle;

import org.vermin.mudlib.Damage;

public class FailedAttack extends Attack {

	public Damage getMaxDamage() {
		throw new UnsupportedOperationException();
	}
	
	
}
