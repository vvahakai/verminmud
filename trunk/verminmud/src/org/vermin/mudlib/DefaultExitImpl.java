/* DefaultExitImpl.java
	13.1.2002	Tatu Tarvainen / Council 4
	
	Default Exit implementation.
	Works between to rooms and doesn't
	restrict coming or going in any way.
*/

package org.vermin.mudlib;

import java.util.LinkedList;

public class DefaultExitImpl extends DefaultObjectImpl implements Exit {
	protected String[] rooms = new String[2];
	protected String[] directions = new String[2];
	protected String[] passMessages = new String[2];
	protected boolean[] obvious = new boolean[] { true, true };
	
   public DefaultExitImpl() {} // 0-arg constructor for serialization

	protected LinkedList[] barrierModifiers;

	public DefaultExitImpl(String room1, String dir1, String room2, String dir2) {
		rooms = new String[2];
		directions = new String[2];
		rooms[0] = room1;
		rooms[1] = room2;
		directions[0] = dir1;
		directions[1] = dir2;
	}
	
	public boolean tryMove(Living who, String roomId) {
		int ind; 
		if(rooms[0].equals(roomId)) ind = 0;
		else ind = 1;

		if(barrierModifiers == null) return true;

		DefaultModifierImpl.BooleanResult br = 
			DefaultModifierImpl.calculateBooleanWithDescription(this, true, barrierModifiers[ind]);
		
		if(!br.result && br.description != null)
			who.notice(br.description);

		return br.result;
	}
	
	public String getTarget(String roomId) {
		if(roomId.equals(rooms[0]))
			return rooms[1];
		else if(roomId.equals(rooms[1]))
			return rooms[0];
		else
			return null;
	}
	
	public String getDirection(String roomId) {
		if(roomId.equals(rooms[0]))
			return directions[0];
		else if(roomId.equals(rooms[1]))
			return directions[1];
		else
			return null;	
	}
	
	public String getArrivalDirection(String roomId) {
		if(roomId.equals(rooms[0]))
			return directions[1];
		else if(roomId.equals(rooms[1]))
			return directions[0];
		else
			return null;	
	}
	
	public String getPassMessage(String roomId) {
		return passMessages[ind(roomId)];
	}

		
	public boolean isAlias(String name) {
		return false;
	}

	public void addModifier(Modifier m) {
		
		if(m.getType() == ModifierTypes.BARRIER) {

			if(barrierModifiers == null) 
				barrierModifiers = new LinkedList[2];
			int ind = ind(m.getArguments()[0].toString());
			if(ind < 0) return;
			
			if(barrierModifiers[ind] == null)
				barrierModifiers[ind] = new LinkedList();

			barrierModifiers[ind].add(m);
		} else {
			super.addModifier(m);
		}
	}
			
			
	private int ind(String roomId) {
		if(roomId.equals(rooms[0]))
			return 0;
		else if(roomId.equals(rooms[1]))
			return 1;
		else
			throw new IllegalArgumentException("Exit is not linked to room: "+roomId);
	}

	public boolean isObvious(Living who, String roomId) {
		return obvious[ind(roomId)];
	}
}
