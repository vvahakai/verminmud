package org.vermin.mudlib;


/**
 * A one way exit.
 */
public class OneWayExit extends DefaultObjectImpl implements Exit {

	private String from, to, direction;
	
	public OneWayExit() {}
	
	public OneWayExit(String from, String to, String direction) {
		this.from = from;
		this.to = to;
		this.direction = direction;
	}
	
	public boolean tryMove(Living who, String roomId) {
		return true;
	}

	public String getTarget(String roomId) {
		return roomId.equals(from) ? to : null;
	}

	public String getDirection(String roomId) {
		return roomId.equals(from) ? direction : null;
	}
	
	public String getArrivalDirection(String roomId) {
		return direction;
	}

	public String getPassMessage(String roomId) {
		return null;
	}

	public Types getType() {
		return Types.EXIT;
	}
		
}
