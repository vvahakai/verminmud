/*
 * Created on 21.1.2005
 *
 */
package org.vermin.io.brpc;

/**
 * An exception that can occur in remote calls.
 * 
 * @author Tatu Tarvainen
 *
 */
public class RemoteException extends RuntimeException {
    private Exception nested;
    
    public RemoteException(String msg, Exception nested) {
        super(msg);
        this.nested = nested;
    }
    
    public Exception getNestedException() {
        return nested;
    }
}
