package org.vermin.mudlib;

import org.vermin.driver.*;

import java.util.Iterator;

import java.util.Calendar;
import java.util.Date;

public class Armageddon {

	private static Armageddon _instance;

	private Long when = 0l; // when is the time to reboot
	private ArmageddonThread thread;
	private Driver d = Driver.getInstance();

	private Armageddon() {
		thread = new ArmageddonThread();

		// boot at midnight
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		when = c.getTime().getTime();

		thread.start();
	}

	public synchronized static Armageddon getInstance() {
		if(_instance == null) {
			_instance = new Armageddon();
		}
		return _instance;
	}

	public void setTime(long millis) {
		synchronized(when) {
			when = millis;
		}
	}

	private class ArmageddonThread extends Thread {
		public void run() {


			while(true) {
				try {

					if(when == 0l) {
						synchronized(when) {
							when.wait();
						}
					}
					else {
						
						long timeToBoot = when - System.currentTimeMillis();
						long minutes = timeToBoot/60000;

						if(minutes > 10) {

							// sleep until 10 minutes to boot
							Thread.sleep(timeToBoot - 10*60000);
							info("The world is coming to an end in 10 minutes.");
                            Iterator it = d.connectionListeners();
                            while(it.hasNext())
                                ((Service)it.next()).stopService();

						} else if(minutes < 1) {

							info("The world is ending NOW!");
							System.exit(1); // World will save player upon exit
							
						} else {
						    World.log("Armageddon: sleeping for "+timeToBoot+" msecs.");
							Thread.sleep(timeToBoot);

						}
					}

				} catch(InterruptedException ie) {}
			}

		}
	}

	private void info(String msg) {
		Iterator it = World.getPlayers();
		while(it.hasNext()) {
			Player p = (Player) it.next();
			p.notice("[Armageddon] "+msg);
		}
	}
}
