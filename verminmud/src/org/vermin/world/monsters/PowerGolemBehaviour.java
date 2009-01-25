/*
 * Created on 7.1.2006
 */
package org.vermin.world.monsters;

import java.util.Iterator;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Skill;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.behaviour.BehaviourAdapter;

public class PowerGolemBehaviour extends BehaviourAdapter {

	@Override
	public void onBattleTick(Living who) {
		super.onBattleTick(who);
		if(!owner.inBattle()) {
			Iterator it = owner.getRoom().findByType(Types.LIVING);
			while(it.hasNext()) {
				Living l = (Living) it.next();
				if(l instanceof Player) {
					if(Dice.random(10) == 1) {
						owner.getRoom().notice(null, "Power golem says: 'INTRUDER DETECTED'");
				        owner.addAttacker(l);
					}
					return;
				}
			}
		}
	}

	@Override
	public void startsUsing(Living who, Skill skill) {
		super.startsUsing(who, skill);
		
		if(!owner.inBattle()) {
			owner.getRoom().notice(null, "Power golem says: 'POTENTIAL THREAT DETECTED'");
	        owner.addAttacker(who);
		}
	}
	
}
