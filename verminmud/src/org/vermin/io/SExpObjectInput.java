/* SExpObjectInput.java
 * 16.6.2002 Tatu Tarvainen / Council 4
 *
 * Load objects from S-Expressions.
 */
package org.vermin.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import java.lang.annotation.*;

import org.vermin.io.Executable.Dynvars;
import org.vermin.io.Executable.Parent;
import org.vermin.util.ReflectHelper;
import static org.vermin.io.SExpTokenizer.*;
                                                                                
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) @interface handler  {
	String name();
	boolean  special() default false;
}
                                                                                
public class SExpObjectInput {
	
	private static Logger log = Logger.getLogger(SExpObjectInput.class.getName());
	
	private File currentFile;
	
	private Map<Integer, Object> cache = new HashMap<Integer,Object>();
	
	public static class MethodInfo {
		public boolean special;
		public Method method;
		public MethodInfo(boolean special, Method method) {
			this.special = special;
			this.method = method;
		}
	}
	
	/* The no operation method, this is used when the first position in a list is not any function */
	public static MethodInfo NOP = new MethodInfo(true, null);
	
	/* Nested class representing an S-Expression. */
	public class SExp {
		public MethodInfo func;
		public List<Object> args;
		public int line, column;
		public SExp() {
			func = null;
			args = new ArrayList<Object>();
		}
		
		public void add(Object tok) {
			if(func == null) {
				if(tok instanceof SExp) {
					// this is a list where the first place is not a function, but another list
					func = NOP;
					args.add(tok);
				} else 
					func = getFunction(tok.toString());
				
				if(func == null)
					throw new RuntimeException("No handler for SExp function: "+tok.toString());
			}
			else
				args.add(tok);
		}
		public Object execute(Dynvars dynvars) throws Exception {
			return executeSExp(this, dynvars);
		} 
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Line: ");
			sb.append(line);
			sb.append(", Column: ");
			sb.append(column);
			sb.append(", Function: ");
			sb.append(func.method.getName());
			sb.append(", Arg count: ");
			sb.append(args.size());
			
