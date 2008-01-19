package org.vermin.mudlib;

public interface Wieldable extends Item
{
	
   /**
    * Get the all hit damages with this item. 
    * 
    * @param target The <code>Living</code> that is being hit, this is used by some behaviours.
    * @return an array of <code>Damage</code> objects
    */
   public Damage[] getHitDamage(Living target);
   
   /**
    * Returns the damage this item does when used as a projectile.
    * 
    * @param target The <code>Living</code> that is being hit, this is used by some behaviours.
    * @return an array of <code>Damage</code> objects
    */
   public Damage[] getProjectileDamage(Living target);
   
   /**
    * Return a verb describing how this weapon is used to
    * inflict given damage type.
    * (eg. "slash/slashes", "bounce/bounces"...)
    *
    * Used in battle messages like: "You *hit* monster in the leg",
    * where *hit* is the verb.
    */
   public String getObjectHitVerb(Damage.Type type);
   public String getSubjectHitVerb(Damage.Type type);

	/**
	 * Return a noun describing the action
	 * that is performed when using this weapon .
	 *
	 * Used in battle messages like: "The *hit* connects with monster's neck causing...",
	 * where *hit* is the noun.
	 *
	 * @param type the damage type of the hit
	 * @return the hit noun
	 */
	public String getHitNoun(Damage.Type type);

   /** 
	 * Describes how good this item is for parrying/defense.
	 * Scale 0 - 100.
	 */
   public int getDefensiveValue();
   
   /* Describes the weapon type (sword, axe, etc...) */
   public WeaponType getWeaponType();
   
   public boolean tryWield(Living who);
   public boolean tryUnwield(Living who);
   
   /* Notify this item that it has been wielded by someone.
    * This method can add any modifiers to the Living.
    */
   public void onWield(Living who);
   
   /* Notify that someone has stopped wielding this item.
    * This method MUST remove any modifiers added by
    * wield().
    */
   public void onUnwield(Living who);
   
   public void hitReact();
   
   public boolean hitOverride(Living attacker, Living target);

   public float getSpeed();


}
