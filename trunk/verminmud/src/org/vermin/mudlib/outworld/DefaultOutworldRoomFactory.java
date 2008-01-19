package org.vermin.mudlib.outworld;


import org.vermin.mudlib.*;

public class DefaultOutworldRoomFactory implements OutworldRoomFactory {

	private Area greaterVerminWorldArea;


	
	public Room createRoom(OutworldLoader loader, int x, int y, byte[] type) {
		OutworldRoom r = new OutworldRoom(type[0], loader, x, y);
		//r.setLocation(loader.getLocation((char) type[0], x, y));
		r.setArea(loader.getArea((char) type[0], x, y));
		
		
		if(r.getArea() == null) {
			r.setArea(greaterVerminWorldArea);			
		} else {
			r.getArea().setParent(greaterVerminWorldArea);
		}
		return r;
	}

	public synchronized Area getMainArea(OutworldLoader loader) {
		if(greaterVerminWorldArea == null) {
			greaterVerminWorldArea = new DefaultAreaImpl(loader.getMapper(), WeatherService.getInstance(), null, null);
		}
		return greaterVerminWorldArea;
	}
	
	public boolean isPassable(byte[] type) {

		if(type[0] == 0) return false;

		if(type[1] != ' ') return true;
		
		switch(type[0]) {
		  case ' ': return false;
		  case '^': return false;
		  case '#': return false;
		  case 'M': return false;
		  default: return true;
		}
	}
	
	public boolean isUnderwater(byte[] type) {
		switch(type[0]) {
		  case '_': return true;
		  case ';': return true;
		  case ':': return true;
		  case '{': return true;
		  case '}': return true;
		  default: return false;
		}
	}

	public boolean needToFly(byte[] type) {
		switch(type[0]) {
		  case '.': return true;
		  case ',': return true;
		  case '\'': return true;
		  default: return false;
		}
	}

	public String getLegend(byte[] type) {
		switch(type[0]) {
		  case 'p': return "&8;p";
		  case 'd': return "&B8;d";
		  case 'F': return "&4;F";
		  case 'f': return "&B4;f";
		  case 'R': return "&5;R";
		  case 'r': return "&B5;r";
		  case '~': return "&5;~";
		  case 'j': return "&4;j";
		  case 'V': return "&3;V";
		  case 't': return "&B2;t";
			  
		  case '^': return "&2;^";
		  case 'h': return "&B8;h";
		  case '?': return "&B2;?";
		  case '#': return "&B1;#";
		  case 'b': return "&B3;b";
		  case 's': return "&B1;s";
		  case 'l': return "&B5;l";
			  
		  case 'y': return "&B8;y";
		  case 'z': return "&B8;s";
		  case '"': return "&5;~";
		  case 'i': return "&6;i";
		  case 'w': return "&B7;w";
		  case 'M': return "&2;M";
			  
		  case '.': return "&B2;."; // air
		  case ',': return "&B2;."; // high air
		  case '\'': return "&B2;."; // above clouds

		  case 'm': return "&2;m";  // passable mountain

		  case 'T': return "&4;^";  // treetops
		  case 'o': return "&B8;o"; // hilltops

		  case '%': return "&2;%";  // ruins

		  case '_': return "&B5;_"; // bottom of the water
		  case ';': return "&B5;_"; // deep bottom
		  case ':': return "&5;_"; // sea bottom
		  case ' ': return " ";     // unpassable sea
		  case '{': return "&5;{";  // underwater
		  case '}': return "&5;}";  // deep underwater
			  
		  case '-':
		  case '+':
		  case '\\':
		  case '/':
		  case '=':
		  case '|':
			  return "&2;"+(char) type[0];
			  
		  default: return ""+(char)type[0];
		}
	}

	public String[][] getMapLegend() {
		return new String[0][0];
	}

	public ExitStrategy getExitStrategy(byte[] type) {
		return OutworldRoomFactory.ExitStrategy.ALL;
	}
}
