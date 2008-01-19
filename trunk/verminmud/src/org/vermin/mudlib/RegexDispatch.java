package org.vermin.mudlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.vermin.util.ReflectHelper;
import org.vermin.util.Regex;

public class RegexDispatch {

	private RegexDispatchTarget target;

	private static class Dispatch {
		String method;

		String[] argtypes; // inventory, living, (null means String)
		int[] args;

		Pattern regex;
	};

	private Dispatch[] _dispatch;

	private static final Pattern DISPATCH_PATTERN = Pattern.compile("^(.*)\\s+=>\\s+(\\w+)\\((.*)\\)$");

	public RegexDispatch(RegexDispatchTarget tgt) {
		target = tgt;

		String[] dispatch = target.getDispatchConfiguration();
		_dispatch = new Dispatch[dispatch.length];

		for(int i=0; i<dispatch.length; i++) {
			_dispatch[i] = parseDispatch(dispatch[i]);
		}
	}
	
	// returns true if matched, false otherwise
	public boolean dispatch(Living who, String command) {
		for(int i=0; i<_dispatch.length; i++) {
			String[] matches = Regex.matches(_dispatch[i].regex, command);
			if(matches != null) {
				dispatch(who, _dispatch[i], matches);
				return true;
			}
		}
		return false;
	}

	private void dispatch(Living who, Dispatch d, String[] matches) {
		//System.out.println("....dispatch to "+d.method+" ....");

		Object[] args = new Object[d.argtypes.length];
		for(int i=0; i<args.length; i++) {
			String type = d.argtypes[i];
			String value = d.args[i] == -1 ? null : matches[d.args[i]-1];

			args[i] = resolveValue(who, type, value);
		}

		try {
			Method m = findMethod(d.method, d.argtypes);
			//System.out.println("INVOKING: "+m+"\n  with args:");
			Object[] arguments = makePrimitives(m.getParameterTypes(), args);
			//for(int i=0; i<arguments.length; i++) {
			//	System.out.println("     "+arguments[i]);
			//}
			m.invoke(target, arguments);

		} catch(InvocationTargetException e) {
//			World.logDevel(e.getMessage());
//			World.logDevel("CAUSE: "+e.getTargetException().getMessage());
			throw new RuntimeException(e.getCause());
		} catch(NoSuchMethodException nsme) {
//			World.logDevel("NO SUCH METHOD: "+nsme.getMessage());
			World.log("RegexDispatch: NO SUCH METHOD: "+nsme.getMessage());
		} catch(IllegalAccessException iae) {
//			World.logDevel("ILLEGAL ACCESS: "+iae.getMessage());
			World.log("RegexDispathc: ILLEGAL ACCESS: "+iae.getMessage());
		}
	}

	private Object[] makePrimitives(Class[] types, Object[] values) {
		for(int i=0; i<types.length; i++) {
			if(types[i].isPrimitive())
				values[i] = ReflectHelper.makePrimitive(types[i].getName(), values[i].toString());
		}
		return values;
	}

	private Method findMethod(String name, String[] types) throws NoSuchMethodException {
		
		Method[] candidates = target.getClass().getDeclaredMethods();

		for(int i=0; i<candidates.length; i++) {
			if(!candidates[i].getName().equals(name))
				continue;

			Class[] wantedTypes = candidates[i].getParameterTypes();

			if(wantedTypes.length != types.length)
				continue;

			// check that types match
			boolean ok = true;
			for(int t=0; t<wantedTypes.length; t++) {

				if(types[t] == null && (wantedTypes[t].isPrimitive() || wantedTypes[t] == String.class))
					continue;

				if(MObject.class.isAssignableFrom(wantedTypes[t]))
					continue;

				// not compatible!!!
				//System.out.println("RegexCommand, type mismatch: "+types[t]+" != "+wantedTypes[t]);
				ok = false;
				break;
			}

			if(ok)
				return candidates[i];
		}
		throw new NoSuchMethodException("No matching method was found.");
	}

	private Object resolveValue(Living who, String type, String value) {
		if(type == null)
			return value; // this will be later converted to primitive/String

		if(type.equalsIgnoreCase("actor")) { // the player executing the command
			return who;
        } else if(type.equalsIgnoreCase("room")) {
            return who.getRoom();
		} else if(type.equalsIgnoreCase("inventory")) { // Get item from inventory
			return who.findByNameAndType(value, Types.TYPE_ITEM);
		} else if(type.equalsIgnoreCase("living")) { // Get living from room
			return who.getRoom().findByNameAndType(value, Types.TYPE_LIVING);
		} else if(type.equalsIgnoreCase("ground")) { // Get item from ground
			return who.getRoom().findByNameAndType(value, Types.TYPE_ITEM);
		} else if(type.equalsIgnoreCase("item")) { // Get item from ground or inventory
			MObject item = who.findByNameAndType(value, Types.TYPE_ITEM);
			if(item == null)
				item = who.getRoom().findByNameAndType(value, Types.TYPE_ITEM);
			return item;
		} else if(type.equalsIgnoreCase("any")) { // any item/living/exit we can find
			MObject item = who.findByName(value);
			if(item == null)
				item = who.getRoom().findByName(value);
			return item;
		} else if(type.equalsIgnoreCase("player")) { // player anywhere in the game
			if(World.isLoggedIn(value)) {
				return (MObject) World.get("players/"+value.toLowerCase());
			}
			return null;
		}
		
		throw new RuntimeException("Unable to resolve value, type: "+type+", value: "+value);
	}
	
	private Dispatch parseDispatch(String d) {

		String[] disp = Regex.matches(DISPATCH_PATTERN, d);
		if(disp.length != 3)
			throw new IllegalArgumentException("Unrecognized dispatch string.");
		
		Dispatch di = new Dispatch();

		// compile the pattern. for convenience replace " " with "\\s+"
		di.regex = Pattern.compile("^" + disp[0].replaceAll(" +", "\\\\s+") + "\\s*$");
		di.method = disp[1];

		String[] args = disp[2].split(",");

		if(args[0].length() == 0) {
			args = new String[0];
			di.args = new int[0];
			di.argtypes = new String[0];
		} else {
			di.args = new int[args.length];
			di.argtypes = new String[args.length];
		}
		
		for(int i=0; i<args.length; i++) {
			String[] arg = args[i].trim().split(" +");
			
			if(arg.length == 1) {
				// just the number or the type
				arg[0] = arg[0].trim();

				int number = -1;
				try {
					number = Integer.parseInt(arg[0]);
				} catch(NumberFormatException nfe) {}

				if(number == -1) {
					di.argtypes[i] = arg[0];
					di.args[i] = -1;
				} else {
					di.argtypes[i] = null;
					di.args[i] = number;
				}

			} else if(arg.length == 2) {
				arg[0] = arg[0].trim();
				arg[1] = arg[1].trim();

				// type number
				di.argtypes[i] = arg[0].length() == 0 ? null : arg[0]; // PENDING: maybe check that argtype is valid now
				
				try {
					di.args[i] = Integer.parseInt(arg[1]);
				} catch(NumberFormatException nfe) {
					throw new IllegalArgumentException("'"+arg[1]+"' is not number.");
				}
			} else {
				throw new IllegalArgumentException("Unrecognized parameter: '"+args[i]+"'");
			}
		}
		return di;
	}
}
