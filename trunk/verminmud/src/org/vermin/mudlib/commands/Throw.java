package org.vermin.mudlib.commands;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.battle.ThrowOrder;

public class Throw extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"throw (.*) at (.*) => throwIt(actor, item 1, living 2)"
		};
	}
	
	public void throwIt(Living who, MObject item, Living target) {
		
		if(item == null) {
			who.notice("Throw what?");
			return;
		}
		
		Wieldable[] wielded = who.getWieldedItems(false);
		boolean found = false;
		for(Wieldable w : wielded)
			if(w == item)
				found = true;
		
		if(!found) {
			who.notice("You are not wielding "+item.getDescription()+".");
			return;
		}
		
		if(target == null || who.getRoom() != target.getRoom())
			who.notice("Throw at who?");
		else {
			who.addAttacker(target);
			who.getBattleStyle().setOrder(new ThrowOrder(who, target, (Wieldable) item));
		}
	}

}
