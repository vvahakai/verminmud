package org.vermin.wicca.remote;

/**
 * Provides chat services.
 */
@ComponentID(id=1)
public interface Chat {

	/**
	 * Called when a message is added to a channel.
	 * 
	 * @param who the name of the player who is talking
	 * @param channel the name of the channel
	 * @param text what was said
	 */
	@MethodID(id=1)
	public void message(String who, String channel, String text);
	
	/**
	 * Called when someone tells the player something.
	 * 
	 * @param source the name of the player doing the telling
	 * @param dest the name of the player receiving the told message
	 * @param text what was said
	 */
	@MethodID(id=2)
	public void tell(String source, String dest, String text);
	
}
