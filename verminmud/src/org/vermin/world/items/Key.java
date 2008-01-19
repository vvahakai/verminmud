
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class Key extends DefaultItemImpl {

	public Key() {
		setDescription("A key");
		setLongDescription("It is a perfectly normal looking key.");
	}

	/* The key code. This key can only
	 * open doors that have a matching
	 * key code.
	 */
	protected String keyCode;

	public Key(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getKeyCode() {
		return keyCode;
	}

}
