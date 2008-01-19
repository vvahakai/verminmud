/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib;

/**
 * Defines reaction hooks for implementation of behaviour.
 * 
 * @author tadex
 *
 */
public interface Behaviour {
    /** 
     * Receive notification that someone has left
     * the current room.
     * 
     * @param who  The <code>Living</code> that left. 
     */
    public void leaves(Living who);

    /** 
     * Receive notification that someone arrived
     * to the current room.
     * 
     * @param who  The <code>Living</code> that arrived. 
     */
    public void arrives(Living who);
    
    /** 
     * Receive notification that someone arrived to
     * the current room.
     * 
     * @param who  The <code>Living</code> that arrived. 
     * @param from The <code>Exit</code> from where it came from. 
     */
    public void arrives(Living who, Exit from);
    
    /**
     * Receive notification that someone arrived
     * to the current room, and that the <code>arrives()</code>
     * hook of the <code>Behaviour</code> interface has been called
     * for everyone receiving such notifications.
     * 
     * The main reason to use this hook instead of
     * <code>arrives()</code>, is that it guarantees that the
     * arriving <code>who</code> has completed its
     * automatic look at the room.
     *  
     * @param who  The <code>Living</code> that arrived.
     */
    public void afterArrives(Living who);
    
    /** 
     * Receive notification that someone has left the
     * current room.
     * 
     * @param who  The <code>Living</code> that left. 
     * @param to   The <code>Exit</code> it left through. 
     */
    public void leaves(Living who, Exit to);
       
    /** 
     * Receive notification that someone has
     * started concentrating on a skill.
     * 
     * @param who  the skill user
     * @param skill the skill being used
     */
    public void startsUsing(Living who, Skill skill);
    

    /**
     * Output a formatted text notification.
     */
    //  public void printf(String fmt, Object ... args);

    /** 
     * Receive notification that someone has
     * taken something from the ground.
     * 
     * @param who  The creature who took it.
     * @param what The item that was taken.
     */
    public void takes(Living who, Item what);   
    
    /** 
     * Receive notification that someone has
     * dropped something to the ground.
     * 
     * @param who  The creature who dropped. 
     * @param what The item that was dropped. 
     */
    public void drops(Living who, Item what);
    
    /** 
     * Receive notification that someone has
     * wielded something.
     * 
     * @param who  The creature that wielded. 
     * @param what The item that was wielded. 
     */
    public void wields(Living who, Item what);

    /**
     * Receive notification that someone has
     * unwielded something.
     *
     * @param who The creature that unwielded.
     * @param what The item that was unwielded.
     */
    public void unwields(Living who, Item what);
   
    /**
     * Receive notification that someone has said
     * something in the current room.
     *
     * @param who The creature that spoke
     * @param what The spoken text
     */
    public void says(Living who, String what);
    
    /**
     * Receive notification that something has died.
     * 
     * @param victim  The living who died
     * @param killer  The living who caused the death, or
     *                null if the cause was something else
     */
    public void dies(Living victim, Living killer);
    
	/**
	 * Receive notification that somebody asked this
	 * being about <code>subject</code>.
	 * 
	 * @param asker  the living who asked
	 * @param target the living who was asked 
	 * @param subject  the subject of the question as a string
	 */
	public void asks(Living asker, Living target, String subject);
	
    /**
     * Called on each battle tick.
     * 
     * @param who
     */
    public void onBattleTick(Living who);
    
    /**
     * Called on each regen tick.
     * @param who
     */
    public void onRegenTick(Living who);
    
    /**
     * An abstract command that can be run on a 
     * behaviour object.
     * 
     * @param args the arguments
     */
    public void command(Object ... args);
}
