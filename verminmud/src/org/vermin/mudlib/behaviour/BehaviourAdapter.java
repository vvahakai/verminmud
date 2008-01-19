/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib.behaviour;

import org.vermin.mudlib.Behaviour;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Skill;

/**
 * @author tadex
 *
 */
public class BehaviourAdapter implements Behaviour {

    protected Living owner;
    
    public void setOwner(Living owner) {
        this.owner = owner;
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living, org.vermin.mudlib.Exit)
     */
    public void arrives(Living who, Exit from) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living)
     */
    public void arrives(Living who) {}
    
    public void afterArrives(Living who) { }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#drops(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void drops(Living who, Item what) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#leaves(org.vermin.mudlib.Living, org.vermin.mudlib.Exit)
     */
    public void leaves(Living who, Exit to) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#leaves(org.vermin.mudlib.Living)
     */
    public void leaves(Living who) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#says(org.vermin.mudlib.Living, java.lang.String)
     */
    public void says(Living who, String what) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#startsUsing(org.vermin.mudlib.Living, org.vermin.mudlib.Skill)
     */
    public void startsUsing(Living who, Skill skill) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#takes(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void takes(Living who, Item what) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#unwields(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void unwields(Living who, Item what) {}
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#wields(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void wields(Living who, Item what) {}
    
    public void dies(Living victim, Living killer) {}
	
	public void asks(Living asker, Living target, String subject) {}

    public void onBattleTick(Living who) {}

    public void onRegenTick(Living who) {}
    
    public void command(Object ... args) {}
}
