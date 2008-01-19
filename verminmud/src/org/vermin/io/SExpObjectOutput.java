/* SExpObjectOutput.java
 * 15.6.2002 Tatu Tarvainen /  Council 4
 *
 * Serialize objects as S-Expressions.
 */
package org.vermin.io;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.vermin.driver.Transient;
import org.vermin.io.Executable.Dynvars;

public class SExpObjectOutput {

   private PrintWriter out;

	private Map<Object,Integer> cache = new HashMap<Object,Integer>();
	private int objectIndex;

   private int indent;
   public SExpObjectOutput(OutputStream out) throws IOException {
      this.out = new PrintWriter(out);
      indent = 0;
   }
   
   public SExpObjectOutput(PrintWriter out) throws IOException {
	      this.out = out;
	      indent = 0;
	   }

   public void serialize(Object obj) {
		cache.clear();
		objectIndex=0;
      out.println(writeObject(obj));
		out.flush();
   }

	protected Stack parent = new Stack();

	private int debugIndent = 0;

   private String writeObject(Object obj) {
      if(obj == null) {
         // debug("DEBUG: Null object");
         return "null";
      } 
		
		String result;

		debugIndent += 2;
		debug("Writing object: "+obj);

		if(cache.containsKey(obj)) {
			result = writeAt(cache.get(obj));
		} else if(obj instanceof Class) {
			debug("  -- class");
			result = writeClass((Class) obj);
		} else if(obj.getClass().isPrimitive() || isWrapper(obj.getClass())) {
			debug("  -- primitive");
			result = writePrimitive(obj);
		} else if(obj instanceof String) {
			debug("  -- String");
			result = quoteString(obj.toString());
		} else if(obj.getClass().isArray()) {
			debug("  -- array");
			result = writeArraySequence(obj);
		} else if(obj instanceof Vector) {
			debug("  -- Vector");
			result = writeSequence((Iterable) obj);
		} else if(obj instanceof Map) {
			debug("  -- Map");
			result = writeMap((Map) obj);
		} else if(obj.getClass().isEnum()) {
			debug("  -- Enum");
			result = writeEnum((Enum) obj);
		} else if(obj instanceof EnumSet) {
			debug("  -- EnumSet");
			result = writeEnumSet((EnumSet) obj);
		} else if(obj instanceof Iterable) {
			debug("  -- Set");
			result = writeSequence((Iterable) obj);
			//return writeSet((Set) obj);
		} else if(obj instanceof Date) {
			debug("  -- date");
			result = writeDate((Date) obj);
		} else if(parent.search(obj) >= 2) {
			
			debug("  -- parent");
			int parentDepth = parent.search(obj) - 1;
			result = "(parent)";
		} else {
			
			debug("  -- regular object");
			parent.push(obj);
			
			String ret;
			Executable func = getAugmentedFunction(obj);
			if(func != null) {
				LinkedList ll = new LinkedList();
				ll.add(obj);
				ret = getAugmentedFunction(obj).execute(ll, new Dynvars()).toString();
			} else {
				ret = writeOrdinaryObject(obj);
			}
			
			parent.pop();
			result = ret;
		}
		
		debugIndent -= 2;
		return result;
   }
	
	private String writeAt(int index) {
		return "(@ "+index+")";
	}

	private String quoteString(String str) {
		if(str.indexOf('"') != -1)
			return "#"+str.length()+":"+str;
		else
			return "\""+str+"\"";
	}

	/**
	 * Subclasses can override this method to augment
	 * the list of type specific write functions.
	 * The default implementation just returns <code>null</code>.
	 *
	 * @param obj the object to get the augmented function for
	 */
	protected Executable getAugmentedFunction(Object obj) {
		return null;
	}

   private boolean isWrapper(Class cls) {
      if(cls == Integer.class ||
            cls == Long.class ||
            cls == Double.class ||
            cls == Float.class ||
            cls == Short.class ||
            cls == Byte.class ||
            cls == Character.class ||
            cls == Boolean.class) {
         return true;
      } else {
         return false;
      }
   }

