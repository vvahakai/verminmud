package org.vermin.mudlib.battle;

import org.vermin.mudlib.Living;

/**
 * Models a battle order which can be executed
 * in stead of doing melee.
 */
public interface Order {

	/**
	 * Execute this battle order.
	 * Returns true if the order is completed.
	 * 
	 * @param who the actor 
	 * @return true if completed, false otherwise
	 */
	public boolean execute(Living who);
	
	/**
	 * Called after the order is done to check
	 * if this order generated attacks.
	 * For peaceful orders, this method can return
	 * null of an array of zero length.
	 * 
	 * @param target the <code>Living</code> being targeted
	 * @return attack messages
	 */
	public Message[] getAttackMessages(Living target);
	
	/**
	 * Get the target of the attack messages.
	 * If this order generated attack messages,
	 * this method must return a valid target.
	 * 
	 * @return the attack target
	 */
	public Living getTarget();
}
