package org.vermin.io.brpc;

import java.util.*;
import java.io.*;
import java.net.Socket;

/**
 * A remote procedure call client using a simple BEncoded
 * protocol.
 * 
 * @author Tatu Tarvainen
 */
public class Client {

    private boolean open = true;
	private BEncoder out;
	private BDecoder in;

	/**
	 * Construct a new client to the given socket.
	 *
	 * @param s the connection to use
	 */
	public Client(Socket s) throws IOException {
		out = new BEncoder(s.getOutputStream());
        in = new BDecoder(s.getInputStream());
	}

    /**
     * Construct a new client with the given input and
     * output objects.
     * 
     * @param in the incoming <code>BDecoder</code>
     * @param out the outgoing <code>BEncoder</code>
     */
    public Client(BDecoder in, BEncoder out) {
        this.in = in;
        this.out = out;
    }
    
	/**
	 * Call the given method with the given arguments.
	 * On success, returns the value returned by the remote call.
	 * All arguments must be bencodable objects. 
     * 
     * @see BEncoder
	 * @param method the name of the method
	 * @param args arguments to the method
	 */
	public synchronized Object call(String method, Object ... args) {
		if(!open)
			throw new IllegalStateException("This client has already been closed.");

		try {
			Vector l = new Vector(args.length+1);
			l.addElement(method);
			for(int i=0; i<args.length; i++)
				l.addElement(args[i]);
			out.encode(l);
			out.flush();
            System.out.println("CALL ENCODING DONE");
			return in.decode();
		} catch(IOException ioe) {
            throw new RemoteException("Unable to encode remote procedure call.", ioe);
		}
	}
	
	/**
	 * Close this client. 
	 * Notifies the server to shutdown this connection and
	 * closes the socket.
	 * No further calls can be made after this.
	 */
	public synchronized void close() {
		try {
			out.encode("close");
			out.close();
			in.close();
		} catch(IOException ioe) {
			// We don't really care if closing the connection doesn't work.
		} finally {
            open = false;
        }
	}
}
