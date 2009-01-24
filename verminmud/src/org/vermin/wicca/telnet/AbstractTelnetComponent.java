package org.vermin.wicca.telnet;

import org.vermin.wicca.ClientOutput;

/**
 * An abstract base class for telnet implementations of
 * WICCA components.
 */
public abstract class AbstractTelnetComponent {

	private TelnetClientOutput clientOutput;
	
	public AbstractTelnetComponent(TelnetClientOutput co) {
		setClientOutput(co);
	}
	
	public void setClientOutput(TelnetClientOutput co) {
		clientOutput = co;
	}

	public ClientOutput getClientOutput() {
		return clientOutput;
	}
	
	public String getPlayer() {
		return clientOutput.getName();
	}
	

}
