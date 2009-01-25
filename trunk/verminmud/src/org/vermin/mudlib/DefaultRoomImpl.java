/* DefaultRoomImpl.java
	5.1.2002 Tatu Tarvainen / Council 4
	
	Default room implementation.
	
*/
package org.vermin.mudlib;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vermin.driver.*;
import org.vermin.driver.Queue;
import org.vermin.util.Pair;

/**
 * Default implementation of the <code>Room</code> interface.
 */
public class DefaultRoomImpl extends DefaultObjectImpl implements Room {
	/* Vector of Living things in this room */
	protected transient HashSet<Living> living;
	
	/* Vector of Items in this room */
	protected transient HashSet<Item> items;
	
	/* Vector of exits */
	protected transient HashSet<Exit> exits;
	
	/* have transients been initialized? */
	protected transient boolean transientsInitialized = false;
	
	/* Exits to load on start/reset */
	protected Vector startExits;
	
	/* Items to load on start/reset */
	protected Vector startItems;
	
	/* Monsters to load on start/reset */
	protected Vector startMonsters;
	
	/* A map of extended descriptions. */
	protected HashMap<String, String> extendedDescriptions;
	
	protected String mapper;
	protected transient Mapper mapperObj;
	protected String location;
	
	protected transient LivingProxy lp;
	
	/* Weather control */
	protected boolean outdoor;
	protected int waterLevel;
	protected int vegetation;
	
	protected LinkedList<Modifier> waterLevelModifiers;
	protected LinkedList<Modifier> outdoorModifiers;
	protected LinkedList<Modifier> vegetationModifiers;

	private long lastPlayerLeave; // timestamp of when a player last left.

    private Money money = new DefaultMoneyImpl();
    protected DefaultCompositePropertyProvider<RoomProperty> propertyProvider;
    
    private Area area;
    
    private boolean explorable = true;
    
	public DefaultRoomImpl() {
		living = new HashSet();
		lp = new LivingProxy(living);
		items = new HashSet();
		
		exits = new HashSet();
		startExits = new Vector();
		startItems = new Vector();
		startMonsters = new Vector();
		propertyProvider = new DefaultCompositePropertyProvider<RoomProperty>();
		description = "A Room";
		longDescription = "You are in a dark room.";
		mapper = null;
	}
	
    public Money getMoney() {
        return money;
    }
    
	private void initTransients() {
        // FIXME: this toyin around with transients and stuff really SUCKS
        // remove them, and while you're at it find a way to do so
        // without breaking all the save files.
		if(!transientsInitialized) {
			living = new HashSet();
			lp = new LivingProxy(living);
			buildItemList();
			buildExitList();
			buildMonsterList();
			transientsInitialized = true;
		}
	
	}
	
	public boolean contains(MObject obj) {
		if(obj.getType() == Types.LIVING)
			return living.contains(obj);
		else if(obj.getType() == Types.EXIT)
			return exits.contains(obj);
		else 
			return items.contains(obj);
	}

	public void start() {
		super.start(); // adds a garbage tick
		try {
			initTransients();
		} catch(Exception e) {
			World.log("Exception in DefaultRoomImpl start!");
			e.printStackTrace();
		}
		
		if(getArea() != null) {
			getArea().spawn(this);
		}
	}
	
