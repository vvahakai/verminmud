/**
 * DefaultLoader.java
 * 13.7.2003 Tatu Tarvainen / Council 4
 *
 * Default loader implementation.
 * Implements cache and augmentable loading.
 */
package org.vermin.driver;

import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultLoader implements Loader {

	private static Logger log = Logger.getLogger(DefaultLoader.class.getName());
	
	/* Registered loaders */
	private LinkedList loaders;

	/* Loaded prototypes */
	private WeakHashMap prototypes;
	
	public DefaultLoader() {
		loaders = new LinkedList();
		prototypes = new WeakHashMap();
	}

	public void unload(String path) {
		path = path.replace('\\', '/');
		prototypes.remove(path);
	}

	public Prototype load(String path) throws Exception {
		
		path = path.replace('\\', '/');

		Prototype p = (Prototype) prototypes.get(path);
		if(p != null) return p;

		LinkedList<LoadException> errors = new LinkedList();
		
		Iterator it = loaders.listIterator();
		while(it.hasNext()) {

			Loader l = (Loader) it.next();
			try {
				p = l.load(path);

				if(p != null) {
                    prototypes.put(path, p);
                    p.setId(path);
					return p;
                }
			} catch(LoadException le) {
				/*String msg = "Unable to load '"+path+"': "+le.getMessage();
				log.severe(msg);
				World.logDevel(msg);*/
				errors.add(le);
			}
		}

		log.severe("Unable to load '"+path+"':");
		for(LoadException e : errors) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		throw new LoadException("UNABLE TO LOAD: "+path);
	}
	
	public Object get(String path) throws Exception {
		return load(path).get();
	}

	public void addLoader(Loader l) {
		loaders.add(l);
	}

	public boolean isLoaded(String path) {
		return prototypes.containsKey(path);
	}
}
