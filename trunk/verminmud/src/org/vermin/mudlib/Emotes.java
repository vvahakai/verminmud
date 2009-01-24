package org.vermin.mudlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.vermin.util.Print;

/**
 * Handles emotes.
 */
public class Emotes {


	private static class Emote {
		public boolean mayTarget = false;
		public boolean requireTarget = false;
		public boolean adverbAllowed = false;
		public String defaultTarget = null;
		public String defaultAdverb = null;
		public String actorMessage = null;
		public String spectatorMessage = null;
		public String targetMessage = null;
	};


	protected static Emotes _instance = new Emotes();

	private HashMap<String,Emote> emotes = new HashMap<String, Emote>();

	// Adverbs mapped by their first 3 letters
	// the value contains a list of the adverbs that start
	// with the key
	private HashMap<String,List<String>> adverbs = new HashMap<String, List<String>>();
	
	protected Emotes() {
		loadData();
	}
	
	public static Emotes getInstance() {
		return _instance;
	}

	// load emotes and adverbs
	private void loadData() {
		loadEmotes();
		loadAdverbs();
	}

	private void loadAdverbs() {
		try {
			File f = new File("objects/common/emotes/adverbs");
			BufferedReader in = new BufferedReader(new FileReader(f));

			for(String line = in.readLine(); line != null; line = in.readLine()) {
				
				line = line.trim();
				if(line.startsWith("#") || line.length() == 0)
					continue;

				String key = line.substring(0,3);
				List<String> list = adverbs.get(key);
				if(list == null) {
					list = new ArrayList<String>();
					adverbs.put(key, list);
				}
				list.add(line);
			}
		} catch(Exception e) {
			World.log("Unable to load adverbs: "+e.getMessage());
			e.printStackTrace(System.out);
		}

	}


	private void loadEmotes() {
		try {
			File f = new File("objects/common/emotes/emotelist2");
		
			BufferedReader in = new BufferedReader(new FileReader(f));

			int lineNum=0;
			for(String line = in.readLine(); line != null; line = in.readLine()) {
				lineNum++;

				if(line.startsWith("#") || line.trim().length() == 0)
					continue;

				String[] components = line.split(":");
				if(components.length < 6) {
					System.out.println("Unable to parse emote (line "+lineNum+"), expected 6 or 7 components, got "+components.length);
					continue;
				}

				Emote e = new Emote();
				String emote = components[0];

				/* Parse emote options:
				 * M (may target), R (target required), A (adverb allowed)
				 */

				if(components[1].contains("M")) {
					e.mayTarget = true;
				} else if(components[1].contains("R")) {
					e.requireTarget = true;
				}
				if(components[1].contains("A")) {
					e.adverbAllowed = true;
				}


				/* Defaults for adverb and target */
				if(components[2].length() > 0)
					e.defaultAdverb = components[2];
				if(components[3].length() > 0)
					e.defaultTarget = components[3];

				/* Messages for actor, spectators and target */

				e.actorMessage = components[4];
				e.spectatorMessage = components[5];
				if(components.length > 6)
					e.targetMessage = components[6];

				emotes.put(components[0], e);
			}
		} catch(Exception e) {
			World.log("Unable to load emotes.");
			e.printStackTrace(System.out);
		}
	}

	public boolean run(Player who, String command) {

		//System.out.println("EMOTE CMD: "+command);
		String[] comps = command.trim().split("\\s+");
		
		if(comps.length < 1)
			return false;

		String emote = comps[0];
		Emote e = emotes.get(emote);
		if(e == null)
			return false;

		String adverb = null;
		String target = null;

		if(comps.length == 1) {
			doEmote(who, e, adverb, target);
		} else {
			if(!comps[1].equalsIgnoreCase("at")) {
				adverb = comps[1];
				if(comps.length == 2)
					doEmote(who, e, adverb,target);
				else {
					if(comps[2].equalsIgnoreCase("at") && comps.length > 3)
						target = comps[3];
					else 
						target = comps[2];
					doEmote(who, e, adverb, target);
				}

			} else if(comps.length > 2) {
				target = comps[2];
				doEmote(who, e, adverb, target);
			} else {
				who.notice("Emotes with 'at' keyword must specify a target.");
			}
		}
		return true;
	}

	private String AMBIGUOUS = new String();
	private void doEmote(Player who, Emote e, String adverb, String target) {
		
		Object advo = null;
		String adv = null;
		if(adverb != null) {
			advo = getAdverb(adverb);
			if(advo == null) {
				who.notice("'"+adverb+"' is not a valid adverb.");
				return;
			} else if(advo instanceof List) {
				if(((List)advo).isEmpty() && target == null) { // This is probably supposed to be the target
					target = adverb; 
				}
				else {
					who.notice("The adverb '"+adverb+"' is ambiguous (possible adverbs "+Print.join((List) advo, ", ")+").");
					return;
				}
			} else if(advo instanceof String)
				adv = advo.toString();
		} else adv = e.defaultAdverb;

		String tgt = null;
		MObject tgtObject = null;
		if(target != null) {
			if(e.mayTarget || e.requireTarget) {
				tgtObject = getTarget(who, target);
				if(tgtObject == null) {
					who.notice(target+" is not a valid target.");
					return;
				} else tgt = tgtObject.getDescription();
			}
		} else tgt = e.defaultTarget;

		if(e.requireTarget && tgt == null) {
			who.notice("Please specify a target.");
			return;
		}

		renderEmote(who, e, adv, tgt, tgtObject);

	}

	// render the emote, all parsing and error checking is performed before this call
	private void renderEmote(Player who, Emote emote, String adverb, String target, MObject tgtObject) {

		Room r = who.getRoom();
		Iterator it = r.findByType(Types.TYPE_LIVING);

		String spectMessage = renderMessage(who, emote.spectatorMessage, adverb, target);

		while(it.hasNext()) {

			Living living = (Living) it.next();

			if(living == tgtObject)
				living.notice(renderMessage(who, emote.targetMessage, adverb, target));
			else if(living == who)
				living.notice(renderMessage(who, emote.actorMessage, adverb, target));
			else
				living.notice(spectMessage);
		}
	}

	private MObject getTarget(Player who, String name) {
		return who.getRoom().findByName(name);
	}

	private Object getAdverb(String adverb) {

		if(adverb.length() < 3)
			return null;

		String key = adverb.substring(0,3);
		List<String> l = adverbs.get(key);

		if(l == null)
			return null;

		ArrayList<String> candidates = new ArrayList<String>();
		for(String s : l) {
			if(s.startsWith(adverb)) {
				candidates.add(s);
			}
		}
		
		if(candidates.size() == 1)
			return candidates.get(0);
		else 
			return candidates;
		
	}

	private String renderMessage(Player who, String msg, String adverb, String target) { 
		//System.out.println("---- renderMessage ----");
		//System.out.println("  who:    "+who.getName());
		//System.out.println("  msg:    "+msg);
		//System.out.println("  adverb: "+adverb);
		//System.out.println("  target: "+target+"\n");

		msg = msg.replace("$name$", who.getName());
		if(adverb != null)
			msg = msg.replace("$adv$", adverb);
		else 
			msg = msg.replace("$adv$", "");

		msg = msg.replace("$possessive$", who.getPossessive());
		if(target == null) {
			msg = msg.replaceAll(" \\(.*\\)", "");
		} else {
			msg = msg.replace("$tgt$", target);
			msg = msg.replace("(", "");
			msg = msg.replace(")", "");
		}
		msg = msg.replace("  ", " ");
		return msg;
	}

}