	public synchronized boolean tick(Queue queue) {
		
		if(queue == Tick.BATTLE) {
			int ticks = 0;
			
			/* Roll for initiative */
			// Dice(mental con + physical dex)... largest value goes first
			ArrayList<Pair<Living,Integer>> iniative = new ArrayList<Pair<Living, Integer>>();
			
			for(Living l : living) {
				l.onBattleTick(l);
				iniative.add(new Pair<Living, Integer>(l, Dice.random(l.getMentalConstitution()+l.getPhysicalDexterity())));
			}
			
			Collections.sort(iniative, new Comparator<Pair<Living,Integer>>() {
				public int compare(Pair<Living,Integer> p1, Pair<Living,Integer> p2) {
					if(p1.second > p2.second)
						return -1;
					else if(p1.second < p2.second)
						return 1;
					else						
						return 0;
				}});
			
			
			for(Pair<Living,Integer> p : iniative) {
				// if(hasPlayers) World.log("Room "+getId()+" ticking "+p.first.getName()+" with iniative "+p.second);
				p.first.tick(queue);
				ticks++;
			}
			// if(hasPlayers) World.log("Room "+getId()+" ticked "+ticks+" living(s).");
			return ticks > 0; // If we ticked someone, keep ticking
			
		} else if(queue == Tick.GARBAGE) { // garbage tick
			if(System.currentTimeMillis() - lastPlayerLeave < 1000*60*7) { // if player left less than 7 minutes ago
				if(getArea() != null) {
					getArea().spawn(this);
				}
				return true; // tick again, not ready to unload yet
			} else {
				// check if there are players in the room
				boolean hasPlayer = false;
				for(Living l : living)
					if(l instanceof Player) {
						hasPlayer = true;
						break;
					}

				if(hasPlayer)
					return true; // tick again, don't unload when players are present

				// ready to unload
				//World.log("Unloading room: "+this);
				World.unload(this.getId());

				/* Unload start monsters that are unique */
				for(Object sm : startMonsters) {
					if(sm instanceof String) {
						Prototype p = World.load((String) sm);
						if(p.isUnique()) {
							World.log("Unloading unique monster '"+sm+"' from room: "+getId());
							World.unload((String) sm);
						}
					}
				}
				
				if(getArea() != null) {
					
					for(Living l : living) {
						World.stopTick(l);
						getArea().removed(l);
					}
					
					for(Item i : items) {
						getArea().removed(i);
					}
				}
				return false;
			}
		} else
			return false;
	}
	
	public synchronized void enter(Living who, Exit from) {
		explore(who);
		add(who);
		lp.arrives(who, from);
		lp.afterArrives(who);
	}
	public synchronized void enter(Living who) {
		explore(who);
		add(who);
		lp.arrives(who);
		lp.afterArrives(who);
	}
	
	public void wield(Living who, Item what) {
		lp.wields(who, what);
	}

	public void unwield(Living who, Item what) {
		lp.unwields(who, what);
	}
	
	public synchronized void leave(Living who, Exit to) {
		remove(who);
		lp.leaves(who, to);
	}
	
	public void say(Living who, String what) {
		lp.says(who,what);
	}
	
	public void takes(Living who, Item what) {
		lp.takes(who, what);
	}
	
	public void drops(Living who, Item what) {
		lp.drops(who, what);
	}
	
	public void dies(Living victim, Living killer) {
		lp.dies(victim, killer);
		remove(victim);
		if(getArea() != null) 
			getArea().removed(victim);
	}
	
	public void notice(Living who, String what) {
		noticeOthers(who, what);
	}
	
	public void noticeExcluding(String message, Living...excluded) {
		for(Living l : living) {
			boolean isExcluded = false;
			for(Living e : excluded) {
				if(l == e) {
					isExcluded = true;
					break;
				}
			}
			
			if(!isExcluded) {
				l.notice(message);
			}
		}
	}
	
	public void asks(Living asker, Living target, String subject) {
		lp.asks(asker, target, subject);
	}	
	
	public String getExtendedDescription(String what) {
		if(extendedDescriptions != null) {
			return extendedDescriptions.get(what);
		}
		return null;
	}

	
	/* Send a notice to all living things in this room */
	private void noticeAllLiving(String what) {
		for(Living l : living) l.notice(what);
	}
	
	/* Send a notice to all living things except one */
	private void noticeOthers(Living exclude, String what) {
		for(Living l : living) 
			if(l != exclude) l.notice(what);
	}

	public Types getType() { return Types.ROOM; }
	
