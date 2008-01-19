/**
 * Some regular expression utilities.
 * 21.9.2003 Tatu Tarvainen
 */
package org.vermin.util;

import java.util.regex.*;

public class Regex {

	/**
	 * Matches source against regex and returns all captured
	 * groups as an array. If the source does not match, returns
	 * null.
	 *
	 * @param regex the regular expression as string
	 * @param source the source string
	 * @return the captured groups
	 */
	public static String[] matches(String regex, String source) {
		return matches(Pattern.compile(regex), source);
	}

	/**
	 * Like the other matches method, only takes a precompiled 
	 * regular expression pattern instead of a string.
	 *
	 * @param regex the regular expression pattern
	 * @param source the source string
	 * @return the captured groups
	 */
	public static String[] matches(Pattern regex, String source) {
		Matcher m = regex.matcher(source);
		if(m.matches()) {
			String[] g = new String[m.groupCount()];
			for(int i=0; i<m.groupCount(); i++)
				g[i] = m.group(i+1);
			return g;
		}
		return null;
	}

}
