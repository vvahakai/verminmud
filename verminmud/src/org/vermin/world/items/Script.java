/* Script.java
	30.3.2005	MV
	
	A script for use by prepare script skill
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;

public class Script extends DefaultItemImpl implements MagicItem
{
	private String powerword1;
	private String powerword2;
	private String powerword3;
	private int power;

	public Script() {
		name = "script";
		description = "a waxed paper scroll";
		pluralForm = "scripts";
	}
	
	public String getLongDescription() {
		return "This is a scroll with a few words written on it. It says \""+powerword1+" "+powerword2+" "+powerword3+"\".";
	}

	public Material getMaterial() {
		return MaterialFactory.createMaterial("paper");
	}
	
	public boolean isWeapon() { return false; }
	
	public String getPowerword1() {
		return powerword1;
	}
	
	public String getPowerword2() {
		return powerword2;
	}
	
	public String getPowerword3() {
		return powerword3;
	}
	public int setPower(int power) {
		return power;
	}
	
	public void setWords(String word1, String word2, String word3)
	{
		powerword1 = word1;
		powerword2 = word2;
		powerword3 = word3;
	}

	public int getMagicValue() {
		return 1;
	}
}
