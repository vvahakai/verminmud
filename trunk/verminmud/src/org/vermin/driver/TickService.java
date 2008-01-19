/* TickService.java
	14.1.2002	Tatu Tarvainen / Council 4
	
	Main tick() service for objects.
*/

package org.vermin.driver;

import java.util.*;

public class TickService {
	private static Object lock = new Object();
	
	public static class TickQueue {
		long interval;
		LinkedList queue = new LinkedList(); // the queue order
		HashSet<Tickable> tickables = new HashSet(); // tickables in this queue
	};

	// ArrayList<Queue>
	private HashMap<Queue, TickQueue> queues = new HashMap();

	public void addQueue(Queue queue) {
		TickQueue q = new TickQueue();
		q.interval = queue.getInterval();
		queues.put(queue, q);
	}
	
	public void addTick(Tickable who, Queue queue) {
		TickQueue q = queues.get(queue);
        if(q == null)
            throw new IllegalArgumentException("No such queue: "+queue);
        
		synchronized(q) {
			if(q.tickables.contains(who)) {
				//System.out.println("INFO - Tried to add double tick for: "+who);
				return;
			} else {
				//System.out.println(new Date()+" :: START TICK (queue: "+queue+") :: "+who);
				q.tickables.add(who);
				addTick(new TickEntry(who), queue);
			}
		}
	}
	
    private TickQueue getQueue(Queue queue) {
        TickQueue q = queues.get(queue);
        if(q == null)
            throw new IllegalArgumentException("No such queue: "+queue);
        return q;
    }

	void addTick(TickEntry who, Queue queue) {
		TickQueue q = getQueue(queue);
        
		synchronized(q) {
				q.queue.add(who);
				q.notify();
			}
	}
	
	public TickEntry getNextTickObject(Queue queue) {
		
        TickQueue q = getQueue(queue);
		
		/* wait here until an object is available */
		synchronized(q) {
			try {
				while(q.queue.isEmpty())
					q.wait();
			} catch(InterruptedException e) {
				System.err.println("[TickService.getNextTickObject] Interrupted. "+e.getMessage());
				return null;
			}
			return (TickEntry) q.queue.removeFirst();
		}
	}

	public void removeTickable(Tickable obj, Queue queue) {
		TickQueue q = getQueue(queue);
		synchronized(q) {
			//System.out.println(new Date()+" :: STOP TICK :: "+obj);
			q.tickables.remove(obj);
		}
	}

	public void startTickerThread(Queue queue) {
		Thread t = new TickerThread(this, queue);
		t.start();
	}
}
