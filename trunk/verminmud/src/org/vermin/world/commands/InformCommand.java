/* InformCommand.java
 * 27.5.2002 Tatu Tarvainen
 *
 * Handles the information channel.
 */
package org.vermin.world.commands;

import org.vermin.mudlib.*;

import java.util.Vector;
import java.util.TreeMap;

public class InformCommand extends TokenizedCommand 
{
	protected class InformMask {
		public boolean enter;    // When a player enters the game
		public boolean exit;     // When a player quits the game
		public boolean world;    // When the world is being rebooted etc.
		
		public InformMask() {
			enter = true;
			exit = true;
			world = true;
		}
	}
	
	private TreeMap masks;   // inform masks for all players
	
	private InformMask getMask(Player who) {
		InformMask m = (InformMask) masks.get(who.getId());
		if(m == null) {
			m = new InformMask();
			masks.put(who.getId(), m);
		}
		return m;
	}
	
	
	public void run(Living who, Vector params) {
		if(!(who instanceof Player)) {
			return;
		}
		Player actor = (Player) who;
		if(params.size() < 3) {
			usage(actor);
		} else {
			String type = (String) params.get(1);
			String state = (String) params.get(2);
			
			boolean st;
			if(state.equalsIgnoreCase("on")) {
				st = true;
			} else if(state.equalsIgnoreCase("off")) {
				st = false;
			} else {
				usage(actor);
				return;
			}
			
			InformMask im = getMask(actor);
			if(type.equalsIgnoreCase("enter")) {
				im.enter = st;
			} else if(type.equalsIgnoreCase("exit")) {
				im.exit = st;
			} else if(type.equalsIgnoreCase("world")) {
				im.world = st;
			} else {
				usage(actor);
			}
			
			return;
		}
	}
	
	private void usage(Player who) {
		who.notice("Usage: inform <type> <on | off>\n");
	}
}

