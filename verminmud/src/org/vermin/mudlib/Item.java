/* Item.java
	5.1.2002	Tatu Tarvainen / Council 4
	
	Any item.
*/
package org.vermin.mudlib;

public interface Item extends MObject
{
	/* Get the size of this item */
	public int getSize();
	public int getSize(boolean modifiers);
	
	/**
	 * Return this items weight in grams.
	 * @return weight of this item
	 */
	public int getWeight();

	/* Get the durability points of this item */
	public int getDp();
   public int getMaxDp();
	
	/* Get the material of this item */
	public Material getMaterial();
	
	/* Substract durability */
	public void subDp(int amount);
	
	/* Add durability */
	public void addDp(int amount);
	
	/* The living who tries to take this item. */
	public boolean tryTake(Living who);

	/* Try to drop this item */
	public boolean tryDrop(Living who, MObject where);
	
	/* Is this object visible?.
	 *
	 * Invisible objects are meta objects, that can
	 * add functionality and command to the player.
	 * (for example race and guild objects)
	 */
	public boolean isVisible();
	
	/* Notify this item that it has been picked up by someone. 
	 * This method is responsible for adding any commands and 
	 * modifiers to the Living. 
	 */
	public void take(Living who);
	
	/* Notify this item that is has been dropped by someone.
	 * This method MUST remove any commands and modifiers
	 * added by the take() method.
	 */
	public void drop(Living who);

   /* Plural form of the short description. */
   public String getPluralForm();

}
