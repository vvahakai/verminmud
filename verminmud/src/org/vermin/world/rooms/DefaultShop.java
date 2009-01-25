/* DefaultShop.java
	13.6.2002 VVä / Council 4
	
	Default shop.
	
*/
package org.vermin.world.rooms;

import java.util.*;
import org.vermin.mudlib.*;
import org.vermin.util.*;

public class DefaultShop extends DefaultRoomImpl
{

	/* Vector of Items in shop inventory */
	protected Vector inventory;
	protected int[] shopType;

	/* Shop Types. */
	public static final int GENERAL	 = 1;
	public static final int WEAPON	 = 2;
	public static final int ARMOUR	 = 3;
	public static final int CLOTHES	 = 4;
	public static final int FOOD	 = 5;
	public static final int DRINK    = 6;
	public static final int BOOK	 = 7;
	public static final int MAGICAL	 = 8;

	public static final int MAGICPOINTVALUE = 3469;
	public int startItemCount = 0;
	public int startItemCraftmanship = 40;

	public DefaultShop()
	{
		inventory = new Vector();
		shopType = new int[1];
		shopType[0] = GENERAL;
		setDescription("Shop");
		setLongDescription("This is a shop. You can sell and buy things here.");
	}

	public void start()
	{
		super.start();
		boolean weapons = false;
		boolean armours = false;

		for(int i=0;i<shopType.length;i++)
		{
			if(shopType[i] == WEAPON)
				weapons = true;
			if(shopType[i] == ARMOUR)
				armours = true;
		}

		for(int i=0; i < startItemCount; i++)
		{
			if(weapons)
			{
				WeaponFactory wf = WeaponFactory.getInstance();
				WeaponFactory.Description desc = new WeaponFactory.Description();
				desc.craftmanship = Dice.random(startItemCraftmanship/2)+(startItemCraftmanship/2);
				Item eq = wf.create(desc);
				int count = Dice.random(20);
				while(count > 0)
				{
					Item clonedEq = (Item) eq.getClone();
					addToInventory(clonedEq);
					count--;
				}
			}
			if(armours)
			{
				ArmourFactory af = ArmourFactory.getInstance();
				ArmourFactory.Description desc = new ArmourFactory.Description();
				desc.craftmanship = Dice.random(startItemCraftmanship/2)+(startItemCraftmanship/2);
				Item eq = af.create(desc);
				int count = Dice.random(20);
				while(count > 0)
				{
					if(eq != null) {
						Item clonedEq = (Item) eq.getClone();
						addToInventory(clonedEq);
					}
					count--;
				}
			}
		}
	}

	public String getLongDescription()
	{
		return super.getLongDescription()+"\nAvailable commands: buy <num>, buy <stack> <item>, sell <item>, value <item>, view <item>, view <stack> <item>, list, list <type>, list stack <num>";
	}

