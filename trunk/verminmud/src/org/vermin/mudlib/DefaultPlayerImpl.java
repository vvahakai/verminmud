package org.vermin.mudlib;

import org.vermin.driver.*;
import org.vermin.driver.Queue;

import java.util.*;

import org.vermin.mudlib.commands.*;

import java.util.regex.*;
import org.vermin.mudlib.LivingProperty;
import org.vermin.util.Collections;
import org.vermin.util.Print;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.world.behaviours.WeatherObservationBehaviour;

public class DefaultPlayerImpl extends DefaultLivingImpl implements Player, ClientInputHandler {

	protected static final Pattern COMMAND_REGEX;
	protected static final Pattern REMOVE_COUNT_REGEX;

	protected transient Prompt prompt = null;
	protected transient ClientOutput co = new NullClientOutput();
	
	protected String startingRoom; 
	protected Hashtable aliasTable;
	
    protected String plan;
    
	/* When was this player created (null for players created before 13.6.2004) */
	protected Date created;

	/* Items that handle certain actions are mapped here
	 * with (String command, MObject handler) as (key, value)
	 */
	protected transient HashMap<String,ActionHandler<MObject>> handlerTable;
	
	/* If some object wants to handle all actions */
	protected transient ActionHandler<MObject> allHandler = null;
	
	
	/* The total number of levels (sum of all guild levels) */
	protected int level;
	
	protected int freeStatPoints;
	protected EnumMap<Stat, Integer> usedStatPoints;
	
	protected SkillObject skills;
	
   protected String surname;
   protected String title;

	/* The gender. This is race specific (most shoud have 'male' and 'female') */
	protected String gender; 
	
	protected LinkedHashSet<String> availableTitles;

	/* Contains preference settings, such as
	 * verbose and idle prompting. 
	 */
	protected Map<String, Object> preferences;
	
	/* Available battle styles */
	protected Vector<BattleStyle> availableBattleStyles;

	/* Flee when hitpoints go below this percent. */
	protected int wimpyPercent;

	/* Summary fields */
	protected transient long summarySessionStart; // session start time (timestamp)
	protected transient long summaryInitialExperience; // experience at the start time
	protected transient long summaryInitialMoney; // money at the start time
	protected transient int summaryKills; // monsters killed (zero when session starts)
	protected transient int summaryRoomsExplored; // rooms explored in this summary

	/* Statistical information for the finger command */
	protected String bestSoloKillDescription = null;
	protected long bestSoloKillExperience = 0;
	protected String bestPartyKillDescription = null;
	protected long bestPartyKillExperience = 0;

	/* friends lists of this player (a hashtable of sets) */
	protected Hashtable<String, HashSet> friends;	

	protected int exploreCount;

	protected transient HungerModifier hungerModifier;

	public static transient final Map<Integer, String> hungerMessages;
	static {
		COMMAND_REGEX = Pattern.compile("^ *(\\d+\\s+)?(:|\\S+)(\\s*(.*))?$");
		REMOVE_COUNT_REGEX = Pattern.compile("^ *\\d*\\s+(.*)$");
		ALIAS_REGEX = Pattern.compile("^\\s*alias\\s*(\\S+)?(\\s+(.*))?$");
		SET_REGEX = Pattern.compile("^ *set\\s+(\\S+)(\\s+(.*))?$");
		WORD_PATTERN = Pattern.compile("(\\S+)");
		ESCAPE_PATTERN = Pattern.compile("[$]");

		hungerMessages = new HashMap<Integer, String>();
		hungerMessages.put(new Integer(3000), "You feel a bit hungry.");
		hungerMessages.put(new Integer(2000), "You feel hungry.");
		hungerMessages.put(new Integer(1000), "You feel very hungry.");
		hungerMessages.put(new Integer(500), "You are ravenous.");
		hungerMessages.put(new Integer(400), "Your stomach hurts.");
		hungerMessages.put(new Integer(300), "You feel faint.");
		hungerMessages.put(new Integer(200), "Your vision dims... you need food!");
		hungerMessages.put(new Integer(100), "You are starving.");
		hungerMessages.put(new Integer(0), "You are STARVING.");
	}

	private static String DEFAULT_PROMPT = "[Hp: $hp$ / $maxhp$] [Sp: $sp$ / $maxsp$] [Exp: $exp$] > ";
	
	private transient boolean started;
	public DefaultPlayerImpl() {
		created = new Date();

		exploreCount = 0;

		startingRoom = "city:0,0";
		aliasTable = new Hashtable();
		handlerTable = new HashMap<String,ActionHandler<MObject>>();
		friends = new Hashtable();
		
		/* Add a skill object */
		skills = new SkillObject();
		
		handlerTable.put("attack", skills);
		handlerTable.put("use", skills);
		handlerTable.put("cast", skills);
		handlerTable.put("kill", skills);
		
		surname = "";
		title = "";
		level = 0;
		freeStatPoints = 0;
		usedStatPoints = new EnumMap(Stat.class);

		preferences = new HashMap();
		preferences.put("idleprompt", "on");
		preferences.put("verbose", "on");
		preferences.put("prompt", DEFAULT_PROMPT);
		preferences.put("showexp", "off");
		
		availableTitles = new LinkedHashSet();
		availableTitles.add("Newbie");

		availableBattleStyles = new Vector<BattleStyle>();
		availableBattleStyles.add(new DefaultBattleStyle(this));
	}	

