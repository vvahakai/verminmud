/*
 * Created on 11.3.2005
 *
 */
package org.vermin.mudlib;

/**
 * @author tadex
 *
 */
public interface DamageListener {

    /**
     * Called when damage is being taken.
     * If this method returns a non-null Damage object,
     * it is used instead of the one given as argument.
     * 
     * @param dmg the damage object
     * @param attacker the attacker
     * @param hitLocation the hit location
     * @return the replacement damage object, or null
     */
    public Damage onSubHp(Damage dmg, Living attacker, int hitLocation);
    
    /**
     * Check if this listener is active.
     * If the listener is active, the living may remove
     * this listener from it's chain of listeners.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive();
    
}
