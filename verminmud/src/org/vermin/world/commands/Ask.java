/*
 * Created on 27.5.2005
 */
package org.vermin.world.commands;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.RegexCommand;

public class Ask extends RegexCommand {

	public String[] getDispatchConfiguration() {

		return new String[] { "ask (.*) about (.*) => ask(actor, living 1, 2)",
							  "ask (.*) => askUsage(actor)",
							  "ask => askUsage(actor)"};
	}

	public void ask(Living asker, Living who, String subject) {
		if(who != null && subject != null) {
			// asker.getRoom().notice(asker, asker.getName()+" asks "+who.getName()+" about "+subject+".");
			// asker.notice("You ask "+who.getName()+" about "+subject+".");
			//who.asks(asker, who, subject);
			asker.getRoom().asks(asker, who, subject);
		} else {
			asker.notice("Ask who about what?\n");
			askUsage(asker);
		}
	}
	
	public void askUsage(Living asker) {
		asker.notice("Usage:");
		asker.notice("Ask <who> about <what>");
	}
}
