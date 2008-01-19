package org.vermin.world.items;

import org.vermin.mudlib.DefaultItemImpl;

public class MaterialChunk extends DefaultItemImpl {
	
	private int purity;
	
	public int getPurity() {
		return purity;
	}
	
	public void setPurity(int purity) {
		this.purity = purity;
	}
}
