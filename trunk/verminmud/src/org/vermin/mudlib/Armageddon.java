package org.vermin.mudlib;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.vermin.driver.Driver;
import org.vermin.driver.Service;
import org.vermin.driver.TickerThread;

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
					World.wall("[Armageddon] The world is coming to an end in 10 minutes.");
					Iterator<Service> it = d.connectionListeners();
					while(it.hasNext())
						it.next().stopService();
					return;
				}
				if(minutes <= 2 && notifiedMinutes != 2) {
					notifiedMinutes = 2;
					World.wall("[Armageddon] The end of the world is imminent!");
					return;
				}
				if(minutes < 1) {
					World.wall("[Armageddon] The world is ending NOW!");
					TickerThread.stopAllTickerThreads();
					Driver d = Driver.getInstance();
					Iterator<Service> services = d.connectionListeners();
					while(services.hasNext()) services.next().stopService();
					d.closeAllConnections();
					System.exit(1); // World will save player upon exit
				}		
		}}};
}
