package org.vermin.world.rooms;

import org.vermin.mudlib.*;
import org.vermin.mudlib.outworld.OutworldLoader;

public class CattlebridgeOutworldRoomFactory extends CityOutworldRoomFactory {

	private Area cattlebridgeArea;
	
	public Room createRoom(OutworldLoader loader, int x, int y, byte[] type) {
		if(cattlebridgeArea == null) {
			cattlebridgeArea = new DefaultAreaImpl(loader.getMapper(), WeatherService.getInstance(), "City of Cattlebridge", null);
		}
		
		Room r = new CattlebridgeRoom(type);
		r.setArea(cattlebridgeArea);
		return r;
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
		return legend;
	}

}
