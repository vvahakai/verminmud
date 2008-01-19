package org.vermin.io;

public class URLDecode {

	private static final String HEX = "0123456789ABCDEF";

	public static String decode(String txt) {
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<txt.length(); i++) {
			
			if(txt.charAt(i) == '+')
				sb.append(" ");
			else if(txt.charAt(i) != '%')
				sb.append(txt.charAt(i));
			else {
				sb.append((char) (get(txt.charAt(i+1)) * 16 + get(txt.charAt(i+2))));
				i += 2;
			}
		}
		return sb.toString();
	}

	private static int get(char ch) {
		ch = Character.toUpperCase(ch);
		for(int i=0; i<HEX.length(); i++)
			if(ch == HEX.charAt(i))
				return i;
		throw new RuntimeException("Not a hexadecimal character.");
	}
}
