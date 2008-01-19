/**
 * RegexRoom.java
 * 25.10.2003 Tatu Tarvainen
 *
 * Extends DefaultRoomImpl to provide regular expression command dispatch.
 */
package org.vermin.mudlib;

public abstract class RegexRoom extends DefaultRoomImpl implements RegexDispatchTarget {

	private transient RegexDispatch dispatch;

	public RegexRoom() {
		dispatch = new RegexDispatch(this);
	}

	public boolean action(MObject caller, String params) {
		return dispatch.dispatch((Player) caller, params);
	}
	
}
