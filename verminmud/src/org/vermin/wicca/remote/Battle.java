/*
 * Created on 19.8.2005
 */
package org.vermin.wicca.remote;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.Reaction;

@ComponentID(id = 4)
public interface Battle {

	@MethodID(id = 1)
	public void attack(Attack a, Reaction r, Living subject);
	
	@MethodID(id = 2)
	public void defend(Attack a, Reaction r, Living subject);
	
	@MethodID(id = 3)
	public void end();
	
	@MethodID(id = 4)
	public void die(String who);

}
