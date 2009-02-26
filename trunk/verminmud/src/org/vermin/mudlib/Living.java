/* Living.java
	5.1.2002	Tatu Tarvainen / Council 4
	
	Defines the interface that all living things (actors) must
	implement.
	
*/

package org.vermin.mudlib;
import org.vermin.driver.*;
import org.vermin.util.Functional;

/** 
 * Interface for all living things.
 * The inherited container interface provides
 * access to the inventory.
 * 
 * @author Tatu Tarvainen
 * @version 1.0
 */
public interface Living 
    extends MObject, Behaviour, 
            Container, Purse,
            Alignment, CompositePropertyProvider<LivingProperty> {
    
    /**
     * Add damage listener.
     * 
     * @param listener the damage listener to add
     */
    public void addDamageListener(DamageListener listener);
    
	/** 
	 * Get the current amount of hit points.
	 * 
	 * @return The amount of hit points.
	 */
	public int getHp();

	/** 
	 * Get the maximum amount of hit points.
	 * 
	 * @return  The amount of hit points. 
	 */
	public int getMaxHp();
	
	/** 
	 * Set the current amount of hit points.
	 * 
	 * @param p The new value. 
	 */
	public void setHp(int p);

	/** 
	 * Add to the current amount of hit points.
	 * 
	 * @param p The amount of hit points to add.
	 */
	public void addHp(int p);
	
	/** 
	 * Substract from the current amount of hit points.
	 * NOTE: You will most likely want to call <code>subHp(Damage, Living)</code> or <code>subHp(Damage, int, Living)</code> instead!
	 * 
	 * @param amount The damage to substract from hitpoints. 
	 * @return  true if the substraction resulted in death, false otherwise. 
	 */	
	public boolean subHp(Damage amount);

	/** 
	 * Substract from the current amount of hit points.
	 * 
	 * @param amount The damage to substract from hitpoints. 
	 * @param attacker The living who inflicted the damage.
	 * @return  true if the substraction resulted in death, false otherwise. 
	 */	
	public boolean subHp(Damage amount, Living attacker);

	/**
	 * Substract from the current amount of hit points.
	 * This is the preferred way of giving damage as it
	 * must handle death properly.
	 * This will also take into account the armour in the given hitlocation
	 * NOTE: most armour will only protect against physical damage (and subtypes)
	 *
	 * @param amount the amount of damage inflicted
	 * @param hitlocation the hit location the damage was caused to
	 * @param attacker The living who inflicted the damage.
	 * @return true if the substraction resulted in death, false otherwise.
	 */
	public boolean subHp(Damage amount, int hitlocation, Living attacker);

	/**
	 * Substract from the current amount of hit points.
	 * This will take into account the armour in the given hitlocation
	 * NOTE: most armour will only protect against physical damage (and subtypes)
	 *
	 * @param amount the amount of damage inflicted
	 * @param hitlocation the hit location the damage was caused to
	 * @return true if the substraction resulted in death, false otherwise.
	 */
	public boolean subHp(Damage amount, int hitlocation);

	/** 
	 * Get the current amount of spell points.
	 * 
	 * @return  The amount of spell points. 
	 */
	public int getSp();

	/** 
	 * Get the maximum amount of spell points.
	 * 
	 * @return  The amount of spell points. 
	 */
	public int getMaxSp();

	/** 
	 * Set the current amount of spell points.
	 * 
	 * @param p The new value. 
	 */
	public void setSp(int p);

	/** 
	 * Add to the current amount of spell points.
	 * 
	 * @param p The amount of spell points to add. 
	 */
	public void addSp(int p);

	/** 
	 * Substract from the current amount of spell points.
	 * 
	 * @param p The amount of spell points to substract. 
	 */
	public void subSp(int p);
	
	/** 
	 * Check if this is dead.
	 * 
	 * @return  true if this object is dead, false otherwise. 
	 */
	public boolean isDead();

	/** 
	 * Get the physical strength with possible
	 * modifiers.
	 * 
	 * @return  The physical strength. 
	 */
	public int getPhysicalStrength();

	/** 
	 * Get the mental strength with possible
	 * modifiers.
	 * 
	 * @return  The mental strength. 
	 */
	public int getMentalStrength();

	/** 
	 * Get the physical strength. 
	 * 
	 * @param modifiers  If true the value will be returned with modifiers. 
	 * @return  The physical strength. 
	 */
	public int getPhysicalStrength(boolean modifiers);
	
	/** 
	 * Get the mental strength.
	 * 
	 * @param modifiers  If true the value will be returned with modifiers. 
	 * @return  The mental strength. 
	 */
	public int getMentalStrength(boolean modifiers);

	/** 
	 * Set the physical strength. Does not affect 
	 * modifiers.
	 * 
	 * @param p The new value.
	 */
	public void setPhysicalStrength(int p);

	/** 
	 * Set the mental strength. Does not affect
	 * modifiers.
	 * 
	 * @param p The new value.
	 */
	public void setMentalStrength(int p);
	
	
	/** 
	 * Get the physical constitution with possible modifiers.
	 * 
	 * @return  The physical constitution. 
	 */
	public int getPhysicalConstitution();

	/** 
	 * Get the mental constitution with possible modifiers.
	 * 
	 * @return  The mental constitution. 
	 */
	public int getMentalConstitution();

	/** 
	 * Get the physical constitution.
	 * 
	 * @param modifiers  If true the value is returned with modifiers. 
	 * @return  The physical constitution. 
	 */
	public int getPhysicalConstitution(boolean modifiers);

	/** 
	 * Get the mental constitution.
	 * 
	 * @param modifiers  If true the value is returned with modifiers. 
	 * @return  The mental constitution. 
	 */
	public int getMentalConstitution(boolean modifiers);

	/** 
	 * Set the physical constitution.
	 * 
	 * @param p The new value. 
	 */
	public void setPhysicalConstitution(int p);

	/** 
	 * Set the mental constitution.
	 * 
	 * @param p The new value. 
	 */
	public void setMentalConstitution(int p);
	
	/** 
	 * Get the physical charisma with possible modifiers.
	 * 
	 * @return  The physical charisma. 
	 */
	public int getPhysicalCharisma();

	/** 
	 * Get the mental charisma with possible modifiers.
	 * 
	 * @return  The mental charisma. 
	 */
	public int getMentalCharisma();

	/** 
	 * Get the physical charisma.
	 * 
	 * @param modifiers  If true the value is returned with modifiers. 
	 * @return  The physical charisma. 
	 */
	public int getPhysicalCharisma(boolean modifiers);

	/** 
	 * Get the mental charisma.
	 * 
	 * @param modifiers  If true the value is returned with modifiers. 
	 * @return  The mental charisma. 
	 */
	public int getMentalCharisma(boolean modifiers);

	/** 
	 * Set the physical charisma.
	 * 
	 * @param p The new value. 
	 */
	public void setPhysicalCharisma(int p);

	/** 
	 * Set the mental charisma.
	 * 
	 * @param p The new value. 
	 */
	public void setMentalCharisma(int p);
	
	/** 
	 * Get the physical dexterity with possible modifiers.
	 * 
	 * @return  The physical dexterity. 
	 */
	public int getPhysicalDexterity();
	
	/** 
	 * Get the mental dexterity with possible modifiers.
	 * 
	 * @return  The mental dexterity. 
	 */
	public int getMentalDexterity();

	/** 
	 * Get the physical dexterity.
	 * 
	 * @param modifiers If true the value will be returned with modifiers.
	 * @return  The physical dexterity. 
	 */
	public int getPhysicalDexterity(boolean modifiers);

	/** 
	 * Get the mental dexterity.
	 * 
	 * @param modifiers  If true the value will be returned with modifiers. 
	 * @return  The mental dexterity. 
	 */
	public int getMentalDexterity(boolean modifiers);

	/** 
	 * Set the physical dexterity.
	 * 
	 * @param p The new value. 
	 */
	public void setPhysicalDexterity(int p);

	/** 
	 * set the mental dexterity.
	 * 
	 * @param p The new value.
	 */
	public void setMentalDexterity(int p);
	
	/** 
	 * Get the race of this creature.
	 * 
	 * @return  The <code>Race</code>.
	 */
	public Race getRace();
	
    /**
     * Set the race of this creature.
     * This should only be called upon instantiation
     * or reincarnation.
     * 
     * @param race the race to use
     */
    public void setRace(Race race);
            
	/** 
	 * Get a skill percentage with possible modifiers.
	 * When checking for skill success, you should use <code>checkSkill()</code>
	 * 
	 * @param name The name of the desired skill. 
	 * @return  The percentage. 
	 */
	public int getSkill(String name);

	/** 
	 * Get a skill percentage.
	 * 
	 * @param name The name of the desired skill. 
	 * @param modifiers  If true the value will be returned with modifiers. 
	 * @return  The percentage. 
	 */
	public int getSkill(String name, boolean modifiers);
	
	/** 
	 * Add an attacker.
	 * 
	 * @param who  The attacker. 
	 */
	public void addAttacker(Living who);

	/** 
	 * Get a damage type resistance.
	 * 
	 * @param type The type of resistance. 
	 * @return  The resistance percentage. 
	 * @see <code>Damage</code> 
	 */
	public int getResistance(Damage.Type type);
	
	/**
	 * Get the resistance to the given damage type, with optional modifiers.
	 *
	 * @param type the damage type
	 * @param modifiers if false, modifiers are omitted
	 */
	public int getResistance(Damage.Type type, boolean modifiers);
	
	/** 
	 * Get the room this creature is currently in.
	 * 
	 * @return  The current room. 
	 */
	public Room getRoom();
	
	/** 
	 * Get a list of wielded items.
	 * Must be in the same order as race limbs.
	 * wielded[0] == Item wielded in 1. limb
	 * wielded[n] == Item wielded in n. limb 
	 * 
	 * If naturals is true, return natural weapons for 
	 * limbs that don't have anything wielded in.
	 * Otherwise empty limbs are returned as null.
	 * 
	 * @return An array of wielded items.
	 */
	public Wieldable[] getWieldedItems(boolean naturals);

	/** 
	 * Get a list of worn items.
	 * Must be in the same order as race slots.
	 * wielded[0] == Item worn in 1. slot
	 * wielded[n] == Item worn in n. slot 
	 * NOTE: one piece of armour may be worn in more than one slot 
	 *
	 * @return An array of wielded items.
	 */
	public Wearable[] getWornItems();
	
	/** 
	 * Get the amount of experience points this creature
	 * is worth when killed.
	 * 
	 * @return The experience worth.
	 */
	public long getExperienceWorth();
	
	/** 
	 * Add to the amount of experience. Both current
	 * and total experience counters are incremented.
	 * 
	 * @param amount  The amount of experience to add. 
	 */
	public void addExperience(long amount);
	
	/** 
	 * Get the possessive form (this creature has or is the source of something).
	 * For example: his, hers and it's. 
	 * 
	 * @return  The possessive. 
	 */
	public String getPossessive();

	/** 
	 * Get the objective form (this creature is the target of something). 
	 * For example: him, her and it. 
	 * 
	 * @return  The objective. 
	 */
	public String getObjective();
	
   /** 
    * Get the pronoun.
    * For example: he, she and it.
    * 
    * @return  The pronoun. 
    */
   public String getPronoun();

	/** 
	 * Called upon death to leave remains.
	 * Monsters should implement this and
	 * leave a corpse in the current room. 
	 */
	public void dumpCorpse();
	
	/** 
	 * Wield an item.
	 * 
	 * @param what The item to wield. 
	 * @return  true if the wield was successful, false otherwise. 
	 */
	public boolean wield(Wieldable what);

	/**
	 * Unwield an item.
	 * 
	 * @param what the item to wield.
	 * @return true it the unwield was successful, false otherwise.
	 */
	public boolean unwield(Wieldable what);

	/** 
	 * Move to a direction.
	 * 
	 * @param to   The exit to move through. 
	 * @return  true if moving was successful, false otherwise. 
	 */
	public boolean move(Exit to);
	

	/**
	 * Set this creature's BattleStyle.
	 * 
	 * @param style The new style.
	 */
	public void setBattleStyle(BattleStyle style);
   
	/**
	 * Get this creature's BattleStyle.
	 * 
	 * @return The style.
	 */
	public BattleStyle getBattleStyle();
   
	/**
	 * Set this creature's offensiveness.
	 * 
	 * @param amount How offensive (0-100) this creature is in battle.
	 */
	public void setOffensiveness(int amount);
   
	/**
	 * Get this creature's offensiveness.
	 * 
	 * @return How offensive (0-100) this creature is in battle.
	 */
	public int getOffensiveness();
	
	/** 
	 * Called at every battle tick.
	 * Can be used to monitor status and act accordingly.
	 * For example: wimpying and messages.
	 * 
	 */
	public void battleReact();
	

   /** 
    * Get this creatures size.
    * 
    * @return  The size. 
    */
   public int getSize();

   /** 
    * Get an array of available armour slots.
    * Slot is considered to be available if nothing
    * is worn in it. 
    * 
    * @return  The available armour slots. 
    */
   public Slot[] getAvailableSlots();

	/**
	 * Get the root battle group of this living.
	 */
	public BattleGroup getBattleGroup();

	/**
	 * Get the leaf battle group of this living.
	 */
	public BattleGroup getLeafBattleGroup();

	/**
	 * Get the personal battle group of this living.
	 */
	public BattleGroup getPersonalBattleGroup();
	
	/**
	 * Check if this living is in a battle.
	 *
	 * @return true if in battle, false otherwise
	 */
	public boolean inBattle();

	/**
	 * Start doing battle.
	 */
	public void doBattle();

	/**
	 * Get the lawfulness of this creature
	 *
	 * @return the lawfulness chaotic < 0, 0 neutral, lawful > 0
	 */
	 public int getLawfulness();

    /**
     * Modify the sustenance status of this living
     * by amount. For reference, a full stomach is
     * considered to hold 10000 sustenance points,
     * a loaf of bread is worth 1000 points, and
     * moving from a room to another consumes 1 point.
     * 
     * @param amount  the amount to modify, negative values make this
     *                living more hungry
     */
	public void modifySustenance(int amount);
	
	/**
	 * Sets the sustenance status of this living to
	 * an absolute value.
	 * 
	 * @param sustenance  the sustenance status to set for this living
	 */
	public void setSustenance(int sustenance);

	/**
	 * Gets the sustenance status of this living.
	 * 
	 * @return  an int value describing the sustenance status of this living
	 */
	public int getSustenance();

	/**
	 * Remove everything from the inventory.
	 * Including (but not limited to) invisible objects.
	 */
	public void clearInventory();

	/**
	 * Force living to unwear & unwield all equipment.
	 * All items will be moved to the inventory.
	 */
	public void putItemsToInventory();

	/**
	 * Update state that depends on stats.
	 * Recalculates sp/hp maximums etc.
	 * This method is safe to call even if no stat changes have been made.
	 */
	public void updateStats();

	/**
	 * Returns a short description of the physical shape.
	 */
	public String getShape();

	/**
	 * Returns the value of the given stat with modifiers.
	 *
	 * @return the stat value
	 */
	public int getStat(Stat stat);

	/**
	 * Returns the value of the given stat.
	 *
	 * @param modifiers if false, returns the unmodified value
	 * @return the stat value
	 */
	public int getStat(Stat stat, boolean modifiers);

	/**
	 * Returns the party this creature belongs to.
	 * If no party, return null.
	 *
	 * @return a <code>Party</code> instance or null
	 */
	public Party getParty();

	/**
	 * Set the party of this creature.
	 *
	 * @param party the party, may be null
	 */
	public void setParty(Party pi);

	/**
	 * Check if this living is blocking the given direction.
	 * Mainly used for monsters like guards.
	 *
	 * @param dir the name of the direction
	 * @return true if blocking, false otherwise
	 */
	public boolean isBlocking(String dir);

	/**
	 * Check if this living can fly.
	 * 
	 * @return true if yes, false otherwise
	 */
	public boolean canFly();

	/**
	 * Check if this living can breath water.
	 *
	 * @return true if yes, false otherwise
	 */
	public boolean canBreatheWater();

	/**
	 * Check if this living has to breathe to survive.
	 *
	 * @return true if yes, false otherwise
	 */
	public boolean hasToBreathe();

	/**
	 * Add an affliction to this living.
	 * A living can have many afflictions at the same time,
	 * but only one affliction of a given type can be
	 * active at the same type.
	 *
	 * @param a
	 */
	public void addAffliction(Affliction a);
	
	/**
	 * Removes an affliction from this living.
	 * @param a
	 */
	public void removeAffliction(Affliction a);
    
    /** 
     * Receive text notification. This method
     * is primarily for players.
     * 
     * @param text The text. 
     */
    public void notice(String text);
    
    /** 
     * Add pluggable behaviour object.
     * @param b
     */
    public void addBehaviour(Behaviour b);
    
    /**
     * Remove a pluggable behaviour object.
     * 
     * @param b
     */
    public void removeBehaviour(Behaviour b);
	
    /**
     * Remove a pluggable behaviour object by a
     * filter expression. This removes all
     * behaviour objects that the predicate matches.
     * 
     * @param p the predicate
     */
    public void removeBehaviour(Functional.Predicate<Behaviour> p);
    
    /**
     * Find a pluggable behaviour object by a
     * filter expression. This returns the 
     * first behaviour the predicate matches.
     * If no behaviour is matched, return null.
     * 
     * @param p the predicate
     */
    public Behaviour findBehaviour(Functional.Predicate<Behaviour> p);
    
	/**
	 * Performs a skill check on a given skill.
	 * Margin of success is positive for successful 
	 * checks and negative for failures.
	 * 
	 * @param skill to be checked
	 * @return margin of success
	 */
	public int checkSkill(String skill);
    
    /**
     * Returns theis living's skill object.
     * @return
     */
	public SkillObject getSkillObject();

    /**
     * Returns this living's gender object.
     * Should usually be "male" or "female"
     * @return the gender
     */
	public String getGender();
}
