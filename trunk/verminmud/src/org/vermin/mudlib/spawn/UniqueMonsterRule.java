package org.vermin.mudlib.spawn;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SpawningRule;
import org.vermin.mudlib.World;

/**
 * A spawning rule that positions a single instance of a given
 * monster in the area.
 * 
 * @author tadex
 *
 */
public class UniqueMonsterRule implements SpawningRule {

	private int propability; // spawn propability in a single room
	private String monster;  // path reference to the monster
	
	private MObject theMonster; // the spawned monster instance (if null, ok to spawn)
	
	/* PENDING:
	 * Perhaps add a time-out so that there must be atleast a garbage tick amount
	 * of time between spawns..
	 * 
	 *  consider the following
	 *  1. player walks into an unloaded room and the monster is spawned
	 *  2. player kills the monster, causing it to unspawn
	 *  3. player walks into the next room, which is also unloaded
	 *  4. the monster is spawned again
	 * 
	 */
	
	
	
	public synchronized void spawn(Room room) {
		if(theMonster != null)
			return;
	
		if(Dice.random() < propability) {
			
			MObject mo = (MObject) World.load(monster).create();
			room.add(mo);
			if(mo instanceof Living) {
				((Living) mo).arrives((Living) mo);
			}

			
		}
	}
	public synchronized void unspawn(MObject what) {
		if(what == theMonster) 
			theMonster = null;
	}
	
	
}
