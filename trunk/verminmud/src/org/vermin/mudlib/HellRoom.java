/* HellRoom.java
	3.2.2002
	
	The place where you go when you die.
*/
package org.vermin.mudlib;

import java.util.Vector;

public class HellRoom extends DefaultRoomImpl {

	private String moveToRoom = "";

	public void add(MObject o) {
		if(o instanceof DefaultPlayerImpl) {
			((DefaultPlayerImpl) o).handleAll(this);
		}
	}
	
	public boolean action(MObject who, Vector params) {
		DefaultPlayerImpl p = (DefaultPlayerImpl) who;
		
		if(params.get(0).toString().equals("pray")) {
			//p.setRoom((Room) World.get(moveToRoom)); // move to cs
			p.setParent((Room) World.get(moveToRoom));
			p.setExperience((long) (0.25*(double)p.getExperience()));
			p.setHp(p.getMaxHp()/10);
			if(p.getHp() == 0)
				p.setHp(1);
			p.notice("You have been given a new chance in life. Use it wisely.");
			p.unhandleAll();
			remove(who);
			return true;
		}
		return false;
	}

	public boolean mayTeleport(Living who) {
		return false;
	}
	
	public void explore(Living explorer) {}

}
