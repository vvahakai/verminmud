package org.vermin.mudlib;

/**
 * An item that cannot be seen, taken or dropped.
 * Useful for making meta objects.
 */
public class SpecialItem extends DefaultItemImpl {

	
	public SpecialItem(String name) {
		setName(name);
	}

	public boolean isVisible() { return false; }
	public boolean tryTake(Living who) { return false; }
	public boolean tryDrop(Living who, MObject where) { return false; }

}
