/*
 * Created on 2.4.2005
 */
package org.vermin.world.quest;

import java.util.Map;
import java.util.Vector;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.World;
import org.vermin.mudlib.quest.Journal;

public class OutworldBattleObjective {

	private Iterable EMPTY_ITERABLE = new Vector();
	private Map<String, Integer> targets;
	private String directions;
	private boolean available = true;
	
	public OutworldBattleObjective() {
	}
	
	public OutworldBattleObjective(Map<String,Integer> targets, String directions) {
		this.targets = targets;
		this.directions = directions;
	}
	
	public void noteSlaying(String id) {
		if(targets.containsKey(id)) {
			int number = targets.get(id);
			number--;
			if(number == 0) {
				targets.remove(id);
			} else {
				targets.put(id, number);
			}
		}
		
		if(targets.size() == 0) {
			available = false;
		}
	}
	
	public String getCode() {			
		return "slayobjective";
	}
	public boolean isAvailable(Journal journal) {
		return available;
	}
	

	public String getDescription() {
		StringBuffer desc = new StringBuffer("By order of the Imperial Mercenary Force Commander, you must track down and slay:\n\n");
		
		for(Map.Entry<String, Integer> e : targets.entrySet()) {
			if(e.getValue() > 0) {
				desc.append(e.getValue()+" x '"+((Living) World.get(e.getKey())).getDescription()+"'\n\n");
			}
		}
		desc.append(directions);
		return desc.toString();
	}
	
	public Map<String, Integer> getTargets() {
		return targets;
	}
}
