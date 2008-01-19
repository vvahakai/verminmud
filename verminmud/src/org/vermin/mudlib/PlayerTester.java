package org.vermin.mudlib;

import org.vermin.driver.AuthenticationProvider;
import org.vermin.driver.Driver;

public class PlayerTester {

	private Player p;
	private String pId;
	
	public void test(String id) {
		pId = id;
		
		p = null;
		
		try { 
			p = (Player) World.get(id);	
		} catch(Exception e) {
			failure("Unable to load player", e);
		}
		
		if(p == null) failure("World.get() for player returned null.");
		
		testStartingRoom();

		testRace();
		
		success();
	}
	
	private void testStartingRoom() {
		try {
			Room r = (Room) World.get(p.getStartingRoom());
			r.add(p);
			
		} catch(Exception e) {
			failure("Starting room problem", e);
		}
	}
	
	private void testRace() {
		if(p.getRace() == null) 
			failure("Player race is null!");
	}
	private void failure(String msg) {
		failure(msg, null);
	}
	private void failure(String msg, Exception e) {
		World.log("TEST FAILURE [player id="+pId+"]: "+msg);
		if(e != null)
			e.printStackTrace();
		throw new RuntimeException("TEST FAILED");
	}
	private void success() {
		World.log("Player '"+pId+"' successfully tested.");
	}
	
	public static void runTests() {
		World.log("STARTING TESTING");
		PlayerTester pt = new PlayerTester();
		
		try {
			AuthenticationProvider ap = Driver.getInstance().getAuthenticator();
			
			for(String name : ap.getPlayerNames()) {
				World.log("Test player: "+name);
				pt.test(ap.getIdForRecord(name));
			}
			
			World.log("EVERYTHING IS OK");
		} catch(RuntimeException re) {
			World.log("!!!!!!! THE MUD IS BROKEN !!!!!!!!");
		}
	}
}
