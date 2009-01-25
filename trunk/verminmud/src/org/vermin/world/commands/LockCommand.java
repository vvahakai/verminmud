package org.vermin.world.commands;


import org.vermin.mudlib.*;
import org.vermin.world.exits.LockableDoor;
import org.vermin.world.items.Key;

import java.util.Vector;
import java.util.Iterator;

public class LockCommand extends TokenizedCommand {

	public void run(Living who, Vector params) {

		if(params.size() < 2) {
			who.notice("Usage: lock <direction>");
		} else {
			String dir = params.get(1).toString();
			Exit exit = (Exit) who.getRoom().findByNameAndType(dir, Types.EXIT);

			if(exit instanceof LockableDoor) {

				LockableDoor ld = (LockableDoor) exit;

				if(ld.isLocked()) {
					who.notice("It is already locked.");
					return;
				}
				
				boolean haveKeys = false;
				boolean properKey = false;
				Iterator items = who.findByType(Types.ITEM);
				while(items.hasNext()) {
					Item item = (Item) items.next();
					if(item instanceof Key) {

						haveKeys = true;
						Key key = (Key) item;
						if(key.getKeyCode().equals(ld.getKeyCode())) {
							
							properKey = true;
							ld.setLocked(true);
							who.notice("You lock the door with "+key.getDescription()+".");
							break;
						} 
					}
				}
				if(!haveKeys) {
					who.notice("You don't have any keys.");
				} else if(!properKey) {
					who.notice("You don't have the right key.");
				}
			} else {
				who.notice("Lock what?");
			}

		}
		return;
	}

}
