/* DefaultMapperImpl.java
	13.2.2002	Tatu Tarvainen / Council 4
	
	Default mapper implementation.
*/

package org.vermin.mudlib;

public class DefaultMapperImpl implements Mapper {

	private static class MapNode  {
		String legend;
		String id;

		public MapNode(String legend, String id) {
			this.legend = legend;
			this.id = id;
		}
		
		public String getLegend() { return legend; }
		public String getId() { return id; }
	}
	
	private MapNode[][] map;
	
	private int width;
	private int height;
	private boolean suppressSpecials;
	
	public DefaultMapperImpl(int width, int height) {
		map = new MapNode[width][height];
		this.width = width;
		this.height = height;
	}	
	
	public void setEntry(int x, int y, String legend, String id) {
		map[x][y] = new MapNode(legend, id);
	}
	
	public String getMap(String id) {
		/* Find the map position */
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				if(map[x][y] != null && map[x][y].getId().equals(id))
					return doMapRender(x, y);
			}
		}
		
		return "You don't know this area.";
	}

	public String getLargeMap(String id) {
		return getMap(id);
	}

	private String doMapRender(int posX, int posY) {
		StringBuffer sb = new StringBuffer();
		
		/* Set starting position */
		int sx = posX-4;
		if(sx < 0) sx = 0;
		
		int sy = posY-3;
		if(sy < 0) sy = 0;
		
		for(int y=0; y<7; y++) {
			if( sy+y >= height ) break;
			
			for(int x=0; x<9; x++) {
				if(sx+x >= width) break;
				
				if((sx+x == posX) && (sy+y == posY))
					sb.append("&B2;*");
				else if(map[sx+x][sy+y] != null)
					sb.append(map[sx+x][sy+y].getLegend());
				else
					sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void showLegend(Player who) {} // Danny

	public void setSuppressSpecials(boolean suppress) {
		suppressSpecials = suppress;
		
	}

	public boolean isSuppressSpecials() {
		return suppressSpecials;
	}
}
