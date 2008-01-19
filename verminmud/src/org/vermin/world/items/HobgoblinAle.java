/* HobgoblinAle.java
	13.9.2003 VV

	a pint of Hobgoblin ale
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class HobgoblinAle extends DefaultItemImpl implements Drinkable
{

	private boolean full = true;

	public HobgoblinAle() {
		name = "pint";
		description = "a pint of hobgoblin ale";
		longDescription = "a nice pint of hobgoblin ale";
	    pluralForm = "pints";
		size = 200;
		material = "glass";
		dp = 1;
		maxDp = 1;
		addAlias("ale");
		addAlias("hobgoblin ale");
		addAlias("a pint of hobgoblin ale");
		addAlias("pint of hobgoblin ale");
		addAlias("pint of ale");
	}

	public int getNutritionValue()
	{
		return 15;

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
			//FIXME: tässä pitäis varmaan ottaa se humaltuminen huomioon, jos ihmisillä olisi tarvetta humaltumiseen (mutta eipähän tule krapulaakaan)
			l.notice("You quaff a pint of ale.");
			full = false;
			longDescription = "an empty glass pint";
			description = "an empty pint";
			size = 50;
		}
		else
		{
			l.notice("Your pint is empty, maybe you should order another one.");
		}
	}

}
