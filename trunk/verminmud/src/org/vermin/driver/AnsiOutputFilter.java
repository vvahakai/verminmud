/* AnsiOutputFilter.java
	24.1.2002	Tatu Tarvainen / Council 4
	
	Output filter for ANSI color terminals.
	
*/
package org.vermin.driver;

import org.vermin.io.ClientConnection;
import org.vermin.wicca.telnet.OutputFilter;

public class AnsiOutputFilter extends OutputFilter {

	protected static final int ESCAPE       = 27;  // the ^[ 
	protected static final int LEFT_BRACKET = 91;  // [
	protected static final int M            = 109; // m
	protected static final int SEMICOLON    = 59;  // ;
	protected static final int ZERO         = 48;
	protected static final int ONE          = 49;
	protected static final int TWO          = 50;
	protected static final int THREE        = 51;
	protected static final int FOUR         = 52;
	protected static final int FIVE         = 53;
	protected static final int SIX          = 54;
	protected static final int SEVEN        = 55;
	protected static final int EIGHT        = 56;
	protected static final int NINE         = 57;

	
	/* Called to get the appropriate codes to change output style */
	protected void writeTo(ClientConnection ps, int attrib, int fgcolor, int bgcolor) {
		// System.out.println("ATTR: "+attrib+", FG: "+fgcolor+", BG: "+bgcolor);
		
		ps.write(ESCAPE);
		ps.write(LEFT_BRACKET);
		
		ps.print(getAnsiAttrCode(attrib));
		
		if(fgcolor == COLOR_UNDEFINED) {
			ps.write(M);
			return;
		}
		
		ps.write(SEMICOLON);
		ps.write(THREE);
		ps.write(getAnsiColorCode(fgcolor));
		
		if(bgcolor == COLOR_UNDEFINED) {
			ps.write(M);
			return;
		}
		
		ps.write(SEMICOLON);
		ps.write(FOUR);
		ps.write(getAnsiColorCode(bgcolor));
		ps.write(M);
	}
	
	
	protected void reset(ClientConnection ps) {
		ps.write(ESCAPE);
		ps.write(LEFT_BRACKET);
		ps.write(ZERO); 
		ps.write(M);
		
	}
	
	protected void newline(ClientConnection ps) {
		ps.print("\r\n");
	}

	private int getAnsiAttrCode(int attrib) {
		switch(attrib) {
		  case ATTR_NORMAL: return ZERO;
		  case ATTR_BOLD: return ONE;
		  case ATTR_BLINK: return FIVE;
		  case ATTR_REVERSE:	return SEVEN;
		  case ATTR_INVISIBLE: return EIGHT;
		  default: return ZERO;
		}
	}

	/* get the color code (index) without the 30 or 40 addition */
	private int getAnsiColorCode(int col) {
		switch(col) {
			case COLOR_BLACK: 	return ZERO;
			case COLOR_RED:		return ONE;
			case COLOR_GREEN:		return TWO;
			case COLOR_YELLOW:	return THREE;
			case COLOR_BLUE:		return FOUR;
			case COLOR_MAGENTA:	return FIVE;
			case COLOR_CYAN:		return SIX;
			case COLOR_WHITE: 	return SEVEN;
			default: return ZERO;
		}
	}
}
