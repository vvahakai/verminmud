/* DefaultObjectImpl.java
	5.1.2002	Tatu Tarvainen / Council4
	
	Default object implementation (with lifecycle management).
*/

package org.vermin.mudlib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.vermin.driver.Queue;

public abstract class DefaultObjectImpl extends DefaultPersistentImpl implements MObject {

	
	protected String name;
	protected String description;
	protected String longDescription;
	
	protected Vector aliases;
	
	protected Container parent;


	// light level
   protected int illumination = 0;
	protected LinkedList lightModifiers;

	public DefaultObjectImpl() {
		aliases = new Vector();
		setId("");
		name = "";
		description = "";
		longDescription = "";
		
	}
	
	public boolean contains(MObject obj) {
		return false;
	}

   public int getIllumination() {
		return DefaultModifierImpl.calculateInt(this, illumination, lightModifiers);
   }

	public void setIllumination(int illumination) {
		this.illumination = illumination;
	}

	public void addModifier(Modifier m) {
		if(m.getType() == ModifierTypes.LIGHT) {
			if(lightModifiers == null)
				lightModifiers = new LinkedList();
			lightModifiers.add(m);
		} else {
			throw new RuntimeException("Unsupported modifier type "+m.getType()+" for object: "+this);
		}
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}
	public final Container getParent() {
		return parent;
	}

	/* Return the downcast type for this object. */
	public Types getType() {
		return Types.OBJECT;
	}
	
	public boolean tick(Queue type) {
		return false;
	}
	
	public boolean needSave() {
		return false;
	}
	
	public void start() {
		World.addGarbageTick(this);
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getLongDescription() { return longDescription; }	
	
	public void setName(String n) { name = n; }
	public void setDescription(String desc) { description = desc; }
	public void setLongDescription(String lDesc) { longDescription = lDesc; }
	public void addAlias(String alias) { aliases.add(alias.toLowerCase()); }

	public boolean isAlias(String alias) {
		if(name.equalsIgnoreCase(alias))
			return true;
		
		if(aliases.contains(alias.toLowerCase()))
			return true;
		
		return false;
	}

	/* Composite methods */
	public void add(MObject child) {}
	
	public void remove(MObject child) {}
	
	public Iterator findByType(Types type) {
		return null;
	}
	
	public MObject findByName(String name) {
		return null;
	}
	
	/* Find an object by name an type. */
	public MObject findByNameAndType(String name, Types type) {
		return null;
	}

	public MObject findByName(String name, int index) {
		return null;
	}
	
	/* Find the index:th object by name an type. */
	public MObject findByNameAndType(String name, int index, Types type) {
		return null;
	}	
	
   public MObject getClone() {
      try {
         return (MObject) this.clone();
      } catch(CloneNotSupportedException cns) {
         return null;
      }
   }

	/* Support old style actions (Vector params). */
	public boolean action(MObject actor, String cmd) {
		String[] params = cmd.split(" +");
		Vector v = new Vector();
		for(int i=0; i<params.length; i++) {
			v.add(params[i]);
		}

		return action(actor, v);
	}

	/* Default action handler. Return false (to indicate
	 * that this object didn't handle the action).
	 */
	public boolean action(MObject caller, Vector params) {
		return false;
	}
	
}
