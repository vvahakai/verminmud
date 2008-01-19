
package org.vermin.world.rooms;

import java.util.HashMap;
import java.util.Vector;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Edible;
import org.vermin.mudlib.FoodFactory;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;

/**
 * A shop to buy consumables from.
 * 
 * @author Jaakko Pohjamo
 */
public class FoodShop extends DefaultShop {
	
	public FoodShop() {
		inventory = new Vector();
		shopType = new int[1];
		shopType[0] = DefaultShop.FOOD;
		startItemCount = 10;
		setDescription("The Sleepless Giants' 24/7 dead cow take-away shoppe");
		setLongDescription("This one installment of the Sleepless Giants' fast food shop chain famous all over the Vermin world. The shop is always bustling with activity day and night as customers happily carry, or in some cases, drag their purchases out. The mixed smells of raw and deep fried meats make the air almost solid. On the walls you see various posters advertising the wholesome dishes available, and a complete price list is posted above the long service counter.");
		extendedDescriptions = new HashMap();
		extendedDescriptions.put("poster", "One of the posters reads \"Why notte try our 'An Unusually Large Dead Cow Meal' for a Giants hunger!\".");
		extendedDescriptions.put("posters", "One of the posters reads \"Why notte try our 'An Unusually Large Dead Cow Meal' for a Giants hunger!\".");
	}
	
	public void start() {
//		System.out.println("[FoodShop] starting.");
		super.start();
//		System.out.println("[FoodShop] super start returned.");
				
		FoodFactory ff = FoodFactory.getInstance();
//		System.out.println("[FoodShop] gof FoodFactory instance: "+ff);
		
//		System.out.println("[FoodShop] item count: "+startItemCount);
		for(int i=0; i < startItemCount; i++) {
			Item food = ff.create();
//			System.out.println("[FoodShop] created food: "+food);
			int count = Dice.random(20);
			while(count > 0)
			{
				if(food != null) {
					Item clonedFood = (Item) food.getClone();
//					System.out.println("[FoodShop] cloned food: "+clonedFood);
					addToInventory(clonedFood);
				}
				count--;
			}
		}
		int count = 5 + Dice.random(20);
		for(int i=0; i<count; i++) {
			addToInventory(new org.vermin.mudlib.skills.Tinning.TinningKit());
		}
	}

	public String getLongDescription()
	{
		return longDescription;
	}
	
	public boolean action(Living who, Vector cmd) {
		String command = (String) cmd.get(0);
		
		if(command.equalsIgnoreCase("sell")) {
			who.notice("You cannot sell anything here.");
			return true;
		}
		
		return super.action(who, cmd);
	}

	public long calculateCost(Item eq) {
		if(eq instanceof Edible) {
			return (int) (((Edible) eq).getNutritionValue() / 5);
		}
		return super.calculateCost(eq);
	}

}
