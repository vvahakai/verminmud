package org.vermin.world.rooms;

import org.vermin.mudlib.*;


public class CattlebridgeRoom extends CityRoom {

	public boolean action(MObject actor, String cmd) {
		return false;
	}

	public CattlebridgeRoom(byte[] type) {
		super(type);
		this.type = type[0];
		illumination = 50;
	}

	public String getDescription() {
		switch(type) {
		  case '.': return "Streets of Cattlebridge.";
		  case '%': return "Cattlebridge gates.";
		  case 'P': return "City park.";
		  case 'C': return "Central square of Cattlebridge.";
		  case '+': return "Unquiet Graveyard.";
		}
		return "";
	}
	public String getLongDescription() {
		switch(type) {
		  case '.': return "You are on the streets of Cattlebridge.";
		  case '%': return "You are standing between the gates of Cattlebridge.";
		  case 'P': return "You are in a city park. It is very beautiful here.";
		  case 'C': return "This the central square of Cattlebridge.";
		  case '+': return "You are walking through Cattlebridge graveyard. You are surrounded by tombstones.";
		}
		return "";
	}

	public void explore(Living explorer) {}
}
