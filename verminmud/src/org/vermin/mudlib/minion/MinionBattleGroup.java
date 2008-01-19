package org.vermin.mudlib.minion;

import org.vermin.mudlib.BattleGroup;
import org.vermin.mudlib.BranchBattleGroup;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Room;

public class MinionBattleGroup extends BranchBattleGroup {

	private Living minion;	
	private BattleGroup parent;
	
	public String getName() { return "MinionBattleGroup"; }
	
	public void setMinion(Living who) { minion = who; }
	
	public BattleGroup getParent() {
		return parent;
	}
	
	public void parentChanged(BattleGroup p) {
		parent = p;
	}
	
	public Living getCombatant(Room room) {
		if(minion.getParent() == room)
			return minion;
		else
			return super.getCombatant(room);
	}
	
}
 