/**
 * AbstractPrototype.java
 * 4.12.2004
 *
 * Contains an abstract prototype implementation,
 * which provides basic prototype methods.
 */
package org.vermin.driver;

import java.io.File;
import java.util.HashMap;

import org.vermin.io.SExpObjectInput;

public abstract class AbstractPrototype implements Prototype {

	private Object UNIQUE = new Object();
	
	/* The file this prototype was loaded from */
	private File source;
	
	/* Is this prototype unique? */
	private boolean unique;

	/* Name of the prototype */
	private String name;

	/* Map of instances (null if unique) */
	private HashMap instances; //FIXME: make these soft refs?
	
	/* The S-Expression that creates the object */
	private SExpObjectInput.SExp sexp;

	/* Counter for instance name */
	private int instanceCount = 0;
	
	protected String id;

	public AbstractPrototype() {}

	public AbstractPrototype(File source, boolean unique, String name) {
		
		this.source = source;
		this.unique = unique;
		this.name = name;
		
		instances = new HashMap();

	}

	public void setId(String id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be set to null");
		this.id = id;
	}

	/**
	 * Gets the unique instance.
	 */
	public Object get() {
		Object p = instances.get(UNIQUE);
		if(p == null) {
			p = makeObject();
			instances.put(UNIQUE, p);
			PrototypeUtils.startObject(p);
			if(p instanceof Persistent)
			    ((Persistent)p).setId(id);
		}

		if(p instanceof Persistent)
			((Persistent) p).setAnonymous(false);
		return p;
	}

	/**
	 * Get a named instance.
	 */
	public Object get(String name) {
		Object p = instances.get(name);
		if(p instanceof Persistent)
			((Persistent) p).setAnonymous(true);
		return p;
	}

	/**
	 * Create a new instance. It will be named with
	 * an increasing index.
	 */
	public Object create() {
		if(unique)
			throw new PrototypeException("Tried to create instance of a unique prototype.");
		Object instance = makeObject();
		PrototypeUtils.startObject(instance);
		if(instance instanceof Persistent)
		    ((Persistent)instance).setId(id);
		instances.put(Integer.toString(++instanceCount), instance);
		if(instance instanceof Persistent)
			((Persistent) instance).setAnonymous(true);
		return instance;
	}

	/**
	 * Create a named instance. The name must not
	 * be a number (it might clash with the 'anonymous' 
	 * instances).
	 */
	public Object create(String name) {
		if(unique)
			throw new PrototypeException("Tried to create instance of a unique prototype.");
		Object instance = makeObject();
		PrototypeUtils.startObject(instance);
        if(instance instanceof Persistent)
            ((Persistent)instance).setId(id);
		instances.put(name, instance);
		if(instance instanceof Persistent)
			((Persistent) instance).setAnonymous(true);
		return instance;
	}

	public File getSource() {
		return source;
	}

	public boolean isUnique() {
		return unique;
	}

	public String getName() {
		return name;
	}

	/**
	 * Called when an object is created.
	 * Subclasses must provide a meaningful implementation
	 * of this method.
	 */
	protected abstract Object makeObject();

	protected void setSource(File source) {
		this.source = source;
	}
	protected void setUnique(boolean unique) {
		this.unique = unique;
	}
	protected void setName(String name) {
		this.name = name;
	}
}
