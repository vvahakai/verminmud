/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib.quest;


/**
 * A default implementation of a journal.
 * 
 * @author tadex
 *
 */
public class DefaultJournalImpl implements Journal {

    private String quest;
    
    public DefaultJournalImpl() {}
    
    public DefaultJournalImpl(String q) {
        quest = q;
    }
    
  
    public String getQuest() {
        return quest;
    }


}
