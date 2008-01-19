package org.vermin.world.items;

import org.vermin.mudlib.Item;
import org.vermin.mudlib.MObject;

public class GolemWorkbench extends Workbench {

	public GolemWorkbench() {
		super();
		description = "golem workbench";
		name = "golem workbench";

	}
	
	public boolean tryAdd(MObject obj) {
		if(obj instanceof MaterialChunk && ((Item)obj).getSize(false) <= maxSize && items.size() <= maxItemCount ) {
			return true;
		}
		return false;
	}
}