	public Object getPreference(String key) {
		if(preferences.containsKey(key)) {
			return preferences.get(key);
		}
		return null;
	}
	
	public void addSummaryKill() {
		summaryKills++;
	}

	public int getSummaryRoomsExplored() {
		return summaryRoomsExplored;
	}
	public long getSummaryInitialExperience() {
		return summaryInitialExperience;
	}
	public int getSummaryKills() {
		return summaryKills;
	}
	public long getSummaryInitialMoney() {
		return summaryInitialMoney;
	}
	public long getSummarySessionStart() {
		return summarySessionStart;
	}

	public void addFriend(String name, String list) {
		if(friends.containsKey(list)) {
			((HashSet) friends.get(list)).add(name);
		}
		else {
			HashSet s = new HashSet();
			s.add(name);
			friends.put(list, s);
		}
	}

	public HashSet getFriends(String list) {
		if(friends.containsKey(list)) {
			return (HashSet) friends.get(list);
		}
		return null;
	}

	public boolean removeFriend(String name, String list) {
		if(friends.containsKey(list)) {
			if(((HashSet) friends.get(list)).contains(name)) {
				return ((HashSet) friends.get(list)).remove(name);
			}
			else return false;
		}
		else return false;
	}

	public void startSession() {
		
		// Register this connection
		World.register(name, co.getSession());
		
		// If object is already started, no need to do other initialization
		
		// connection crashed, hijack the session 
		if(started) return;
		
		/*
		if(!co.isSessionActive()) {
			World.log("No client connected for player "+getName()+", delaying session start.");
			return;
		} else {
			World.log("Starting session for player "+getName()+".");
		}
        */
        
		// start a session
		summaryInitialExperience = getTotalExperience() + getExperience();
		
		DefaultMoneyImpl bmi = ((CentralBank) CentralBank.getInstance()).getAccount(this).getValue();
		
		summaryInitialMoney = (long) (getMoney().getValue() + bmi.getValue());
		
		summaryKills = 0;
		summarySessionStart = System.currentTimeMillis();
		
		World.info(getName() + " has entered the game.");

		Room room = (Room) World.get(startingRoom);
		if(room == null)
			room = (Room) World.get("city:0,0,0");
		
		room.enter(this);
		

		started = true;
	}

	public void endSession() {
		quit();
	}
	
	public void start() {

		setAnonymous(false);

		World.log("Starting player "+getName()+". (@ "+this+")");
		
	    handlerTable = new HashMap<String,ActionHandler<MObject>>();
		handlerTable.put("attack", skills);
		handlerTable.put("use", skills);
		handlerTable.put("cast", skills);
		handlerTable.put("kill", skills);
		
		hungerModifier = new HungerModifier(ModifierTypes.REGEN, new Object[0], 0);
		
		addModifier(hungerModifier);
		
		boolean found = false;
		for(Behaviour b : behaviours) {
			if(b instanceof WeatherObservationBehaviour) {
				found = true;
			}
		}
		if(!found) {
			addBehaviour(new WeatherObservationBehaviour());
		}
		super.start();

	}
	
	public String getPronoun() {
		String g = getGender();

		if(g.equalsIgnoreCase("male"))
			return "he";
		else if(g.equalsIgnoreCase("female"))
			return "she";
		else
			return "it"; // Race should handle this
	}

	public ClientOutput getClientOutput() {
		return co;
	}
	
	public void notice(String what) {
		if(co == null) return;
		co.put(what);
	}
	
	/* Map a command to be handled by MObject */
	public void addHandler(String cmd, ActionHandler<MObject> handler) {
		handlerTable.put(cmd, handler);
	}
	
	/* Remove handle mappings for MObject */
	public void removeHandler(ActionHandler<MObject> handler) {
        
		Iterator keys = handlerTable.keySet().iterator();
		
		while(keys.hasNext()) {
			String key = (String) keys.next();
			
			if (handlerTable.get(key) == handler) {
				handlerTable.remove(key);
				break;
			}
		}
	}
	
	private void maybeWimpy() {
		if(wimpyPercent < 1)
			return;

		int wimpyHp = wimpyPercent * getMaxHp() / 100;
		if(getHp() < wimpyHp) {
			Room r = getRoom();
			Iterator e = r.findByType(Types.EXIT);
			ArrayList<Exit> al = new ArrayList<Exit>();
			while(e.hasNext())
				al.add((Exit) e.next());

			int loc = Dice.rng.nextInt(al.size());
			
			notice("You flee.");
			move(al.get(loc));
		}
	}

	public Room getRoom() {
		//return currentRoom;
		return (Room) getParent();
	}
	
	public ActionHandler<MObject> getHandler(String cmd) {
		return handlerTable.get(cmd);
	}
	
	public void handleAll(ActionHandler<MObject> handler) {
		allHandler = handler;
	}
	
	public void unhandleAll() {
		allHandler = null;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int l) {
		level = l;
	}
	
	public int getSkill(String name) { 
		if(skillModifiers == null)
			return skills.getSkill(name);
		else
			return DefaultModifierImpl.calculateInt(this, skills.getSkill(name), (List) skillModifiers.get(name));
	}
	
	public int getSkill(String name, boolean modifiers) {
		return modifiers ? getSkill(name) : skills.getSkill(name);
	}
	
	public void setSkill(String name, int percent) {
		skills.setSkill(name, percent);
	}
	
	public void clearSkills() {
		skills.clearSkills();
	}

