/**
 * ClassMapping.java
 * 6.12.2004 Tatu Tarvainen
 *
 * Provides a mapping of concepts to classnames and vice versa.
 * This is used in the de/serialization engine to hide concrete
 * classnames thus making saved files more resistant to changes
 * in the class hierarchy.
 */
package org.vermin.driver;

import java.util.*;

public class ClassMapping {

	// map concepts to classes
	private HashMap<String,String> classes = new HashMap<String, String>();

	// map classes to concepts
	private HashMap<String,String> concepts = new HashMap<String, String>();

	public String getClassForConcept(String concept) {
		return classes.get(concept);
	}

	public String getConceptForClass(String cls) {
		return concepts.get(cls);
	}

	public void addMapping(String concept, String cls) {
		classes.put(concept, cls);
		concepts.put(cls, concept);
	}
}
