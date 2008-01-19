
package org.vermin.mudlib.battle;

import org.vermin.mudlib.*;

public class Attack extends Message {

	public Attack() {}
	
	public boolean critical; // is this a critical hit?

	public Living attacker;
	public Wieldable weapon;
	public Damage[] damage;
	public int hitLocation; // -1 for NONE
        
	/**
	 * Convinience method for resolving the largest
	 * damage of several.
	 */
	public Damage getMaxDamage() {
		Damage max = damage[0];
		for(int i=0; i<damage.length; i++) {
			if(max.damage < damage[i].damage) {
				max = damage[i];
			}
		}
           
		return max;
	}

}
