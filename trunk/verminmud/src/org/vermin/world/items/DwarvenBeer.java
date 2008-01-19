/* DwarvenBeer.java
	13.9.2003 VV

	a pint a strong dwarven beer
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class DwarvenBeer extends DefaultItemImpl implements Drinkable
{

	private boolean full = true;

	public DwarvenBeer() {
		name = "pint";
		description = "a pint of dwarven beer";
		longDescription = "a delicious looking pint of strong dwarven beer";
	    pluralForm = "pints";
		size = 200;
		material = "glass";
		dp = 1;
		maxDp = 1;
		addAlias("beer");
		addAlias("dwarven beer");
		addAlias("strong dwarven beer");
		addAlias("a delicious looking pint of strong dwarven beer");
		addAlias("delicious looking pint of strong dwarven beer");
		addAlias("pint of strong dwarven beer");
		addAlias("pint of dwarven beer");
		addAlias("pint of strong beer");
		addAlias("pint of beer");
	}

	public int getNutritionValue()
	{
		return 10;
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
			l.notice("You quaff the pint of dwarven beer.");
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
