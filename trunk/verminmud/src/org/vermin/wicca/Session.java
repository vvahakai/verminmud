package org.vermin.wicca;

/**
 * Represents a WICCA client session.
 * A session is an actual client connection.
 */
public interface Session {
	
	/**
	 * Get the duration of this session so far in seconds.
	 */
	public long getDuration();
	
	/**
	 * Get the idle time (the number of seconds since
	 * the client last sent input).
	 */
	public long getIdleTime();
	
	/**
	 * Set the client output for this session.
	 * This can be used if a player logs in and already
	 * has a session (in case of link failure, etc).
	 *
	 * @param co the new client output
	 */
	public void setClientOutput(ClientOutput co);
	
	/**
	 * Set the client input handler for this session.
	 * Most of the time this is the actual player object, but
	 * special handlers can be made.
	 *
	 * @param cih the new client input handler
	 */
	public void setClientInputHandler(ClientInputHandler cih);
	
	/**
	 * Set the client input for this session.
	 * 
	 * @param ci the new client input
	 */
	public void setClientInput(ClientInput ci);
	
	
}
