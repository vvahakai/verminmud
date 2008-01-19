/* CreatedFood.java
	13.9.2003 VV

	a food dish created by the spell create food
	*/
package org.vermin.world.items;

import org.vermin.mudlib.*;

public class CreatedFood extends DefaultEdibleImpl
{


	public CreatedFood() {
		name = "food";
		longDescription = "a magically created food";
		pluralForm = "foods";
		size = 1;
		dp = 1;
		maxDp = 1;
	}

	public void setNutritionValue(int n)
	{
		maxSustenance = n;
		sustenance = n;
	}

	public void setName(String n)
	{
		name = n;
		longDescription = "a magically created " + n;
		pluralForm = n + "s";
	}

}
