package org.vermin.mudlib;

public interface UtilitySlot {

	/**
	 * Check if this slot is reserved 
	 * (ie. has an item in it).
	 * 
	 * @return true if reserved, false otherwise
	 */
	public boolean isReserved();
		
	/**
	 * Get the item that is equipped in this slot.
	 * 
	 * @return the item or null if not reserved
	 */
	public MObject getItem();
	
	/**
	 * Check if this slot is suitable for the given item.
	 * 
	 * @param item the item to check for
	 * @return true if suitable, false otherwise
	 */
	public boolean isSuitable(MObject item);
	
	/**
	 * Try to equip the given item to this slot.
	 * 
	 * @param item the item to equip
	 * @return true if successful, false if item was not accepted
	 */
	public boolean equip(MObject item);
	
}
