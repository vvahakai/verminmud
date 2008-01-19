package org.vermin.mudlib.outworld;

public class StringMapSource implements MapSource {

	private String[] map;
	
	public StringMapSource(String data) {
		map = data.split("\n");
	}
	
	public int getMinimumLayer() {
		return 0;
	}

	public int getMaximumLayer() {
		return 0;
	}

	public byte[] getType(int x, int y, int layer) {
		
		if(layer != 0 || y < 0 || y >= map.length || x < 0 || x >= map[y].length())
			return new byte[] { (byte) ' ' };
		
		return new byte[] { (byte) map[y].charAt(x) };
	}

	private int width = 0;
	public synchronized int getWidth() {
		if(width != 0)
			return width;
		
		for(String line : map)
			width = width < line.length() ? line.length() : width;
			
		return width;
	}

	public int getHeight() {
		return map.length;
	}

	public boolean hasMaskedExits() {
		return false;
	}

	public int getExits(int x, int y, int layer) {
		return 0;
	}

}
