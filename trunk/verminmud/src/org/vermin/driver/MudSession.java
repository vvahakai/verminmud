/* MudSession.java
	2.7.2005 Tatu Tarvainen
	
	Retrofitted to WICCA from old Connection code. 
	
*/
package org.vermin.driver;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Formatter;

import org.vermin.mudlib.World;
import org.vermin.wicca.ClientInput;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.wicca.Session;
import org.vermin.wicca.telnet.OutputFilter;

public class MudSession implements Runnable, Session {

	private ClientOutput clientOutput;
	private ClientInputHandler player;
	private ClientInput clientInput;
	
	
	private AuthenticationProvider authenticator;
	
	private Driver driver;


	private boolean motdShown = false;

	private long start; // timestamp of connection start
	private long lastCommand; // timestamp of last client input
	
	private static PrintStream exceptionLog;
	static {
		try {
			exceptionLog = new PrintStream(new FileOutputStream("exceptions.log"));
		} catch(IOException ioe) {
			exceptionLog = System.out;
			System.out.println("Unable to open exception log for writing, logging to standard output.");
		}
	};

	private synchronized static void logException(Exception e, String info) {
		exceptionLog.println("### EXCEPTION BEGINS ###");
		exceptionLog.println("   Time:    "+new java.util.Date());
		exceptionLog.println("   Type:    "+e.getClass().getName());
		exceptionLog.println("   Info: "+info+"\n");
		exceptionLog.println("Stacktrace:");
		e.printStackTrace(exceptionLog);
		exceptionLog.println("### EXCEPTION ENDS ###\n");
	}

	public MudSession(ClientOutput co, ClientInput ci, ClientInputHandler cih) throws IOException {

		co.setSession(this);
		
		this.driver = Driver.getInstance();

		authenticator = driver.getAuthenticator();

		clientOutput = co;
		clientInput = ci;
		player = cih;

		cih.setClientOutput(co);
		
		lastCommand = System.currentTimeMillis();
		start = System.currentTimeMillis();
		
		
		cih.startSession();
	}

	/* Main client method */
	public void run() {

		showMOTD();
		boolean handled;
		String line = clientInput.readLine();
		
		while(line != null) {
			lastCommand = System.currentTimeMillis();
			
			try {
				if(!line.matches("^\\s*$")) // filter out lines of pure whitespace
					handled = player.clientCommand(line);
				else
					handled = true;
			} catch(Exception ex) {
				
				if(player instanceof ExceptionHandler)
					((ExceptionHandler)player).handleException(ex, null);
				else {
					logException(ex, line);

					put("ERROR: Logged exception "+ex.getClass().getName()+": "+ex.getMessage());
				}
				handled = true;
			}
			
			if(!handled) syntaxError();
			player.prompt();
			line = clientInput.readLine();
		}
		World.log("Connection closed for: "+player);
		player.endSession();
	}
	

	public void close() {
		// FIXME: implement close
		//connection.close();
	}
	
	public void setClientInputHandler(ClientInputHandler handler) {
		player = handler;
		player.setClientOutput(clientOutput);

		if(!motdShown) showMOTD();
	}

	private void syntaxError() {
		clientOutput.put(driver.getTypoMessage());
	}
	

		
	public void showMOTD() {
		motdShown = true;
		try {
			BufferedReader in = new BufferedReader(new FileReader("misc/motd"));
			String line = in.readLine();
			while(line != null) {
				put(line);
				line = in.readLine();
			}
			in.close();
		} catch(Exception e) {}
	}

	public void put(String text) {
		
		clientOutput.put(text);
	
	}
	
	public void printf(String fmt, Object ... args) {

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(fmt, args);
		put(sb.toString());
		
	}

	public void prompt(String text) {
		clientOutput.prompt(text);
		
	}

	
	public void showRandomLogo() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("misc/verminlogin1"));
		
			String line = null;
		
			line = in.readLine();
		
			while(line != null) {
				clientOutput.put(line);
				line = in.readLine();
			}
			in.close();
		} catch(Exception e) {}
	}

	/**
	 * Returns the idle time in milliseconds.
	 */
	public long getIdleTime() {
		return System.currentTimeMillis() - lastCommand;
	}

	/**
	 * Returns the connection duration in milliseconds.
	 */
	public long getDuration() {
		return System.currentTimeMillis() - start;
	}

	public boolean isSessionActive() {
		return true;
	}
    public MudSession getConnection() {
        return this;
    }

	public void setClientOutput(ClientOutput co) {
		clientOutput = co;
	}

	public void setClientInput(ClientInput ci) {
		clientInput = ci;
	}
}
