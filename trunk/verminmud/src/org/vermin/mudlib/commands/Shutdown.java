package org.vermin.mudlib.commands;

import org.vermin.mudlib.Armageddon;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Wizard;

public class Shutdown extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"shutdown (\\d+) => shutdown(actor, 1)",
				"shutdown => shutdown(actor)"
		};
	}
	
	public void shutdown(Wizard actor) {
		shutdown(actor, 0);
	}
	public void shutdown(final Wizard actor, int minutes) {
		final long msecs = minutes * 60l * 1000l;
		final long targetTime = System.currentTimeMillis() + msecs;
		Armageddon.getInstance().setTime(targetTime);
		actor.notice("Instructed armageddon to shut down the world in "+minutes+" minutes.");
	}

}
