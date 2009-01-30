package org.vermin.mudlib.outworld;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vermin.driver.Loader;
import org.vermin.driver.Persistent;
import org.vermin.driver.Prototype;
import org.vermin.mudlib.Area;
import org.vermin.mudlib.DefaultAreaImpl;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.OneWayExit;
import org.vermin.mudlib.SpawningRule;
import org.vermin.mudlib.WeatherService;


/**
 * A convenient outworld loader that allows outworld-style
 * areas to be defined in a single file and as a subloader.
 */
public class EasyOutworldLoader implements Loader, Persistent  {

	private MyOutworldLoader loader;
	
	private String map;

	private int rowLength;

	private HashMap<String,String> roomTypes = new HashMap<String, String>();
	private HashMap<String,List<String>> things = new HashMap<String, List<String>>();
	
	
	private PrototypeRoomFactory prf;
	private String id;
	
	private HashMap<String,String> mapDisplay = new HashMap<String, String>();
	
	private LinkedList<SpawningRule> spawningRules = new LinkedList<SpawningRule>();
	
	private Area area;
	
	private boolean mapped = true;
	private boolean suppressSpecials;
	
	private HashMap<String,OutworldRoomFactory.ExitStrategy> exitStrategy;
	
	private String[][] mapLegend;
	
	public EasyOutworldLoader() {
	}
	
	public void start() {
		
		prf = new PrototypeRoomFactory(roomTypes, mapDisplay, exitStrategy);
		prf.setMapLegend(mapLegend);
		
		StringMapSource sms = new StringMapSource(map);
		map = null;
		
		loader = new MyOutworldLoader(getId()+"|", prf, sms);
		for(String key : things.keySet()) {
			List<String> roomThings = things.get(key);
			
			if(key.indexOf(',') == -1) {
				for(String thing : roomThings)
					loader.addThingForType(key, thing);
			} else {
				String[] location = key.split(",");
				int x = Integer.parseInt(location[0].trim());
				int y = Integer.parseInt(location[1].trim());
				int layer = Integer.parseInt(location[2].trim());
			
				for(String thing : roomThings) {
					loader.addThingForLocation(x, y, layer, thing);
				}
			}
		}
		
		if(area == null) {
			area = new DefaultAreaImpl(mapped ? loader.getMapper() : null, 
					WeatherService.getInstance(), null, spawningRules);
		} else //XXX: Should we add spawning rules to an existing area?
			area.setMapper(mapped ? loader.getMapper() : null);
		
		loader.getMapper().setSuppressSpecials(suppressSpecials);
		prf.setArea(area);
	}

	// Pattern for parsing manual exits in the format "dir X Y Z"
	private static Pattern manualExitPattern = 
		Pattern.compile("^\\s*(\\w{1,2})\\s+(\\d+)\\s+(\\d+)\\s(\\d+)\\s*$");
	
	
	private class MyOutworldLoader extends OutworldLoader {
		public MyOutworldLoader(String prefix, OutworldRoomFactory orf, MapSource ms) {
			super(prefix, orf, ms);
		}
		
		@Override
		protected MObject evaluateThing(String roomId, String id) {
			// This loader accepts exits in the format of manualExitPattern
			Matcher m = manualExitPattern.matcher(id);
			if(m.matches()) {
				String to = prefix+":"+m.group(2)+","+m.group(3)+","+m.group(4);
				String dir = m.group(1);
				log.fine("Creating manual one way exit as thing, to: "+to+", dir: "+dir);
				Exit e = new OneWayExit(roomId, to, dir);
				return e;
			}
			return super.evaluateThing(roomId, id);
		}
	};
	
	public Object get(String path) {
		return loader.get(path);
	}

	public boolean isLoaded(String path) {
		return loader.isLoaded(path);
	}

	public Prototype load(String ids) {
		return loader.load(ids);
	}

	public void unload(String path) {
		loader.unload(path);
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id; 		
	}

	public void save() {}
	public void setAnonymous(boolean anonymous) {}
	public boolean isAnonymous() {
		return false;
	}

	/**
	 * Add a room type. This can be called from savefile to add a room type
	 * with the map character, room id and display character.
	 */
	public void roomType(String mapChar, String displayChar, String id) {
		roomTypes.put(mapChar, id);
		mapDisplay.put(mapChar, displayChar);
	}
}
