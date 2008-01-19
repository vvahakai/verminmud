package org.vermin.wicca.remote;

/**
 * WICCA component for normal mud output.
 */
@ComponentID(id=2)
public interface Output {
	
	/**
	 * Insert text into the client's message window.
	 */
	@MethodID(id=1)
	public void put(String msg);
	
	/**
	 * Show an URL link to the client.
	 *
	 * @param title the title of the link
	 * @param url the url of the link
	 */
	public void showLink(String title, String url);
}
