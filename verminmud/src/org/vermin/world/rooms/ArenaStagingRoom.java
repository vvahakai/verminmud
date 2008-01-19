/*
 * Created on 11.11.2006
 */
package org.vermin.world.rooms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexRoom;
import org.vermin.mudlib.World;
import org.vermin.util.Print;
import org.vermin.world.rooms.ArenaRoom.ArenaParticipant;

public class ArenaStagingRoom extends RegexRoom {

	private Map<String, String> challenges = new HashMap<String, String>();
	
	public String[] getDispatchConfiguration() {
		return new String[] {"challenge (.*) => challenge(actor, living 1)",
							 "view challenges => viewChallenges(actor)"};
	}
	
	public void challenge(Living actor, Living challenged) {
		if(challenged == null) {
			actor.notice("Challenge who?");
			return;
		}
		if(!(challenged instanceof Player)) {
			actor.notice("You can only issue challenges to other players.");
			return;
		}
		if(actor == challenged) {
			actor.notice("That wouldn't be a very heroic battle.");
			return;
		}
		
		String counterChallenge = challenges.get(challenged.getName());
		
		if(counterChallenge != null && counterChallenge.equals(actor.getName())) {
			actor.notice("You accept "+Print.capitalize(challenged.getName())+"'s challenge.");
			startChallenge(actor, challenged);
		}
		
		this.noticeExcluding(Print.capitalize(actor.getName())+" issues a challenge to "+Print.capitalize(challenged.getName())+".", actor, challenged);
		actor.notice("You issue a challenge to "+Print.capitalize(challenged.getName())+".");
		challenged.notice(Print.capitalize(actor.getName())+" issues a challenge to you.");
		
		challenges.put(actor.getName(), challenged.getName());
	}
	
	public void viewChallenges(Living actor) {
		if(challenges.size() == 0) {
			actor.notice("No challenges have been issued.");
			return;
		}
		
		actor.notice("Outstanding arena challenges:");
		for(Map.Entry<String, String> entry : challenges.entrySet()) {
			actor.notice(Print.capitalize(entry.getKey())+" has challenged "+Print.capitalize(entry.getValue()));
		}
	}
	
	private void startChallenge(Living l1, Living l2) {
		ArenaRoom room = (ArenaRoom) World.get("common/city/arena");
		List<ArenaParticipant> participants = new LinkedList<ArenaParticipant>();
		participants.add(new ArenaParticipant(l1, "Black knight"));
		participants.add(new ArenaParticipant(l2, "White knight"));
		room.startBattle(participants);
	}
	
	public synchronized void leave(Living who, Exit to) {
		String removed = challenges.remove(who.getName());
		if(removed != null) {
			who.notice("You withdraw your challenge.");
		}
		super.leave(who, to);
	}

}