	public boolean action(Living who, Vector cmd) {
		if(!(who instanceof Player))
			return false;
			
		String command = (String) cmd.get(0);

		if(command.equalsIgnoreCase("buy")) {
			if(cmd.size() >= 2)
			{
				String typenum = (String) cmd.get(1);
				String itemnum = "0";
				if(cmd.size() >= 3)
					itemnum = (String) cmd.get(2);
				buyItem(who, typenum, itemnum);
				return true;
			}
			who.notice("Buy what?");
			return true;		
		} else if(command.equalsIgnoreCase("sell")) {
			if(cmd.size() >= 2)
			{
				if(((String)cmd.get(1)).equals("all"))
				{
					String item = null;
					if(cmd.size() >= 3)
					{
						item = Print.vectorToString(cmd, " ", 2);
					}
					sellAll(who,item);
					return true;
				}
				else
				{
					String item = Print.vectorToString(cmd, " ", 1);
					sellItem(who, item);
					return true;
				}
			}
			who.notice("Sell what?");
			return true;
		} else if(command.equalsIgnoreCase("value")) {
			if(cmd.size() >= 2)
			{
				String item = Print.vectorToString(cmd, " ", 1);
				valueItem(who, item);
				return true;
			}
			who.notice("Value what?");
			return true;
		} else if(command.equalsIgnoreCase("view")) {
			if(cmd.size() >= 2)
			{
				String typenum = (String) cmd.get(1);
				String itemnum = "0";
				if(cmd.size() >= 3)
					itemnum = (String) cmd.get(2);
				viewItem(who, typenum, itemnum);
				return true;
			}
			who.notice("View what?");
			return true;
		} else if(command.equalsIgnoreCase("list")) {
			if(cmd.size() >= 2)
			{
				String item = (String) cmd.get(1);
				if(item.equalsIgnoreCase("stack") && cmd.size() >= 3)
				{
					String stacknum = Print.vectorToString(cmd, " ", 2);
					System.out.println("stack list");
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

	public long calculateCost(Item eq)
	{
		long cost = 0;
		Material mat = eq.getMaterial();
		if(eq instanceof Wieldable)
		{
			Wieldable weapon = (Wieldable) eq;
			int damageCounter = 0;
			Damage[] dam = weapon.getHitDamage(new DummyLiving());
			for(int i=0;i<dam.length;i++)
			{
					damageCounter += dam[i].damage * 5;
			}
			cost = (long) ((mat.getValue() * eq.getSize(false)) + (damageCounter + weapon.getDefensiveValue()) * 3.141596);
		}
		else if(eq instanceof Wearable)
		{
			Wearable armour = (Wearable) eq;
			
			cost = (long) ((mat.getValue() * eq.getSize(false)) + (( armour.getDp() * 100 / armour.getMaxDp() ) + (armour.getArmourValue()) * 5) * 3.141596);
		}
		else
		{
			cost = (long) ((mat.getValue() * eq.getSize(false)) * 3.141596);
		}
		if(eq instanceof MagicItem)
		{
			MagicItem magical = (MagicItem) eq;

			cost += (magical.getMagicValue() * MAGICPOINTVALUE);
		}
		return cost;
	}

	private boolean checkType(Item eq)
	{
		for(int i=0;i<shopType.length;i++)
		{
			if(shopType[i] == GENERAL && !(eq instanceof MagicItem))
				return true;
			else if(eq instanceof Wieldable && shopType[i] == WEAPON)
				return true;
			else if(eq instanceof Drinkable && shopType[i] == DRINK)
				return true;
			else if(eq instanceof MagicItem && shopType[i] == MAGICAL)
				return true;
			else if(eq instanceof Wearable)
			{
				Wearable armour = (Wearable) eq;
				if(armour.getArmourValue() > 0 && shopType[i] == ARMOUR)
					return true;
				else if(armour.getArmourValue() == 0 && shopType[i] == CLOTHES)
					return true;
			}
		}
		return false;
	}


	private void valueItem(Living who, String what)
	{
		Item eq = (Item) who.findByNameAndType(what, Types.ITEM);
		if(eq != null && eq.isVisible())
		{
	        long cost = calculateCost(eq) / 2;	// Tämä on sitä paljon puhuttua tunetusta
			who.notice(eq.getDescription() +" is worth "+(float) (cost) / (float) (Money.Coin.COPPER.getValue())+" copper coins.");
			return;
		}
		who.notice("Value what?");
	}

	//typenum = itemstack number, itemnum = item number
	protected void viewItem(Living who, String typenum, String itemnum)
	{
		int type = 0;
		int item = 0;
		try
		{
			type = Integer.parseInt(typenum);
		} catch(NumberFormatException nfe) {
			who.notice("Syntax: view <num>");
			return;
		}

		try
		{
			item = Integer.parseInt(itemnum);
		} catch(NumberFormatException nfe) {
			who.notice("Syntax: view <num>");
			return;
		}

		if(type < inventory.size())
		{
			Vector itemstack = (Vector) inventory.get(type);
			if(item < itemstack.size())
			{
				
				Item eq = (Item) itemstack.get(item);
				who.notice(eq.getLongDescription());
				return;
			}
		}
		who.notice("view what?");
	}

	protected void addToInventory(Item eq)
	{
		if(inventory.size() > 0)
		{
			for(int i=0;i < inventory.size(); i++)
			{
				Vector itemstack = (Vector) inventory.get(i);
				Item ware = (Item) itemstack.get(0);
				if(ware.getDescription().equals(eq.getDescription()))
				{
					itemstack.add(eq);
					return;
				}
			}

		}
		Vector itemstack = new Vector();
		itemstack.add(eq);
		inventory.add(itemstack);
	}

	//typenum = itemstack number, itemnum = item number
	private void removeFromInventory(int typenum, int itemnum) 
	{
		if(inventory.size() > typenum)
		{
			Vector itemstack = (Vector) inventory.get(typenum);
			if(itemstack.size() > itemnum)
			{
				itemstack.remove(itemnum);
			}
			if(itemstack.size() == 0)
			{
				inventory.remove(typenum);
			}
		}
	}

	private void sellAll(Living who, String what)
	{
		Vector soldItems = new Vector();
		Iterator e = who.findByType(Types.ITEM);
		while(e.hasNext())
		{
			Item i = (Item) e.next();
			Item soldEq = null;
			
			if(what != null)
			{
				if(i.isAlias(what))
				{
					soldEq = sellItem(who,i);
				}
			}
			else
			{
				soldEq = sellItem(who,i);
			}

			if(soldEq != null) {
				soldItems.add(soldEq);
			}
		}

		e = soldItems.iterator();
		while(e.hasNext())
		{
			Item i = (Item) e.next();
			who.remove(i);
		}
	}

	private void sellItem(Living who, String what)
	{
		Item eq = (Item) who.findByNameAndType(what, Types.ITEM);
		boolean eqWornOrWielded = false;

		for(Wieldable weapon : who.getWieldedItems(false)) {
			if(weapon != null && weapon.equals(eq)) {
				eqWornOrWielded = true;
			}
		}
		for(Wearable armour : who.getWornItems()) {
			if(armour != null && armour.equals(eq)) {
				eqWornOrWielded = true;
			}
		}

		if(!eqWornOrWielded) {
			eq = sellItem(who, eq);
			who.remove(eq);
		}
	}


	//returns the item if it was sold, notice that caller must make sure that the item is removed from living's inventory!
	private Item sellItem(Living who, Item eq)
	{
		if(eq != null && eq.isVisible() && eq.tryDrop(who,this))
		{
	        long cost = calculateCost(eq) / 2;
			if(cost > 0 && checkType(eq))
			{
				Money cash = who.getMoney();
				cash.addValue(cost);
				addToInventory(eq);
				who.notice("You sell "+ eq.getDescription() +" for "+ (float) (cost) / (float) (Money.Coin.COPPER.getValue()) +" copper coins.");
				return eq;
			}
			else
			{
				who.notice("Your "+ eq.getDescription() +" is worthless.");
			}		
		}
		return null;
	//	who.notice("Sell what?");
	}

	//typenum = itemstack number, itemnum = item number
	protected void buyItem(Living who, String typenum, String itemnum)
	{
		int type = 0;
		int item = 0;
		try
		{
			type = Integer.parseInt(typenum);
		} catch(NumberFormatException nfe) {
			who.notice("Syntax: buy <num>");
			return;
		}

		try
		{
			item = Integer.parseInt(itemnum);
		} catch(NumberFormatException nfe) {
			who.notice("Syntax: buy <num>");
			return;
		}

		if(type < inventory.size())
		{
			Vector itemstack = (Vector) inventory.get(type);
			if(item < itemstack.size())
			{
				
				Money cash = who.getMoney();
				Item eq = (Item) itemstack.get(item);
				long cost = calculateCost(eq);
				if(cash != null && cash.getValue() > cost)
				{
					cash.substractValue(cost);
					who.add(eq);
					removeFromInventory(type,item);
					who.notice("You buy "+ eq.getDescription() +" for "+ (float) (cost) / (float) (Money.Coin.COPPER.getValue()) +" copper coins.");
				}
				else
				{
					who.notice("It would cost you "+ (float) (cost/Money.Coin.COPPER.getValue()) + " copper coins. You can't afford that");
				}
				return;
			}
		}
		who.notice("Buy what?");
	}

	protected void listStack(Living who, String typenum)
	{
		int type = -1;

		if(typenum != null)
		{
			try
			{
				type = Integer.parseInt(typenum);
			} catch(NumberFormatException nfe) {
				who.notice("Syntax: list, list <type>, list stack <num>");
				return;
			}
		}
		
		if(type == -1)
		{
			listInventory(who, inventory, null);
		}
		else
		{
			System.out.println("stack "+type);
			if(inventory.size() > type)
			{
				listInventory(who, (Vector) inventory.get(type), null);
			}
			else
			{
				who.notice("No such item stack.");
			}
		}
	}

	protected void listInventory(Living who, Vector inv, String desc)
	{
		boolean tableContainsRows = false;
		if(inv.size() > 0)
		{
			Table t = new Table();
			t.addHeader("#", 3, Table.ALIGN_LEFT);
			t.addHeader("Item", 35, Table.ALIGN_LEFT);
			t.addHeader("Cost", 7, Table.ALIGN_MIDDLE);
			t.addHeader("Weight", 6, Table.ALIGN_MIDDLE);
			if(inv.get(0) instanceof Vector)
				t.addHeader("Count", 4, Table.ALIGN_MIDDLE);

			for(int i=0; i<inv.size(); i++) {
				Object stuff = inv.get(i);
				Item eq = null;
				if(stuff instanceof Item)
				{
					eq = (Item) stuff;
				}
				else if(stuff instanceof Vector)
				{
					Vector itemstack = (Vector) stuff;
					eq = (Item) itemstack.get(0);
				}
				else
				{
					continue;
				}
				
				boolean matchArmourSlot = false;
			    if(eq instanceof Wearable) {
					Wearable armour = (Wearable) eq;
					for(Slot slot : armour.getSlots()) {
						if(slot.type.equals(desc)) {
							matchArmourSlot = true;
						}
					}
				}
					

				if(desc == null || 
				   ( eq instanceof Wieldable && ((Wieldable) eq).getWeaponType().getSkillName().indexOf(desc) != -1 ) || 
				   eq.isAlias(desc) || 
				   matchArmourSlot ||
				   eq.getDescription().indexOf(desc) != -1 )
				{		
					t.addRow();
					t.addColumn(Integer.toString(i),3);
					t.addColumn(eq.getDescription(), 35, Table.ALIGN_LEFT);
					t.addColumn(Float.toString((float) ( (float) calculateCost(eq)/ (float) Money.Coin.COPPER.getValue() )), 7, Table.ALIGN_RIGHT); 
					Material mat = eq.getMaterial();
					float weight = (float) (( (float) eq.getSize() * (float) mat.getWeight() ) / 1000.0);
					t.addColumn(Float.toString(weight),
					6, Table.ALIGN_RIGHT);
					if(stuff instanceof Vector)
					{
						Vector itemstack = (Vector) stuff;
						t.addColumn(Integer.toString(itemstack.size()), 4, Table.ALIGN_RIGHT);
					}
					tableContainsRows = true;
				}
			}
			if(tableContainsRows)
				who.notice(t.render());
			else
				who.notice("No such items in stock.");
		}
		else
		{
			who.notice("Shop has nothing to sell.");
		}

	}
	
}