			return sb.toString();
		}
	}
	
	public static class ExecutionException extends RuntimeException {
		private SExp failure;
		public ExecutionException(SExp failure, Exception cause) {
			super(cause);
			this.failure = failure;
		}
		public SExp getFailure() {
			return failure;
		}
		@Override
		public String getMessage() {
			return failure.toString();
		}
	}
	
	int indent = 0;
	private Object executeSExp(SExp sexp, Dynvars dynvars) throws ExecutionException {
		
		indent++;
		//System.out.println(sexp.func.method.getName()+" ("+indent+")");
		
		List<Object> pArgs = null;
		Dynvars d = new Dynvars();
		d.parent = dynvars.parent;
		
		MethodInfo func = sexp.func;
		
		if(func == NOP) {
			throw new IllegalStateException("Cannot execute NOP");
		}
		
		if(!func.special) {
			pArgs = new LinkedList<Object>();
			ListIterator<Object> it = sexp.args.listIterator();
			while(it.hasNext()) {
				Object arg = it.next();
				if(arg instanceof SExp) {
					pArgs.add(executeSExp((SExp) arg, d));
				} else {
					pArgs.add(arg);
				}
			}
		} else {
			pArgs = new ArrayList<Object>(sexp.args);
		}
		
		Object value = null;
		try {
			value = func.method.invoke(this, pArgs, dynvars);
		} catch(Exception e) {
			if(e instanceof ExecutionException)
				throw (ExecutionException) e;
			throw new ExecutionException(sexp, e); 
		}
		
		indent--;
		return value;
	}
	
	/* Static cache of executable functions.
	 * Mapped as (String funcName, Executable func)
	 */
	private static HashMap<String, MethodInfo> functions = new HashMap();
	
	
	
	private SExpTokenizer tokenizer;
	
	
	/* file for the stream (or null if the InputStream constructor was used) */
	private File file = null; 
	
	public SExpObjectInput(File in) throws FileNotFoundException {
		file = in;
		tokenizer = new SExpTokenizer(new BufferedReader(new FileReader(in)));
	}
	
	public SExpObjectInput(InputStream in) {
		tokenizer = new SExpTokenizer(new BufferedReader(new InputStreamReader(in)));
	}
	
	public SExpObjectInput(Reader in) {
		tokenizer = new SExpTokenizer(in);
	}
	
	
	
	/* A recursive read */
	public Object read(boolean eofOK) throws IOException {
		
		int line = tokenizer.getCurrentLine(), 
			column = tokenizer.getCurrentColumn();
		
		Object tok = tokenizer.nextToken();
		
		if(tok == STREAM_END) {
			if(eofOK)
				return STREAM_END;
			else
				throw new RuntimeException("Unexpected end of stream.");
		} else if(tok == SEXP_START) {
			return readList(line, column);
		} else if(tok == SEQUENCE_START) {
			return readSequence(line, column);
		} else if(tok == DICTIONARY_START) {
			return readDictionary(line, column);
		} else {
			return tok;
		}
	}
	
	
	protected Object eval(Object o, Dynvars d) throws Exception {
		if(o instanceof SExp)
			return executeSExp((SExp) o, d);
		else
			return o;
	}
	
	
	/* Constructs an SExp from the stream. */
	private SExp readList(int line, int column) throws IOException {
		SExp s = new SExp();
		s.line = line; 
		s.column = column;
		Object tok;
		while((tok = read(false)) != SEXP_END) {
			s.add(tok);
		}
		return s;
	}
	
	private SExp readDictionary(int line, int column) throws IOException {
		
		SExp s = new SExp();
		s.line = line;
		s.column = column;
		s.add("dictionary");
		
		Object tok;
		while((tok = read(false)) != DICTIONARY_END) {
			
			Object key = tok;
			if(read(false) != DICTIONARY_SEPARATOR)
				throw new RuntimeException("Garbage in dictionary, expected separator after key.");
			Object value = read(false);
			
			s.add(key);
			s.add(value);
		}
		return s;
	}
	
	private SExp readSequence(int line, int column) throws IOException {
		
		SExp s = new SExp();
		s.line = line;
		s.column = column;
		s.add("sequence");
		
		Object tok;
		while((tok = read(false)) != SEQUENCE_END) {
			s.add(tok);
		}
		return s;
	}
	
	public Object deserialize() throws Exception {
		SExp s = (SExp) read(false);
		cache.clear();
		Object value = s.execute(new Dynvars());
		return value;
	}
	
	/**
	 * Read an S-Expression and return it.
	 * This is usefull if you want to create the object later
	 * or many times and use the S-Exp as template.
	 */
	public SExp loadSExpression() {
		try {
			return (SExp) read(false);
		} catch(IOException io) {
			// System.out.println("IOException: "+io.getMessage());
			io.printStackTrace();
			return null;
		}
	}
	
	public void close() throws IOException {
		tokenizer.close();
	}
	
	/**
	 * Get a named function.
	 */
	private MethodInfo getFunction(String name) {
		MethodInfo func = getAugmentedFunction(name);
		if(func == null)
			func = functions.get(name);
		
		return func;
	}
	
	/**
	 * Subclasses can override this method to augment
	 * the list of functions understood by S-Expression
	 * evaluation.
	 * The default implementation just returns <code>null</code>.
	 *
	 * @param name the name of the function
	 * @return the <code>Executable</code> or <code>null</code>.
	 */
	protected MethodInfo getAugmentedFunction(String name) {
		return null;
	}
	
	/* Coerce value to type */
	private static Object coerce(Object value, Class type) {
		if(type == null) {
			return value;
		} else if(value == null) {
			return null;
		}
		
		//System.out.println("COERCE value of type "+value.getClass().getName()+" TO type "+type.getName());
		if(type.isPrimitive()) {
			return ReflectHelper.makePrimitive(type.getName(), value.toString());
		} else if(type.isEnum()) {
			if(value.getClass().isEnum())
				return value;
			else {
				// System.out.println("ENUM "+type+" VALUE "+value);
				return Enum.valueOf(type, value.toString());
			}
		} else if(type.isAssignableFrom(value.getClass())) {
			return value;
		} else if(value instanceof String && isWrapper(type)) {
			return parseWrapper((String) value, type);
		} else {
			throw new RuntimeException("Unable to coerce "+value+" to type "+type);
		}
	}
	/* returns whether given Class is a primitive wrapper */
	private static boolean isWrapper(Class c) {
		if(c == Integer.class ||
				c == Long.class ||
				c == Byte.class ||
				c == Boolean.class ||
				c == Float.class ||
				c == Double.class) {
			return true;
		}
		return false;
	}
	private static Object parseWrapper(String value, Class type) {
		if(type == Integer.class) {
			return Integer.parseInt(value);
		} else if(type == Long.class) {
			return Long.parseLong(value);
		} else if(type == Byte.class) {
			return Byte.parseByte(value);
		} else if(type == Boolean.class) {
			return Boolean.parseBoolean(value);
		} else if(type == Float.class) {
			return Float.parseFloat(value);
		} else if(type == Double.class) {
			return Double.parseDouble(value);
		}
		// should never happen
		throw new RuntimeException("parseWrapper invoked for a non-wrapper type, this should never happen");
	}
	/* Functions for S-Expression evaluation. */
	
	
	@handler(name="@") Object handleAt(List args, Dynvars dynvars) {
		Integer key = new Integer(args.get(0).toString());
		if(!cache.containsKey(key))
			throw new RuntimeException("Cache does not contain value for key "+key);
		return cache.get(key);
	}
	
	/* Function for referring to the parent */
	@handler(name="parent") Object handleParent(List args, Dynvars dynvars) {
		if(args.size() > 0)
			System.out.println("SExp WARNING: don't know how to handle (parent) with "+args.size()+" arguments");
		// System.out.println("SETTING PARENT OF "+dynvars.parent.object+" TO "+dynvars.parent.next.object);
		return dynvars.parent.next.object;
	}
	
	/* Function that creates a new object and sets it's
	 * field values.
	 */
	@handler(name="object",special=true) Object handleObject(List args, Dynvars dynvars) throws Exception {
		
		String cls = eval(args.get(0), dynvars).toString();
		Object obj = null;
		try {
			obj = ReflectHelper.makeInstance(cls);
		} catch(RuntimeException re) { 
			System.out.println("UNABLE TO INSTANTIATE OBJECT OF CLASS: "+cls);
			System.out.println("EXCEPTION: "+re);
			return null;
		}
		
		int argStart = 1;
		if(args.size() > 1 && !(args.get(1) instanceof SExp)) {
			argStart = 2;
			try {
				int ind = Integer.parseInt(args.get(1).toString());
				cache.put(ind, obj);
			} catch(NumberFormatException nfe) {}
		}
		
		Dynvars d = new Dynvars();
		d.parent = new Parent();
		d.parent.object = obj;
		d.parent.next = dynvars.parent;
		
		d.type = obj.getClass();
		
		Iterator it = args.listIterator(argStart);
		while(it.hasNext()) {
			SExp s = (SExp) it.next();
			
			// execute field with object class as a dynamic variable
			Object[] field = null;
			try {
				Object executed = s.execute(d);
				if(executed != null && executed instanceof Object[])
					field = (Object[]) executed;
			} catch(Exception re) {
				if(re instanceof ExecutionException)
					throw (ExecutionException) re;
				throw new ExecutionException(s, re);
			}
			
			if(field == null)
				continue;
			
			if(field[1] == null)
				continue; // field[1] = null;
			
			try {
				ReflectHelper.setField(obj, field[0].toString(), field[1]);
			} catch(RuntimeException re) {
				// System.out.println("Unable to set field: "+field[0]+" ("+re.getMessage()+")");
			}
		}
		
		return obj;
	}
	
	/* Function to create field setters (used inside object function)
	 */
	@handler(name="field",special=true) Object handleField(List args, Dynvars dynvars) throws Exception{
		String fieldName = eval(args.get(0), dynvars).toString();
		Object value = args.get(1);
		Object realValue = null;
		Class objectType = (Class) dynvars.type;
		Field field = ReflectHelper.findField(objectType, fieldName);
		if(field == null) {
			// System.out.println("No field named '"+fieldName+"' in class "+objectType.getName());
			return null;
		}
		
		if(value instanceof SExp) {
			Dynvars d = new Dynvars();
			d.parent = dynvars.parent;
			d.type = field.getType();
			d.genericType = field.getGenericType();
			
			SExp sv = (SExp) value;
			realValue = executeSExp(sv, d);
		} else {
			realValue = coerce(value, field.getType());
		}
		
		return new Object[] { fieldName, realValue };
		// we need to calculate it here in any
	}
	
	@handler(name="=!",special=true) Object handleFieldAbbr(List args, Dynvars dynvars) throws Exception {
		return handleField(args, dynvars);
	}
	
	/* Function that returns the file the object is being loaded from. */
	@handler(name="this-file") Object handleThisFile(List args, Dynvars dynvars) {
		return currentFile;
	}
	
	/* Function to call a method on the object being constructed.
	 * Puts dynvars so the arguments can be lists and are automatically made the right type.
	 */
	@handler(name="=>",special=true) Object handleCall(List args, Dynvars dynvars) throws Exception {
		
		Object invocationTarget = dynvars.parent.object; // the object to invoke the call on
		String methodName = eval(args.get(0), dynvars).toString();		
		int argCount = args.size()-1;
		Method method = ReflectHelper.findMethod(invocationTarget.getClass(), methodName, argCount);
		
		Object[] invocationArgs = new Object[argCount];
		Class[] argTypes = method.getParameterTypes();
		Type[] genericArgTypes = method.getGenericParameterTypes();
		Annotation[][] argAnnotations = method.getParameterAnnotations();
		for(int i=0; i<args.size()-1; i++) {
			Object arg = args.get(i+1);
			if(arg instanceof SExp) {
				Dynvars d = new Dynvars();
				d.parent = dynvars.parent;
				d.type = argTypes[i];
				d.genericType = genericArgTypes[i];
				invocationArgs[i] = executeSExp((SExp) arg, d);
			} else {
				invocationArgs[i] = coerce(arg, argTypes[i]);
			}
		}
		
		return method.invoke(invocationTarget, invocationArgs);
	}
	
	/* Function to call a method multiple times with different argument lists.
	 * This is just a convenience wrapper to call => many times.
	 * 
	 * Returns null every time so this is useful for calling methods that produce
	 * side effects on the object.
	 */
	@handler(name="=>*",special=true) Object handleCallStar(List args, Dynvars dynvars) throws Exception {
		
		Object methodName = args.get(0);
		for(int i=1; i<args.size(); i++) {
			ArrayList argList = new ArrayList();
			argList.add(methodName);
			argList.addAll( ((SExp) args.get(i)).args );
			handleCall(argList, dynvars);
		}
		
		return null;
	}
	
	/* Function to create a dictionary of the given type.
	 * This is meant to be used inside (field <name> ...) because
	 * it depends on the field type dynamic variable.
	 */
	@handler(name="dictionary",special=true) Object handleDictionary(List args, Dynvars dynvars) throws Exception {
		
		Class type = (Class) dynvars.type; // field type is a dynamic variable
		Type genericType = (Type) dynvars.genericType;
		
		if(type == null || type.isInterface()) { // no type or this is the Map interface
			type = HashMap.class;
		}
		
		Dynvars keyd = new Dynvars();
		keyd.parent = dynvars.parent;
		
		Dynvars vald = new Dynvars();
		vald.parent = dynvars.parent;
		
		if(genericType != null) {
			if(genericType instanceof ParameterizedType) {
				Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
				if(typeArgs[0] instanceof Class)
					keyd.type = (Class) typeArgs[0];
				if(typeArgs[1] instanceof Class)
					vald.type = (Class) typeArgs[1];
			}
		}
		
		Map map = null;
		try {
			if(type == EnumMap.class) {
				// System.out.println("CREATING EnumMap of class: "+keyd.type);
				map = new EnumMap(keyd.type);
				
			} else {
				map = (Map) type.newInstance();
			}
		} catch(InstantiationException ie) {
			throw new RuntimeException("Unable to instantiate dictionary of class: "+type.getName());
		} catch(IllegalAccessException iae) {
			throw new RuntimeException("Unable to access dictionary class: "+type.getName());
		}
		
		int count = args.size() / 2;
		Iterator it = args.iterator();
		
		for(int i=0; i<count; i++) {
			
			Object k = null, v = null;
			
			Object key = it.next();
			if(key instanceof SExp)
				k = coerce(executeSExp((SExp) key, keyd), keyd.type);
			else
				k = coerce(key, keyd.type);
			
			Object value = it.next();
			if(value instanceof SExp)
				v = coerce(executeSExp((SExp) value, vald), vald.type);
			else
				v = coerce(value, vald.type);
			
			if(k != null && v != null) // don't allow null values or keys
				map.put(k, v);
		}
		return map;
	}
	
	/* Function to create a sequence (List) or an array of the given type.
	 * This is meant to be used inside (field <name> ...) because it depends
	 * on the field type dynamic variable.
	 */
	@handler(name="sequence",special=true) Object handleSequence(List args, Dynvars dynvars) throws Exception {
		Class type = dynvars.type;
		Type genericType = dynvars.genericType;
		
		if(type == null) {
			type = java.util.ArrayList.class;
		} else if(type.isInterface()) { 
			// no type or the List interface
			if(type == java.util.Set.class)
				type = java.util.HashSet.class;
			else
				type = java.util.ArrayList.class;
		}
		
		int count = args.size();
		boolean array = type.isArray();
		Object value = null;
		
		Dynvars d = new Dynvars();
		d.parent = dynvars.parent;
		
		if(array) {
			d.type = type.getComponentType();
			value = Array.newInstance(type.getComponentType(), count);
		} else if(type == EnumSet.class) {
			Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
			// System.out.println("CREATING EnumSet of class: "+typeArgs[0]);
			value = EnumSet.noneOf((Class)typeArgs[0]);
			d.type = (Class) typeArgs[0];
			
		} else {
			try {
				value = type.newInstance();
			} catch(InstantiationException ie) {
				throw new RuntimeException("Unable to instantiate sequence of class: "+type.getName());
			} catch(IllegalAccessException iae) {
				throw new RuntimeException("Unable to access sequence class: "+type.getName());
			}
		}
		
		if(array) {
			
			Iterator it = args.iterator();
			for(int i=0; i<count; i++) {
				Object elt = it.next();
				if(elt instanceof SExp)
					Array.set(value, i, coerce(((SExp) elt).execute(d), d.type));
				else
					Array.set(value, i, coerce(elt, d.type));
			}
		} else {
			
			Iterator it = args.iterator();
			Collection l = (Collection) value;
			for(int i=0; i<count; i++) {
				Object elt = it.next();
				Object eltVal = null;
				if(elt instanceof SExp)
					eltVal = coerce(((SExp) elt).execute(d), d.type);
				else
					eltVal = coerce(elt, d.type);
				if(eltVal != null) // don't add null values
					l.add(eltVal);
			}
		}
		
		return value;
	}
	
	@handler(name="date") Object handleDate(List args, Dynvars dynvars) {
		try {
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			return df.parse(args.get(0).toString());
		} catch(ParseException pe) {
			// System.out.println("Unable to parse date '"+args.get(0)+"'. Returning epoch.");
			return new Date(0);
		}
	}
	
	/* Handlers for old-style primitives */
	@handler(name="int") Object handleP1(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("int", args.get(0).toString()); 
	}
	@handler(name="byte") Object handleP2(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("byte", args.get(0).toString()); 
	}
	@handler(name="boolean") Object handleP3(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("boolean", args.get(0).toString()); 
	}
	@handler(name="long") Object handleP4(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("long", args.get(0).toString()); 
	}
	@handler(name="short") Object handleP5(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("short", args.get(0).toString()); 
	}
	@handler(name="double") Object handleP6(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("double", args.get(0).toString()); 
	}
	@handler(name="float") Object handleP7(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("float", args.get(0).toString()); 
	}
	@handler(name="char") Object handleP8(List args, Dynvars dynvars) { 
		return ReflectHelper.makePrimitive("char", args.get(0).toString()); 
	}
	
	@handler(name="array") Object handleArray(List args, Dynvars dynvars) {
		Class cType = ReflectHelper.findClass(args.get(0).toString());
		int dimension = Integer.parseInt(args.get(1).toString());
		
		Object arr = Array.newInstance(cType, args.size()-2);
		ListIterator it = args.listIterator(2);
		int i=0;
		while(it.hasNext()) {
			Object n = it.next();
			
			/* Don't set 'null' values to avoid type mismatches. */
			if(n != null) { 
				Array.set(arr, i, n);
			}
			i++;
		}
		return arr;
	}
	
	/* Function to create vectors. */
	@handler(name="vector") Object handleVector(List args, Dynvars dynvars) {
		Vector v = new Vector();
		ListIterator it = args.listIterator();
		while(it.hasNext()) {
			v.add(it.next());
		}
		return v;
	}
	
	/* Function to create maps (Hashtable, etc...) */
	@handler(name="map") Object handleMap(List args, Dynvars dynvars) {
		Map m = (Map) ReflectHelper.makeInstance(args.get(0).toString());
		ListIterator it = args.listIterator(1);
		while(it.hasNext()) {
			Object[] entry = (Object[]) it.next();
			m.put(entry[0], entry[1]);
		}
		return m;
	}
	
	/* Function to create map entries. */
	@handler(name="mapentry") Object handleMapEntry(List args, Dynvars dynvars) {
		Object[] entry = new Object[2];
		entry[0] = args.get(0);
		entry[1] = args.get(1);
		return entry;
	}
	
	/* Function to create a set. */
	@handler(name="set") Object handleSet(List args, Dynvars dynvars) {
		Set s = (Set) ReflectHelper.makeInstance(args.get(0).toString());
		ListIterator it = args.listIterator(1);
		while(it.hasNext()) {
			s.add(it.next());
		}
		return s;
	}
	
	/* Function to create an enum set. */
	@handler(name="enumset") Object handleEnumSet(List args, Dynvars dynvars) {
		EnumSet s;
		Class eType = ReflectHelper.findClass(args.get(0).toString());
		s = EnumSet.noneOf(eType);
		
		ListIterator it = args.listIterator(1);
		while(it.hasNext()) {
			s.add(it.next());
		}
		return s;
	}
	
	/* Function to create an enum. */
	@handler(name="enum") Object handleEnum(List args, Dynvars dynvars) {
		Class eType = ReflectHelper.findClass(args.get(0).toString());
		Enum e;
		e = Enum.valueOf(eType, args.get(1).toString());
		
		return e;
	}
	
	/* Function to create a linked list. */
	@handler(name="list") Object handleList(List args, Dynvars dynvars) {
		List ll = new LinkedList();
		ListIterator it = args.listIterator(0);
		while(it.hasNext()) {
			ll.add(it.next());
		}
		return ll;
	}
	
	static {
		
		// Create static handler map
		populateHandlerMap(SExpObjectInput.class, functions);
		
	}
	
	public static void populateHandlerMap(Class cls, Map<String,MethodInfo> functions) {
		Method[] methods = cls.getDeclaredMethods();
		for(Method m : methods) {
			java.lang.annotation.Annotation[] a = m.getAnnotations();
			if(a.length == 1 &&
					a[0] instanceof handler) {
				
				handler h = (handler) a[0];
				
				String name = h.name();
				MethodInfo mi = new MethodInfo(h.special(), m);
				
				functions.put(name, mi);
			}
		}
	}
	
}
