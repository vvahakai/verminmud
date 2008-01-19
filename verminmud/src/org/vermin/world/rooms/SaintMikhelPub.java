/* SaintMikhelPub.java
	13.9.2003 VV / Council 4
	
	the Legendary SaintMikhelPub.
	
*/
package org.vermin.world.rooms;

import java.util.Vector;

import org.vermin.mudlib.Living;
import org.vermin.util.Print;
import org.vermin.world.items.DwarvenBeer;
import org.vermin.world.items.HobgoblinAle;
import org.vermin.world.items.TrollishStout;

public class SaintMikhelPub extends DefaultShop
{

	public SaintMikhelPub()
	{
		inventory = new Vector();
		shopType[0] = DRINK;
		setDescription("the Public house Saint Mikhel");
		setLongDescription("PUB!");
		Vector beerBarrel = new Vector();
		Vector stoutBarrel = new Vector();
		Vector aleBarrel = new Vector();
		for(int i=0; i < 50; i++)
		{
			beerBarrel.add(new DwarvenBeer());
			stoutBarrel.add(new TrollishStout());
			aleBarrel.add(new HobgoblinAle());
		}
		inventory.add(beerBarrel);
		inventory.add(stoutBarrel);
		inventory.add(aleBarrel);
	}

	public String getLongDescription()
	{
		return "You are standing in a somewhat crowded pub. The center of this room contains a "+
				"large bar with multiple taps lining it. The walls are covered with large mirrors "+
				"and cupboards full of precious looking bottles of whiskey. There are some free "+
				"seats at the corner of the bar and at the back. The regulars sitting at the bar "+
				"don't seem to notice you.\n"+
				"Available commands: buy <num>, list";
	}

	public boolean action(Living who, Vector cmd) {
		if(!(who instanceof org.vermin.mudlib.DefaultPlayerImpl))
			return false;
			
		String command = (String) cmd.get(0);
		
		if(command.equalsIgnoreCase("buy")) {
			if(cmd.size() >= 2)
			{
				String typenum = Print.vectorToString(cmd, " ", 1);
				String itemnum = "0";
				if(cmd.size() >= 3)
					itemnum = Print.vectorToString(cmd, " ", 2);

				buyItem(who, typenum, itemnum);
				return true;
			}
			who.notice("Buy what?");
			return true;		
		} else if(command.equalsIgnoreCase("sell")) {
			who.notice("The barkeep is not interested in your attempts to change your stuff for beer.");
			return true;
		} else if(command.equalsIgnoreCase("value")) {
			who.notice("The barkeep values your item worth two beers.");
			return true;
		} else if(command.equalsIgnoreCase("list")) {
			if(cmd.size() >= 2)
			{
				String item = Print.vectorToString(cmd, " ", 1);
				if(item.equals("stack") && cmd.size() >= 3)
				{
					String stacknum = Print.vectorToString(cmd, " ", 2);
					listStack(who, stacknum);
				}
				else
					listInventory(who, inventory, item);
				return true;
			}
			listInventory(who, inventory, null);
			return true;
		} else {
			return false;
		}
	}

}
