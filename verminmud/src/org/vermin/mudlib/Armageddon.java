package org.vermin.mudlib;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.vermin.driver.Driver;
import org.vermin.driver.Service;

public class Armageddon {

	private static Armageddon _instance;

	private Long when = 0l; // when is the time to reboot
	private ScheduledExecutorService executor;
	private Driver d = Driver.getInstance();

	private int notifiedMinutes = 0;
	
	private Armageddon() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleWithFixedDelay(armageddon, 60, 60, TimeUnit.SECONDS);
	}

	public synchronized static Armageddon getInstance() {
		if(_instance == null) {
			_instance = new Armageddon();
		}
		return _instance;
	}

	public synchronized void setTime(long millis) {
			when = millis;
	}

	private Runnable armageddon = new Runnable(){
		public synchronized void run() {
			if(when == 0l) {
				return;
			} else {
						
				long timeToBoot = when - System.currentTimeMillis();
				long minutes = timeToBoot/60000;

				if(minutes == 10 && notifiedMinutes != 10) {
					notifiedMinutes = 10;
					info("The world is coming to an end in 10 minutes.");
					Iterator<Service> it = d.connectionListeners();
					while(it.hasNext())
						it.next().stopService();
					return;
				}
				if(minutes <= 2 && notifiedMinutes != 2) {
					notifiedMinutes = 2;
					info("The end of the world is imminent!");
					return;
				}
				if(minutes < 1)
					info("The world is ending NOW!");
					System.exit(1); // World will save player upon exit
				}		
		}};
			
	private void info(String msg) {
		Iterator<Player> it = World.getPlayers();
		while(it.hasNext()) {
			Player p = (Player) it.next();
			p.notice("[Armageddon] "+msg);
		}
	}
}
