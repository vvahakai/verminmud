
package org.vermin.mudlib.commands;

import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Edible;
import org.vermin.util.Print;
/**
 * Allows a player to eat.
 * 
 * @author Jaakko Pohjamo
 */
public class EatCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"eat => eat(actor)",
			"eat (.+) => eat(actor, 1)"
		};
	}
	
	public void eat(Living actor) {
		actor.notice("Eat what?");
	}
	
	public void eat(Living actor, String what) {
		Item food = (Item) actor.findByNameAndType(what, Types.TYPE_ITEM);
		
		if(food == null) {
			actor.notice("You don't have any '"+what+"'.");
			return;
		}
		
		if(!(food instanceof Edible)) {
			actor.notice(Print.capitalize(food.getName())+ " isn't edible.");
		} else {
			Edible e = (Edible) food;
			
			if(e.tryConsume(actor)) {
				e.consume(actor);
			}
		}
	}
}
