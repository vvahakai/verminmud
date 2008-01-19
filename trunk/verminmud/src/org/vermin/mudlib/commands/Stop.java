package org.vermin.mudlib.commands;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.RegexCommand;

/**
 * Stop the madness!
 */
public class Stop extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] { "stop => stop(actor)" };
	}
	
	
	public void stop(Living actor) {
		actor.getBattleGroup().clearHostiles();
		actor.notice("You decide to stop fighting.");
		actor.getRoom().notice(actor, actor.getName()+" stops fighting.");
	}

}
