package org.vermin.mudlib;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;

import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;


/**
 * Manages the world plaque.
 * 
 * 
 * @author tadex
 *
 */
public class Plaque extends DefaultPersistentImpl implements Tickable {
	
	public interface PlaqueEntry {
		public String getName();
		public long getBeginExperience();
		public long getExperience();
		public long delta();
	};
	
	/**
	 * Defines the plaque types
	 * 
	 * @author tadex
	 *
	 */
	public enum Type {
		
		/**
		 * Plaque that shows the current total experience worth of players
		 */
		WORTH, 
		
		/**
		 * Plaque that shows the prev
		 */
		DELTA;
	}
	
	
	public static class PlaqueEntryData implements PlaqueEntry {
		
		// Player id
		String playerId;
		
		// Player name
		String playerName;
		
		// Experience at the beginning of this week (monday morning)
		long beginExperience;
		
		// Latest experience value (update at player save and garbage tick)
		long experience;
		
		public String getName() {
			return playerName;
		}
		public long getBeginExperience() {
			return beginExperience;
		}
		public long getExperience() {
			return experience;
		}
		
		public long delta() {
			return experience - beginExperience;
		}
	};
	
	// Current week (used to track time change)
	private int week;
	
	// Maps player ids to plaque entries
	private HashMap<String,PlaqueEntryData> entries = new HashMap();
	
	// Sorted list of entries (realtime/worth)
	private transient PlaqueEntryData[] sortedEntries;
	
	// Sorted delta entries (for previous week)
	private PlaqueEntryData[] deltaEntries; 
	
	// True if sorted entries is not in sync with the entry mapping
	private boolean dirty = true;
	
	public synchronized void updatePlaque(Player who) {
		PlaqueEntryData ped = entries.get(who.getId());
		if(ped == null) {
			ped = new PlaqueEntryData();
			entries.put(who.getId(), ped);
			ped.playerId = who.getId();
			ped.playerName = who.getName();
			ped.beginExperience = who.getTotalExperience();
		}
		ped.experience = who.getTotalExperience();
		dirty = true;
	}
	
	/**
	 * Look up the given player from the sorted plaque list. 
	 * Returns an array of plaque entries, one of which is the given player's.
	 * If the player has no entry on the plaque, returns an empty array.
	 * 
	 * @param playerId the id of the player to look up
	 * @param before how many entries before the player are shown
	 * @param after how many entries after the player are shown
	 * @param t the type, WORTH or DELTA
	 * @return a <code>PlaqueEntry</code> array of length 0 to before+1+after 
	 */
	public synchronized PlaqueEntry[] showPlayer(String playerId, int before, int after, Type t) {
		sortPlaque(t);
		
		int index = find(playerId, t);
		if(index == -1)
			return new PlaqueEntry[0];
		
		// calculate start position
		int start = index - before;
		if(start < 0)
			start = 0;
				
		PlaqueEntryData[] theEntries = t==Type.WORTH ? sortedEntries : deltaEntries;
		if(theEntries == null)
			return new PlaqueEntry[0];
		
		// calculate end position
		int end = index + after;
		if(end > sortedEntries.length)
			end = sortedEntries.length;

		// return the values as array
		PlaqueEntry[] ret = new PlaqueEntry[end-start];
		int ind = 0;
		for(int i = start; i<end; i++) {
			ret[ind++] = theEntries[i];
		}
		return ret;
	}
	
	
	/**
	 * Show the top listing.
	 *
	 * @param howMany how many top entries to show
	 * @param t the type, WORTH or DELTA
	 * @return a <code>PlaqueEntry</code> array
	 */
	public PlaqueEntry[] showTop(int howMany, Type t) {
		
		sortPlaque(t);
		
		if(howMany <= 0)
			throw new IllegalArgumentException("howMany must be positive");
		
		PlaqueEntryData[] theEntries = t == Type.WORTH ? sortedEntries : deltaEntries;
		if(theEntries == null)
			return new PlaqueEntry[0];
		
		if(howMany > theEntries.length) 
			howMany = theEntries.length;
		
		PlaqueEntry[] res = new PlaqueEntry[howMany];
		int ind = 0;
		for(int i=0; i<howMany; i++)
			res[ind++] = theEntries[i];
		
		return res;
	}
	
	/**
	 * Find the given player in the plaque and return the index.
	 * If the player is not on the plaque return -1.
	 * 
	 * @param playerid the player id 
	 * @return the index or -1 
	 */
	public synchronized int find(String playerid, Type t) {
		sortPlaque(t);
		
		PlaqueEntryData ped = entries.get(playerid);
		
		/* PENDING: maybe do a binary search for this player's exp delta
		 * and then adjust the position (because many players can have 
		 * the same delta value).
		 */
		
		PlaqueEntryData[] theEntries = t==Type.WORTH ? sortedEntries : deltaEntries;
		if(theEntries == null)
			return -1;
		
		for (int i = 0; i < theEntries.length; i++) {
			if(theEntries[i].playerId.equals(playerid))
				return i;
		}
		
		return -1;
	}
	
	// Sort the plaque, if sorted entries is dirty
	private synchronized void sortPlaque(Type t) {
		if(!dirty) return;
		
		switch(t) {
			case DELTA:
				deltaEntries = sort(t);
				break;
			case WORTH:
				sortedEntries = sort(t);
				break;
		}
		
		dirty = false;
	}
	
	private synchronized PlaqueEntryData[] sort(Type t) {
		PlaqueEntryData[] entryArray = new PlaqueEntryData[entries.size()];
		entries.values().toArray(entryArray);
		
		Arrays.sort(entryArray,
				t == Type.DELTA 
				? new Comparator<PlaqueEntryData>() {
					public int compare(PlaqueEntryData arg0, PlaqueEntryData arg1) {
						long d0 = arg0.experience - arg0.beginExperience;
						long d1 = arg1.experience - arg1.beginExperience;
						return (d0 < d1) ? 1 : (d0 > d1) ? -1 : 0;
					}}
				: new Comparator<PlaqueEntryData>() {
					public int compare(PlaqueEntryData arg0, PlaqueEntryData arg1) {
						long w0 = arg0.experience;
						long w1 = arg1.experience;
						return (w0 < w1) ? 1 : (w0 > w1) ? -1 : 0;
					}}			
		);
		
		return entryArray;
	}
	
	
	@Override
	public void start() {
		World.addGarbageTick(this);
	}

	// Tick that saves on garbage tick and updates delta stats when week changes
	public boolean tick(Queue queue) {
		synchronized(this) {
			
			// If this.week < the current week, update the plaque begin experience
			Calendar c = Calendar.getInstance();
			int w = c.get(Calendar.WEEK_OF_YEAR);
			if(week != w) { 
				
				// Export current delta information (delta plaque is always shown for the 'previous week')
				deltaEntries = sort(Type.DELTA);
				
				// Update beginExperience for a new week
				for(PlaqueEntryData ped : entries.values())
					ped.beginExperience = ped.experience;
				week = w;
			}
			save();
		}
		return true;
	}
	
	
}
