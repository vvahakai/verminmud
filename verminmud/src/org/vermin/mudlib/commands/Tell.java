/* Tell.java
	8.5.2004	Tatu Tarvainen

	Modified from the old TellCommand.java
	
	Recognized forms:
	  'tell <player> <text>'   Say something to someone
	  'tell last'              Show last 20 messages
	  'reply <text>'           Tell something to the person(s) who last told you something

*/
package org.vermin.mudlib.commands;

import java.util.ArrayList;
import java.util.WeakHashMap;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.World;

public class Tell extends RegexCommand {

	private static class TellEntry {
		public Living[] last; // sender+recipients of the last received tell (not including self)
		public String[] tells = new String[20]; // last 20 tells
		public int lastTellIndex=0;
	};

	// store data needed by this command (the key is the living)
	private static WeakHashMap<Living, TellEntry> entries = new WeakHashMap();

	public String[] getDispatchConfiguration() {
		return new String[] {
			"tell ((\\w+\\s*,\\s*)+\\w+) (.*) => tellMany(actor, 1, 3)",
			"tell (\\w+) (.+) => tell(actor, player 1, 2)",
			"tell last => tellLast(actor)",
			"reply (.+) => reply(actor, 1)"
		};
	}

	public void tell(Player actor, Living target, String text) {
		
		String targetMessage = actor.getName() +" tells you '&B;"+text+"&;'.";
		String actorMessage = "You tell "+target.getName()+" '&B;"+text+"&;'.";

		TellEntry te = getTellEntry(target);
		synchronized(te) {
			te.last = new Living[] { actor };
			te.lastTellIndex++;
			te.tells[te.lastTellIndex%20] = targetMessage;
			
			actor.getClientOutput().chat().tell(actor.getName(), target.getName(), text);
			if(target instanceof Player)
				((Player) target).getClientOutput().chat().tell(actor.getName(), target.getName(), text);
			else
				// 	is this the proper action? how to tell monsters stuff
				target.notice(targetMessage); 

			//target.notice(targetMessage);
			//actor.notice(actorMessage);
		}
	}

	public void tellMany(Player actor, String recipients, String text) {
		
		String[] rcpts = recipients.split(",");
		
		ArrayList<Living> al = new ArrayList<Living>();
		for(int i=0; i<rcpts.length; i++) {
			Living l = getPlayer(rcpts[i].trim());
			if(l != null)
				al.add(l);
		}

		tell(actor, al.toArray(new Living[al.size()]), text);
	}

	private Player getPlayer(String name) {
		if(World.isLoggedIn(name)) {
			return (Player) World.get("players/"+name);
		}
		return null;
	}
			
	public void tell(Player actor, Living[] recipients, String text) {
		
		// build actor message
		StringBuilder sb = new StringBuilder();
		sb.append("You tell ");
		for(int r=0; r<recipients.length; r++) {
			sb.append(recipients[r].getName());
			if(r < recipients.length-1)
				sb.append(", ");
		}
		sb.append(" '");
		sb.append(text);
		sb.append("'.");
		actor.notice(sb.toString());

		for(int i=0; i<recipients.length; i++) {

			// create recipient array for reply command (swap target for actor)
			Living[] rcpts = (Living[]) recipients.clone();
			rcpts[i] = actor; 
			
			// build target message
			sb = new StringBuilder();
			sb.append(actor.getName());
			sb.append(" tells you");
			for(int r=0; r<recipients.length; r++) {
				if(r==i) continue;
				sb.append(", ");
				sb.append(recipients[r].getName());
			}
			sb.append(" '&B;");
			sb.append(text);
			sb.append("&;'.");
			String targetMessage = sb.toString();

			TellEntry te = getTellEntry(recipients[i]);
			synchronized(te) {
				te.last = rcpts;
				te.lastTellIndex++;
				te.tells[te.lastTellIndex%20] = targetMessage;
				recipients[i].notice(targetMessage);
			}
		}

	}

	public void tellLast(Player actor) {
		TellEntry te = getTellEntry(actor);
		synchronized(te) {
			int ind = te.lastTellIndex+1;
			for(int i=0; i<20; i++) {
				if(te.tells[ind%20] != null) actor.notice(te.tells[ind%20]);
				ind++;
			}
		}
	}

	public void reply(Player actor, String text) {
		TellEntry te = getTellEntry(actor);
		Living[] to;
		synchronized(te) {
			to = te.last;
			if(to == null) {
				actor.notice("You haven't been told anything yet.");
				return;
			}
		}

		if(to.length == 1)
			tell(actor, to[0], text);
		else 
			tell(actor, to, text);
	}


	private static synchronized TellEntry getTellEntry(Living key) {
		TellEntry te = entries.get(key);
		if(te==null) {
			te = new TellEntry();
			entries.put(key, te);
		}
		return te;
	}
	
}
