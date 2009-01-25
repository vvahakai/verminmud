/* OutworldExit.java
	31.3.2002	Tatu Tarvainen
	
	Exit implementation for outer world rooms.

	All outerworld rooms share exits the 8 directional exits
	(1 for each direction).
	
*/
package org.vermin.mudlib.outworld;

import java.util.TreeMap;

import org.vermin.mudlib.DefaultObjectImpl;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Modifier;
import org.vermin.mudlib.Types;

public class OutworldExit extends DefaultObjectImpl implements Exit {
	 
	public static final int N    = 0;
	public static final int NE   = 1;
	public static final int E    = 2;
	public static final int SE   = 3;
	public static final int S    = 4;
	public static final int SW   = 5;
	public static final int W    = 6;
	public static final int NW   = 7;
	public static final int UP   = 8;
	public static final int DOWN = 9;


	/* X and Y coordinate adders for different directions */
	public static final int adder[][] =
	{
		{  0, -1},
		{  1, -1},
		{  1,  0},
		{  1,  1},
		{  0,  1},
		{ -1,  1},
		{ -1,  0},
		{ -1, -1}
	};
	
	/* List of aliases for different directions */
	public static final String dirs[][] =
	{
		{ "n", "north" 		},
		{ "ne", "northeast"  },
		{ "e", "east" 			},
		{ "se", "southeast"  },
		{ "s", "south" 		},
		{ "sw", "southwest"  },
		{ "w", "west" 			},
		{ "nw", "northwest"  },
		{ "u", "up" },
		{ "d", "down" }
	};
	
	/* The direction for this exit (0 to 7) */
	protected int direction;
	
	/* Room id prefix */
	protected String prefix;

	protected TreeMap barrierModifiers;

	protected OutworldLoader loader;
	protected OutworldRoomFactory factory;

	protected OutworldExit(OutworldLoader loader, OutworldRoomFactory factory, String prefix, int dir) {
		this.loader = loader;
		this.factory = factory;
		direction = dir;
		this.prefix = prefix;
	}
	
	public String getTarget(String ids) {
		String id = ids.substring(ids.indexOf(':')+1);
		
		int xloc = -1;
		int yloc = -1;
		int layer = -1;

		int[] pos = OutworldLoader.parsePosition(ids);
		xloc = pos[0];
		yloc = pos[1];
		layer = pos[2];

		if(direction < 8) {
			// directional exits
			String e = prefix+":"+(xloc+adder[direction][0])+","+(yloc+adder[direction][1])+","+layer;
			return e;
		} else {
			// up/down exits
			return prefix+":"+xloc+","+yloc+","+(direction == UP ? (layer+1) : (layer-1));
		}
	}
	
	public String getDirection(String id) {
		switch(direction) {
		  case 0: return "n"; 
		  case 1: return "ne";
		  case 2: return "e";
		  case 3: return "se"; 
		  case 4: return "s";
		  case 5: return "sw";
		  case 6: return "w";
		  case 7: return "nw";
		  case 8: return "u";
		  case 9: return "d";
		  default: return "";
		}
	}
	
	public String getArrivalDirection(String id) {
		switch(direction) {
		  case 0: return "s"; 
		  case 1: return "sw";
		  case 2: return "w";
		  case 3: return "nw"; 
		  case 4: return "n";
		  case 5: return "ne";
		  case 6: return "e";
		  case 7: return "se";
		  case 8: return "d";
		  case 9: return "u";
		  default: return "";
		}
	}
	
	public boolean tryMove(Living who, String roomId) {
		String target = getTarget(roomId);

		int[] pos = loader.parseId(target);
		byte[] b = loader.getType(pos[0], pos[1], pos[2]);

		if(factory.needToFly(b) && !who.canFly()) {
			who.notice("You are unable to fly.");
			return false;
		}
		
		return true;
	}
	
	public String getPassMessage(String roomId) {
		return null;
	}
	
	public Types getType() { return Types.ROOM; }
	
	public void addBarrierModifier(String roomId, Modifier m) {
		if(barrierModifiers == null) 
			barrierModifiers = new TreeMap();
		barrierModifiers.put(roomId, m);
	}

}
