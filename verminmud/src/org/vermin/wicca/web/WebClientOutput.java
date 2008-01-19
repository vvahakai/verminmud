package org.vermin.wicca.web;

import org.vermin.mudlib.Item;
import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;
import org.vermin.wicca.Session;
import org.vermin.wicca.remote.Chat;
import org.vermin.wicca.remote.Inventory;
import org.vermin.wicca.remote.Output;
import org.vermin.wicca.remote.State;

public class WebClientOutput implements ClientOutput {

	private Chat chat = new org.vermin.wicca.web.Chat(this);

	private Inventory inventory = new Inventory(){
	
		public void clear() {
			write((short)0, (byte)5, "alert('Inventory.clear()')");
		}
	
		public void remove(Item item) {
			write((short)0, (byte)5, "alert('Inventory.remove(Item)')");
		}
	
		public void add(Item item) {
			write((short)0, (byte)5, "alert('Inventory.add(Item)')");
		}
	};
	
	private Output output = new org.vermin.wicca.web.Output(this);
	
	private State state = new org.vermin.wicca.web.State(this);
	
	private Battle battle = new org.vermin.wicca.web.Battle(this);
	
	private Connection connection;

	private Session session;
	
	public WebClientOutput(Connection c) {
		this.connection = c;
	}
	
	
	public void write(short component, byte method, String data) {
		connection.write(component, method, data);
	}
	
	public Chat chat() {
		return chat;
	}

	public Inventory inventory() {
		return inventory;
	}

	public Output output() {
		return output;
	}
	
	public State state() {
		return state;
	}
	
	public Battle battle() {
		return battle;
	}
	
	public void put(String text) {
		output().put(text);
	}

	public void prompt(String text) {

	}

	public void changeHandler(ClientInputHandler handler) {

	}

	public void close() {
		connection.close();
	}

	public void hideInput() {

	}

	public void showInput() {

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
