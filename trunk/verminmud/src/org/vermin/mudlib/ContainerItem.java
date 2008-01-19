/* ContainerItem.java
	2.2.2002	TT&VV
	
	
	Item that contains other items.
*/

package org.vermin.mudlib;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;


/**
 * ContainerItem is an item that can contain other items.
 * This is a useful superclass for containers.
 */
public class ContainerItem extends DefaultItemImpl implements Container, Purse {

	protected Vector<MObject> items;
	private Money money = new DefaultMoneyImpl();
    
    
	public ContainerItem() {
		items = new Vector();
	}
	
    public Money getMoney() {
        return money;
    }
    
    public Iterator<MObject> children() {
        return items.iterator();
    }
    
	public void add(MObject o) {
		o.setParent(this);
		if(o instanceof Item)
			items.add(o);
	}

	public void remove(MObject o) {
		items.remove(o);
	}
	
	public boolean contains(MObject obj) {
		return items.contains(obj);
	}
	
	public boolean tryAdd(MObject obj) { return true; }
	public boolean tryRemove(MObject obj) { return true; }	

	public String getLongDescription() {

		StringBuilder sb = new StringBuilder();
		
		sb.append(description + "\n");
		
		if(items.isEmpty() && getMoney().isEmpty())
			sb.append("It is empty.\n");
		else {
			sb.append("It contains: ");
			if(!getMoney().isEmpty()) {
                sb.append(getMoney().getDescription());
                sb.append(", ");
            }
			for(int i=0; i<items.size(); i++) {
				if(!((Item) items.get(i)).isVisible()) continue;
				sb.append( ((Item) items.get(i)).getDescription() );
                sb.append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append(".\n");
		
		}
		
		return sb.toString();
	}

	public Iterator findByType(Types type) {
		if(type == Types.TYPE_ITEM) {
			return items.iterator();
		}
		return null;
	}

	public MObject findByName(String name) {
		
		Matcher m = Container.INDEXED_CONTAINER_ACCESS.matcher(name);
		
		if(m.matches()) {
			findByName(m.group(1), Integer.parseInt(m.group(2)));
		}
		
		for(int i=0; i<items.size(); i++) {
			if( ((Item) items.get(i)).isAlias(name) )
				return (Item) items.get(i);
		}
		return null;
	}
	
	public MObject findByName(String name, int index) {
		for(int i=0; i<items.size(); i++) {
			if( ((Item) items.get(i)).isAlias(name)) {
				if(index == 1) {
					return (Item) items.get(i);
				}
				index--;
			}
		}
		return null;
	}
	
	/* Find an object by name an type. */
	public MObject findByNameAndType(String name, Types type) {

		Matcher m = Container.INDEXED_CONTAINER_ACCESS.matcher(name);
		
		if(m.matches()) {
			findByNameAndType(m.group(1), Integer.parseInt(m.group(2)), type);
		}		
		
		if(type != Types.TYPE_ITEM)
			return null;
		
		return findByName(name);
	}

	public MObject findByNameAndType(String name, int index, Types type) {
		if(type != Types.TYPE_ITEM)
			return null;
		
		return findByName(name, index);
	}
	

}
