package org.vermin.world.items;

import java.util.Vector;

import org.vermin.mudlib.ContainerItem;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;

public class Workbench extends ContainerItem {


	protected static final int maxSize = 27000;
	protected static final int maxItemCount = 10;	
	protected int totalSize = 0;
	
	public Workbench() {
		addAlias("bench");
		addAlias("workbench");
		description = "a workbench";
		name = "a workbench";

	}
	
	public void add(MObject obj) {
		totalSize += ((Item)obj).getSize(false);
		super.add(obj);
	}
	
	public void remove(MObject obj) {
		totalSize -= ((Item)obj).getSize(false);
		super.remove(obj);
	}
	
	public boolean tryAdd(MObject obj) {
		if(((Item)obj).getSize(false) <= maxSize && items.size() <= maxItemCount ) {
			return true;
		}
		return false;
	}
	
	public boolean tryTake(Living who) {
		return false;
	}
	
	public void emptyBench() {
		items = new Vector();
	}
	
}
