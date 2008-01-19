/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib;

/**
 * Anything that can contain money must implement this.
 * 
 * @author tadex
 *
 */
public interface Purse {

    /**
     * The money item.
     * @return a valid <code>Money</code> instance
     */
    public Money getMoney();
}
