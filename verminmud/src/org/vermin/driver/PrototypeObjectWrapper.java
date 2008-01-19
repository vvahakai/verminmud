/* PrototypeObjectWrapper.java
 * 8.3.2003 Tatu Tarvainen
 *
 * Wraps a plain object into a Prototype.
 */
package org.vermin.driver;
import java.io.File;

public class PrototypeObjectWrapper extends AbstractPrototype {

	private Object object;

	public PrototypeObjectWrapper(File source, Object obj) {
		super(source, true, obj.toString());
		object = obj;
	}

	protected Object makeObject() {
        if(object instanceof Persistent)
            ((Persistent)object).setId(this.id);
		return object;
	}
}
