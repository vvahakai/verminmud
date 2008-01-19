/*
 * Created on 5.2.2005
 *
 */
package org.vermin.mudlib;

import org.vermin.driver.Queue;

/**
 * @author tadex
 *
 */
public enum Tick implements Queue {
    
    /**
     * The 4 second battle tick queue. 
     * CHANGED: from 2 seconds, 12.7.2005, TT
     */
    BATTLE(4),
    
    /**
     * The regen tick queue.
     */
    REGEN(10),
    
    /**
     * The unload check queue.
     */
    GARBAGE(15*60);
    
    private long interval;
    
    private Tick(int seconds) {
        interval = seconds * 1000;
    }
    
    /**
     * Returns the interval in milliseconds.
     * 
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }
    
    
}
