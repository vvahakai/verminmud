package org.vermin.wicca.telnet;

import org.vermin.mudlib.Item;

/**
 * The telnet implementation of WICCA Inventory.
 * All operations are NOPs because the telnet version does
 * not need structured notification of inventory events.
 */
public class Inventory extends AbstractTelnetComponent implements
		org.vermin.wicca.remote.Inventory {

	public Inventory(TelnetClientOutput co) {
		super(co);
	}
	
	public void add(Item item) {}
	public void remove(Item item) {}
	public void clear() {}

}
