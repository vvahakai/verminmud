package org.vermin.mudlib;

import org.vermin.driver.Maybe;

public class CommandDelegate implements Command { 
    
    private Maybe<Command> command;
    
    public boolean action(Living who, String params) {
	Command c = command.get();
	if(c != null)
	    return c.action(who,params);
	else {
	    who.notice("Command not available, delegate has not been loaded.");
	    return true;
	}
    }

}