	/* composite methods */
	public void add(MObject child) {
		if(!transientsInitialized)
			initTransients();

		if(child instanceof Living) {
			add((Living) child);
		} else if(child instanceof Item) {
			add((Item) child);
		} else if(child instanceof Exit) {
			add((Exit) child);
		}
	}

	public void add(Living who) {
		who.setParent(this);
		living.add(who);
		if(living.size() == 1) {
			// First living entered
			World.addBattleTick(this);
		}
		/* if(!(who instanceof Player))
			startMonsters.add(who);
		*/
	}
	
	public void add(Exit exit) {

		exit.setParent(this);
		if(exits==null)
			buildExitList();
			
		// replace an earlier exit for the same direction
		Iterator<Exit> it = exits.iterator();
		while(it.hasNext()) {
			Exit e = it.next();
			if(e.getDirection(getId()).equals(exit.getDirection(getId()))) {
				it.remove();
				break;
			}
		}
		exits.add(exit);

	}

	public void add(Item item) {
		item.setParent(this);
		if(items==null)
			buildItemList();
		
		items.add(item);
		// startItems.add(item);
	}

	public void remove(MObject child) {
		if(child instanceof Living)
			remove((Living) child);
		else if(child instanceof Item)
			remove((Item) child);
		else if(child instanceof Exit)
			remove((Exit) child);
	}
	
	public void remove(Living l) {
		if(l instanceof Player)
			lastPlayerLeave = System.currentTimeMillis();
		living.remove(l);
	}
	
	public void remove(Item i) {
		items.remove(i);
	}
	
	public void remove(Exit e) {
		exits.remove(e);
	}
	
	public MObject findByName(String name) {
	
		MObject obj;
		Matcher m = Container.INDEXED_CONTAINER_ACCESS.matcher(name);
		int index = 1;
		
		if(m.matches()) {
			name = m.group(1);
			index = Integer.parseInt(m.group(2));
		}
		
		/* try a player */
		obj = getByName(living, index, name);
		if(obj != null) return obj;
		
		/* try item */		
		obj = getByName(items, index, name);
		if(obj != null) return obj;
	
		/* try an exit */
		obj = getExitByName(exits, index, name);
		if(obj != null) return obj;
		
		/* all possible items searched, but no matching object
		 * was found. Return null. */
		return null;
	}
    
	// index:th object
	public MObject findByName(String name, int index) {
		
			MObject obj;

			/* try a player */
			obj = getByName(living, index, name);
			if(obj != null) return obj;
			
			/* try item */		
			obj = getByName(items, index, name);
			if(obj != null) return obj;
		
			/* try an exit */
			obj = getExitByName(exits, index, name);
			if(obj != null) return obj;
			
			/* all possible items searched, but no matching object
			 * was found. Return null. */
			return null;
		}	

	public Iterator findByType(Types type) {
		if(type == Types.LIVING)
			return living.iterator();
		else if(type == Types.ITEM)
			return items.iterator();
		else if(type == Types.EXIT)
			return exits.iterator();
		else
			return null;
	}
	
	/* Find an object by name an type. */
	public MObject findByNameAndType(String name, Types type) {
		Collection haystack = null;
		
		Matcher m = Container.INDEXED_CONTAINER_ACCESS.matcher(name);
		int index = 1;
		
		if(m.matches()) {
			name = m.group(1);
			index = Integer.parseInt(m.group(2));
		}
		
		if(type == Types.EXIT)
			return getExitByName(exits, index, name);
			
		else if(type == Types.LIVING)
			haystack = living;
			
		else if(type == Types.ITEM)
			haystack = items;

		if(haystack == null)
			return null;
					
		return getByName(haystack, index, name);
	}