	public void add(MObject obj) {
		if(obj instanceof Item) {
			obj.setParent(this);
			inventory.add(obj);
		} else {
			throw new IllegalArgumentException("Tried to add a non-item to player.");
		}
	}

   public String getSurname() {
      return surname;
   }
   public String getTitle() {
      return title;
   }

	private static final Pattern ALIAS_REGEX;
	private void commandAlias(String command) {
		
		Matcher matcher = ALIAS_REGEX.matcher(command);
		matcher.find();

		String subcmd = matcher.group(1);
		String alias = matcher.group(3);
			
		if(subcmd == null) {
			StringBuffer sb = new StringBuffer();
			Enumeration en = aliasTable.keys();
			sb.append("Command aliases:\n");
			while(en.hasMoreElements()) {
				String key = (String) en.nextElement();
				sb.append(key + " = " + aliasTable.get(key) + "\n");
			}
				
			co.put(sb.toString());
		} else {
				
			if(subcmd.equals("remove")) {
				if(alias == null)
					co.put("You must supply an alias name to remove.");
				else
					aliasTable.remove(alias);
			} else {
				if(alias == null)
					co.put("You must supply the expansion form.");
				else
					aliasTable.put(subcmd, alias);
			}
		}
	}

	private static final Pattern SET_REGEX;
	private void commandSet(String command) {
		Matcher m = SET_REGEX.matcher(command);
		if(!m.find()) {
			notice("Usage: set <variable> [value]");
			notice("For more information: help settings");
		} else {
			set(m.group(1), m.group(3));
		}
	}

		
	/* User command from mud client.
	 * Returns true if the command was understood and handled,
	 * false otherwise. 
	 */
	public boolean clientCommand(String command) {
		
		/* Allhandler is checked before alias expansion.
		 * This is essential for the line editor.
		 * The allhandler should not be used for 'normal' game commands.
		 */
		if(allHandler != null) {
			if(allHandler.action(this, command))
				return true;
		}
		
		command = command.trim();
		
		// handle the alias command as a special case (before alias expansion)
		if(ALIAS_REGEX.matcher(command).matches()) {
			commandAlias(command);
			return true;
		}
		
		// Expand aliases
		if(!command.startsWith("say") && !command.startsWith("reply") &&
				!command.startsWith("tell") && !command.startsWith(":")) {
			command = expandAliases(command);
		}
		
		// Extract count and dispatch word
		Matcher m = COMMAND_REGEX.matcher(command);
		if(!m.find())
			return false;

		String countStr = m.group(1);
		String cmd = m.group(2);
		String rest = m.group(4);

		int count = 1;
		if(countStr != null) {
			try {
				// If count was found, parse it and remove it from the command 
				count = Integer.parseInt(countStr.trim());
				Matcher matcher = REMOVE_COUNT_REGEX.matcher(command);
				matcher.find();
				command = matcher.group(1);
			} catch(NumberFormatException nfe) {}
		}
		
		ActionHandler<MObject> handler = getHandler(cmd);

		/* If a registered handler was found, give the command to it.
		 * We don't need to check our list anymore.
		 */
		if(handler != null) {
//this.notice("HELLO! COMMAND '"+command+"' HANDLED BY: "+handler);
			return handler.action(this, command);
		}
		
		/* Give the command to the current room */
		if(getRoom().action(this, command))
			return true;
		
		Command c = getCommand(cmd);
		if(c != null) {
			c.action(this, command);
			return true;
		}
		
		if(cmd.equals("say")) {
			say(rest);
			return true;
		}
		if(cmd.equals("inventory") || cmd.equals("inv")) {
			inventory();
			return true;
		}
		if(cmd.equals("wield")) {
			if(rest == null)
				return false;
			return wield(rest);
		}
		if(cmd.equals("unwield")) {
			if(rest == null)
				return false;
			return unwield(rest);
		}
		if(cmd.equals("wear")) {
			if(rest == null)
				return false;
			return wear(rest);
		}
		if(cmd.equals("unwear") || cmd.equals("remove")) {
			if(rest == null)
				return false;
			return unwear(rest);
		}
		if(cmd.equals("quit")) {
         quit();
			return true;
		}
		if(cmd.equals("map")) {
            Area area = getRoom().getArea();
            
			Mapper mapper = area != null ? area.getMapper() : null;
			
			if(mapper != null) {
				if(rest != null && rest.equalsIgnoreCase("large"))
					co.put(mapper.getLargeMap(getRoom().getId()));
				else if(rest != null && rest.equalsIgnoreCase("legend"))
					mapper.showLegend(this);
				else
					co.put(mapper.getMap(getRoom().getId()));
			}
			else co.put("You are in an unmapped area.");
			return true;
		}
		
		if(cmd.equals("set")) {
			commandSet(command);

			return true;
		}

		if(Emotes.getInstance().run(this, command))
			return true;

		if(cmd.equals(cmd.toUpperCase())) {

			// Uppercase movement command

			String dir = cmd.toLowerCase();
			if(move(dir)) {

				// continue moving until terrain type changes
				String desc = getRoom().getDescription();
				int moved=1;
				while(moveIfDescription(dir, desc) && moved < 50) moved++;
				return true;
			} else return false;
			
		} else if(move(cmd)) {
			if(count > 1) {
				count = count > 50 ? 50 : count;
				int moved=1;
				while(moved < count && move(cmd))
					moved++;
			}

			return true;
		}
		
		return false;
	
	}

