package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.World;

public class ThrowOrder implements Order {

	private Living actor, target;
	private Wieldable weapon;
	
	
	public ThrowOrder(Living actor, Living target, Wieldable weapon) {
		this.actor = actor;
		this.target = target;
		this.weapon = weapon;
	}
	
	public boolean execute(Living who) {
		World.log("THROW: "+weapon.getDescription());
	
		
		return true;
	}
	
	public Message[] getAttackMessages(Living target) {
		ProjectileAttack attack = new ProjectileAttack();
		attack.attacker = actor;
		attack.weapon = weapon;		
		attack.hitLocation = Dice.random();
		attack.damage = weapon.getProjectileDamage(target);
		
		return new Message[] { attack };
	}
	public Living getTarget() {
		return target;
	}

}
