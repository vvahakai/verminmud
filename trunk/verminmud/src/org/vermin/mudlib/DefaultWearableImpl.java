
package org.vermin.mudlib;

import java.util.EnumMap;
import java.util.LinkedList;

public class DefaultWearableImpl extends DefaultItemImpl implements Wearable {

	protected Slot[] slots;
	protected LinkedList armourModifiers;
	protected int armourValue;
	protected UtilitySlot[] utilitySlots;
	protected int sizeScale;
	protected EnumMap<Damage.Type, Integer> damageTypeModifiers;
	
	public boolean tryUnwear(Living who) {
		return true;
	}

	public void onWear(Living who) {

	}

	public void onUnwear(Living who) {

	}
	
	public void setArmourValue(int armourValue) {
		this.armourValue = armourValue;
	}

	public void addModifier(Modifier m) {
		if(m.getType() == ModifierTypes.ARMOUR) {
			if(armourModifiers == null) 
				armourModifiers = new LinkedList();
			armourModifiers.add(m);
		} else {
			super.addModifier(m);
		}
	}

	public Slot[] getSlots() {
		return slots;
	}

	public UtilitySlot[] getUtilitySlots() {
		return utilitySlots;
	}

	public boolean tryWear(Living who) {
		if((sizeScale-1) * 20 <= who.getSize() && (sizeScale+2) * 20 >= who.getSize()) {
			return true;
		}
		return false;
	}

	public int getArmourValue() {
		return armourValue;
	}

	public int getArmourValue(Damage.Type damageType) {
		if(damageTypeModifiers.containsKey(damageType)) {
			return armourValue * (100 + damageTypeModifiers.get(damageType)) / 100;
		}
		else
			return armourValue;
	}
	
		
}
