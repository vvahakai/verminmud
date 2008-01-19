package org.vermin.wicca.telnet;

import java.util.Iterator;

import org.vermin.driver.Driver;
import org.vermin.io.TelnetConnection;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.PlayerCreator;
import org.vermin.mudlib.World;
import org.vermin.wicca.ClientInputHandler;

public class LoginHandler {
	
	private TelnetConnection connection;
	private Driver driver;
	private String name = null;
	
	public LoginHandler(Driver driver, TelnetConnection conn) {
		this.driver = driver;
		this.connection = conn;
	}
	
	/**
	 * Authentication asks user for username and password.
	 * After verifying login information the players object
	 * is loaded.
	 *
	 * @return the loaded object or null on login failure
	 */
	public ClientInputHandler login() {
		
		driver.showRandomLogo(connection);
		
		
		ClientInputHandler player = null;
		
		boolean loginFinished = false;
		while(!loginFinished) {
			name = null;
			while(name == null || name.trim().length() == 0) {
				connection.print("> ");
				connection.flush();
				name = connection.readLine();
				if(name == null)
					return null;
			}
			
			if(name.equalsIgnoreCase("n")) {
				// create new player
				PlayerCreator pc = new PlayerCreator(connection);
				player = pc.create();
				name = ((Player)player).getName();
				loginFinished = true;
			} else if(name.equalsIgnoreCase("l")) {
				Iterator it = World.getPlayers();
				StringBuffer sb = new StringBuffer();
				int playerCount = 0;
				while(it.hasNext()) {
					Player p = (Player) it.next();
					String surnameSeparator = "";
					if(p.getSurname().length() != 0) surnameSeparator = " ";
					sb.append(p.getName()+surnameSeparator+p.getSurname()+" the "+p.getRace().getName()+" "+p.getTitle()+"\n");
					playerCount++;
				}
				if(sb.length() == 0) {
					sb.append("No players logged in.\n");
				} else {
					
					sb = new StringBuffer("Currently logged in:\n").append(sb).append("Total "+playerCount+" players.\n");
				}
				connection.print(sb.toString());
				connection.flush();
			} else {
				// login for existing players
				
				connection.print("Password: ");
				connection.flush();
				connection.hideInput();
			
				String password = connection.readLine();
				connection.showInput();
			
				String pId = password == null ? null : Driver.getInstance().getAuthenticator().authenticate(name, password);
					
				if(pId == null) {
					connection.println("Login incorrect. Goodbye.");
					connection.flush();
					connection.close();
					return null;
				}
			
				Object pObj = null;
	            try {
	                pObj = driver.getLoader().get(pId);
	            } catch(Exception e) {            	            	
	                System.err.println("Unable to load player: "+e.getMessage());
	                System.err.println("Cause: "+e.getCause());
	                e.printStackTrace();
	            }
	            
				if(pObj == null) {
					connection.println("Unable to load your player object, please send email to tadex@verminmud.org");
					connection.println("Please include the name of your character and whether you tried to login on the testing or the stable server.");
					connection.flush();
					System.err.println("[Connection] Loader returned null player object. Closing client.");
					return null;
				}
				
				try {
					player = (ClientInputHandler) pObj;
				} catch(ClassCastException cc) {
					System.err.println("[Connection] Object "+pId+" is not a ClientInputHandler.");
					connection.println("Your player file seems to be corrupt, please send email to tadex@verminmud.org");
					connection.println("Please include the name of your character and whether you tried to login on the testing or the stable server.");
					connection.flush();
					return null;
				}
				loginFinished = true;
			}
		}	
		
		connection.println("Welcome to Vermin MUD!");
		connection.flush();
		return player;
		
	}

	public String getName() {
		return name;
	}

}