	protected Command getCommand(String cmd) {
		return Commander.getInstance().get(cmd);
	}
	
	protected void set(String key, String value) {
		if(key.equalsIgnoreCase("verbose")) {
			if(value == null) {
				notice("Verbose mode is currently "+preferences.get("verbose")+".");
			} else {
				if(value.equalsIgnoreCase("on")) {
					preferences.put("verbose", "on");
				} else if(value.equalsIgnoreCase("off")) {
					preferences.put("verbose", "off");
				}
				notice("Verbose mode is now "+preferences.get("verbose")+".");
			}
		} else if(key.equalsIgnoreCase("idleprompt")) {
			if(value == null) {
				notice("Idle prompting is currently "+preferences.get("idleprompt")+".");
			} else {
				if(value.equalsIgnoreCase("on")) {
					preferences.put("idleprompt", "on");
				} else if(value.equalsIgnoreCase("off")) {
					preferences.put("idleprompt", "off");
				}
				notice("Idle prompting mode is now "+preferences.get("idleprompt")+".");
			}
		} else if(key.equalsIgnoreCase("showexp")) {
			if(value == null) {
				notice("Exp display is currently "+preferences.get("showexp")+".");
			} else {
				if(value.equalsIgnoreCase("on")) {
					preferences.put("showexp", "on");
				} else if(value.equalsIgnoreCase("off")) {
					preferences.put("showexp", "off");
				}
				notice("Exp display is now "+preferences.get("showexp")+".");
			}
		} else if(key.equalsIgnoreCase("showrounds")) {
			if(value == null) {
				notice("Round marker is currently "+preferences.get("showrounds")+".");
			} else {
				if(value.equalsIgnoreCase("on")) {
					preferences.put("showrounds", "on");
				} else if(value.equalsIgnoreCase("off")) {
					preferences.put("showrounds", "off");
				}
				notice("Round marker is now "+preferences.get("showrounds")+".");
			}
		} else if(key.equalsIgnoreCase("prompt")) {
			if(value == null) {
				notice("Current prompt: "+preferences.get("prompt"));
			} else {
				preferences.put("prompt", value);
				notice("Prompt is now: "+value);
			}

		} else if(key.equalsIgnoreCase("surname")) {
			if(value == null) {
				if(surname.length() == 0) {
					notice("You don't have a surname.");
				} else {
					notice("Your surname is "+surname);
				}
			} else {
				if(surname.length() == 0) {
					surname = value;
					notice("Your surname is now "+surname);
				} else {
					notice("You already have a surname.");
				}
			}
		} else if(key.equalsIgnoreCase("title")) {
			if(value == null) {

				notice("Titles available to you:");
				int index = 0;
				for(String title : availableTitles) {
					notice((index+1)+". "+title);
					index++;
				}
				notice("Use: \"set title <number>\" to set the desired title.");
			} else {
				int t = -1;
				try {
					t = Integer.parseInt(value);
				} catch(NumberFormatException fe) {}

				if( t-1 < 0 || t > availableTitles.size()) {
					notice("No such title.");
				} else {
					int index = 0;
					for(String title : availableTitles) {
						index++;
						if(index == t) {
							notice("Your title is now: "+title);
							this.title = title;
							break;
						}
					}
				}
			}
		} else if(key.equals("wimpy")) {
			int percent = 0;
			boolean ok = true;
			try {
				percent = Integer.parseInt(value);
			} catch(NumberFormatException nfe) {
				ok = false;
			}

			if(!ok || percent < 0 || percent > 100) {
				notice("Wimpy value must be a number between 0 (no wimpy) and 100.");
				return;
			}

			wimpyPercent = percent;
			notice("You currently flee at "+wimpyPercent+"% of your maximum hit points.");

		} else {
			notice("No such setting: "+key);
		}
	}

	private static final Pattern WORD_PATTERN;
	private static final Pattern ESCAPE_PATTERN;

	private String expandAliases(String command) {
		StringBuffer result = new StringBuffer();
		Matcher m = WORD_PATTERN.matcher(command);
		while(m.find()) {
			String word = m.group(1);
			String replacement;
			Object rpl = aliasTable.get(word);
			if(rpl == null) {
				Matcher esc = ESCAPE_PATTERN.matcher(word);
				replacement = esc.replaceAll("\\\\\\$"); // don't ask why so many backslashes ;)
			} else {
				replacement = rpl.toString();
			}
			m.appendReplacement(result, replacement);
		}
		m.appendTail(result);
		return result.toString();
	}
			
	private Boolean stringToBoolean(String str) {
		if(str.equalsIgnoreCase("on")) {
			return new Boolean(true);
		} else if(str.equalsIgnoreCase("1")) {
			return new Boolean(true);
		} else if(str.equalsIgnoreCase("true")) {
			return new Boolean(true);
		} else if(str.equalsIgnoreCase("off")) {
			return new Boolean(false);
		} else if(str.equalsIgnoreCase("0")) {
			return new Boolean(false);
		} else if(str.equalsIgnoreCase("false")) {
			return new Boolean(false);
		} else {
			return null;
		}
	}
		 
		
   private void quit() {

		if(inBattle()) {
			notice("You are in battle!");
			return;
		}
		
		Party pi = getParty();
		if(pi != null)
			pi.removeMember(this);

		startingRoom = getRoom().getId();
		getRoom().remove(this);
      
      	getRoom().notice(this, getName()+" suddenly vanishes.");
		co.close();

		World.logout(getName());
		save();
		
		started = false; // to call sessionStart when reconnect
		
		
		World.info(getName()+" has left the game.");
   }
	
