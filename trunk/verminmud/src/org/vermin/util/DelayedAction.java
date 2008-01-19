/*
 * Created on 15.10.2005
 */
package org.vermin.util;

import org.vermin.driver.Driver;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;

/**
 * An utility class for performing an action n ticks later.
 * 
 * @author Jaakko Pohjamo
 */
public abstract class DelayedAction<Q extends Queue> implements Tickable {
	private int count;
	
	public DelayedAction(int ticks, Queue q) {
		count = ticks;
		Driver.getInstance().getTickService().addTick(this, q);
	}
	
	public boolean tick(Queue queue) {
		count--;
		if(count <= 0) {
			action();
			return false;
		}
		return true;
	}
	
	public abstract void action();

}
