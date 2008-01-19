/*
 * Created on 2.4.2005
 */
package org.vermin.world.quest;


import java.util.HashMap;

import org.vermin.mudlib.World;
import org.vermin.mudlib.quest.DefaultJournalImpl;

public class OutworldBattleJournal extends DefaultJournalImpl {

	public static class Entry {
		public Entry() {}
		public Entry(int i, int count) {
			killed = i;
			required = count;
		}

		int killed, required;
	}
	// Map a monster id to pair (monsters killed , required kill count) */ 
	private HashMap<String, Entry> killObjectives;

	public OutworldBattleJournal() {
		killObjectives = new HashMap();
	}
	
	public boolean isComplete() {
		for(Entry p : killObjectives.values())
			if(p.killed < p.required)
				return false;
		return true;
	}
	
	public HashMap<String,Entry> getKillObjectives() {
		return killObjectives;
	}
	
	/**
	 * Add a kill objective to this journal.
	 * 
	 */
	public void addKillObjective(String monsterId, int count) {
		killObjectives.put(monsterId, new OutworldBattleJournal.Entry(0, count));
	}
	
	/**
	 * Used to mark slayed monsters into this quest journal. All
	 * outworld monsters which are capable of being the target
	 * of a search & destroy quest should have the QuestDeathBehaviour, which
	 * searches the inventory of the slayer for an OutworldBattleJournal, 
	 * and tries to mark the slaying by passing their id to this method.
	 *
	 * @param id  the id of the monster slayed
	 * @return true if the objective was completed by this kill, false otherwise
	 */
	public boolean noteSlaying(String id) {
		Entry objective = killObjectives.get(id);
		if(objective == null) {
			World.log("KILLED non-quest monster: "+id);
			return false;
		}
		
		objective.killed++; // kill count can exceed required count
		
		return objective.killed == objective.required;
	}

}
