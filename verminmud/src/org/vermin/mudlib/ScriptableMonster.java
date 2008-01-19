/**
 * ScriptableMonster.java
 * A monster that can be configured with actions.
 *
 * 20.7.2003 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

import java.util.HashMap;

import org.vermin.driver.Queue;

public class ScriptableMonster extends DefaultMonster {

	/* HashMap<String, ScriptAction>
	 * a hashmap containing a scheme procedure for each
	 * named event type.
	 */
	private HashMap<String,ScriptAction> actions = new HashMap();
	
	public void leaves(Living who) {
		//System.out.println("ScriptableMonster: leaves");
		try {
			ScriptAction proc = actions.get("leaves");
			if(proc != null)
				proc.run(this, who);
			else super.leaves(who);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: leaves): "+e);
			actions.remove("leaves");
		}
	}
	public void arrives(Living who) {
		//System.out.println("ScriptableMonster: arrives");
		try {
			ScriptAction proc = actions.get("arrives");
			if(proc != null)
				proc.run(this, who);
			else super.arrives(who);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: arrives): "+e);
			actions.remove("arrives");
		}
	}
	public void arrives(Living who, Exit from) {
		//System.out.println("ScriptableMonster: arrives");
		try {
			ScriptAction proc = actions.get("arrives");
			if(proc != null)
				proc.run(this, who, from);
			else super.arrives(who, from);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: arrives): "+e);
			actions.remove("arrives");
		}
	}

	public void leaves(Living who, Exit to) {
		//System.out.println("ScriptableMonster: leaves");
		try {
			ScriptAction proc = actions.get("leaves");
			if(proc != null)
				proc.run(this, who, to);
			else super.arrives(who, to);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: leaves): "+e);
			actions.remove("leaves");
		}
	}

    /*
	public void startsCasting(Living who) {
		//System.out.println("ScriptableMonster: startsCasting");
		try {
			ScriptAction proc = actions.get("starts-casting");
			if(proc != null)
				proc.run(this, who);
			else super.startsCasting(who);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: startsCasting): "+e);
		}
	}
*/

	public void startsUsing(Living who, Skill skill) {
		//System.out.println("ScriptableMonster: startsUsing");
		try {
			ScriptAction proc = actions.get("starts-using");
			if(proc != null)
				proc.run(this, who);
			else super.startsUsing(who, skill);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: startsUsing): "+e);
			actions.remove("starts-using");
		}
	}

	public void takes(Living who, Item what) {
		//System.out.println("ScriptableMonster: takes");
		try {
			ScriptAction proc = actions.get("takes");
			if(proc != null)
				proc.run(this, who, what);
			else super.takes(who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: takes): "+e);
			actions.remove("takes");
		}
	}

	public void drops(Living who, Item what) {
		//System.out.println("ScriptableMonster: drops");
		try {
			ScriptAction proc = actions.get("drops");
			if(proc != null)
				proc.run(this, who, what);
			else super.drops(who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: drops): "+e);
			actions.remove("drops");
		}
	}

	public void wields(Living who, Item what) {
		//System.out.println("ScriptableMonster: wields");
		try {
			ScriptAction proc = actions.get("wields");
			if(proc != null)
				proc.run(this, who, what);
			else super.wields(who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: wields): "+e);
			actions.remove("wields");
		}
	}

	public void unwields(Living who, Item what) {
		//System.out.println("ScriptableMonster: unwields");
		try {
			ScriptAction proc = actions.get("unwields");
			if(proc != null)
				proc.run(this, who, what);
			else super.unwields(who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: wields): "+e);
			actions.remove("unwields");
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
			actions.remove("battle");
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
			actions.remove("regen");
		}
	}


	public void says(Living who, String what) {
		//System.out.println("ScriptableMonster: says");
		try {
			ScriptAction proc = actions.get("says");
			if(proc != null)
				proc.run(this, who, what);
			else super.says(who, what);
		} catch(Exception e) {
			World.log("ScriptableMonster exception (method: says): "+e);
			actions.remove("says");
		}
	}

}
