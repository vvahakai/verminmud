package org.vermin.mudlib;

import org.vermin.driver.Persistent;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.driver.Utility;

/**
 * A race that is meant for players.
 * Holds persistent race data.
 */
public class PlayerRace extends DefaultRaceImpl implements Persistent, Tickable {

	private boolean dirty;
	
	private String id;
	
	private String leader; // Name of the race leader
	
	public void setAnonymous(boolean anonymous) {}

	public boolean isAnonymous() {
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void save() {
		Utility.save(this, id);
		dirty = false;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		dirty();
		this.leader = leader;
	}

	public final void start() {
		super.start();
		World.addGarbageTick(this);
		World.saveOnExit(this);
	}

	public boolean tick(Queue queue) {
		if(queue == Tick.GARBAGE && dirty)
			save();
		return true;
	}

	/**
	 * Set the dirty flag.
	 * When dirty, the object will be saved on the next garbage tick.
	 */
	protected void dirty() {
		dirty = true;
	}
}
