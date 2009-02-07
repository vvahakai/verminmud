/* OutworldStore.java
	19.3.2002	Tatu Tarvainen
	
	Store to load outer world rooms from an ascii map.
	
	Outer world object id's have the following form:
	
	outworld:X,Y[,layer]
	
	X = (int) x location (column in map)
	Y = (int) y location (row in map)
	layer = (int) an optional layer
	        negative numbers mean below the ground level
			  positive numbers mean above the ground level

	
*/

package org.vermin.mudlib.outworld;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vermin.driver.*;
import org.vermin.mudlib.*;

import java.util.logging.Logger;
import java.util.regex.*;
import java.awt.Point;
import java.awt.Rectangle;

public class OutworldLoader implements Loader {
	
	protected static Logger log = Logger.getLogger(OutworldLoader.class.getName());
	
	private MapSource source;
	
	/* Path to outworld files */
	protected String path;

	/* Prefix for ids */
	protected String prefix;
	
	public static final int SPECIAL_NONE = ' ';
	public static final int SPECIAL_ROOM = '.';

	protected Mapper mapper = null;
	
	/* Room factory */
	protected OutworldRoomFactory orf;

	/* To guard positions against map evolution */
	protected int origoX, origoY;

	/**
	 * List of special things to load per room.
	 */
	public HashMap specialThings = new HashMap();

	/**
	 * Set of special locations' ids. 
	 */
	public HashSet<String> specialLocations = new HashSet<String>();
	
	/* Geographical areas in this loader */
	private LinkedList<LocationSpec> locations = new LinkedList();
	private Map<String, Area> areaMap = new HashMap();
	
	
	private static class LocationSpec {
		public LocationSpec() {}
		public Rectangle area;
		public String types;
		public String location;
	}

	public OutworldLoader(String path, final String mapfilePrefix, String specials,
								 int rowLength, int bytesPerEntry, int stride,
								 String prefix, OutworldRoomFactory orf, int origoX, int origoY) 
		throws IOException {

		source = new FileMapSource(path, mapfilePrefix, rowLength, bytesPerEntry, stride);
		
		this.path = path;
		this.prefix = prefix;
		this.orf = orf;
		this.origoX = origoX;
		this.origoY = origoY;
		
		createMapper();
		
		buildSpecialThings(new File(path, specials));
		
	}
	
	public OutworldLoader(String prefix, OutworldRoomFactory orf, MapSource source) {
		this.prefix = prefix;
		this.orf = orf;
		this.source = source;
		createMapper();
	}
	
	public void loadSpawningRules(String ruleDirectory) {
		World.log("[OutworldLoader] Loading spawning rules from: "+ruleDirectory);
		Prototype dirP = World.load(ruleDirectory);
		if(dirP == null) {
			World.log("[OutworldLoader] Unable to load spawning rule directory: "+ruleDirectory);
			return;
		}
		
		FileLoader.Directory dir = null;
		try {
			dir = (FileLoader.Directory) dirP.get();
		} catch(ClassCastException cce) {
			World.log("[OutworldLoader] Expected a Directory type for spawning rules, got: "+
					dirP.get().getClass().getName());
		}
		
		for(String file : dir.getContainedIDs()) {
			
			if(file.endsWith("~")) // don't load emacs backup files
				continue;
			Object ruleMaybe = World.get(file); 
			if(!(ruleMaybe instanceof Map))
				continue;
			Map rule = (Map) ruleMaybe; 
			String type = (String) rule.get("TYPE");
			if(type == null) {
				World.log("[OutworldLoader]   WARN: spawning rule TYPE is null.");
			} else if(type.equalsIgnoreCase("area")) {
				try {
					World.log("[OutworldLoader]  Adding area rule: "+file);
					String area = (String) rule.get("AREA");
					Object spawningRule = rule.get("RULE");
					addAreaSpawningRule((String) rule.get("AREA"), (SpawningRule) rule.get("RULE"));
				} catch(Exception e) {
					World.log("[OutworldLoader]  ERROR: Unable to add area spawning rule, "+e.getMessage());
				}
			} else if(type.equalsIgnoreCase("global")) {
				try {
					World.log("[OutworldLoader]   Adding global rule: "+file);
					addGlobalSpawningRule((SpawningRule)rule.get("RULE"));
				} catch(Exception e) {
					World.log("[OutworldLoader]  ERROR: Unable to add global spawning rule, "+e.getMessage());
				}
			}
		}
		
	}
	
