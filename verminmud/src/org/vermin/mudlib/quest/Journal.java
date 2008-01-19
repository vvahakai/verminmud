/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib.quest;


/**
 * A journal keeps track of a quest's status.
 * 
 * @author tadex
 *
 */
public interface Journal {
	
    /**
     * Returns the name of the Quest this journal
     * belongs to.
     * 
     * @return the <code>Quest</code>
     */
    public String getQuest();
	
}