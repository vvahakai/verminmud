package org.vermin.mudlib;

public interface Wearable extends Item
{
   public boolean tryWear(Living who);
   public boolean tryUnwear(Living who);

   public void onWear(Living who);
   public void onUnwear(Living who);

   public int getArmourValue();

   public int getArmourValue(Damage.Type damageType);

   /* Returns type names for used slots.
    * For example: torso, arm, arm.
    */
   public Slot[] getSlots();

   /**
    * Get an array of utility slots in this item.
    * If there are no utility slots, this method can return
    * null or an empty array.
    * 
    * @return an array of <code>UtilitySlot</code>
    */
   public UtilitySlot[] getUtilitySlots();
   
}
