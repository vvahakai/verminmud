/*
 * Created on 11.11.2006
 */
package org.vermin.world.rooms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vermin.mudlib.Commander;
import org.vermin.mudlib.DefaultRoomImpl;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.World;
import org.vermin.mudlib.commands.Look;
import org.vermin.util.Print;

public class ArenaRoom extends DefaultRoomImpl {
	
	private String exitRoomId;
	private Map<Living, ArenaParticipant> participants = new HashMap<Living, ArenaParticipant>();
	
	public static class ArenaParticipant {
		private int originalHp;
		private int originalSp;
		private String side;
		private Living living;
		
		public ArenaParticipant(Living living, String side) {
			this.side = side;
			this.living = living;
			originalHp = living.getHp();
			originalSp = living.getSp();
		}
		public int getOriginalHp() { return originalHp; }
		public int getOriginalSp() { return originalSp; }
		public String getSide() { return side; }
		public Living getLiving() { return living; }
	}
	
	public void startBattle(List<ArenaParticipant> gladiators) {
		for(ArenaParticipant participant : gladiators) {
			participants.put(participant.getLiving(), participant);
			
			participant.getLiving().getRoom().remove(participant.getLiving());
			this.add(participant.getLiving());
			
			Look l = (Look) Commander.getInstance().get("look");
			l.look(participant.getLiving(), true);
			
			participant.getLiving().notice("&B2;You have been transported to the arena.&;");
		}
		
		for(ArenaParticipant participant : gladiators) {
			for(ArenaParticipant participant2 : participants.values()) {
				if(!participant.getSide().equals(participant2.getSide())) {
					participant.getLiving().addAttacker(participant2.getLiving());
				}
			}
		}
	}
	
	public void dies(Living victim, Living killer) {
		ArenaParticipant loser = participants.get(victim);
		victim.notice("&B2;You have been defeated by "+Print.capitalize(killer.getName())+".&;");
		
		Room exitRoom = (Room) World.get(exitRoomId);
		victim.setHp(loser.getOriginalHp());
		victim.setSp(loser.getOriginalSp());

		this.remove(loser.getLiving());
		exitRoom.add(loser.getLiving());
		
		Look l = (Look) Commander.getInstance().get("look");
		l.look(loser.getLiving(), true);
		
		loser.getLiving().notice("&B2;You have left the arena.&;");
		participants.remove(loser);
		
		// TODO: perhaps give a trophy of some kind to the winner?
		if(participants.size() == 1) {
			Living last = (Living) participants.values().toArray()[0];
			last.notice("&B2;You won the fight!&;");
			this.remove(last);
			exitRoom.add(last);
			return;
		}
		
		//TODO: implement handling for team fights, the side string in
		//      ArenaParticipant identifies teams
		/*
		String side = "";
		for(ArenaParticipant participant : participants.values()) {
			
		}
	*/
	}
	
	public void setExitRoomId(String roomId) {
		exitRoomId = roomId;
	}
}