	/* Find the index:th object by name and type. */
	public MObject findByNameAndType(String name, int index, Types type) {
		Collection haystack = null;
		
		if(type == Types.EXIT)
			return getExitByName(exits, index, name);
			
		else if(type == Types.LIVING)
			haystack = living;
			
		else if(type == Types.ITEM)
			haystack = items;

		if(haystack == null)
			return null;
					
		return getByName(haystack, index, name);
	}
	
	
	/* Search exits by name */
	private MObject getExitByName(Collection haystack, String needle) {
		for(Object e : haystack) {
			String dir = ((Exit)e).getDirection(getId());
			if(dir != null && dir.equalsIgnoreCase(needle))
				return (Exit) e;
		}
		return null;
	}
	
	/* Search index:th exit by name */
	private MObject getExitByName(Collection haystack, int index, String needle) {
		for(Object e : haystack) {
			String dir = ((Exit)e).getDirection(getId());
			if(dir != null && dir.equalsIgnoreCase(needle)) {
				if(index == 1) {
					return (Exit) e;
				}
				index--;
			}
		}
		return null;
	}
	
	/* Helper method for searching a Vector of MObjects */	
	private MObject getByName(Collection haystack, String needle) {
		for(Object obj : haystack) {
			if(((MObject)obj).isAlias(needle))
				return (MObject) obj;
		}
		return null;
	}

	/* Helper method for searching the index:th Vector of MObjects */	
	private MObject getByName(Collection haystack, int index, String needle) {
		for(Object obj : haystack) {
			if(((MObject)obj).isAlias(needle)) {
				if(index == 1) {
					return (MObject) obj;
				}
				index--;
			}
		}
		return null;
	}
	
	private static Pattern dynamicExitPat = Pattern.compile("^(\\w+)\\s*->\\s*(.*)$");
	
	private Exit maybeCreateExit(String desc) {
		Matcher m = dynamicExitPat.matcher(desc);
		if(m.matches()) {
			String dir = m.group(1), room = m.group(2);
			return new OneWayExit(getId(), room, dir);
		}
		return null;
	}
	
	private void buildExitList() {
		if(exits == null)
			exits = new HashSet();
			
		for(int i=0; i<startExits.size(); i++) {
			Object exitDescriptor = startExits.get(i);
			Exit o = null;
			if(exitDescriptor instanceof String) {
				o = maybeCreateExit((String) exitDescriptor);
			}
			
			if(o == null) {
				o = (Exit) getObject(exitDescriptor);
			}
			
            if(o != null)
            	exits.add(o);
		}
	}

	/* Handles loading persistent objects in the room.
	 * Objects can be items (MObject) or a reference to
	 * a persistent object (a String id).
	 */
   private Object getObject(Object obj) {
      if(obj instanceof String) {
         try {
				Prototype p = Driver.getInstance().getLoader().load(obj.toString());
				if(p.isUnique())
					return p.get();
				else
					return p.create();

         } catch(Exception e) {
            World.log("[DefaultRoomImpl.getObject(String)] Repository load failed for id: "+obj);
				World.log("  message: "+e.getMessage());
				e.printStackTrace();

            return null;
         }
      } else {
         MObject mobj = (MObject) obj;
         mobj.start();
         return mobj;
      }
   }
	
	private void buildItemList() {
		if(items == null)
			items = new HashSet();
		
		for(int i=0; i<startItems.size(); i++) {
         Item o = (Item) getObject(startItems.get(i));
         if(o != null) {
            items.add(o);
         } 
      }
   }
	
	private void buildMonsterList() {
		if(living == null)
			living = new HashSet();
		
		for(int i=0; i<startMonsters.size(); i++) {
			Living l = (Living) getObject(startMonsters.get(i));
         if(l != null) {
        	 	add(l);

           	 l.arrives(l);
            /*living.add(l);
            //l.setRoom(this);
            l.setParent(this);*/
         }
		}
	}
	
    /*
	public void setMapper(Mapper m) {
	    mapperObj = m;
	}
	
	public Mapper getMapper() {
		try {
         if(mapperObj != null) {
            return mapperObj;
         } else if(mapper != null) {
				return (Mapper) Driver.getInstance().getLoader().get(mapper);
         }
		} catch(Exception cc) {}
         return null;
	}
	*/
    
