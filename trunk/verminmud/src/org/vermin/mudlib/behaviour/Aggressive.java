/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib.behaviour;

import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;

/**
 * Imeplements aggressive behaviour for monsters.
 * Aggressive monsters attack everything on sight.
 * 
 * @author tadex
 *
 */
public class Aggressive extends BehaviourAdapter {

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living, org.vermin.mudlib.Exit)
     */
    public void arrives(Living who, Exit from) {
        attack(who);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living)
     */
    public void arrives(Living who) {
        attack(who);
    }
    
    private void attack(Living who) {
        owner.addAttacker(who);
        owner.doBattle();
    }
}
