/*
 * Created on 15.1.2005
 *
 */
package org.vermin.mudlib.commands;

import org.vermin.mudlib.LineEditor;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;

/**
 * @author tadex
 *
 */
public class Plan extends RegexCommand {

    public String[] getDispatchConfiguration() {  
        return new String[] {
                "plan show => showPlan(actor)",
                "plan clear => clearPlan(actor)",
                "plan set => setPlan(actor)",
                "plan => showUsage(actor)",
                "plan (.*) => showUsage(actor)"
        };
    }
    
    public void showPlan(Player actor) {
        if (actor.getPlan() == null) {   	
        	actor.notice("You have currently no plan.");
        } else {
        	actor.notice("\nCurrent plan:");
        	actor.notice(actor.getPlan());
        	actor.notice("\n");
        }
    }
    
    public void clearPlan(Player actor) {
        actor.setPlan(null);
        actor.notice("Plan cleared.");
    }
    
    public void setPlan(final Player actor) {
        new LineEditor(actor, 
            new LineEditor.Listener() {
                public void done(String txt) {
                    actor.setPlan(txt);
                    actor.notice("Plan set.");
                }
                public void canceled() {
                }},
            5, ">").activate();
    }
    
    public void showUsage(Player actor) {
    	actor.notice("Usage: plan <show|set|clear>");
    	actor.notice("For more information: help plan");
    }
}
