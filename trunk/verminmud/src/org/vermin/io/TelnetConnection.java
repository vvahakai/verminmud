/* TelnetConnection.java 5.10.2002
 *
 * 
 */


package org.vermin.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.net.Socket;


/**
 * A minimal telnet implementation. This allows us to use
 * telnet commands to supress echo and not get confused by
 * telnet commands.
 */
public class TelnetConnection extends AbstractConnection {
	
	/* Telnet control codes */
	protected static final int IAC	= 255;	/* interpret as command: */
	protected static final int DONT	= 254;	/* you are not to use option */
	protected static final int DO	   = 253;	/* please, you use option */
	protected static final int WONT	= 252;	/* I won't use option */
	protected static final int WILL	= 251;	/* I will use option */
	protected static final int SB	   = 250;	/* interpret as subnegotiation */
	protected static final int GA	   = 249;	/* you may reverse the line */
	protected static final int EL	   = 248;	/* erase the current line */
	protected static final int EC	   = 247;	/* erase the current character */
	protected static final int AYT	= 246;	/* are you there */
	protected static final int AO	   = 245;	/* abort output,but let prog finish*/
	protected static final int IP	   = 244;	/* interrupt process--permanently */
	protected static final int BREAK = 243;	/* break */
	protected static final int DM	   = 242;	/* data mark--for connect. */
	protected static final int NOP	= 241;	/* nop */
	protected static final int SE	   = 240;	/* end sub negotiation */
	protected static final int EOR   = 239;   /* end of record  */
	protected static final int ABORT = 238;	/* Abort process */
	protected static final int SUSP	= 237;	/* Suspend process */
	protected static final int EOF	= 236;	/* End of file */

	/* Telnet options (incomplete list) */
	protected static final int ECHO = 1;
	protected static final int SGA  = 3; /* suppress go ahead */

	/* Ascii controls */
	protected static final int CR = 13;
	protected static final int LF = 10;
	
	/* OTHER */
	protected static final int EOS = -1; /* End Of Stream */
	
	private InputStream input;
	
	private boolean eos = false; // set true when EOS is encountered;
	
	/**
	 * Construct a new telnet connection on the specified
	 * socket.
	 */
	public TelnetConnection(Socket socket) throws IOException {
		super(new PrintStream(socket.getOutputStream()));
		input = new BufferedInputStream(socket.getInputStream());

		output.write(IAC);
		output.write(WILL);
		output.write(EOR);
		output.flush();
	}
	
	/**
	 * Read a string of input until a newline is encountered.
	 * 
	 * @return the read string, without the trailing newline
	 */
	public String readLine() {
	
		if(eos) return null;
		
		StringBuilder string = new StringBuilder(64);
		
		try {
			
			boolean done = false;
			while(!done) {
				int in = input.read();
				
				// System.out.println("read int: "+in);

				switch(in) {

				  case IAC: 
					  handleIAC();
					  break;
					  
				  case CR: 
					  done = true;
					  break;

					  /* ignore linefeed and null */
				  case LF: 
				  case 0:
					  System.out.println("LF or null character: "+in);
					  break;

				  case EOS:
					  eos = true;
					  return null;
					  
				  default:
					  string.append((char) in);

				}
			}

			return string.toString();
		} catch(IOException ie) {
			System.out.println("[TelnetConnection] IOException: "+ie.getMessage());
			eos = true;
			return null;
		}
	}
		
	public void close() {
		try {
			input.close();
			output.close();
		} catch(IOException ie) {}
	}


	private void handleIAC() throws IOException {

		int in = input.read();
		
		//System.out.println("GOT IAC : "+(char)in);
		switch(in) {

			/* consume a subnegotiation */
		  case SB:
			  int test = SB;
			  
			  short fuck = 0;
			  while(test != SE) {
				  test = input.read();
				  fuck++;
				  if(fuck > 9) {
					  System.out.println("TELNET NEGOTIATION FAILURE!!!! consumed 10  bytes");
					  break;
				  }
			  }
			  
			  //System.out.println("Subnegotioation Begins!");
				break;
			  
		  case SE:
			  //System.out.println("Subnegotiation Ends!");
			   break;
			  
			  /* consume options */
		  case DONT:
			  input.read();
			  break;
		  case DO: 
			  input.read();
			  break;
		  case WILL:
			  input.read();
			  break;
		  case WONT:
			  input.read();
			  break;
			  
		  default:
			  System.out.println("Don't know this one: "+(int) in);
		}
	}
	
	/**
	 * Tell the other end to hide user input.
	 * Used for password fields.
	 */
	public void hideInput() {
		//System.out.println("hideInput()");
		int[] b = new int[] {
			// IAC, SB, WILL, ECHO, SE
			IAC, WILL, ECHO
		};

		for(int i=0; i<b.length; i++)
			output.write(b[i]);

		output.flush();
	}

	/**
	 * Tell the other end to show user input.
	 * Used after a password field has been asked
	 * to re-enable echoing.
	 */
	public void showInput() {
		//System.out.println("showInput()");
		int[] b = new int[] {
			// IAC, SB, WONT, ECHO, SE
			IAC, WONT, ECHO,
			//IAC, DO, ECHO
		};

		for(int i=0; i<b.length; i++)
			output.write(b[i]);
		output.flush();
	}
		
	/**
	 * Tell the other end to go ahead.
	 * This is used in prompts when we are not
	 * doing anymore output.
	 */
	public void goAhead() {
		//System.out.println("GO AHEAD");
		output.write(IAC);
		output.write(EOR); // was GA
		output.flush();
	}
			 

}
