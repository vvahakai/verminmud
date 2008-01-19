/* ClientInputHandler.java
 * 8.3.2003 Tatu Tarvainen
 *
 * Minimal interface for objects that want to 
 * listen to user commands and output text to users.
 */
package org.vermin.wicca;


/**
 * Provides the client input interface for WICCA.
 * Any game objects (namely players) that want to handle
 * client input must implement this interface.
 * 
 * XXX: This should propably be renamed to better reflect
 * the important nature of this interface as the <b>main</b>
 * interface between WICCA and the game.
 */
public interface ClientInputHandler {

	/**
	 * Handle a command from the client.
	 * If the listener does not handle the command
	 * it should return false. An error message is
	 * automatically printed to the client.
	 *
	 * @param command the line read from client input
	 * @return true if the command was handled, false otherwise
	 */
	public boolean clientCommand(String command);

	/**
	 * Set the client output handler.
	 * The given object will forward output to the
	 * user's connection (with proper color filtering).
	 *
	 * @param output the client output handler
	 */
	public void setClientOutput(ClientOutput output);

	/**
	 * Format a suitable prompt message.
	 */
	public void prompt();
    
    /**
     * Start the session.
     * This is called when login has been done.
     */
    public void startSession();
    
    /**
     * End the session.
     * This is called when connection is being closed.
     */
    public void endSession();
    
	 
}
