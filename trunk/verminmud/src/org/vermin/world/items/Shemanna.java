/* Shamanna.java
	12.1.2005 MV

	healing shemanna powder
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class Shemanna extends DefaultItemImpl implements Drinkable
{

	private boolean full = true;
	private int power;
	
	public Shemanna() {
		name = "shemanna";
		description = "a bag of shemanna powder";
		longDescription = "a bag of well prepared shemanna powder";
		pluralForm = "shemannas";
		size = 10;
		dp = 1;
		maxDp = 1;
		material = "leather";
		addAlias("powder");
		addAlias("shemanna powder");
		addAlias("bag");
		addAlias("bag of powder");
		addAlias("bag of shemanna");
		addAlias("bag of shemanna powder");
	}

	public void setPower(int p) {
		power = p;
	}

	public int getNutritionValue()
	{
		return 5;
	}

	/* Check that the given living can consume this */
	public boolean tryConsume(Living l)
	{
		return true;
	}

	/* Hook for consuming */
	public void consume(Living l)
	{
		if(full)
		{
			l.notice("You use a bag of shemanna.");
			l.notice("Your feel your wounds healing almost instantly.");
			l.addHp(power+100);
			full = false;
			longDescription = "an empty bag";
			description = "an empty bag";
			size = 3;
			l.remove(this);
		}
		else
		{
			l.notice("The bag is empty.");
		}
	}

}
