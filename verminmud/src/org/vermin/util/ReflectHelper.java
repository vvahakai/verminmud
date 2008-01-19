/* ReflectHelper.java
 * 3.6.2002 Tatu Tarvainen
 *
 * Helper functions for reflective class/method/field access.
 */
package org.vermin.util;

import java.lang.reflect.*;

import java.util.*;
import java.io.*;
import java.net.*;

public class ReflectHelper
{

	private static class ClassEntry {
		public long loadTime;
		public Class clazz;
	};

	private static HashMap<String, ClassEntry> classes = new HashMap(); // reloadable classes
	
	private static HashSet<String> reloadablePackages = new HashSet();

	private static class ReloadableClassLoader extends ClassLoader {
		
		
		public URL getResource(String name) {
			return ClassLoader.getSystemResource(name);
		}
      
		public InputStream getResourceAsStream(String name) {
			return ClassLoader.getSystemResourceAsStream(name);
		} 
		
		protected Class findClass(String name) throws ClassNotFoundException {
			
			/**
			 * LOAD a fresh version of any modified classes that are
			 * referred to....
			 * ...
			 * This must be done in order to reload inner classes and 
			 * other non-core classes.
			 * For example a summon familiar skill class might need to reload
			 * the familiar living class...
			 *  myös viitatut luokat, jotka ladataan uudelleen mäpätään classes tauluun
			 *
			 *
			 * mud core classes must never be reloaded (vermin.mudlib & vermin.driver).
			 *
			 * override loadClass
			 */

			try {
				InputStream in = getResourceAsStream(name);
				byte[] data = new byte[in.available()];
				in.read(data);
				in.close();
				
				return defineClass(name, data, 0, data.length);
			} catch(IOException ioe) {
				System.out.println("UNABLE TO LOAD CLASS, IO Exception: "+ioe.getMessage());
				throw new ClassNotFoundException();
			}
		}

		public Class defineReloadable(String name, byte[] data) {
			return defineClass(name, data, 0, data.length);
		}
	}

	
   public static Hashtable primitives = new Hashtable();
   static {
      primitives.put("int", Integer.TYPE);
      primitives.put("long", Long.TYPE);
      primitives.put("float", Float.TYPE);
      primitives.put("double", Double.TYPE);
      primitives.put("short", Short.TYPE);
      primitives.put("byte", Byte.TYPE);
      primitives.put("char", Character.TYPE);
      primitives.put("boolean", Boolean.TYPE);
   }
      
	public static void addReloadablePackage(String name) {
		reloadablePackages.add(name);
	}

	private static boolean isReloadable(String name) {
		int last = name.lastIndexOf('.');
		if(last == -1)
			return false;
		else
			return reloadablePackages.contains(name.substring(0, last));
	}

	private static Class findReloadableClass(String name) {

		File classFile = new File("build", name.replace(".", "/")+".class");
		System.out.println("Loading class: "+classFile);
		
		ClassEntry ce = classes.get(name);
		
		if(ce != null) {
			if(classFile.lastModified() <= ce.loadTime)
				return ce.clazz;
		} else
			ce = new ClassEntry();
		
		try {
			FileInputStream fis = new FileInputStream(classFile);
			byte[] data = new byte[(int) classFile.length()];
			fis.read(data);
			fis.close();
			
			ReloadableClassLoader rcl = new ReloadableClassLoader();
			ce.loadTime = System.currentTimeMillis();
			ce.clazz = rcl.defineReloadable(name, data);
			classes.put(name, ce);
			return ce.clazz;
		} catch(IOException ioe) {
			System.out.println("UNABLE TO FIND CLASS, IO Exception: "+ioe.getMessage());
			return null;
		}
	}

   /* findClass tries to find a named class.
    */
   public static Class findClass(String name) {

		if(isReloadable(name)) {
			return findReloadableClass(name);
		} else {

			// Check if it is a primitive type.
			Class c = (Class) primitives.get(name);
			if(c != null)
				return c;
			
			// Try to find the proper class
			try {
				return Class.forName(name);
			} catch(ClassNotFoundException cnf) {
				throw new RuntimeException("No such class: "+name);
			}
			/* NOTE: Where should we search, if the name is not
			 * a fully qualified class name. Perhaps have another
			 * method that accepts a list of imports?
			 */
		}
   }

