package org.vermin.wicca.remote;

import org.vermin.mudlib.Player;

/**
 * A WICCA component for updating the current
 * player state for prompting purposes.
 */
public interface State {

	/**
	 * Update the relevant player state fields
	 * to the client.
	 * 
	 * @param who the player whose fields to update
	 */
	public void update(Player who);
}
