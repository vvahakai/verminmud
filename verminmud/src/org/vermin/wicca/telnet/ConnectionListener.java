/* ConnectionListener.java
	5.1.2002	Tatu Tarvainen / Council 4
	
	Forks new MUD connections.
*/
package org.vermin.wicca.telnet;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import org.vermin.driver.AnsiOutputFilter;
import org.vermin.driver.Driver;
import org.vermin.driver.MudSession;
import org.vermin.driver.Service;
import org.vermin.driver.ServiceException;
import org.vermin.io.TelnetConnection;
import org.vermin.wicca.ClientInput;
import org.vermin.wicca.ClientInputHandler;

public class ConnectionListener implements Service {
	private static final Logger log = Logger.getLogger(ConnectionListener.class.getName());
	private ServerSocket ss;
	private Driver driver;
	private boolean active;
	private int port;
	private int backlog;

	public ConnectionListener(Driver driver, int port, int backlog) throws IOException {
		this.driver = driver;
		this.port = port;
		this.backlog = backlog;

	}

	private void startConnection(final Socket s) {
		new Thread(new Runnable(){
			public void run() {
				try {
					final TelnetConnection tc = new TelnetConnection(s);
					LoginHandler lh = new LoginHandler(driver, tc);
					ClientInputHandler cih = lh.login();
					if(cih != null) {
						
						new MudSession(new TelnetClientOutput(lh.getName(), tc, new AnsiOutputFilter()), 
								new ClientInput(){
									public String readLine() {
										return tc.readLine();
									}
								}, cih).run();
						
					}
				} catch(IOException ioe) {
					System.out.println(".... telnet connection thread IO exception ... "+ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		}).start();
	}
	
	public void startService() throws ServiceException {
		active = true;
		new Thread() {
			public void run() {
				try {
					ss = new ServerSocket(port, backlog);
					while(active) {
						Socket s = ss.accept();
						startConnection(s);
					}
				} catch(IOException ioe) {
					stopService();
					System.out.println("ConnectionListener, IOException: "+ ioe.getMessage());
				}
			}}.start();
	}

	public boolean isActive() {
		return active;
	}

	public void stopService() {
		try {
			ss.close();
		} catch(IOException ioe) {
			log.warning("Unable to close server socket: "+ioe.getLocalizedMessage());
		}
		active = false;
	}

	public String getName() {
		return "ConnectionListener";
	}
				
}
