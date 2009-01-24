/* ChannelCommand.java
	23.3.2002	Tatu Tarvainen
	
	Implements chat channels.
*/
package org.vermin.world.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Print;

import java.util.*;

import org.vermin.driver.*;

import java.text.*;


public class ChannelCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			": ?list => showChannels(actor)",
			":\\s*(\\S+) last => last(actor, 1)",
			":\\s*(\\S+) off => leave(actor, 1)",
			":\\s*(\\S+) on => join(actor, 1)",
			":\\s*(\\S+) => showListeners(actor, 1)",
			":\\s*(\\S+) (.*) => say(actor, 1, 2)"
		};
	}

	protected static class Channel {
		public String[] last;	               // ringbuffer of last sayings (25 msgs)
		public Set<Maybe<Player>> ppl;         // players on the channel (Entries)
		public int lastPut;                    // index of last text
      public String topic;                   // channel topic
		
		/* names of allowed player operations (same as in dispatch)
		 * (if null, all operations are allowd)
		 */
		public HashSet<String> playerOperations; 

		
		public Channel(String topic) {
			last = new String[25];
			ppl = new HashSet<Maybe<Player>>();
			lastPut = 0;
         this.topic = topic;
		}
      public Channel() {}
		
	}
		
	/* Channel map: key is channel name
	 * 				 value is a Channel
	 */
	protected HashMap<String,Channel> channels;
	
	public ChannelCommand() {
		channels = new HashMap<String, Channel>();
	}
	
	public void addChannel(String name, String topic) {
		channels.put(name, new Channel(topic));
	}
	
	public void usage(Living who) {
		who.notice("Usage: :<channel> <command or text>\n"+
					  "See 'help channels' for details.");
	}

	public void join(Player who, String channel) {
		Channel ch = channels.get(channel);
		if(ch == null)
			who.notice("No such channel: "+channel);
		else
			if(isAllowed(who, ch, "join"))
				addToChannel(who, channel, ch);
	}
	
	public void leave(Player who, String channel) {
		Channel ch = channels.get(channel);
		if(ch == null)
			who.notice("No such channel: "+channel);
		else
			if(isAllowed(who, ch, "leave"))
				removeFromChannel(who, channel, ch);
	}
	
	public void showListeners(Player who, String channel) {
		Channel ch = channels.get(channel);
		if(ch == null)
			who.notice("No such channel: "+channel);
		else
			if(isAllowed(who, ch, "showListeners"))
				showListeners(who, channel, ch);
	}
	
	public void last(Player who, String channel) {
		Channel ch = channels.get(channel);
		if(ch == null)
			who.notice("No such channel: "+channel);
		else {
			if(isAllowed(who, ch, "last"))
				lastMessages(who, ch);
		}
	}

	public void say(Player who, String channel, String rest) {
		Channel ch = channels.get(channel);
		if(ch == null)
			who.notice("No such channel: "+channel);
		else
			if(isAllowed(who, ch, "say"))
				sayChannel(who, channel, ch, rest);
		
	}
	
	protected void lastMessages(Player who, Channel ch) {
		int last = (ch.lastPut+1) % 25;
		for(int i=0; i<25; i++) {
			if(ch.last[last] != null)
				who.notice(ch.last[last]);
			
			last = (last+1) % 25;
		}
	}

   protected void showListeners(Player who, String chName, Channel ch) {
      
		LinkedList<String> al = new LinkedList<String>();
		int count=0;

		for(Maybe<Player> maybe : ch.ppl) {
			Player p = maybe.get();
			if(p != null) {
				count++;
				al.add(Print.capitalize(p.getName()));
			}
		}

      who.notice("Channel: "+chName+" (Topic: "+ch.topic+")");

      if(count == 0) {
         who.notice("Channel is empty.");
      } else if(count == 1) {
         who.notice(count+ " player: "+Print.listToString(al, ", ")+".");
      } else {
         who.notice(count+ " players: "+Print.listToString(al, ", ")+".");
      }

   }

	
	protected void removeFromChannel(Player who, String chName, Channel chan) {
		Maybe<Player> m = new Maybe<Player>(who.getId());

		if(chan.ppl.contains(m)) {
			chan.ppl.remove(m);
			who.notice("You are no longer listening channel "+chName+".");
		} else {
			who.notice("You are not listening channel "+chName+".");
		}
	}
	
	public void addToChannel(Player who, String chName) {
		Channel ch = channels.get(chName);
		if(ch != null)
			addToChannel(who, chName, ch);
	}

	protected void addToChannel(Player who, String chName, Channel chan) {
		Maybe<Player> m = new Maybe<Player>(who.getId());

		if(chan.ppl.contains(m)) {
			who.notice("You are already listening channel "+chName+".");
		} else {
			chan.ppl.add(m);
			who.notice("You are now listening channel "+chName+".");
		}
	}
	
	/**
	 * Notice something to the given channel. Useful for non-chat channels like info.
	 */
	public void notice(String channel, String what) {
		Channel ch = channels.get(channel);
		if(ch == null) 
			return;

		StringBuilder m = new StringBuilder();
		m.append("[");
		m.append(channel);
		m.append("] ");
		m.append(what);

		String msg = m.toString();
		
		for(Maybe<Player> maybe : ch.ppl) {
			Player p = maybe.get();
			if(p != null) { // Player is in memory
				p.notice(msg);
			}
		}
		
		synchronized(ch) {
			ch.lastPut = (ch.lastPut+1) % 25;
			ch.last[ch.lastPut] = time() + " " + msg;
		}

	}
	protected void sayChannel(Player who, String chName, Channel ch, String what) {

		boolean speakerListening = false;

		StringBuilder m = new StringBuilder();
		m.append(who.getName());
		m.append(" [");
		m.append(chName);
		m.append("]: ");
		m.append(what);

		String msg = m.toString();
		
		for(Maybe<Player> maybe : ch.ppl) {
			
			Player p = maybe.get();
			
			if(p != null) { // Player is in memory
				p.getClientOutput().chat().message(who.getName(), chName, what);
				// p.notice(msg);
				if(p == who) speakerListening = true;
			}
		}
		
		synchronized(ch) {
			ch.lastPut = (ch.lastPut+1) % 25;
			ch.last[ch.lastPut] = time() + " " + msg;
		}

		// if the speaker is is not listening to the channel. add him.
		if(!speakerListening) {
			addToChannel(who, chName, ch);
			who.getClientOutput().chat().message(who.getName(), chName, what);
			//who.notice(msg);
		}

	}

	public void showChannels(Player who) {
		Maybe<Player> m = new Maybe<Player>(who.getId());

		Iterator<String> it = channels.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			if(channels.get(name).ppl.contains(m))
				who.notice(name+" on");
			else
				who.notice(name+" off");
		}
	}
	
	protected String time() {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return df.format(new Date());
	}
	
	private boolean isAllowed(Player who, Channel ch, String operation) {
		if(who instanceof Wizard)
			return true;
		else if(ch.playerOperations == null)
			return true;
		else
			return ch.playerOperations.contains(operation);
	}
}
