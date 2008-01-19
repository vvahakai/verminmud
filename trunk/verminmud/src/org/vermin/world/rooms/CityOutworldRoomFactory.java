package org.vermin.world.rooms;

import org.vermin.mudlib.*;
import org.vermin.mudlib.outworld.OutworldLoader;
import org.vermin.mudlib.outworld.OutworldRoomFactory;

public class CityOutworldRoomFactory implements OutworldRoomFactory {

	private Area vermincityArea;

	public Room createRoom(OutworldLoader loader, int x, int y, byte[] type) {
		Room r = new CityRoom(type);
		r.setArea(loader.getArea((char) type[0], x, y));
		
		if(r.getArea() == null)
			r.setArea(getMainArea(loader));
		return r;
	}

	public synchronized Area getMainArea(OutworldLoader loader) {
		if(vermincityArea == null) {
			vermincityArea = new DefaultAreaImpl(loader.getMapper(), WeatherService.getInstance(), "Vermincity", null);
		}
		return vermincityArea;
	}
	
	public boolean isPassable(byte[] type) {
		if(type[0] == 0)
			return false;

		switch(type[0]) {
		  case '-': return false;
		  case '|': return false;
		  case '#': return false;
		  default: return true;
		}
	}
	
	public boolean isUnderwater(byte[] type) {
		return false;
	}
	public boolean needToFly(byte[] type) {
		return false;
	}

	public String getLegend(byte[] type) {
		switch(type[0]) {
		  case 'P': return "&B4;P";
		  case '.': return "&2;.";
		  case '#': return "&8;#";
		  case '-': return "&B2;-";
		  case '|': return "&B2;|";
		  default: return "&3;"+(char)type[0];
		}
	}

	public String[][] getMapLegend() {
		String[][] legend = new String[8][2];
		legend[0][0] = "&B4;P"; legend[0][1] = "Park";
		legend[1][0] = "&8;."; legend[1][1] = "Street";
		legend[2][0] = "&B1;#"; legend[2][1] = "Building wall";
		legend[3][0] = "&B2;-"; legend[3][1] = "Building entrance";
		legend[4][0] = "&B2;|"; legend[4][1] = "Building entrance";
		legend[5][0] = "&3;%"; legend[5][1] = "City gate";
		legend[6][0] = "&3;+"; legend[6][1] = "Graves";
		legend[7][0] = "&3;M"; legend[7][1] = "Saint Mikhel's dwarvish pub";
		return legend;
	}

	public Exit createUpExit(byte[] type, int x, int y, int layer) {
		return null;
	}

	public Exit createDownExit(byte[] type, int x, int y, int layer) {
		return null;
	}

	public ExitStrategy getExitStrategy(byte[] type) {
		return OutworldRoomFactory.ExitStrategy.ALL;
	}
	
	

}
