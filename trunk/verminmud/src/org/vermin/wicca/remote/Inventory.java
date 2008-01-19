package org.vermin.wicca.remote;

import org.vermin.mudlib.Item;

/**
 * Provides methods to update inventory state.
 */
@ComponentID(id=3)
public interface Inventory {

	/**
	 * Add item to inventory.
	 *
	 * @param item the Item to add
	 */
	@MethodID(id=1)
	public void add(Item item);
	
	/**
	 * Remove item from inventory.
	 * 
	 * @param item the item to remove.
	 */
	@MethodID(id=2)
	public void remove(Item item);
	
	/**
	 * Clear the inventory. This removes all items
	 * in the inventory.
	 */
	@MethodID(id=3)
	public void clear();
	
}
