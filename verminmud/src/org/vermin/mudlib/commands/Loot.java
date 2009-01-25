package org.vermin.mudlib.commands;

import java.util.Iterator;
import java.util.Vector;

import org.vermin.mudlib.Command;
import org.vermin.mudlib.Commander;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Types;
import org.vermin.world.commands.GetCommand;

/**
 * @deprecated 
 * @see org.vermin.world.commands.GetCommand
 * @author tadex
 *
 */
public class Loot implements Command {

	public boolean action(Living who, String cmd) {
		GetCommand gc = (GetCommand) Commander.getInstance().get("get");
		Iterator en = who.getRoom().findByType(Types.ITEM);
		boolean taken=false;
		while(en.hasNext()) {
			MObject corpse = (MObject) en.next();
			if(corpse.isAlias("corpse")) {
				taken = true;
				Vector v = new Vector();
				v.add("all");
				//gc.doGet(who, v, corpse, "corpse");
			}
		}
		if(!taken)
			who.notice("There are no corpses in the room.");
        return false;
	}
}
