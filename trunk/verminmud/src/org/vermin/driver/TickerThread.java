/* TickerThread.java
	14.1.2002	Tatu Tarvainen / Council 4
	
	Ticks objects from the TickerServices tick queue.
*/

package org.vermin.driver;

public class TickerThread extends Thread {
	
	private TickService ts;
	private Queue queue;
	private long interval;
	
	private int id;
	
	public TickerThread(TickService ts, Queue queue) {
		this.queue = queue;
		this.ts = ts;
		this.interval = queue.getInterval();
	}
	
	public void run() {
		boolean active = true;
		
		TickEntry tick;
		
		long elapsed;
		boolean again;
		
		while(active) {
			tick = ts.getNextTickObject(queue);
			
			elapsed = System.currentTimeMillis() - tick.last;
			
			if(elapsed < interval) {
				try {	
               sleep(interval-elapsed); 
            } catch(InterruptedException e) {
					System.out.println("[TickerThread.run] Interrupted.");
				}	/* nothing we can do? let's let the object have its tick a little early */
			}
			
			tick.last = System.currentTimeMillis();
			try {
				//if(queue != Tick.REGEN) // log, but no REGENs
				//	System.out.println("TICKING "+tick.obj+" with queue: "+queue);

				again = tick.obj.tick(queue);
			} catch(Exception ex) {
				System.out.println("::: EXCEPTION IN TICK HOOK ::: >> "+ex.getMessage());
				ex.printStackTrace();
				again = false;
			}
			
			if(again) {
				ts.addTick(tick, queue);
			} else {
				ts.removeTickable(tick.obj, queue);
			}
		}
	}
}
