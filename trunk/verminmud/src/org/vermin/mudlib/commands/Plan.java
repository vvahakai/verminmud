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
                "plan => showPlan(actor)",
                "plan clear => clearPlan(actor)",
                "plan edit => editPlan(actor)"
        };
    }
    
    public void showPlan(Player actor) {
        actor.notice("Current plan:");
        actor.notice(actor.getPlan());
    }
    
    public void clearPlan(Player actor) {
        actor.setPlan(null);
    }
    
    public void editPlan(final Player actor) {
        LineEditor.Listener listener =
            new LineEditor.Listener() {
                public void done(String txt) {
                    actor.setPlan(txt);
                }

                public void canceled() {
                }};    
        
        LineEditor le = new LineEditor(actor, listener, 5); 
            

    }
}
