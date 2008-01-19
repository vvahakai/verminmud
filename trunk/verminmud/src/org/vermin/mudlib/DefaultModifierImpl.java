package org.vermin.mudlib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DefaultModifierImpl implements Modifier {

	protected int amount;
	protected boolean active;
	protected String description;
	protected ModifierTypes type;
	protected Object[] arguments;

	public DefaultModifierImpl() {}

	public DefaultModifierImpl(int amount) {
		this.amount = amount;
		active = true;
	}

	public DefaultModifierImpl(ModifierTypes type, Object[] args, int amount) {
		this.amount = amount;
		this.type = type;
		this.arguments = args;
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int modify(MObject target) {
		return amount;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public static int calculateInt(MObject target, int initial, List modifiers) {
		if(modifiers == null)
			return initial;

		Iterator it = modifiers.iterator();
		
		while(it.hasNext()) {
			Modifier m = (Modifier) it.next();
			if(m.isActive()) {
				initial += m.modify(target);
			} else {
				it.remove();
			}
		}
		return initial;

	}

	public static boolean calculateBoolean(MObject target, boolean initial, LinkedList modifiers) {
		if(modifiers == null)
			return initial;

		Iterator it = modifiers.listIterator(0);

		int strength = 0;
		while(it.hasNext()) {
			Modifier m = (Modifier) it.next();
			if(m.isActive()) {
				strength += m.modify(target);
			} else {
				it.remove();
			}
		}
		if(strength < 0)
			return false;
		else if(strength > 0)
			return true;
		else
			return initial;
	}

	// Calculate boolean and return the description
	// of the strongest modifier that affected in the same 
	// direction as the result
	public static BooleanResult calculateBooleanWithDescription(MObject target, boolean initial, LinkedList<Modifier> modifiers) {

		if(modifiers == null) {
			BooleanResult br = new BooleanResult();
			br.result = initial;
			br.description = null;
			return br;
		}

		// index 0 for negative, 1 for positive
		String[] desc = new String[] { null, null } ;
		int[] amount = new int[] { 0, 0 };

		int strength = 0;
		World.log("calculate boolean modifier: "+modifiers);
		for(Modifier m : modifiers) {
			if(!m.isActive()) continue;
			int a = m.modify(target);
			int abs = Math.abs(a);
			if(a < 0 && abs > amount[0]) { // negative
				amount[0] = abs;
				desc[0] = m.getDescription();
			} else if(a > 0 && abs > amount[1]) { // positive
				amount[1] = abs;
				desc[1] = m.getDescription();
			}
			strength += a;
		}

		BooleanResult res = new BooleanResult();
		if(strength < 0) {
			res.result = false;
			res.description = desc[0];
		} else if(strength > 0) {
			res.result = true;
			res.description = desc[1];
		} else {
			res.result = initial;
		}

		return res;
	}

	public static class BooleanResult {
		public boolean result;
		public String description;
	}


	public void deActivate() {
		this.active = false;
	}

	public ModifierTypes getType() {
		return type;
	}

	public Object[] getArguments() {
		return arguments;
	}
}
