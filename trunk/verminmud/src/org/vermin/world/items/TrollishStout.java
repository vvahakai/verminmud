/* TrollishStout.java
	13.9.2003 VV

	a pint a strong Trollish stout
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class TrollishStout extends DefaultItemImpl implements Drinkable
{

	private boolean full = true;

	public TrollishStout() {
		name = "pint";
		description = "a pint of trollish stout";
		longDescription = "a strong-looking pitch black pint of stout";
	    pluralForm = "pints";
		size = 200;
		material = "glass";
		dp = 1;
		maxDp = 1;
		addAlias("stout");
		addAlias("trollish stout");
		addAlias("a pint of trollish stout");
		addAlias("pint of trollish stout");
		addAlias("pint of stout");
	}

	public int getNutritionValue()
	{
		return 20;

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
			l.notice("You drink a pint of stout.");
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
