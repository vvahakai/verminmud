/* ConsiderSkill.java
	6.7.2002	VV
	
	skill for finding out how tough other players and monsters are
*/
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.world.items.CreatedFood;


public class CreateFoodSkill extends BaseSkill {

	public SkillType[] getTypes() {
		return new SkillType[0];
	}

	public String getName() {
		return "create food";
	}

	private String spellWords = "Suma famu suffa samu";

	/* Try to use this skill. */
	public boolean tryUse(Living who, MObject target) {
		if(!who.getRoom().isOutdoor())
		{
			who.notice("There are no materials here to create a food from.");
			return false;
		}
		return true;
	}
	
   public int getCost(SkillUsageContext suc) {
		return 230;
   }
	
   public int getTickCount() {
      return 7 + Dice.random(3);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Living target = (Living) suc.getTarget();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		CreatedFood food = new CreatedFood();
		String message = "";

		if(success > 0)
		{
				food.setNutritionValue(success);
				food.setName(getRandomFoodName());

				if(success < 10)
					message = "You gather some twigs and create a pathetic food.";
				else if(success < 25)
					message = "You manage to create a decent looking food.";
				else if(success < 75)
					message = "After some hunting for materials, you have created a food that will burn for a long time.";
				else if(success < 95)
					message = "You have created a food that surpasses the specifications of the Foodmakers assosiation of VerminCity.";
				else 
					message = "You have created a food fit for KINGS!";

				who.add(food);
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}

		who.notice(message);
	}


	public String getRandomFoodName() {
		int dices = Dice.random(5);
		switch(dices) {
			case 0: return "cake";
			case 1: return "bread";
			case 2: return "soup";
			case 3: return "stew";
			case 4: return "steak";
		}
		return "pudding";
	}
}
