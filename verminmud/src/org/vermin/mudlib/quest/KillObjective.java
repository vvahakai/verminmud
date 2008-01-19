/*
 * Created on 2.4.2005
 */
package org.vermin.mudlib.quest;

import java.util.Map;

/**
 * Defines a quest objective where the player
 * has to kill one or more monsters.
 *
 * The target monsters are identified by their
 * ids, and each must have the QuestDeathBehaviour,
 * for their deaths to be correctly detected.
 * 
 * @author Jaakko Pohjamo
 */
public class KillObjective {
	
	private Map<String, Integer> targets;
	
	public KillObjective(Map<String, Integer> targets) {
		
	}
	public String getCode() {
		return null;
	}

	public String getDescription() {
		return null;
	}

	public boolean isAvailable(Journal journal) {
		return false;
	}

}
