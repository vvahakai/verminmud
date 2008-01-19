/*
 * Created on 2.4.2005
 */
package org.vermin.world.quest;

import java.util.Iterator;

import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.behaviour.BehaviourAdapter;

/**
 * This implements a death behavior specific to monsters
 * which are the objectives of an outworld based
 * search and destroy quest.
 * 
 * On dying, the monster checks the players inventory
 * for an OutworldBattleJournal and tries to mark its demise
 * there.
 * 
 * @author Jaakko Pohjamo
 */
public class QuestDeathBehaviour extends BehaviourAdapter {

	private Living owner;
	public QuestDeathBehaviour() {
		
	}
	public void dies(Living victim, Living killer) {
		if(victim == owner) {
			Iterator it = killer.findByType(Types.TYPE_ITEM);
//World.log("AAARGH! An ant has died. It was "+Print.capitalize(killer.getName())+"!");
			while(it.hasNext()) {
//World.log("Searching for quest journal...");
				Item i = (Item) it.next();
				if(i instanceof OutworldBattleJournal) {
//World.log("Found! Making entry.");
					((OutworldBattleJournal) i).noteSlaying(victim.getId());
				}
			}
		}
		super.dies(victim, killer);
	}
	
}