	public String getPossessive() {
		return "his";
	}

	protected void look(boolean moved) {

		Look l = 
			(Look) Commander.getInstance().get("look");
		
		l.look(this, moved);
	}

	
	/* Move to a room */
	public boolean move(String cmd) {
		Exit to = (Exit) getRoom().findByNameAndType(cmd, Types.EXIT);

		if(to == null)
			return false;

		Room oldRoom = null;
		
		if(this.getParent() instanceof Room)
			oldRoom = (Room) this.getParent();
		
		if(!super.move(to))
			return true;
		
		//Moved to DefaultLivingImpl
		//moveFollowers(to, oldRoom);		

		return true;
	}

	/* Move to a room (if the description matches) */
	private boolean moveIfDescription(String cmd, String desc) {
		Exit to = (Exit) getRoom().findByNameAndType(cmd, Types.EXIT);

		if(to == null)
			return false;

		Room r = (Room) World.get( to.getTarget(getRoom().getId()) );
		if(r != null && r.getDescription().equals(desc)) {

			Room oldRoom = getRoom();
			
			if(!super.move(to))
				return false;
			
			//look(true); 
			
			moveFollowers(to, oldRoom);
			
			return true;
			
		} else return false;
		
	}

	/* show inventory */
	private void inventory() {
		StringBuffer sb = new StringBuffer();
		
		boolean first = true;
		
        if(!getMoney().isEmpty()) {
            sb.append("&B8;");
            sb.append(getMoney().getDescription());
            sb.append("&;\n");
            first = false;
        }
            
		for(int i=0; i<inventory.size(); i++) {
			if(first) {
				sb.append("Inventory:\n");
				first = false;
			}
			
			Item item = (Item) inventory.get(i);
         if(item instanceof Wearable) {
            sb.append("&B5;");
         } else if(item instanceof Wieldable) {
            sb.append("&5;");
         } else {
            sb.append("&7;");
         }
			if(item.isVisible())
				sb.append(" "+item.getDescription()+"\n");
		}
		
		if(first) sb.append("You aren't carrying anything.");
		
		co.put(sb.toString());
	}
	
	/* Wield an item */
	private boolean wield(String what) {
        what = what.trim();
        if(what.equalsIgnoreCase("all")) {
            wieldAll();
            return true;
        }
        
        String[] items = what.split(" *, *");
        boolean weld = false;
        for(String item : items) {
			Item i = (Item) findByNameAndType(item, Types.ITEM);
			if(i instanceof Wieldable) {
			    Wieldable w = (Wieldable) i;
                weld = true;
				if(wield(w)) {
					getRoom().wield(this, w);
				}
			}
		}
        if(!weld)
            co.put("Wield what?");
		
		return true;
		
	}

	protected boolean unwield(String what) {
		Wieldable[] w = getWieldedItems(true);
		for(int i=0; i<w.length; i++) {
			if(w[i].isAlias(what)) {
				Wieldable weapon = w[i];
				if(unwield(weapon)) {
					getRoom().unwield(this, weapon);
				}
				return true;
			}
		}
		notice("Unwield what?");
		return false;
	}
	
	/* Wear an item */
	protected boolean wear(String what) {

        what = what.trim();
		if(what.equals("all")) {
			wearAll();
			return true;
		}

        String[] items = what.split(" *, *");
        boolean worn = false;
        for(String item : items) {
            
            Item i = (Item) findByNameAndType(item, Types.ITEM);
            if(i instanceof Wearable) { 
				Wearable w = (Wearable) i;
                worn = true;
				if(wear(w)) {
					// currentRoom.wear(this, w);
				}

			}
		}
        if(!worn)
           co.put("Wear what?");
		
		return true;

	}

	protected boolean unwear(String what) {
		boolean success = false;
		Wearable[] w = getWornItems();
		for(int i=0; i<w.length; i++) {
			if(w[i] != null) {
				if(w[i].isAlias(what)) {
					Wearable armour = w[i];
					if(unwear(armour)) {
						//currentRoom.unwear(this, armour);
					}
					return true;
				}
				if(what.equals("all")) {
					Wearable armour = w[i];
					unwear(armour);
					success = true;
				}
			}
		}
		if(!success) {
			notice("Unwear what?");
		}
		return success;
	}

	/* Say something in the room */
	private void say(String what) {
		getRoom().say(this, what);
	}

	public void prompt() {
		
        if(prompt != null) {
            co.prompt(prompt.prompt(this));
            return;
        }
        
        co.state().update(this);
	}        
	
	public void setStartingRoom(String id) { startingRoom = id; }
	public String getStartingRoom() { return startingRoom; }


	/* The game event notifications */	
	
	public void arrives(Living who) {
        super.arrives(who);
		if(who != this && !who.provides(LivingProperty.INVISIBLE))
			co.put(Print.capitalize(who.getName())+" appears.");
		
		if(who == this) {
			look(true);
		}
		return;
	}
	
	public void leaves(Living who) {
        super.leaves(who);
		if(who != this && !who.provides(LivingProperty.INVISIBLE))
			co.put(Print.capitalize(who.getName())+" disappears.");
		return;
	}
	
