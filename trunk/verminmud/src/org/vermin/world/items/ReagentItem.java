/* Reagent.java
	2.4.2005 MV

	Reagent you get from using Gather Reagents skill.
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class ReagentItem extends DefaultItemImpl
{

	private Reagent reagent;
	private int quality;
	
	public ReagentItem() {}
	
	public ReagentItem(Reagent r) {
		this.name = r.toString().toLowerCase().replaceAll("_"," ");
		this.reagent = r;
		addAlias("patch");
		
		addAlias("reagent");		
	}
	
	public void setQuality(int q) {
		this.quality = q;
	}
	
	public int getQuality() {
		return this.quality;
	}

	public String getDescription() {
		return reagent.desc;
	}
	
	public String getLongDescription() {
		return reagent.longdesc;
	}
	
	public Reagent getReagent() {
		return reagent;
	}
}
