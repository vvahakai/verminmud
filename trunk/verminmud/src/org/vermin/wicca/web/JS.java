package org.vermin.wicca.web;

/**
 * Utility class to represent Java objects in JavaScript.
 */
public class JS {

	public static String list(Object ... args) {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<args.length; i++) {
			sb.append(repr(args[i]));
			if(i < args.length-1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	public static String repr(Object obj) {
		
		if(obj == null)
			return "null";
		else if(obj.getClass().isPrimitive())
			return obj.toString();
		else if(obj instanceof String) 
			return escapeString(obj.toString());
		else 
			throw new RuntimeException("Unable to JSify: "+obj); 
	}
	
	public static String escapeString(String evil) {
		return "\"" + evil.replace("\n", "&.;").replace("\"", "\\\"") + "\"";
	}
}