   /* Convert obj to the class type and return the
    * new value.
    */
   public static Object convert(Object obj, Class type) {
      if(type.isAssignableFrom(obj.getClass())) {
         /* Convert to primitive type */
         return obj;
      } else if(type.isPrimitive()) {
         /* Convert primitive types */
         return makePrimitive(type.getName(), obj.toString());
      } else {
         throw new RuntimeException("Incompatible classes: "+type.getName()+" & "+obj.getClass().getName());
      }
   }

   /* Find a method matching given name and parameter count.
	 * If many methods match, the first one is returned.
	 */
	public static Method findMethod(Class c, String name, int paramCount) {
		Method[] m = c.getMethods();
		
		for(int i=0; i<m.length; i++) {
			if(m[i].getName().equalsIgnoreCase(name) &&
				m[i].getParameterTypes().length == paramCount)
				return m[i];
		}
		return null;
	}

	public static Object makePrimitive(String type, String data) {
		Object ret = null;
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
				Byte val = new Byte(data);
				Method m = val.getClass().getMethod("byteValue");
				ret = m.invoke(val);
			} else if(type.equals("char")) {
				Character val = new Character(data.charAt(0));
				Method m = val.getClass().getMethod("charValue");
				ret = m.invoke(val);
			} else if(type.equals("boolean")) {
            Boolean val = new Boolean(data);
            Method m = val.getClass().getMethod("booleanValue");
            ret = m.invoke(val);
         }
		}
		catch(Exception e) {} 
		return ret;
	}

   public static Constructor findConstructor(Class c, int paramCount) {
		Constructor[] m = c.getConstructors();
		for(int i=0; i<m.length; i++) {
			if(m[i].getParameterTypes().length == paramCount)
				return m[i];
		}
		return null;
   }

   /* Create a blank instance for the given classname. */
   public static Object makeInstance(Object cls) {
		Class c = null;
		if(cls instanceof Class)
			c = (Class) cls;
		else
			c = findClass(cls.toString());

      try {
			return c.newInstance();
      } catch(InstantiationException ins) {
         throw new RuntimeException("Constructor is abstract: "+ins.getMessage());
      } catch(IllegalAccessException iae) {
         throw new RuntimeException("Illegal access: "+iae.getMessage());
      }
   }

   /* Set a field on an object. */
   public static void setField(Object obj, String field, Object value) {
      try {
         Field f = findField(obj, field);
			if(f == null)
				throw new RuntimeException("No field name '"+field+"' in class "+obj.getClass().getName());
         f.setAccessible(true);
         f.set(obj, value);
      } catch(IllegalAccessException ia) {
         throw new RuntimeException("Illegal access.");
      }
   }

	/* Replace field value with another in any field. */
	public static void replaceValue(Object obj, Object oldValue, Object newValue) {
		
		try {
			Iterator it = allFields(obj).listIterator();
			while(it.hasNext()) {
				
				Field f = (Field) it.next();
				f.setAccessible(true);
				Object test = f.get(obj);
				if(test == oldValue)
					f.set(obj, newValue);
			}
		} catch(IllegalAccessException ia) {
			throw new RuntimeException("Illegal access.");
		}
	}

	public static LinkedList allFields(Object obj) {
		LinkedList fields = new LinkedList();

		Class c = obj.getClass();
		while(true) {
			
         Field[] f = c.getDeclaredFields();
         for(int i=0; i<f.length; i++) {
				fields.add(f[i]);
         }
			
         c = c.getSuperclass();
         if(c == null) {
            return fields;
         }
      }
	}

   /* Find a named field in an object. */
   public static Field findField(Object obj, String field) {
      Class c = obj.getClass();
		return findField(c, field);
	}

	public static Field findField(Class c, String field) {
      while(true) {
			
         Field[] f = c.getDeclaredFields();
         for(int i=0; i<f.length; i++) {
            if(f[i].getName().equals(field) &&
                  !Modifier.isTransient(f[i].getModifiers()) && 
                  !Modifier.isStatic(f[i].getModifiers())) {
              return f[i];
            }
         }

         c = c.getSuperclass();
         if(c == null) {
            return null;
         }
      }
   }
}
