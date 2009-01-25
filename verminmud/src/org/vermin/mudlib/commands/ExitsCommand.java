/*
 * ExitsCommand.java
 * Created on 19.2.2005
 */
package org.vermin.mudlib.commands;

import java.util.Iterator;

import org.vermin.mudlib.*;

/**
 * @author Ville
 */
public class ExitsCommand extends RegexCommand {

    public String[] getDispatchConfiguration() {
        return new String[] {
                "exits => exits(actor)"
        };
    }

    public void exits(Living who) {
    	Iterator it = who.getRoom().findByType(Types.EXIT); 
    	
    	if(it == null || !it.hasNext()) {
    		who.notice("There are no obvious exits.\n");
    		return;
    	}
    	
    	who.notice("Obvious exits are:\n");
    	
    	while(it.hasNext()) {
    		Exit e = (Exit) it.next();
    		Room r = (Room) World.get(e.getTarget(who.getRoom().getId()));
    		if(r == null)
    			continue;
    		String dir = e.getDirection(who.getRoom().getId());
    		who.notice(dir+" : "+r.getDescription()+"\n");
    	}
    }
}
