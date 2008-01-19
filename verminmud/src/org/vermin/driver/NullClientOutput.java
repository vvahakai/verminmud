/**
 * NullClientOutput.java
 * 21.9.2003 Tatu Tarvainen
 *
 * An implementation of client output that does nothing.
 * This can be used to avoid null checks on every output operation.
 */
package org.vermin.driver;

import org.vermin.util.DummyProxy;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.wicca.Session;
import org.vermin.wicca.remote.Battle;
import org.vermin.wicca.remote.Chat;
import org.vermin.wicca.remote.Inventory;
import org.vermin.wicca.remote.Output;
import org.vermin.wicca.remote.State;

public class NullClientOutput implements ClientOutput {
	private Chat chat = (Chat) DummyProxy.create(Chat.class);
	private Inventory inventory = (Inventory) DummyProxy.create(Inventory.class);
	private State state = (State) DummyProxy.create(State.class);
	private Battle battle = (Battle) DummyProxy.create(Battle.class);
	private Output output = (Output) DummyProxy.create(Output.class);
	
	public State state() { return state; }
	public Chat chat() { return chat; }
	public Inventory inventory() { return inventory; }
	public Battle battle() { return battle; }
	public Output output() { return output; }
		
	public void put(String text) {}
	public void printf(String fmt, Object ... args) {}
	public void prompt(String text) {}
	public void changeHandler(ClientInputHandler handler) {}
	public void close() {}
	public void hideInput() {}
	public void showInput() {}
	public boolean isSessionActive() {
		return false;
	}
    public Session getSession() { return null; }
    public void setSession(Session s) {}
}
