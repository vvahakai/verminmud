package org.vermin.wicca.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Provides a low-level WICCA connection.
 */
public class Connection implements Runnable {

	private static final Logger log = Logger.getLogger(Connection.class.getName());
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	private boolean active; 
	public Connection(String host, int port) throws IOException {
		socket = new Socket(host, port);
		initialize();
	}
	
	public Connection(Socket s) throws IOException {
		socket = s;
		initialize();
	}
	
	private void initialize() throws IOException {
		in = socket.getInputStream();
		out = socket.getOutputStream();
		active = true;
	}
	
	protected void handleMessage(short component, byte method, String data) throws IOException {
		System.out.println("INCOMING component: "+component+", method: "+method+", data: "+data);
	}
	
	public synchronized void write(short component, byte method, String data) {
		try {
		byte[] byteData = data.getBytes("UTF-8");
		
		System.out.println("Writing: "+component+", "+method+", size: "+byteData.length);
		int packetSize = byteData.length + 3;
		
		out.write(packetSize >> 24);
		out.write(packetSize >> 16);
		out.write(packetSize >> 8);
		out.write(packetSize);
		
		out.write(component >> 8);
		out.write(component);
		
		out.write(method);
		
		out.write(byteData);
		out.flush();
		} catch(IOException ioe) {
			handleIOException(ioe);
		}
	}
	
	public void run() {
		while(active) {
			read();
		}
	}
	
	private void read() {
		try {
			
			int first = in.read();
			
			if(first == -1) {
				active = false;
				close();
				return;
			}
			
			System.out.println("Starting to read a packet, first byte is: "+first);
			
			int packetSize = first<<24 | in.read()<<16 | in.read()<<8 | in.read();
			System.out.println("Got packet of size: "+packetSize);
			
			short component = (short) (in.read()<<8 | in.read());
			int method = in.read();
			
			byte[] dataBytes = new byte[packetSize-3];
			int off = 0;
			while(off < dataBytes.length) {
				int read = in.read(dataBytes, off, dataBytes.length-off);
				off += read;
			}
				
			String data = new String(dataBytes, "UTF-8");
			
			handleMessage(component, (byte) method, data);
			
		} catch(IOException ioe) {
			handleIOException(ioe);
		}
	}
	
	public void close()  {
		try {
			in.close();
			out.close();
		} catch(IOException ioe) {
			log.info("Failed to close WICCA web connection properly, ignoring. Exception message: "+ioe.getLocalizedMessage());
		}
		active = false;
	}
	
	private void handleIOException(IOException ioe) {
		System.out.println("IOException: "+ioe.getMessage());
		active = false;
	}
	
	/**
	 * main method for testing
	 */
	public static void main(String[] args) {
		try {
			if(args[0].equals("server")) {
				ServerSocket ss = new ServerSocket(12345);
				Connection c = new Connection(ss.accept());
				new Thread(c).start();
			} else if(args[0].equals("client")) {
				Connection c = new Connection("localhost", 12345);
				short comp = Short.valueOf(args[1]).shortValue();
				byte method = Byte.valueOf(args[2]).byteValue();
				
				c.write(comp, method, args.length > 3 ? args[3] : "jotain ihan muuta");
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
