package org.vermin.io;

/**
 * ClientConnection abstracts methods needed by a telnet
 * connection in order to implement non-telnet connection types
 * which behave in the same way.
 */
public interface ClientConnection {

	/**
	 * Read a string of input until a newline is encountered.
	 * 
	 * @return the read string, without the trailing newline
	 */
	public String readLine();

	
	public void print(String str);
	public void print(int ch);
	public void write(int ch);
	public void flush();
	public void println(String str);
	public void close();
	public void goAhead();

	/**
	 * Tell the other end to hide user input.
	 * Used for password fields.
	 */
	public void hideInput();

	/**
	 * Tell the other end to show user input.
	 * Used after a password field has been asked
	 * to re-enable echoing.
	 */
	public void showInput();
}
