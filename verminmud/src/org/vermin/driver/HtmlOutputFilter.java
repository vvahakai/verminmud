/* HtmlOutputFilter.java
	18.6.2004	Tatu Tarvainen 
	
	Output filter for HTML clients.
	
*/
package org.vermin.driver;

import org.vermin.io.ClientConnection;
import org.vermin.wicca.telnet.OutputFilter;

public class HtmlOutputFilter extends OutputFilter {

	
	private StringBuffer sb = new StringBuffer();
	
	private boolean font = false;

	/* Called to get the appropriate codes to change output style */
	protected void writeTo(ClientConnection ps, int attrib, int fgcolor, int bgcolor) {
		// System.out.println("ATTR: "+attrib+", FG: "+fgcolor+", BG: "+bgcolor);
		
		if(font)
			reset(ps);

		String col="";
		switch(fgcolor) {
		  case COLOR_BLACK: col = "black"; break;
		  case COLOR_WHITE: col = "white"; break;
		  case COLOR_RED: col = "red"; break;
		  case COLOR_GREEN: col = "green"; break;
		  case COLOR_BLUE: col = "blue"; break;
		  case COLOR_MAGENTA: col = "magenta"; break;
		  case COLOR_CYAN: col = "cyan"; break;
		  case COLOR_YELLOW: col = "yellow"; break;
		  default: return;
		}

		ps.print("<font color=\""+col+"\">");
		font = true;
	}
	
	
	protected void reset(ClientConnection ps) {
		if(font)
			ps.print("</font>");
		font = false;
	}
	
	protected void newline(ClientConnection ps) {
		ps.print("\r\n");
	}

}
