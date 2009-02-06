/**
 * AbstractBattleGroup.java 
 * 1.8.2003 Tatu Tarvainen / Council 4
 *
 * Contains the most basic battle group functionality.
 */

package org.vermin.mudlib.battle;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList; 
import java.util.Iterator;
import org.vermin.mudlib.*;

/**
 * An abstract battlegroup which
 * handles bookkeeping about hostile groups
 * and opponent resolution.
 */
public abstract class AbstractBattleGroup implements BattleGroup {

	protected transient Set<BattleGroup> hostiles = Collections.synchronizedSet(new HashSet<BattleGroup>());
	protected final static Random r = new Random();

	public void addHostileGroup(BattleGroup bg) {
		if(bg == this)
			return; // refuse to add self as hostile

		if(bg == null) {
		 throw new IllegalArgumentException("null hostile battlegroup added.");
		}
		//World.log("ADDED BATTLEGROUP "+getName()+": "+bg.getName());
		
		Iterator<BattleGroup> it = children();
		while(it.hasNext()) {
			it.next().addHostileGroup(bg);
		}
		hostiles.add(bg);
		doBattle();
	}

	public Living getLeafValue() {
		return null;
	}

	public Collection<BattleGroup> hostileGroups() {
		return Collections.unmodifiableCollection(hostiles);
	}
	
	public void removeHostileGroup(BattleGroup bg) {
		//World.log("REMOVED BATTLEGROUP "+getName()+": "+bg.getName());
		Iterator<BattleGroup> it = children();
		while(it.hasNext()) {
			it.next().removeHostileGroup(bg);
		}
		hostiles.remove(bg);
	}

	public Living getOpponent(Room room, String name) {
/*		Living opponent = (Living) room.findByNameAndType(name, Types.TYPE_LIVING);
		
		if(opponent != null && 
			isOpponent(opponent) &&
			opponent.getBattleGroup().isCombatant(room, opponent))
			return opponent;
		else*/
			return getOpponent(room);
	}

	public Living getOpponent(Room room) {
		if(hostiles.isEmpty()) {
//World.log("AbstractBattleGroup has no hostiles in room "+room+".");
			return null;
		}

		BattleGroup[] h = (BattleGroup[]) hostiles.toArray(new BattleGroup[hostiles.size()]);
      ArrayList<Living> localOpponents = new ArrayList<Living>();
      for(BattleGroup group : h) {
         Living opponent = group.getCombatant(room);
         if(opponent != null) {
            localOpponents.add(opponent);
         }
      }
      
      if(localOpponents.isEmpty()) {
//World.log("AbstractBattleGroup has no local hostiles in room "+room+".");
         return null;
      }
		// PENDING: maybe cache something
//World.log("Found "+localOpponents.size()+" hostiles in room "+room+".");
		return localOpponents.get(r.nextInt(localOpponents.size()));
	}

	public boolean isOpponent(Living l) {
		return hostiles.contains(l.getBattleGroup());
	}

	public void onDeath(Living who) {}

	public abstract void doBattle();

	// do NOT use in game code
	public Set<BattleGroup> debugGetHostiles() {
		return hostiles;
	}

	public void parentChanged(BattleGroup bg) {}

	/**
	 * Recursively search for the parent battlegroup of the given battlegroup.
	 *
	 * @param child the battlegroup to search for
	 * @return the parent of the given battlegroup
	 */
	public BattleGroup searchParent(BattleGroup child) {
		Iterator<BattleGroup> it = children();
		while(it.hasNext()) {
			BattleGroup bg = (BattleGroup) it.next();
			if(child == bg)
				return this;
		}

		it = children();
		while(it.hasNext()) {
			BattleGroup result = null;
			BattleGroup sub = (BattleGroup) it.next();
			result = sub.searchParent(child);
			if(result != null)
				return result;
		}

		return null;
	}
	
	public synchronized void clearHostiles() {
		hostiles.clear();
		Iterator<BattleGroup> it = children();
		while(it.hasNext())
			it.next().clearHostiles();
	}
}