	public void arrives(Living who, Exit from) {
		super.arrives(who, from);
		
		if(who == this) {
			if(skills.skillActive()) {
				notice("You break your concentration.");
				skills.stopSkill();
			}
		} else if(!who.provides(LivingProperty.INVISIBLE)) {
			if(from.getTarget(getRoom().getId()) == null) {
				if(opposingCanonicalDirection(from.getArrivalDirection(getRoom().getId())) != null) {
					co.put(Print.capitalize(who.getName())+" arrives from "+
				            opposingCanonicalDirection(from.getArrivalDirection(getRoom().getId()))+".");
				} else {
					co.put(Print.capitalize(who.getName())+" arrives.");
				}
			} else {
				co.put(Print.capitalize(who.getName())+" arrives from "+
		            opposingCanonicalDirection(from.getDirection(from.getTarget(getRoom().getId())))+".");
				// add "opposing"
			}
		}
		if(who == this) {
			look(true);
		}
	}

	public void leaves(Living who, Exit to) {
		if(who == this) 
			return;						
		if(!who.provides(LivingProperty.INVISIBLE))
            co.put(Print.capitalize(who.getName())+" leaves "+
                    canonicalDirection(to.getDirection(getRoom().getId()))+".");
		
		super.leaves(who, to);
	}
	
	public void startsUsing(Living who, Skill skill) {
        super.startsUsing(who, skill);
		if(who == this) {
			co.put("You start concentrating on the skill.");
		} else {
			if(getRoom().getIllumination() < getRace().getMinimumVisibleIllumination() ||
				getRoom().getIllumination() > getRace().getMaximumVisibleIllumination() ||
                who.provides(LivingProperty.INVISIBLE)) {
				co.put("You sense someone nearby starting to concentrate on a skill.");
			} else {
				co.put(Print.capitalize(who.getName())+" starts concentrating on a skill.");
			}
		}
	}
	
	public void takes(Living who, Item what) {
        super.takes(who, what);
		if(who == this) {
			co.put("You take "+what.getDescription());
		} else if(who.provides(LivingProperty.INVISIBLE)){
			co.put(Print.capitalize(what.getDescription())+" suddenly dissappears from the ground.");
		} else {
			co.put(Print.capitalize(who.getName())+" takes "+what.getDescription());
		}
	}
	
	public void drops(Living who, Item what) {
        super.drops(who, what);
		if(who == this) {
			co.put("You drop "+what.getDescription());
		} else if(who.provides(LivingProperty.INVISIBLE)){
			co.put(Print.capitalize(what.getDescription())+" suddenly falls on the ground.");
		} else {
			co.put(Print.capitalize(who.getName())+" drops "+what.getDescription());
		}
	}
	
	public void wields(Living who, Item what) {
        super.wields(who, what);
		if(who == this) {
			co.put("You wield " + what.getDescription() + ".");
		} else if(!who.provides(LivingProperty.INVISIBLE)) {
			co.put(Print.capitalize(who.getName()) + " wields " + what.getDescription() + ".");
		}
	}

	public void unwields(Living who, Item what) {
        super.unwields(who, what);
		if(who == this) {
			co.put("You unwield " + what.getDescription() + ".");
		} else if(!who.provides(LivingProperty.INVISIBLE)) {
			co.put(Print.capitalize(who.getName()) + " unwields " + what.getDescription() + ".");
		}
	}

	public void says(Living who, String what) {
	    super.says(who, what);
		if(who == this) {
			co.put("You say: '"+what+"'");
		} else if(who.provides(LivingProperty.INVISIBLE)) {
			co.put("Someone says: '"+what+"'");
		} else {
			co.put(Print.capitalize(who.getName())+" says: '"+what+"'");
		}
	}
	
	@Override
	public void dies(Living victim, Living killer) {
		World.log("DefaultPlayerImpl DIES method");
		super.dies(victim, killer);
		
		if(this == victim) {
			
			notice("You are dead.");
			
			Room hell = (Room) World.get("rooms/hell");
			
			if(hell == null) {
				notice("..But alas there is no heaven or hell so you have nowhere to go to.");
				notice("Please report this as a bug.");
				notice(World.getBugTrackerURL());
				return;
			}
			
			Room oldRoom = getRoom();
			oldRoom.remove(this);
			
			hell.enter(victim);
			
		} else {
			notice(Print.capitalize(victim.getName())+" is DEAD, R.I.P.");
		}
		
		if(killer != null) {
			// Add battle notifications
			
			
			if(getBattleGroup().contains(victim.getLeafBattleGroup())) {
				//notice("DIE!!!!! "+(victim==this?"player":"friend"));
				getClientOutput().battle().die(victim == this ? "player" : "friend");
			} else if(getBattleGroup().isOpponent(victim)) {
				//notice("DIE!!!!! ENEMY");
				getClientOutput().battle().die("enemy");
			} else {
				//notice("SOMEONE DIED, BUT IT DOESN'T CONCERN YOUR BATTLE");
			}
			
		}
	}
	
	@Override
	public void asks(Living asker, Living target, String subject) { 
		super.asks(asker, target, subject);
	}
	
