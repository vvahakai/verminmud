/* OutputFilter.java

	An abstract class that is the base for output filters.
*/

package org.vermin.wicca.telnet;

import java.util.regex.Pattern;

import org.vermin.io.ClientConnection;

public abstract class OutputFilter {
	
	public static final int ATTR_NORMAL =		'n';
	public static final int ATTR_BOLD = 		'B';
	public static final int ATTR_BLINK = 		'b';
	public static final int ATTR_REVERSE =	'r';
	public static final int ATTR_INVISIBLE = 'i';
	
	public static final int COLOR_UNDEFINED = 0;
	public static final int COLOR_BLACK = 	 1;
	public static final int COLOR_WHITE =		 2;
	public static final int COLOR_RED =		 3;
	public static final int COLOR_GREEN =		 4;
	public static final int COLOR_BLUE =		 5;
	public static final int COLOR_MAGENTA =	 6;
	public static final int COLOR_CYAN =		 7;
	public static final int COLOR_YELLOW =	 8;
	
	private StringBuffer sb;
	
	public OutputFilter() {
		sb = new StringBuffer();
	}
	
	/* Write the appropriate codes to the specified stream  */
	protected abstract void writeTo(ClientConnection ps, int attrib, int fgcolor, int bgcolor);
	protected abstract void reset(ClientConnection ps);
	protected abstract void newline(ClientConnection ps);

	private static final Pattern NEWLINE = Pattern.compile("\\r?\\n");

	public final void filterTo(ClientConnection ps, String txt) {
		
		String[] texts = NEWLINE.split(txt);

		for(int i=0; i<texts.length; i++) {
			String text = texts[i];
			boolean done = false;
			int p1, p2;
			while(!done) {
				p1 = text.indexOf('&');
			
				if(p1 == -1) {
					/* No color codes found, dump whole text */
					ps.print(text);
					done = true;
				} else {
					/* Dump up to the first '&' char */
					ps.print(text.substring(0, p1));
					text = text.substring(p1+1);
				
					p1 = text.indexOf(';');
					if(p1 != -1) {
						String code = text.substring(0, p1);
					
						text = text.substring(p1+1);
					
						if(/*code.length() > 0 &&*/ code.length() < 4)
							decodeColor(ps, code);
						else
							ps.print("&" + code + ";");
					
					} else {
						ps.print("&");
					}
				}
			}

			reset(ps);
			if(i != texts.length-1)
				newline(ps);
		}
	}
	
	private void decodeColor(ClientConnection ps, String col) {

		if(col.length() == 0) {
			reset(ps);
			return;
		}

		int attr, fg, bg;
		attr = ATTR_NORMAL;
		fg = COLOR_UNDEFINED;
		bg = COLOR_UNDEFINED;
		
		if(col.length() == 3) {
			attr = charToAttr(col.charAt(0));
			fg = charToColor(col.charAt(1));
			bg = charToColor(col.charAt(2));
		} else if(col.length() == 2) {
			if(Character.isDigit(col.charAt(0))) {
				fg = charToColor(col.charAt(0));
				bg = charToColor(col.charAt(1));
			} else {
				attr = charToAttr(col.charAt(0));
				fg = charToColor(col.charAt(1));
			}
		} else {
			if(Character.isDigit(col.charAt(0)))
				fg = charToColor(col.charAt(0));
			else
				attr = charToAttr(col.charAt(0));
		}
		
		writeTo(ps, attr, fg, bg);
	}
	
	private int charToAttr(char ch) {
		switch(ch) {
		  case 'B': return ATTR_BOLD;
		  case 'b': return ATTR_BLINK;
		  case 'r': return ATTR_REVERSE;
		  case 'i': return ATTR_INVISIBLE;
		  case 'n': return ATTR_NORMAL;
		  default: return ATTR_NORMAL;
		}
	}
	
	private int charToColor(char ch) {
		switch(ch) {
			case '1': return COLOR_BLACK;
	 		case '2': return COLOR_WHITE;
	 		case '3': return COLOR_RED;
			case '4': return COLOR_GREEN;
	 		case '5': return COLOR_BLUE;
	 		case '6': return COLOR_MAGENTA;
	 		case '7': return COLOR_CYAN;
	 		case '8': return COLOR_YELLOW;
			default: return COLOR_UNDEFINED;
		}
	}
}
