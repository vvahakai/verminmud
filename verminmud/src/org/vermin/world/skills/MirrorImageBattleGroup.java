package org.vermin.world.skills;

import org.vermin.mudlib.*;

import java.util.Iterator;

public class MirrorImageBattleGroup extends BranchBattleGroup {

	private BattleGroup parent;
	private Living owner;

	public void removeImage(Living image) { 
		removeChild(image.getPersonalBattleGroup());
		if(children.size() == 1) {
			parent.unwrap(this);
		}
	}

	public void parentChanged(BattleGroup bg) { parent = bg; }

	public String getName() {
		if(children.size() > 1)
			return "multiple "+owner.getName()+"'s";
		else
			return owner.getName();
		// return "MirrorImageBattleGroup"; 
	}
	
	public void setOwner(Living who) { 
		owner = who; 
	}

	public void onDeath(Living who) {
		if(who == owner) {

			BattleGroup foundOwnerBattleGroup = null;
			//find owner's battlegroup
			Iterator it = children();
			while(it.hasNext()) {
				BattleGroup bg = (BattleGroup) it.next();
				if(bg == owner.getLeafBattleGroup() || bg.contains(owner.getLeafBattleGroup()) ) {
					foundOwnerBattleGroup = bg;
				}
			}

			if(foundOwnerBattleGroup != null) {
				parent.removeChild(this);
				parent.addChild(foundOwnerBattleGroup);
			}
			else {
				parent.unwrap(this);
			}
		}
	}
}