	private String writeDate(Date date) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return "(date \""+df.format(date)+"\")";
	}

   private String writePrimitive(Object obj) {
		return obj.toString();
   }


   private String writeClass(Class cls) {
      return "(class \""+cls.getName()+"\")";
   }

	private String writeArraySequence(Object arr) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		indent++;

      for(int i=0; i<Array.getLength(arr); i++) {
         sb.append(" "+writeObject(Array.get(arr, i)));
      }
		sb.append("]");
		indent--;
		return sb.toString();
	}

	private String writeSequence(Iterable sequence) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		indent++;

		for(Object obj : sequence) {
			if(isTransient(obj)){
				System.out.println("SKIPPING TRANSIENT OBJECT: "+obj);
				continue;
			}
				
			sb.append(writeObject(obj));
			sb.append(" ");
		}
		sb.append("]");
		indent--;
		return sb.toString();
	}
	
	private boolean isTransient(Object o) {
		return o != null && o.getClass().getAnnotation(Transient.class) != null;
	}
	
   private String writeMap(Map m) {
	   
	   if(m instanceof EnumMap) return writeEnumMap((EnumMap) m);
	   
      Iterator it = m.keySet().iterator();
      StringBuffer sb = new StringBuffer();
      //sb.append("(map \""+m.getClass().getName()+"\"");
		sb.append("{");
      indent++;

      while(it.hasNext()) {
         Object key = it.next();
         Object entry = m.get(key);
         
         if(isTransient(entry)) {
        	 System.out.println("SKIP TRANSIENT MAP ENTRY: "+entry);
        	 continue;
         }
         
         // debug("Key: "+key+" (Class: "+key.getClass().getName()+")");
         // debug("Value: "+entry+" (Class: "+entry.getClass().getName()+")");
         sb.append("\n");
         indent(sb);
         sb.append(writeObject(key));
			sb.append(": ");
			sb.append(writeObject(entry));
			sb.append("\n");
      }
      sb.append("}");
      indent--;
      return sb.toString();
   }
   
   private String writeSet(Set s) {
      Iterator it = s.iterator();
      StringBuffer sb = new StringBuffer();
      //sb.append("(set \""+s.getClass().getName()+"\"");
		sb.append("[");
      indent++;

      while(it.hasNext()) {
         Object member = it.next();
         if(isTransient(member)) {
        	 System.out.println("SKIP TRANSIENT SET ENTRY: "+member);
        	 continue;
         }
         indent(sb);
         sb.append(writeObject(member));
			if(it.hasNext()) sb.append(" ");
      }
      sb.append("]");
      indent--;
      return sb.toString();      
   }
   
   private String writeEnumMap(EnumMap m) {
	   StringBuilder sb = new StringBuilder();
	   sb.append("{\n");
	   indent+=2;
	   
	   for(Object key : m.keySet()) {
		   Object value = m.get(key);
		   if(isTransient(value)) continue;
		
		   indent(sb);
		   sb.append("\"");
		   sb.append(((Enum)key).name());
		   sb.append("\": ");
		   sb.append(writeObject(m.get(key)));
		   sb.append('\n');
		
	   }
	   indent-=2;
	   indent(sb);
	   sb.append('}');
	   return sb.toString();
   }
   
   private String writeEnumSet(EnumSet s) {
	   StringBuilder sb = new StringBuilder();
	   sb.append("[");
	   for(Object e : s)
		   sb.append("\""+((Enum)e).name()+"\" ");
	   sb.append("]");
	   return sb.toString();
   }
   
  /* private String writeEnumSet(EnumSet s) {
      Class eType;
      if(s.isEmpty()) {
         eType = EnumSet.complementOf(s).iterator().next().getClass();
      } else {
         eType = s.iterator().next().getClass();
      }
      
      StringBuffer sb = new StringBuffer();
      sb.append("(enumset \""+eType.getName()+"\"");
      indent++;

      Iterator it = s.iterator();
      while(it.hasNext()) {
         sb.append("\n");
         sb.append(writeObject(it.next()));
      }
      sb.append(")");
      indent--;
      return sb.toString();      
   }*/
   
   private String writeEnum(Enum e) {
		// ENUM can be written just as a "CONSTANT_NAME" string value,
		// but we need to be sure that the field is parameterized when loading it
		// or we don't know how to coerce the string back to an Enum
		
      StringBuffer sb = new StringBuffer("(enum \""+e.getDeclaringClass().getName()+"\" \""+e.name()+"\")");
      indent(sb);
      return sb.toString();
   }

	
	private void debug(String msg) {
		/*	
		for(int i=0; i<debugIndent; i++)
			System.out.print(" ");
		System.out.println(msg.trim());
		*/

	}

   private String writeOrdinaryObject(Object obj) {

		cache.put(obj, ++objectIndex);

      StringBuffer sb = new StringBuffer();

      int oldIndent = indent;

      indent += 2;
      sb.append("\n");
      indent(sb);
      sb.append("(object \""+obj.getClass().getName()+"\" "+objectIndex);
      indent += 2;
      
      Field[] fields = getSerializableFields(obj);
      for(int i=0; i<fields.length; i++) {
          debug("Field: "+fields[i].getName());
         try {
				Object fieldValue = fields[i].get(obj);
				if(fields[i].getName().equals("parent"))
					debug("parent stack: "+parent+" (obj: "+fieldValue+") (pos: "+parent.search(fieldValue)+")");
            sb.append("\n");
            indent(sb);
            sb.append("(field "+fields[i].getName()+" "+writeObject(fieldValue)+")");
         } catch(IllegalAccessException iae) {
            debug("Serialization of field "+fields[i].getName()+" failed: "+iae.getMessage());
         }
      }
      
      sb.append(")");

      indent = oldIndent;

      return sb.toString();
   }

   private void indent(StringBuilder sb) {
      for(int i=0; i<indent; i++) sb.append(" ");
   }
   private void indent(StringBuffer sb) {
	      for(int i=0; i<indent; i++) sb.append(" ");
   }
	   
   private void println(StringBuffer sb, String str) {
      for(int i=0; i<indent; i++) sb.append(" ");
      sb.append(str+"\n");
   }

   public void close() throws IOException {
      out.close();
   }

   private Field[] getSerializableFields(Object o) {

      Vector fields = new Vector();
      Class cls = o.getClass();
      while(cls != null) {
         // // debug("Traversing class: "+cls.getName());
         Field[] f = cls.getDeclaredFields();
         for(int i=0; i<f.length; i++) {
            if(!Modifier.isTransient(f[i].getModifiers()) && 
                  !Modifier.isStatic(f[i].getModifiers())) {
               fields.add(f[i]);
            }
         }
         cls = cls.getSuperclass();
      }

      Field[] sFields = (Field[]) fields.toArray(new Field[0]);
      Field.setAccessible(sFields, true);
      return sFields;
   }

	/* FOR TESTING PURPOSES */

	/*
	private static class Parent {

		public Child child;

	};

	private static class Child {

		public Parent parent;

	};

	public static void main(String[] args) {

		Parent p = new Parent();
		Child c = new Child();

		p.child = c;
		c.parent = p;

		try {
			FileOutputStream fos = new FileOutputStream("save");
			//new SExpObjectOutput(fos).serialize(p);
			// debug("-----\n"+new SExpObjectOutput(fos).writeObject(p)+"\n----");
			fos.close();
		} catch(Exception e) {
			// debug("EXCEPTION: "+e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	*/
}
