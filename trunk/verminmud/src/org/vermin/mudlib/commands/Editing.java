package org.vermin.mudlib.commands;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.vermin.driver.Driver;
import org.vermin.driver.Loader;
import org.vermin.driver.Persistent;
import org.vermin.io.SExpTokenizer;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.Wizard;
import org.vermin.mudlib.World;

public class Editing extends RegexCommand {

	private static Editing _instance = new Editing();
	
	protected Editing() {}
	
	public static Editing getInstance() {
		return _instance;
	}
	
	public String[] getDispatchConfiguration() {
		return new String[] {
				"edit (.+) => edit(actor, 1)",
				"call (\\w+)( (.+))? => call(actor, 1, 3)",
				"save => save(actor)",
				"list( (\\w+))? => list(actor, 2)",
				"unload( (.+))? => unload(actor, 2)" 
		};
	}
	
	
	public void save(Wizard who) {
		if(who.getEditing() == null || !(who.getEditing() instanceof MObject))
			who.notice("You aren't who.getEditing() anything.");
		
		who.notice("Saving '"+((MObject) who.getEditing()).getId()+"'.");
		((Persistent) who.getEditing()).save();
	}
		

	public void edit(Wizard who, String params) {
	
		if(params.length() == 0) {
			who.notice("Usage: edit <object_id>");
			return;
		}
		
		
		String id = params;
		
		who.notice("Fetching object '"+id+"' for who.getEditing().");
				
		who.setEditing(World.get(id));
	
		if(who.getEditing() == null)
			who.notice("Unable to fetch object for who.getEditing().");
		else
			who.notice("EDIT (id: "+id+", obj: "+who.getEditing()+")");
	}

	public void call(Wizard who, String method, String params) {
		if(who.getEditing() == null) {
			who.notice("You are not who.getEditing() anything.");
		} else if(params.length() == 0) {
			who.notice("Usage: call <method> [param1 ... paramN]");
		} else {
		
			
			// FIXME
			String[] data = tokenifyParams(params);

			Method m = findMethod(who.getEditing().getClass(), 
										 method, data.length);
			
			if(m == null) {
				who.notice("No matching method was found in class "+cn(who.getEditing().getClass())+".");
				return;
			}
			
			Object[] args = makeParams(m.getParameterTypes(), data);
			Object ret = null;
			
			try {
				ret = m.invoke(who.getEditing(), args);
			} catch(Exception e) {
				who.notice("Unable to invoke method. "+e.getMessage());
				e.printStackTrace(System.out);
			}
			
			if(ret != null)
				who.notice(m.getName()+": "+ret.toString());
			else
				who.notice(m.getName()+" DONE");
		}
	}
	
	private String[] tokenifyParams(String p) {
		SExpTokenizer tokenizer = new SExpTokenizer(new StringReader(p));
		ArrayList<String> al = new ArrayList<String>();
		try {
			Object token = tokenizer.nextToken();
			while(token != SExpTokenizer.STREAM_END) {
				al.add(token.toString());
				token = tokenizer.nextToken();
			}
		} catch(IOException ioe) {
			// should not happen when reading from string data
		}
		
		return al.toArray(new String[al.size()]);
	}

	public void list(Wizard who, String params) {
		if(params.length() == 0) {
			who.notice("Usage: list <methods | fields | interfaces>");
		} else if(who.getEditing() == null) {
			who.notice("No object is currently being edited.");
		} else if(params.equals("methods")) {
		
			StringBuffer sb = new StringBuffer();
			sb.append("Listing methods in class "+cn(who.getEditing().getClass())+":\n");
			
			Method[] m = who.getEditing().getClass().getMethods();
			
			for(int i=0; i<m.length; i++) {
				sb.append(cn(m[i].getReturnType()) + " ");
				sb.append(m[i].getName()+"(");
				
				Class<?>[] p = m[i].getParameterTypes();
				for(int j=0; j<p.length; j++) {
					sb.append(cn(p[j]));
					if(j < p.length-1) sb.append(", ");
				}
				sb.append(")\n");
			}
			
			who.notice(sb.toString());
		} else if(params.equals("fields")) {
			StringBuffer sb = new StringBuffer();
			sb.append("Listing public fields in class "+cn(who.getEditing().getClass())+":\n");
			
			Field[] f = who.getEditing().getClass().getFields();
			
			for(int i=0; i<f.length; i++) {
				sb.append(cn(f[i].getType()) + " "+f[i].getName()+"\n");
			}
			
			who.notice(sb.toString());
		
		} else if(params.equals("interfaces")) {
		
			StringBuffer sb = new StringBuffer();
			
			sb.append("Listing implemented interfaces for class "+cn(who.getEditing().getClass())+":\n");
			
			Class<?>[] iface = who.getEditing().getClass().getInterfaces();
			
			for(int i=0; i<iface.length; i++) {
				sb.append(cn(iface[i]));
				if(i < iface.length-1) sb.append(", ");
			}
			sb.append(".\n");
			who.notice(sb.toString());
		
		}
		
	}
	
