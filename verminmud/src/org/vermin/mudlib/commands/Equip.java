package org.vermin.mudlib.commands;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.UtilitySlot;
import org.vermin.mudlib.Wearable;

public class Equip extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"equip (.*) to (.*) => equip(actor, inventory 1, inventory 2)",
				"equip (.*) => equip(actor, inventory 1)"
		};
	}

	public void equip(Living who, MObject item, MObject to) {
		if(!(to instanceof Wearable)) {
			who.notice(to.getDescription() + " is not wearable equipment.");
			return;
		}
		
		Wearable w = (Wearable) to;
		UtilitySlot[] slots = w.getUtilitySlots();
		
		if(slots == null || slots.length == 0) {
			who.notice(to.getDescription() + " does not have any utility slots.");
			return;
		}
		
		boolean hasSuitable = false;
		UtilitySlot target = null;
		
		for(UtilitySlot slot : slots) {
			
			if(slot.isSuitable(item))
				hasSuitable = true;
			
			if(slot.isReserved())
				continue;
			
			target = slot;
			break;
		}
		
		if(target != null) {
			target.equip(item);
			who.notice("You equip "+item.getDescription()+" to your "+to.getDescription()+" for easy access.");
		} else {
			if(hasSuitable)
				who.notice(to.getDescription() + " has suitable slots but all are reserved.");
			else
				who.notice(to.getDescription() + " does not have any suitable slots.");
		}
	}
	
	public void equip(Living who, MObject item) {
		
		Wearable[] armor = who.getWornItems();
		
		MObject to = null;
		UtilitySlot target = null; 
			
		for(Wearable w : armor) {
		
			UtilitySlot[] slots = w.getUtilitySlots();
			if(slots == null || slots.length == 0)
				continue;
			
			for(UtilitySlot slot : slots) {
				if(!slot.isReserved() && slot.isSuitable(item)) {
					to = w;
					target = slot;
					break;
				}
			}
		}
		
		if(to == null) {
			who.notice("No suitable utility slot was found in your worn items.");
		} else {
			target.equip(item);
			who.notice("You equip "+item.getDescription()+" to your "+to.getDescription()+" for easy access.");
		}
	}
}
