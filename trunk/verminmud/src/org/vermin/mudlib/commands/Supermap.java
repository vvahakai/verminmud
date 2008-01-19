package org.vermin.mudlib.commands;

import java.util.Iterator;

import org.vermin.mudlib.Command;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.World;

public class Supermap implements Command {
	
	public boolean action(Living who, String params) {
		char[][] map = new char[15][15];
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++)
				map[i][j] = '?';
		
		boolean[][] visited = new boolean[15][15];
		drawMap(who.getRoom(), map, visited, 7, 7);
		
		map[7][7] = '*';
		StringBuffer sb = new StringBuffer();
		for(int y=1; y<14; y++) {
			for(int x=1; x<14; x++) {
				sb.append(map[x][y]);
			}
			sb.append("\n");
		}
		who.notice(sb.toString());
		return true;
	} 
	
	// draw a map (by recursively checking out adjacent rooms)
	private void drawMap(Room r, char[][] map, boolean[][] visited, int x, int y) {
		
		if(x < 1 || x > 13 || y < 1 || y > 13)
			return;
	

		// mark this as a room
		map[x][y] = '.';
		visited[x][y] = true;

		// visit directions with exits, mark other directions as wall 
		Iterator en = r.findByType(Types.TYPE_EXIT);
		boolean[] exit = new boolean[8];

		while(en.hasNext()) {
			Exit e = (Exit) en.next();
			
			int[] loc = getMapCoords(r, e, x, y);
			if(loc == null)
				continue;
			
			int dir = getDirection(r, e);
			if(dir != -1)
				exit[dir] = true;

			int x1 = loc[0], y1 = loc[1];
			
			if(map[x1][y1] != '.' && !visited[x1][y1])
				drawMap((Room) World.get(e.getTarget(r.getId())),
						  map, visited, x1, y1);
		}	

		// mark surrounding area as wall '#'
		/*
		if(map[x  ][y-1] != '.' || !exit[0])  map[x  ][y-1] = '#';
		if(map[x  ][y+1] != '.' || !exit[1])  map[x  ][y+1] = '#';
		if(map[x-1][y  ] != '.' || !exit[2])  map[x-1][y  ] = '#';
		if(map[x+1][y  ] != '.' || !exit[3])  map[x+1][y  ] = '#';
		if(map[x-1][y-1] != '.' || !exit[4])  map[x-1][y-1] = '#';
		if(map[x+1][y-1] != '.' || !exit[5])  map[x+1][y-1] = '#';
		if(map[x+1][y+1] != '.' || !exit[6])  map[x+1][y+1] = '#';
		if(map[x-1][y+1] != '.' || !exit[7])  map[x-1][y+1] = '#';
		*/
		map[x  ][y-1] = exit[0] ? '.' : '#';
		map[x  ][y+1] = exit[1] ? '.' : '#';
		map[x-1][y  ] = exit[2] ? '.' : '#';
		map[x+1][y  ] = exit[3] ? '.' : '#';
		map[x-1][y-1] = exit[4] ? '.' : '#';
		map[x+1][y-1] = exit[5] ? '.' : '#';
		map[x+1][y+1] = exit[6] ? '.' : '#';
		map[x-1][y+1] = exit[7] ? '.' : '#';
		


	}

	private int[] getMapCoords(Room r, Exit e, int x, int y) {
		int x1=x, y1=y;
		String d = e.getDirection(r.getId());
		if(d.equals("n")) {
			y1 -= 1;
		} else if(d.equals("s")) { 
			y1 += 1;
		} else if(d.equals("w")) { 
			x1 -= 1; 
		} else if(d.equals("e")) { 
			x1 += 1;
		} else if(d.equals("nw")) { 
			x1 -= 1; y1 -= 1;
		} else if(d.equals("ne")) { 
			x1 += 1; y1 -= 1;
		} else if(d.equals("se")) { 
			x1 += 1; y1 += 1;
		} else if(d.equals("sw")) { 
			x1 -= 1; y1 += 1;
		} else {
			World.log("weird direction: "+d);
			return null;
		}

		//System.out.println("TO "+d+" FROM ("+x+","+y+") --> "+x1+","+y1);
		return new int[] { x1, y1 };
	}

	private int getDirection(Room r, Exit e) {
		String d = e.getDirection(r.getId());
		if(d.equals("n")) {
			return 0;
		} else if(d.equals("s")) { 
			return 1;
		} else if(d.equals("w")) { 
			return 2;
		} else if(d.equals("e")) { 
			return 3;
		} else if(d.equals("nw")) { 
			return 4;
		} else if(d.equals("ne")) { 
			return 5;
		} else if(d.equals("se")) { 
			return 6;
		} else if(d.equals("sw")) { 
			return 7;
		} else {
			return -1;
		}
	}

}
