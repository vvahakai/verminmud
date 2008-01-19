package org.vermin.wicca;

/**
 * Interface for WICCA client input.
 * The input is done as string commands, one command per line.
 */
public interface ClientInput {

	/**
	 * Read a line.
	 * 
	 * @return the read line or null on end of stream
	 */
	public String readLine();
}
