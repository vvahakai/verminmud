/* Print.java
 * 28.5.2002 Tatu Tarvainen
 * 
 * Utility functions for prettyprinting.
 */
package org.vermin.util;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;
import java.util.List;

import org.vermin.mudlib.World;

public class Print
{
	private static final Character[] wovels = {'a', 'e', 'i', 'o', 'u', 'y'};
	private static final Character[] suffixes = {'k', 'M', 'G', 'T' };

   public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
   }
   
   /**
    * Prefixes given string with either 'a' or 'an',
    * as appropriate.
    *  
    * @param str  the string to add an article to
    * @return  the string with the article added
    */
   public static String addArticle(String str) {
	   if(Arrays.contains(wovels, str.charAt(0))) {
		   return "an "+str;
	   } else {
		   return "a "+str;
	   }
   }

   public static String vectorToString(Vector v, String glue, int start) {
		return listToString(v, glue, start);
	}

   public static String listToString(List v, String glue, int start) {
      StringBuilder sb = new StringBuilder();
      for(int i=start; i<v.size(); i++) {
         sb.append(v.get(i) +
               (i==v.size()-1
                ? ""
                : glue));
      }
      return sb.toString();
   }

	public static String join(List<String> v) {
		return listToString(v, " ", 0);
	}

	public static String joinI(Iterable<String> strings, String glue) {
		StringBuilder sb = new StringBuilder();
		boolean f = true;
		for(String s : strings) {
			if(!f) sb.append(glue);
			f=false;
			sb.append(s);
		}
		return sb.toString();
	}
	
   public static String join(List v, String glue) {
		return listToString(v, glue, 0);
	}

   public static String join(List v, String glue, int startIndex) {
		return listToString(v, glue, startIndex);
	}

   public static String listToString(List v, String glue) {
		return listToString(v, glue, 0);
	}
   public static String vectorToString(Vector v, String glue) {
      return listToString(v, glue, 0);
   }

	public static String clamp(int size, String what) {
		if(what.length() > size)
			return what.substring(0, size);
		else
			return what;
	}

	/**
	 * Parses a value to a more human readable version,
	 * eg. 12345 => "12k 345".
	 * @param value  the value to be converted
	 * @return  the resulting human readable string
	 */
	public static String humanReadable(long value) {
		String original = Long.toString(value);
		StringBuilder sb = new StringBuilder();
		int suffixIndex = 0;
		try {
			for(int i = original.length()-1; i >= 0; i--) {
				sb.insert(0, original.charAt(i));
				if((original.length() - i) != 0 && 
						(original.length() - i) % 3 == 0 &&
						i != 0) {
					sb.insert(0, suffixes[suffixIndex]+" ");
					suffixIndex++;
				}
			}
		} catch(ArrayIndexOutOfBoundsException aioobe) {
			World.log("Print.humanReadablesta loppui suffixit.");
		}
		return sb.toString();
	}

	/**
	 * Format two arrays of strings as the left and right columns and return a single
	 * array of resultant lines.
	 * 
	 * @param leftLines the lines on the left column
	 * @param leftWidth the width of the left column
	 * @param rightLines the lines on the right column
	 * @param rightWidth the width of the right column
	 * @return
	 */
	public static String[] columnize(String[] leftLines, int leftWidth, String[] rightLines, int rightWidth) {
		ArrayList<String> result = new ArrayList<String>();
		
		Stack<String> left = new Stack<String>();
		Stack<String> right = new Stack<String>();
		Arrays.fill(left, Arrays.reverseCopy(leftLines));
		Arrays.fill(right, Arrays.reverseCopy(rightLines));
		
		while(!left.isEmpty() || !right.isEmpty()) {
			String nextLeft = left.isEmpty() ? null : left.pop();
			String nextRight = right.isEmpty() ? null : right.pop();
			int ll = 0, rl = 0; // right and left lengths
			StringBuilder line = new StringBuilder();
			
			// left side of the line
			if(nextLeft != null) {
				if(printableLength(nextLeft) > leftWidth) {
					line.append(nextLeft.substring(0, leftWidth));
					left.push(nextLeft.substring(leftWidth));
					ll = leftWidth;
				} else {
					line.append(nextLeft);
					ll = printableLength(nextLeft);
				}
			}
			
			line.append("&;"); // reset coloring
			while(ll < leftWidth) {
				line.append(" ");
				ll++;
			}
			
			
			
			// right side of the line
			if(nextRight != null) {
				if(nextRight.length() > rightWidth) {
					int boundary = findPreviousWordBoundary(nextRight, rightWidth);
					if(boundary == -1) boundary = rightWidth; // if word is longer than line, just cut it
					line.append(nextRight.substring(0, boundary));
					right.push(nextRight.substring(boundary == rightWidth ? boundary : boundary+1));
					rl = rightWidth;
				} else {
					line.append(nextRight);
					rl = nextRight.length();
				}
			}
			// no need to pad right side of the line	
			
			result.add(line.toString());
		}
		return result.toArray(new String[result.size()]);
	}
	
	/**
	 * Find previous word boundary (white space character) within the given string
	 * starting backwards from the given position.
	 * 
	 * @param line the line to search
	 * @param startingPos position to start at 
	 * @return boundary position or -1 if not found
	 */
	public static int findPreviousWordBoundary(String line, int startingPos) {
		while(startingPos > 0 && !Character.isWhitespace(line.charAt(startingPos)))
			startingPos--;
		return startingPos < 1 ? -1 : startingPos;
	}
	
	/**
	 * Calculate the number of printable characters in the given string.
	 * The string may contain control codes that affect color. The control
	 * codes are not counted against the total length.
	 * 
	 * @param stringWithControlCodes the string
	 * @return number of printable characters in the string
	 */
	public static int printableLength(String stringWithControlCodes) {
		if(stringWithControlCodes == null)
			throw new IllegalArgumentException("Null is not a valid printable string");
		return stringWithControlCodes.replaceAll("&[^%]{0,3};", "").length();
	}
}
