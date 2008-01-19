package org.vermin.mudlib;

import java.util.HashSet;

public class EasySpawningRule implements SpawningRule {

	public EasySpawningRule() {}
	
	int propability;
	String type = "ALL"; // ALL, ONE-OF
	HashSet<String> rooms;
	
	String[][] entries;
	
	public void spawn(Room room) {
		if((rooms == null || rooms.contains(room.getDescription())) && 
				Dice.random() < propability) {
			
			if(type.equalsIgnoreCase("ALL")) {
				for(String[] entry : entries) {
					int propability = Integer.parseInt(entry[0]);
					if(Dice.random() < propability)
						doSpawn(room, entry[1]);
				}
			} else if(type.equalsIgnoreCase("ONE-OF")) {
				int[] limits = new int[entries.length];
				int l = 0;
				for(int i=0; i<entries.length; i++) {
					int p = Integer.parseInt(entries[i][0]);
					limits[i] = p+l;
					l += p;
				}
				int dice = Dice.random(l);
				for(int i=0; i<entries.length; i++) {
					if(dice >= (i==0 ? 0 : limits[i-1])  && dice < limits[i]) {
						doSpawn(room, entries[i][1]);
						break;
					}
				}
			} else {
				World.log("Unrecognized EasySpawningRule type: "+type);
			}
		}
	}

	private void doSpawn(Room room, String id) {
		World.log("Spawning '"+id+"' to room "+room.getId());
		try {
			MObject mo = (MObject) World.load(id).create();
			room.add(mo);
			if(mo instanceof Living) {
				((Living) mo).arrives((Living) mo);
			}
		} catch(Exception e) {
			World.log("Failed to spawn object '"+id+"': "+e.getMessage());
		}
	}
	
	public void unspawn(MObject what) {
		
	}

}
