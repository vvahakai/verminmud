package org.vermin.mudlib.outworld;

import java.util.HashMap;

import org.vermin.driver.Prototype;
import org.vermin.mudlib.Area;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.World;

public class PrototypeRoomFactory implements OutworldRoomFactory {

	private Area area;
	
	/* Maps room type chars to room prototype ids */
	private HashMap<String,String> types;
	
	/* Maps toom type char to actual map display (for coloring etc) */
	private HashMap<String,String> mapDisplay;
	
	/* Maps room type to exit strategy */
	private HashMap<String,OutworldRoomFactory.ExitStrategy> exitStrategy;
	
	public PrototypeRoomFactory(HashMap<String,String> types, HashMap<String,String> mapDisplay,
			HashMap<String,ExitStrategy> exitStrategy) {
		this.types = types;
		this.mapDisplay = mapDisplay;
		this.exitStrategy = exitStrategy;
	}
	
	public Area getMainArea(OutworldLoader loader) {
		return area;
	}
	
	public void setArea(Area a) {
		area = a;
	}
	
	public Room createRoom(OutworldLoader loader, int x, int y, byte[] type) {
		
		String t = Character.toString((char) type[0]);
		
		String id = types.get(t);
		
		Prototype p = World.load(id);
		
		if(p == null) {
			World.log("EasyOutworldLoader: Unable to load prototype for roomtype "+t);
			return null;
		}
		Room r = (Room) (p.isUnique() ? p.get() : p.create());
		r.setArea(area);
		return r;
		
	}

	public boolean isPassable(byte[] type) {
		String t = Character.toString((char) type[0]);
		return types.containsKey(t);
	}

	public String getLegend(byte[] type) {
		String disp = mapDisplay.get(Character.toString((char) type[0]));
		return disp == null ? " " : disp;
	}

	public String[][] getMapLegend() {
		return new String[0][0];
	}

	public boolean needToFly(byte[] type) {
		return false;
	}

	public boolean isUnderwater(byte[] type) {
		return false;
	}

	public ExitStrategy getExitStrategy(byte[] type) {
		String t = Character.toString((char) type[0]);
		ExitStrategy es = exitStrategy.get(t);
		if(es == null)
			es = exitStrategy.get("default");
		if(es == null)
			es = ExitStrategy.ALL;
		
		return es;
	}
	
}