	public boolean tick(Queue type) {
		synchronized(getRoom()) {
			try {
				if(!started) return true;
				
				updateAfflictions(type);
				
				boolean noregen = false;
				boolean noprompt = false;
				
				// plummet downwards if in air and cannot fly
				if(getRoom().requiresAviation() && !canFly()) {
					notice("The wind rushes past you as you plummet towards the ground.");
					Room old = getRoom();
					
					old.notice(this, getName()+" plummets downwards.");
					old.remove(this);
					
					Exit e = (Exit) old.findByNameAndType("d", Types.EXIT);
					String target = e.getTarget(getId());
					
					Room newRoom = (Room) World.get(target);
					if(newRoom == null) {
						newRoom = (Room) World.get("rooms/hell");
						this.notice("Go straight to hell, do not pass ground.");
						this.notice("Please report this as a bug: "+target);
						this.notice(World.getBugTrackerURL());
					}
					newRoom.add(this);
					newRoom.notice(this, Print.capitalize(getName())+" falls from the sky.");
				}
				
				if(waterDamage(false))
					noregen = true;
				
				if(hp == maxHp && sp == maxSp)
					noprompt = true;
				
				if(type == Tick.REGEN) {
					
					onRegenTick(this);
					if(!isDead()) {
						if(!noregen) regen();
						if(preferences.containsKey("idleprompt") &&
						   preferences.get("idleprompt").equals("on") &&
						   !noprompt) {
							prompt();
						}
					}
				} else if (type == Tick.BATTLE) {
					
					// World.log("BATTLE: "+getName());
					
					//System.out.println("Battle tick for "+name);
					if(isDead()) {
						co.battle().end();
						return false;
					}
					
					if(inBattle() || skills.skillActive()) {
						modifySustenance(-1); // fighting makes hungry				  
					}
					
					// Fight
					if(!getBattleStyle().tryUse())
						setBattleStyle(new DefaultBattleStyle(this));
					boolean opponent = getBattleStyle().use();
					
					if(!opponent) {
						co.battle().end();
					}
					// Use skill
					boolean skill = false;
					if(skills.skillActive()) {
						skill = skills.updateSkill(this);
						//World.log("--------------------");
					}
					
					// If either of above is not finished, tick again
					return opponent || skill;
				} else if(type == Tick.GARBAGE) {
					if(!inBattle() && co != null && co.getSession().getIdleTime() > 24*60*60*1000) {
						/* Log out if more than 24 hours idle */
						say("You will be logged out due to inactivity.");
						quit();
					}
					World.updatePlaque(this);				
				}
				return true;
			} catch(Exception e) {
				if(this instanceof org.vermin.driver.ExceptionHandler)
					((org.vermin.driver.ExceptionHandler)this).handleException(e, null);
				else {
					World.log("%%%% PLAYER TICK KUSI HOMMAT %%%%");
					World.log(" --> "+e.getMessage());
					World.exception(e);
					World.log(" jatketaan silti tikkaamista ");
				}
				return true;
			}
		}
	}

	public void setClientOutput(ClientOutput output) {
		co = output;
	}

	public void addAvailableTitle(String title) {
		if(!availableTitles.contains(title)) {
			this.notice("&B;You have gained a new title:&; "+title);
			availableTitles.add(title);
		}
	}

	public void addAvailableTitle(String title, String msg) {
		if(!availableTitles.contains(title)) {
			if(msg != null) {
				this.notice(msg);
			}
			availableTitles.add(title);
		}
	}
	public void clearAvailableTitles() {
		availableTitles.clear();
		title = "";
	}

	private String canonicalDirection(String d) {
		if(d.equalsIgnoreCase("w")) return "west";
		else if(d.equalsIgnoreCase("e")) return "east";
		else if(d.equalsIgnoreCase("s")) return "south";
		else if(d.equalsIgnoreCase("n")) return "north";
		else if(d.equalsIgnoreCase("nw")) return "northwest";
		else if(d.equalsIgnoreCase("sw")) return "southwest";
		else if(d.equalsIgnoreCase("ne")) return "northeast";
		else if(d.equalsIgnoreCase("se")) return "southeast";
		else if(d.equalsIgnoreCase("u")) return "up";
		else if(d.equalsIgnoreCase("d")) return "down";
		else return d;
	}
	
	private String opposingCanonicalDirection(String d) {
		if(d == null) return null;
		if(d.equalsIgnoreCase("w")) return "east";
		else if(d.equalsIgnoreCase("e")) return "west";
		else if(d.equalsIgnoreCase("s")) return "north";
		else if(d.equalsIgnoreCase("n")) return "south";
		else if(d.equalsIgnoreCase("nw")) return "southeast";
		else if(d.equalsIgnoreCase("sw")) return "northeast";
		else if(d.equalsIgnoreCase("ne")) return "southwest";
		else if(d.equalsIgnoreCase("se")) return "northwest";
		else if(d.equalsIgnoreCase("u")) return "down";
		else if(d.equalsIgnoreCase("d")) return "up";
		else return null;
	}
		
	public void setGender(String gender) {
		String [] genders = this.race.getGenders();
		for(String g : genders) {
			if(g.equals(gender)) {
				this.gender = gender;
				break;
			}
		}
	}

	public String getGender() {
		if(gender == null)
			return "male";
		else
			return gender;
	}

	public boolean isVerbose() {
		if(preferences.containsKey("verbose")) {
			return preferences.get("verbose").equals("on");
		}
		return false;
	}

	public SkillObject getSkillObject() {
		return skills;
	}

