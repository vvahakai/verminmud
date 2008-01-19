package org.vermin.mudlib.commands;

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
		final long msecs = minutes * 60 * 1000;
		final long targetTime = System.currentTimeMillis() + msecs;
		Runnable r = new Runnable(){
			public void run() {
				while(System.currentTimeMillis() < targetTime) {
					long sleep = targetTime - System.currentTimeMillis();
					try {
						if(sleep > 0)
							Thread.sleep(sleep > 60000 ? 60000 : sleep);
					} catch(InterruptedException ie) {}
				}
				System.exit(0);
			}};
		actor.notice("Shutdown in "+minutes+" minutes.");
		new Thread(r).start();		
	}

}
