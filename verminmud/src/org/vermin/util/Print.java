/* Print.java
 * 28.5.2002 Tatu Tarvainen
 * 
 * Utility functions for prettyprinting.
 */
package org.vermin.util;

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
}
