/* FactoryArmour.java
	10.3.2002	Ville Vähäkainu
	
	a configurable armour.
*/

package org.vermin.mudlib;

import java.util.EnumMap;


public class FactoryArmour extends DefaultWearableImpl {
	public FactoryArmour() {}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void setSlots(String[] slotNames) {
		this.slots = new Slot[slotNames.length];
		for(int i=0;i<slotNames.length;i++)
		{
			this.slots[i] = new Slot(slotNames[i]);
		}
	}

	public void setSizeScale(int sizeScale) {
		this.sizeScale = sizeScale;
	}

	public void setArmourValue(int armourValue) {
		this.armourValue = armourValue;
	}

	public void setMaxDp(int maxDp) {
		this.maxDp = maxDp;
	}

	//construction method of this armour
	public void setDamageTypeModifiers(EnumMap<Damage.Type, Integer> damageTypeModifiers) {
		this.damageTypeModifiers = damageTypeModifiers;
	}
}
