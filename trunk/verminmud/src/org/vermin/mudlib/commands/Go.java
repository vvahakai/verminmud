package org.vermin.mudlib.commands;

import org.vermin.mudlib.Command;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.World;

public class Go implements Command {

	public boolean action(Living who, String params) {
		
		params = params.substring(3).trim();
		if(params.length() == 0) {
			who.notice("Usage: go <room id | player name>");
		} else if(World.isLoggedIn(params)) {
			// Go to player
			Player p = (Player) World.get("players/"+params.toLowerCase());
			if(p != null)
				go(who, p.getRoom());
		} else {
			// Load as room
			MObject r = (MObject) World.get(params);
			
			if(r == null) {
				who.notice("Unable to load '"+params+"'.");
			} else if(r instanceof Room) {
				go(who, (Room) r);
			} else {
				who.notice("Object is not a room: '"+params+"'.");
			}
		}
			
		return true;
	}

	private void go(Living actor, Room r) {
		actor.notice("Moving from '"+actor.getRoom().getId()+"' to '"+r.getId()+"'.");
		actor.getRoom().remove(actor);
		actor.setParent((Room) r);
		r.add(actor);
	}

}
