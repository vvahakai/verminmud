/**
 * An exception for indicating load failures.
 * 13.7.2003 Tatu Tarvainen / Council 4
 *
 */
package org.vermin.driver;

public class LoadException extends RuntimeException {

	public LoadException(String msg) {
		super(msg);
	}
    
    public LoadException(String msg, Exception cause) {
        super(msg,cause);
    }
}
