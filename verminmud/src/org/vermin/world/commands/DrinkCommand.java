/* DrinkCommand.java
	13.9.2003		VV
	
*/

package org.vermin.world.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Print;

public class DrinkCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"drink => drink(actor)",
			"drink (.+) => drink(actor, 1)"
		};
	}
	
	public void drink(Living actor) {
		actor.notice("Drink what?");
	}
	
	public void drink(Living actor, String what) {
		Item drink = (Item) actor.findByNameAndType(what, Types.ITEM);
		
		if(drink == null) {
			actor.notice("You don't have any '"+what+"'.");
			return;
		}
		
		if(!(drink instanceof Drinkable)) {
			actor.notice(Print.capitalize(drink.getName())+ " isn't drinkable.");
		} else {
			Drinkable d = (Drinkable) drink;
			
			if(d.tryConsume(actor)) {
				d.consume(actor);
			}
		}
	}
}