	public void modifySustenance(int amount) {
		super.modifySustenance(amount);
		Integer current = new Integer(getSustenance());
		if(hungerMessages.containsKey(current)) {
			notice(hungerMessages.get(current));
		}
		
		if(getSustenance() < 1000) {
			hungerModifier.setAmount( -(100-(getSustenance() / 10)));
		} else {
			hungerModifier.setAmount(0);
		}
	}
	public void setSustenance(int sustenance) {
		if(getSustenance() < 1000) {
			hungerModifier.setAmount( -(100-(getSustenance() / 10)));
		} else {
			hungerModifier.setAmount(0);
		}

		super.setSustenance(sustenance);
	}
	public int getSustenance() {
		return super.getSustenance();
	}
	
	public Vector<BattleStyle> listAvailableBattleStyles() {
		// XXX: Should give out only immutable references
		return availableBattleStyles;
	}

	public void addAvailableBattleStyle(BattleStyle bs) {
		bs.setOwner(this);
		availableBattleStyles.add(bs);
		notice("&B2;You have gained a new battlestyle: '"+bs.getName()+"'.&;");
		notice("See 'help battlestyles' for information.");
	}

	public void clearAvailableBattleStyles() {
		availableBattleStyles.clear();
		BattleStyle bs = new DefaultBattleStyle(this);
		availableBattleStyles.add(bs);
		setBattleStyle(bs);
	}

	/**
	 * Get the creation date of this player.
	 * If the player was created before 13.6.2004 this
	 * method will return null.
	 *
	 * @return the creation date or null
	 */
	public Date getCreated() {
		return created;
	}

	public String getBestSoloKillDescription() {
		return bestSoloKillDescription;
	}

	public long getBestSoloKillExperience() {
		return bestSoloKillExperience;
	}

	public String getBestPartyKillDescription() {
		return bestPartyKillDescription;
	}

	public long getBestPartyKillExperience() {
		return bestPartyKillExperience;
	}

	public void setBestPartyKill(Living kill) {
		bestPartyKillExperience = kill.getExperienceWorth();
		bestPartyKillDescription = kill.getDescription();
	}

	public void setBestSoloKill(Living kill) {
		bestSoloKillExperience = kill.getExperienceWorth();
		bestSoloKillDescription = kill.getDescription();
	}

	public int getExploreCount() {
		return exploreCount;
	}
	public void increaseExploreCount() {
		exploreCount++;
		summaryRoomsExplored++;
	}

	public void save() {
		super.save();
		World.updatePlaque(this);
	}

	public String getDescription() {
		return org.vermin.util.Print.capitalize(getName());
	}

    public void setPrompt(Prompt pmt) {
        this.prompt = pmt;
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Player#setPlan(java.lang.String)
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Player#getPlan()
     */
    public String getPlan() {
        return plan;
    }

	public String getPromptString() {
		return (String) Collections.get(preferences, "prompt", DEFAULT_PROMPT);
	}
 
	public int getFreeStatPoints() {
		return freeStatPoints;
	}
	
	public void setFreeStatPoints(int points) {
		freeStatPoints = points;
	}
	
	public int getUsedStatPoints(Stat s) {
		int u = 0;
		Integer used = usedStatPoints.get(s);
		if(used != null)
			u = used;
		return u;
	}
	
	public void useStatPoint(Stat s) {
		if(freeStatPoints >= getStatPointCost(s) && getStat(s, false) < getMaxStat(s)) {
			freeStatPoints -= getStatPointCost(s);
			Integer used = usedStatPoints.get(s);
			int next = 1;
			if(used != null)
				next = used+1;
			usedStatPoints.put(s, next);
			setStat(s, getStat(s, false)+1);
		}
	}
	
	public int getMaxStat(Stat s) {
		switch(s) {
			case MENT_CHA: return race.getMaxMentalCharisma();
			case MENT_CON: return race.getMaxMentalConstitution();
			case MENT_DEX: return race.getMaxMentalDexterity();
			case MENT_STR: return race.getMaxMentalStrength();
			case PHYS_CHA: return race.getMaxPhysicalCharisma();
			case PHYS_CON: return race.getMaxPhysicalConstitution();
			case PHYS_DEX: return race.getMaxPhysicalDexterity();
			case PHYS_STR: return race.getMaxPhysicalStrength();
		}
		return 0;
	}
	
	public int getStatPointCost(Stat s) {
		int cost;
		int baseCost;
		switch(s) {
			case MENT_CHA: baseCost = race.getMentalCharismaCost(); break; 
			case MENT_CON: baseCost = race.getMentalConstitutionCost(); break;
			case MENT_DEX: baseCost = race.getMentalDexterityCost(); break;
			case MENT_STR: baseCost = race.getMentalStrengthCost(); break;
			case PHYS_CHA: baseCost = race.getPhysicalCharismaCost(); break;
			case PHYS_CON: baseCost = race.getPhysicalConstitutionCost(); break;
			case PHYS_DEX: baseCost = race.getPhysicalDexterityCost(); break;
			case PHYS_STR: baseCost = race.getPhysicalStrengthCost(); break;
			default: return 666666;
		}
		Integer used = usedStatPoints.get(s);
		int nextLev = 1;
		if(used != null) {
			nextLev = used+1;
		}

		return (int) (baseCost * ((2.0-Math.log10(99-nextLev))*115));
	}

	@Override
	public void onBattleTick(Living who) {
		if(inBattle() &&
		   preferences.containsKey("showrounds") &&
		   preferences.get("showrounds").equals("on")) {
			notice("&8;<>--|&; &4;NEW ROUND&; &8;|--<>&;");
			super.onBattleTick(who);	
		}
		
	}
	public void clearUsedStatPoints() {
		usedStatPoints = new EnumMap(Stat.class);
	}
}