	/* Return class name */
	private String cn(Class<?> c) {
		return c.getName();
	}
	
	/* Find a method matching given name and parameter count.
	 * If many methods match, the first one is returned.
	 */
	private Method findMethod(Class<?> c, String name, int paramCount) {
		Method[] m = c.getMethods();
		
		for(int i=0; i<m.length; i++) {
			if(m[i].getName().equalsIgnoreCase(name) &&
				m[i].getParameterTypes().length == paramCount)
				return m[i];
		}
		
		return null;
	}
		
	/* Create typed parameters from string data and return them
	 * as an object array.
	 */
	private Object[] makeParams(Class<?>[] types, String[] data) {
		if(types.length != data.length)
			return null;
			
		Object[] ar = new Object[types.length];
		
		for(int i=0; i<types.length; i++) {
			/* Convert primitive types */
			if(types[i].isPrimitive())
				ar[i] = makePrimitive(types[i].getName(), data[i]);
			
			/* Is it assignable from string */
			else if(types[i].isAssignableFrom(data[i].getClass()))
				ar[i] = data[i];
				
			/* Try to fetch objects from repository */
			else
				ar[i] = World.get(data[i]);
		
		}
		
		return ar;
	}
	
	private Object makePrimitive(String type, String data) {
		Object ret = null;
		
		/* HACK (fix mudclient... REMOVE streamtokenizer) */
		/*	if(!type.equals("double") && !type.equals("float")) {
			data = data.substring(0, data.indexOf('.'));
			}
			System.out.println("DATA "+data);
		*/
		
		try {
			if(type.equals("int")) {
				Integer val = new Integer(data);
				Method m = val.getClass().getMethod("intValue");
				ret = m.invoke(val);
			} else if(type.equals("float")) {
				Float val = new Float(data);
				Method m = val.getClass().getMethod("floatValue");
				ret = m.invoke(val);
			} else if(type.equals("double")) {
				Double val = new Double(data);
				Method m = val.getClass().getMethod("doubleValue");
				ret = m.invoke(val);
			} else if(type.equals("short")) {
				Short val = new Short(data);
				Method m = val.getClass().getMethod("shortValue");
				ret = m.invoke(val);
			} else if(type.equals("long")) {
				Long val = new Long(data);
				Method m = val.getClass().getMethod("longValue");
				ret = m.invoke(val);
			} else if(type.equals("byte")) {
				Integer val = new Integer(data);
				Method m = val.getClass().getMethod("intValue");
				ret = m.invoke(val);
			} else if(type.equals("char")) {
				Character val = new Character(data.charAt(0));
				Method m = val.getClass().getMethod("charValue");
				ret = m.invoke(val);
			} 
		} catch(Exception e) {
			throw new IllegalArgumentException("Can't convert parameters. "+cn(e.getClass())+": "+e.getMessage());
		}
		return ret;
	}

	public void unload(Wizard who, String what) {

		if(what == null) {
			who.notice("Usage: unload <#room | id>");
			return;
		}

		Loader l = Driver.getInstance().getLoader();

		if(what.equals("#room")) {
			who.notice("Reloading current room.");

			String id = who.getRoom().getId();
			l.unload(id);

			Room r = (Room) World.get(id);
			r.add(who);

		} else {
			who.notice("Reloading object: "+what);
			l.unload(what);
		}
	}

}
