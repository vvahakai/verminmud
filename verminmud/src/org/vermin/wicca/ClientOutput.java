/* ClientOutput.java
 * 8.3.2003 Tatu Tarvainen
 *
 */
package org.vermin.wicca;

import org.vermin.wicca.remote.Battle;
import org.vermin.wicca.remote.Chat;
import org.vermin.wicca.remote.Inventory;
import org.vermin.wicca.remote.Output;
import org.vermin.wicca.remote.State;

/**
 * WICCA client output interface.
 * All output, structured and otherwise, to clients is
 * done through this interface.
 */
public interface ClientOutput {

	/**
	 * Returns the WICCA Chat component for this
	 * client.
	 */
	public Chat chat();
	
	/**
	 * Returns the WICCA Inventory component for this client.
	 */
	public Inventory inventory();

	/**
	 * Returns the WICCA player state component for this client.
	 */
	public State state();
	
	/**
	 * Returns the WICCA Battle component for this client.
	 */
	public Battle battle();

	/**
	 * Returns the WICCA Output component for this client.
	 */
	public Output output();
	
	/**
	 * Output text to the client with proper filtering.
	 *
	 * @param text the text to output
	 */
	public void put(String text);

	/**
	 * Output text as a prompt.
	 *
	 * @param text the prompt
	 */
	public void prompt(String text);

	/**
	 * Change the input handler.
	 *
	 * @param handler the new input handler
	 */
	public void changeHandler(ClientInputHandler handler);

	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Hide user input.
	 */
	public void hideInput();

	/**
	 * Show user input.
	 */
	public void showInput();

	/**
	 * Check if a client is connected.
	 *
	 * @return true if a client is connected, false otherwise.
	 */
	public boolean isSessionActive();

    public Session getSession();

	public void setSession(Session session);
}