	public void addAreaSpawningRule(String area, SpawningRule rule) {
		Area a = areaMap.get(area);
		if(a != null)
			a.addSpawningRule(rule);
	}
	public void addGlobalSpawningRule(SpawningRule rule) {
		Area mainArea = orf.getMainArea(this);
		if(mainArea != null) mainArea.addSpawningRule(rule);
		
		for(Area a : areaMap.values())
			a.addSpawningRule(rule);
	}
		
	private static Pattern AREA = Pattern.compile("\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*-\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*:\\s*(\\w+)(.*)");
	private void buildSpecialThings(File f) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line = in.readLine();
		while(line != null) {
			line = line.trim();
			if(line.length() > 0 && !line.matches("^ *#.*$")) {

				
				Matcher m = AREA.matcher(line);
				if(m.matches()) {
					int x1 = Integer.parseInt(m.group(1)) + origoX;
					int y1 = Integer.parseInt(m.group(2)) + origoY;
					int x2 = Integer.parseInt(m.group(3)) + origoX;
					int y2 = Integer.parseInt(m.group(4)) + origoY;

					Rectangle r = new Rectangle(x1, y1, x2-x1, y2-y1);
					if(m.group(5).equals("location"))
						addLocationForArea(r, m.group(6));
					else
						World.log("Unrecognized area thing '"+m.group(5)+"'.");
					
				} else {

					boolean special = false;
					if(line.charAt(0) == '?') {
						System.out.println("SPESIAALI! "+line);
						special = true;
						line = line.substring(1);
						while(Character.isWhitespace(line.charAt(0)))
							line = line.substring(1);
					}
					String[] spes = line.split(":");
				
					String[] coord = spes[0].split(",");
				
					int x = Integer.parseInt(coord[0].trim());
					int y = Integer.parseInt(coord[1].trim());
					int layer = 0;
					if(coord.length == 3)
						layer = Integer.parseInt(coord[2].trim());

					String[] things = spes[1].split(",");
					for(int t=0; t<things.length; t++)
						addThingForLocation(x, y, layer, things[t].trim());
					if(special)
						specialLocations.add(x+","+y+","+layer);
				}
			}
			line = in.readLine();
		}
	}

	private static Pattern LOCATION = Pattern.compile("\\(([^)]+)\\)\\s*=\\s*(.*)");
	private void addLocationForArea(Rectangle r, String spec) {
		
		Matcher m = LOCATION.matcher(spec);
		if(!m.matches()) {
			World.log("Unable to parse location spec: "+spec);
			return;
		}

		LocationSpec ls = new LocationSpec();
		ls.area = r;
		ls.types = m.group(1);
		ls.location = m.group(2); 
		locations.add(ls);
		Area a = new DefaultAreaImpl(mapper, WeatherService.getInstance(), ls.location, null);
		
		areaMap.put(ls.location, a);
	}

	/* Create mapper for outworld */
	protected void createMapper() {
		mapper = 
			new Mapper() {
				private boolean suppressSpecials;
				
				
				public String getMap(String room) {
					return render(19, 11, room);
				}
				public String getLargeMap(String room) {
					return render(source.getWidth() < 35 ? source.getWidth() : 35 , 
							source.getHeight() < 21 ? source.getHeight() : 21, room);
				}
				public String getSmallMap(String room) {
					return render(5, 5, room);
				}

				private String render(int xsize, int ysize, String room) {
					
					int[] xsizes = null;
					/* FIXME: These are hard-coded */
					if(ysize == 11) {
						xsizes = new int[] { 7, 11, 15, 17, 19, 19, 19, 17, 15, 11, 7 };
					} else if(ysize == 21) {
						xsizes = new int[] {
								11,   // 1
								17,   // 2
								21,   // 3
								25,   // 4
								27,  // 5
								29,  // 6
								31,  // 7
								33,  // 8
								33,  // 9
								35, // 10
								35, // 11
								35, // 12
								33,  // 13
								33,  // 14
								31,  // 15
								29,  // 16
								27,  // 17
								25,  // 18								    
								21,  // 19
								17,  // 20
								11  // 21
						};
					}
					
										
					StringBuffer sb = new StringBuffer();
					int[] loc = parseId(room);
					for(int y=0; y<ysize; y++) {
						int xs = xsize;
						if(xsizes != null)
							xs = xsizes[y];
						int pad = 0;
						if(xs != xsize) {
							pad = (xsize-xs)/2;
						}
						
						for(int padi=0; padi<pad; padi++) sb.append(" ");
						for(int x=pad; x<pad+xs; x++) {
							if(y==ysize/2 && x==xsize/2)
								sb.append("&B2;@&;");
							else if(!suppressSpecials && isSpecial((loc[0]-(xsize/2)+x), (loc[1]-(ysize/2)+y), loc[2]))
								sb.append("&B2;?");
							else
								sb.append(orf.getLegend(getType(loc[0]-(xsize/2)+x, loc[1]-(ysize/2)+y, loc[2])));
						}
						for(int padi=0; padi<pad; padi++) sb.append(" ");
						sb.append("\n");
					}
					return sb.toString();
				}

				public void showLegend(Player who) {
					String[][] legend = orf.getMapLegend();
					for(int i=0; i<legend.length; i++) {
						who.notice("  "+legend[i][0]+"&;    "+legend[i][1]);
					}
				}
				public void setSuppressSpecials(boolean suppress) {
					suppressSpecials = suppress;
					
				}
				public boolean isSuppressSpecials() {
					return suppressSpecials;
				}
				
			};
	}

	private boolean isSpecial(int x,int y,int layer) {
		return specialLocations.contains((x-origoX)+","+(y-origoY)+","+layer);
	}

	public int[] parseId(String ids) {
		int[] pos = parsePosition(ids);
		pos[0] += origoX;
		pos[1] += origoY;
		return pos;
	}

	/* Parse id string to X and Y positions. */
	private static final Pattern ID_REGEX = Pattern.compile(".*:(-?\\d+),(-?\\d+)(,-?\\d+)?");
	public static int[] parsePosition(String ids) {
		Matcher m = ID_REGEX.matcher(ids);

		if(!m.matches())
			throw new IllegalArgumentException("Id '"+ids+"' is not a valid outworld-style id.");
		
		try {
			int[] loc = new int[] { 
				Integer.parseInt(m.group(1)),
				Integer.parseInt(m.group(2)),
				0 };
			
			if(m.groupCount() == 3) {
				String layer = m.group(3);
				if(layer != null)
					loc[2] = Integer.parseInt(layer.substring(1));
			}
			
			return loc;
		} catch(NumberFormatException nfe) { // can't happen if regex matched
			throw new RuntimeException("Unable to parse outworld room id: "+ids);
		}
	}
	
	/* Load and Save an MObject */
	public Prototype load(String ids) {

		Loader loader = Driver.getInstance().getLoader();

		if(!ids.startsWith(prefix+":"))
			return null;

		int[] loc = parseId(ids);
		
		int xloc = loc[0];
		int yloc = loc[1];
		int layer = loc[2];
		
		byte[] type = getType(xloc, yloc, layer);

		Room or;
		or = orf.createRoom(this, xloc, yloc, type);
		or.setId(ids);
		
		OutworldRoomFactory.ExitStrategy exitStrategy = orf.getExitStrategy(type);
		
		// add exits for 8+2 directions
		if(exitStrategy != OutworldRoomFactory.ExitStrategy.NONE) {
			for(int i=0; i<10; i++) {
				if(source.hasMaskedExits()) {
					int exits = source.getExits(xloc, yloc, layer);
					if((exits&((i+1)^2)) == 0)
						continue;
				}
				
				if(i<9 && i%2==1) {
					if(exitStrategy == OutworldRoomFactory.ExitStrategy.NO_DIAGONAL)
						continue;
					else if(exitStrategy == OutworldRoomFactory.ExitStrategy.STRICT_DIAGONAL) {
						// check that adjacent rooms are passable
						int[] add1,add2;
						switch(i) {
						case OutworldExit.NE:
							add1 = OutworldExit.adder[OutworldExit.E];
							add2 = OutworldExit.adder[OutworldExit.N];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) ||
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.SE:
							add1 = OutworldExit.adder[OutworldExit.E];
							add2 = OutworldExit.adder[OutworldExit.S];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) ||
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.SW:
							add1 = OutworldExit.adder[OutworldExit.W];
							add2 = OutworldExit.adder[OutworldExit.S];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) ||
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.NW:
							add1 = OutworldExit.adder[OutworldExit.W];
							add2 = OutworldExit.adder[OutworldExit.N];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) ||
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
						}
					}
					else if(exitStrategy == OutworldRoomFactory.ExitStrategy.CORNER_DIAGONAL) {
						// check that adjacent rooms are passable
						int[] add1,add2;
						switch(i) {
						case OutworldExit.NE:
							add1 = OutworldExit.adder[OutworldExit.E];
							add2 = OutworldExit.adder[OutworldExit.N];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) &&
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.SE:
							add1 = OutworldExit.adder[OutworldExit.E];
							add2 = OutworldExit.adder[OutworldExit.S];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) &&
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.SW:
							add1 = OutworldExit.adder[OutworldExit.W];
							add2 = OutworldExit.adder[OutworldExit.S];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) &&
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
							
						case OutworldExit.NW:
							add1 = OutworldExit.adder[OutworldExit.W];
							add2 = OutworldExit.adder[OutworldExit.N];
							if(!orf.isPassable(getType(xloc+add1[0], yloc+add1[1], layer)) &&
									!orf.isPassable(getType(xloc+add2[0], yloc+add2[1], layer)))
								continue;
							break;
						}
					}
				}
				else if(i<9 && i%2==0 && exitStrategy == OutworldRoomFactory.ExitStrategy.ONLY_DIAGONAL)
					continue;
				
				if(i < 8)
					type = getType(xloc+OutworldExit.adder[i][0], yloc+OutworldExit.adder[i][1], layer);
				else
					type = getType(xloc, yloc, layer+(i==8 ? 1 : -1));
				
				if(type == null)
					continue;
				
				if(orf.isPassable(type)) {
					Exit oe = getExitInstance(i);
					or.add(oe);
				}
			}
		}

		// add special exits (if any)
		addThingsToRoom((List) specialThings.get(xloc+","+yloc+","+layer), or);
		addThingsToRoom((List) specialThings.get(Character.toString((char) type[0])), or);
		
		
		return new PrototypeObjectWrapper(null, or);
	}
	
	private void addThingsToRoom(List roomThings, Room or) {
		if(roomThings != null) {
			Iterator it = roomThings.iterator();
			while(it.hasNext()) {
				String loadId = it.next().toString();
				//System.out.println("loader: "+ loader+", id: "+loadId);
				MObject obj = (MObject) evaluateThing(or.getId(), loadId);
				if(obj == null)
					log.warning("addThingsToRoom: evaluateThing returned null for "+loadId);
				else {
					obj.start();
					obj.setParent(or);
					or.add(obj);
				}
			}
		}
	
	}
	
	protected MObject evaluateThing(String roomId, String thing) {
		Prototype p = World.load(thing);
		return (MObject) (p == null ? null : p.isUnique() ? p.get() : p.create());
	}
	
	/* Shared instances */
	protected HashMap _exits = new HashMap();
	
	public Exit getExitInstance(int dir) {
		synchronized(OutworldExit.class) {
			String key = prefix+Integer.toString(dir);
			OutworldExit oe = (OutworldExit) _exits.get(key);
			
			if(oe == null) {
				oe = new OutworldExit(this, orf, prefix, dir);
				_exits.put(key, oe);
			}
			return oe;
		}
	}
	
	/* Returns map entry type as an array.
	 * type[0] = map legend character
	 * type[1] = special code
	 */
	public byte[] getType(int x, int y, int layer) {
		//System.out.println("GET TYPE FOR ROOM, X: "+x+", Y: "+y+", layer: "+layer);
		return source.getType(x, y, layer);
	}
	
	public Mapper getMapper() {
		return mapper;
	}
	
	private boolean containsChar(String str, char ch) {
		for(int i=0; i<str.length(); i++)
			if(str.charAt(i) == ch)
				return true;
		return false;
	}


	public String getLocation(char legend, int x, int y) {
		for(LocationSpec ls : locations) {
			if(ls.area.contains(x, y) && containsChar(ls.types, legend)) {
				return ls.location;
			}
		}
		return null;
	}
	
	public Area getArea(char legend, int x, int y) {
		
		return areaMap.get(getLocation(legend, x, y));
	}

	public boolean isLoaded(String path) {
		return false;
	}

	public void unload(String path) {}

	public Object get(String path) {
		return load(path).get();
	}

	// makes adding special exits to outer world rooms easy
	// with this you can add items/exits/monsies to specific outerworld locations
	public void addThingForLocation(int x, int y, int layer, String e) {
		x += origoX;
		y += origoY;
		
		LinkedList roomThings = (LinkedList) specialThings.get(x+","+y+","+layer);
		if(roomThings == null) {
			roomThings = new LinkedList();
			specialThings.put(x+","+y+","+layer, roomThings);
		}
		roomThings.add(e);
	}

	public void addThingForType(String type, String e) {
		LinkedList roomThings = (LinkedList) specialThings.get(type);
		if(roomThings == null) {
			roomThings = new LinkedList();
			specialThings.put(type, roomThings);
		}
		roomThings.add(e);
	}
	
	public Point getOrigo() {
		return new Point(origoX, origoY);
	}
	
}

