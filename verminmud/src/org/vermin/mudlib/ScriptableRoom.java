/**
 * ScriptableRoom.java
 * A room that can be configured with actions.
 *
 * 14.1.2005 Ville Vähäkainu
 */
package org.vermin.mudlib;

import java.util.HashMap;
import java.util.Vector;

import org.vermin.driver.Queue;

public class ScriptableRoom extends DefaultRoomImpl {

	/* HashMap<String, ScriptAction>
	 * a hashmap containing a scheme procedure for each
	 * named event type.
	 */
	private HashMap<String,ScriptAction> actions = new HashMap();
	
	private HashMap<String,ScriptAction> commands = new HashMap();
	
	public void takes(Living who, Item what) {
		super.takes(who, what);
		//System.out.println("ScriptableMonster: takes");
		try {
			ScriptAction proc = actions.get("takes");
			if(proc != null)
				proc.run(this, who, what); 
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: takes): "+e);
		}
	}

	public void drops(Living who, Item what) {
		super.drops(who, what);
		//System.out.println("ScriptableMonster: drops");
		try {
			ScriptAction proc = actions.get("drops");
			if(proc != null)
				proc.run(this, who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: drops): "+e);
		}
	}

	public boolean tick(Queue queue) {
		if(queue == Tick.BATTLE) 
			battleTick();
		else if(queue == Tick.REGEN)
			regenTick();
		return super.tick(queue);
	}

	public void battleTick() {
		//System.out.println("ScriptableMonster: battle");
		try {
			ScriptAction proc = actions.get("battle");
			if(proc != null)
				proc.run(this);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: battleTick): "+e);
		}
	}

	public void regenTick() {
		//System.out.println("ScriptableMonster: regen");
		try {
			ScriptAction proc = actions.get("regen");
			if(proc != null)
				proc.run(this);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: regenTick): "+e);
		}
	}

	@Override
	public boolean action(Living who, Vector cmd) {
		ScriptAction action = commands.get(cmd.get(0));
		if(action != null) {
			action.run(this, new Object[] { who, cmd });
			return true;
		}
		return false;
	}

	
}
