package org.vermin.wicca.telnet;

import org.vermin.driver.AnsiOutputFilter;
import org.vermin.io.TelnetConnection;
import org.vermin.util.DummyProxy;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.wicca.Session;
import org.vermin.wicca.remote.Battle;
import org.vermin.wicca.remote.Chat;
import org.vermin.wicca.remote.Inventory;
import org.vermin.wicca.remote.Output;
import org.vermin.wicca.remote.State;

public class TelnetClientOutput implements ClientOutput, Output {

	private TelnetConnection connection;
	private OutputFilter of;
	private String name; // name of the player whose client output this is
	
	private Chat chat = new org.vermin.wicca.telnet.Chat(this);
	private Inventory inventory = new org.vermin.wicca.telnet.Inventory(this);
	private State state = new org.vermin.wicca.telnet.State(this);
	
	private Session session;
	
	private Battle battle = (Battle) DummyProxy.create(Battle.class);
	
	public TelnetClientOutput(String name, TelnetConnection tc, AnsiOutputFilter filter) {
		this.connection = tc;
		this.of = filter;
		this.name = name;
	}

	String getName() { return name; }
	
	public Chat chat() {
		return chat;
	}

	public Inventory inventory() {
		return inventory;
	}

	public State state() {
		return state;
	}
	
	public Output output() {
		return this;
	}
	
	public void showLink(String title, String url) {
		put(title+": "+url);
	}
	
	// FIXME: implement telnet battle messaging wicca style
	public Battle battle() { return battle; }
	
	public void put(String text) {
		
		if(text == null || text.length() < 1)
			return;
		
		if(text.charAt(text.length()-1) == '\n')
			of.filterTo(connection, text.substring(0, text.length()-1));
		else
			of.filterTo(connection, text);
		
		of.reset(connection);
		connection.print("\r\n");
		connection.flush();
		
	}

	public void prompt(String text) {

		of.filterTo(connection, text); 
		connection.goAhead();
		connection.flush();
	}

	public void changeHandler(ClientInputHandler handler) {
		
	}

	public void close() {
		connection.close();
	}

	public void hideInput() {
		connection.hideInput();
	}

	public void showInput() {
		connection.showInput();
	}

	public boolean isSessionActive() {
		return false;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	
}
