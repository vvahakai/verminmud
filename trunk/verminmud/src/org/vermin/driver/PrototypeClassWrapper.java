/**
 * PrototypeClassWrapper.java
 * 4.12.2004
 *
 * Wraps classes as prototypes.
 * Instances are created from the class using a 0-arg constructor.
 */
package org.vermin.driver;

import java.io.File;

public class PrototypeClassWrapper extends AbstractPrototype  {

	private Class cls;

	public PrototypeClassWrapper(File source, boolean unique, String name, Class cls) {
		super(source, unique, name);
		this.cls = cls;
	}

	public Object makeObject() {
		try {
			return cls.newInstance();
		} catch(InstantiationException ie) {
			throw new PrototypeException("Unable to instantiate wrapped class.");
		} catch(IllegalAccessException iae) {
			throw new PrototypeException("Unable to access wrapped class constructor.");
		}
	}

}