	public boolean action(MObject obj, Vector params) {
		if(obj instanceof Living)
			return action((Living) obj, params);
		else
			return false;
	}
	
	public boolean action(Living who, Vector cmd) {
		return false;
	}

   public boolean isOutdoor() {
		return DefaultModifierImpl.calculateBoolean(this, outdoor, outdoorModifiers);
   }

   public int getWaterLevel() {
		return DefaultModifierImpl.calculateInt(this, waterLevel, waterLevelModifiers);
   }

	public int getVegetation() {
		return DefaultModifierImpl.calculateInt(this, vegetation, vegetationModifiers);
	}


	public boolean requiresAviation() {
        return provides(RoomProperty.REQUIRES_AVIATION);
	}

	/**
	 * Return the illumination of this room.
	 * The calculated value includes all light sources
	 * in the room in addition to the room's own illumination.
	 *
	 * @return the illumination level
	 */
	public int getIllumination() {
		int base = super.getIllumination();
		for(Living l : living)
			base += l.getIllumination();
		for(Item i : items) 
			base += i.getIllumination();
		
		if(base > 100) { base = 100; }
		if(base < 0) { base = 0; }
		return base;
	}

	public void addModifier(Modifier m) {
		switch(m.getType()) {
		  case WATERLEVEL:
			  if(waterLevelModifiers == null)
				  waterLevelModifiers = new LinkedList<Modifier>();
			  waterLevelModifiers.add(m);
			  break;

		  case OUTDOOR:
			  if(outdoorModifiers == null) 
				  outdoorModifiers = new LinkedList<Modifier>();
			  outdoorModifiers.add(m);
			  break;

		  case VEGETATION:
			  if(vegetationModifiers == null)
				  vegetationModifiers = new LinkedList<Modifier>();
			  vegetationModifiers.add(m);
			  break;

		  default: super.addModifier(m);
		}
	}

	public void setOutdoor(boolean outdoor) {
		this.outdoor = outdoor;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}


	public BattleGroup getBattleGroup(String name) {
		Iterator en = findByType(Types.LIVING);
		while(en.hasNext()) {
			Living l = (Living) en.next();
			if(l.getBattleGroup().getName().equalsIgnoreCase(name))
				return l.getBattleGroup();
		}
		return null;
	}

	/**
	 * Override this in rooms that are not explorable (like the outer world).
	 *
	 * @param who the actor
	 */
	public void explore(Living who) {
		if(!(who instanceof Player) || !this.explorable)
			return;

		World.explore((Player) who, this);
	}

	public boolean isBlocked(String dir) {
		for(Living l : living) {
			if(l.isBlocking(dir))
				return true;
		}
		return false;
	}

	public void ticked(Living who, short type) {}

	public boolean mayTeleport(Living who) {
		return true;
	}

	public String getLocation() {
		return location;
	}
    
    public boolean provides(RoomProperty p) {
        return propertyProvider != null && propertyProvider.provides(p);
    }
    public boolean providesAny(RoomProperty first, RoomProperty ... rest) {
        if(provides(first))
            return true;
        for(RoomProperty p : rest)
            if(provides(first))
                return true;
        return false;
    }
    public boolean provides(RoomProperty first, RoomProperty ... rest) {
        if(!provides(first))
            return false;
        for(RoomProperty p : rest)
            if(!provides(first))
                return false;
        return true;
    }
    
    public Area getArea() {
        return area;
    }
    
	public void setArea(Area area) {
		this.area = area;
	}
    
    public Exit[] getExits() {
        return exits.toArray(new Exit[exits.size()]);
    }
    
    public Exit getExitTo(String direction) {
        for(Exit e : exits)
            if(e.getDirection(getId()).equals(direction))
                return e;
        return null;
    }

	public boolean tryAdd(MObject obj) {
		return true;
	}

	public boolean tryRemove(MObject obj) {
		return true;
	}

}
