/*
 * Created on 19.2.2005
 */
package org.vermin.mudlib;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


/**
 * A default implementation of SpawningRule based on a 
 * collection of SpawnEntries. See comments on inner interface.
 * 
 * @author Jaakko Pohjamo
 */
public class DefaultSpawningRuleImpl implements SpawningRule {

	private Vector<SpawnEntry> spawnEntries;
	private transient Map<SpawnEntry, Set<MObject>> currentlySpawned;
	
	/**
	 * This interface defines a single entry in a spawn rule.
	 * A spawn rule entry provides information about the types, 
	 * propabilities and amounts of monsters that should be
	 * spawned in a certain set of rooms it is associated with.
	 * 
	 * @author Jaakko Pohjamo
	 */
	public interface SpawnEntry {
		
		/**
		 * Returns the amount of monsters that should at
		 * the most be spawned for this entry.
		 * 
		 * @return  the maximum amount of monsters to spawn
		 */
		public int getSpawnMax();
		
		/**
		 * Returns a boolean value indicating whether this
		 * entry wants to spawn in room r.
		 * 
		 * @param r  the Room to query for
		 * @return  true if this entry has spawn information 
		 * 			for Room r
		 */
		public boolean maySpawnInRoom(Room r);
		
		/**
		 * A Map mapping monster ids to percent propabilities
		 * of spawning.
		 * 
		 * @return  a list containing mappings between monster
		 * 			ids and propabilities for spawning
		 */
		public List<SpawnDataMapping> getSpawns();
				
	}
	
	public static class SpawnDataMapping {
		public String id;
		public int probability;
	}
	protected static class DefaultSpawnEntry implements SpawnEntry {
		private int spawnMax;
		private Set<String> rooms = new HashSet();
		private List<SpawnDataMapping> spawnData;
		private int entryProbability;
		
		public DefaultSpawnEntry() {
		}
		
		public DefaultSpawnEntry(List<SpawnDataMapping> spawnData) {
			this.spawnData = spawnData;
		}
		
		public int getSpawnMax() {
			return spawnMax;
		}
		
		public boolean maySpawnInRoom(Room r) {
			if(Dice.random() > entryProbability) {
				return false;
			}
			if(rooms.isEmpty()) {
				return true;
			}
			
			return rooms.contains(r.getDescription());
		}
		
		public List<SpawnDataMapping> getSpawns() {
			return spawnData;
		}
	}
	public DefaultSpawningRuleImpl() {
	}
	
	public DefaultSpawningRuleImpl(Collection<SpawnEntry> entries) {
		if(entries == null) {
			spawnEntries = new Vector();
		} else {
			spawnEntries = new Vector(entries);
		}
		init();
	}
	
	/**
	 * Spawns monsters as defined in the SpawnEntries passed
	 * in the constructor.
	 */
	public void spawn(Room room) {
		for(SpawnEntry entry : spawnEntries) {
			if(currentlySpawned == null) {
				init();
			}
			if((entry.getSpawnMax() == 0 || currentlySpawned.get(entry).size() < entry.getSpawnMax()) &&
					entry.maySpawnInRoom(room)) {
				
				for(SpawnDataMapping mapping : entry.getSpawns()) {
					if(Dice.random() < ((Integer) mapping.probability) && 
							(entry.getSpawnMax() == 0 || currentlySpawned.get(entry).size() < entry.getSpawnMax())) {
						MObject spawn = (MObject) World.load(mapping.id).create();
						room.add(spawn);
						if(spawn instanceof Living) {
							((Living) spawn).arrives((Living) spawn);
						}
						currentlySpawned.get(entry).add(spawn);
					}
				}
			}
		}
	}

	public void unspawn(MObject what) {
		for(Set<MObject> s : currentlySpawned.values()) {
			s.remove(what);
		}
	}
	
	private void init() {
		currentlySpawned = new HashMap();
		if(spawnEntries != null) {
			for(SpawnEntry entry : spawnEntries) {
				currentlySpawned.put(entry, new HashSet<MObject>());
			}
		}
	}
	/*
	public static void main(String[] args) {
		SExpObjectOutput out = null;
		Map<String, SpawningRule> ruleMap = null;
		try {
			SExpObjectInput in = new SExpObjectInput(new File("C:\\spawn.txt"));
			
			Object o = in.deserialize();
			System.out.println("-------------------------------");
			System.out.println(o);
			
			List<SpawnDataMapping> spawnData = new Vector();
			
			SpawnDataMapping m = new SpawnDataMapping();
			
			m.id = "outworld/ant";
			m.probability = 100;
			spawnData.add(m);
			
			m = new SpawnDataMapping();
			m.id = "outworld/anthill";
			m.probability = 100;
			spawnData.add(m);
			
			SpawnEntry e = new DefaultSpawnEntry(spawnData);
			Set s = new HashSet();
			s.add(e);
			DefaultSpawningRuleImpl dssi = new DefaultSpawningRuleImpl(s);
			
			ruleMap = new HashMap();		
			ruleMap.put("Desert of Homo", dssi);
			

	
			out = new SExpObjectOutput(System.out);
		} catch(Exception ex) {
			System.out.println("haghiuehgliauwhegliulshglwheg");
			ex.printStackTrace();
		}
		
		out.serialize(ruleMap);
	}*/
}
