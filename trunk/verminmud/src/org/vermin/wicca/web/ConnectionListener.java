package org.vermin.wicca.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.vermin.driver.Driver;
import org.vermin.driver.MudSession;
import org.vermin.driver.Service;
import org.vermin.driver.ServiceException;
import org.vermin.wicca.ClientInput;
import org.vermin.wicca.ClientInputHandler;

/**
 * Connection listener for the Web client.
 */
public class ConnectionListener implements Service {

	private ServerSocket ss;
	private int port;
	private boolean active;
	
	public ConnectionListener(int port) {
		this.port = port; 
	}
	public void startService() throws ServiceException {
		active = true;
		new Thread() {
			public void run() {
				try {
					ss = new ServerSocket(port);
					while(active) {
						Socket s = ss.accept();
						new Thread(new WebConnection(s)).start();
					}
					
				} catch(IOException ioe) {
					stopService();
					System.out.println("ConnectionListener, IOException: "+ ioe.getMessage());
				}
			}}.start();
	}
	

	public void stopService() throws ServiceException {
		// Auto-generated method stub
		
	}

	public String getName() {
		// Auto-generated method stub
		return null;
	}

	public boolean isActive() {
		// Auto-generated method stub
		return false;
	}
	
	class WebConnection extends Connection {
	
		
		public WebConnection(Socket s) throws IOException {
			super(s);
		}
		
		ClientInputHandler clientInputHandler = null;
		private Object lock = new Object();
		private String line = null;
		
		private ClientInput ci = new ClientInput(){
		
			public String readLine() {
				synchronized(lock) {
					while(line == null) 
						try {
							lock.wait();
						} catch(InterruptedException ie) {}
						
					String retval = line;
					line = null;
					lock.notify();
					return retval;
				}
			}
		
		};
		
		
		@Override
		protected void handleMessage(short component, byte method, String data) throws IOException {
			System.out.println("GOT component/method: "+component+", "+method);
			boolean handled = false;
			if(component == 0) {
				if(method == 0 /*&& clientInputHandler == null*/) {
					
					// Handle "login" message
					int ind = data.indexOf(':');
					if(ind == -1) {
						write((short)0, (byte)2, "1:15:Corrupted data.");
						return;
					}
					
					String name = data.substring(0, ind);
					String password = data.substring(ind+1);
					
					String pId = Driver.getInstance().getAuthenticator().authenticate(name, password);
					if(pId == null) {
						write((short)0, (byte)4, "0:");
						return;
					}
					
					ClientInputHandler cih = null;
					try {
						cih = (ClientInputHandler) Driver.getInstance().getLoader().get(pId);
					} catch(Exception e) {
						System.out.println("Load failed: "+e.getMessage());
					}
					if(cih == null) {
						write((short)0, (byte)2, "1:23:Unable to load player.");
						return;
					}
					
					write((short)0, (byte)6, "0:"); // login accepted message
					
					clientInputHandler = cih;
					Thread t = new Thread(new MudSession(new WebClientOutput(this), ci, clientInputHandler));
					t.start();
					
					
					handled = true;
				} else if(method == 1 && clientInputHandler != null) {
					
					// handle "put line" method
					synchronized(lock) {
						while(line != null) { // wait until the previous line has been fetched
							try {
								lock.wait();
							} catch(InterruptedException ie) {}
						}
						line = data;
						lock.notify();
					}
					handled = true;
				}
			}
			
			if(!handled) {
				write((short)0,(byte)2, "1:15:Protocol error.");
				System.out.println("GOT unrecognized component/method pair: "+component+", "+method);
			}
		}
	}
}
		
	

