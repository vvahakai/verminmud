/* MagicalWearableImpl.java
	17.7.2004	Ville Vähäkainu
	
	a configurable magical armour.
*/

package org.vermin.mudlib;
import java.util.Vector;
import java.util.Enumeration;

public class MagicalWearableImpl extends FactoryArmour implements MagicItem
{

	protected Vector modifiers;

	public void onWear(Living who) {

		if(modifiers == null)
			return;

		Enumeration en = modifiers.elements();

		while(en.hasMoreElements()) {
			DefaultModifierImpl m = (DefaultModifierImpl) en.nextElement();
			m.setActive(true);
			who.addModifier(m);
		}
	}


	public void onUnwear(Living who) {

		if(modifiers == null)
			return;

		Enumeration en = modifiers.elements();

		while(en.hasMoreElements()) {
			Modifier m = (Modifier) en.nextElement();
			m.deActivate();
		}
	}

	public int getMagicValue() {
		if(modifiers == null)
			return 0;
		
		return modifiers.size() * 10;
	}
}
