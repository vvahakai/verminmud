/* LargeBirdRace.java
	12.9.2003   MV
	
	Large bird race implementation.
*/

package org.vermin.world.races;

import org.vermin.mudlib.Race;

public class LargeBirdRace extends BirdRace implements Race
{
	private int size = 20;

	private int physicalstr = 30;

	private int physicalcon = 25;

	private int physicaldex = 20;

	private int physicalcha = 10;

	public int getMinimumVisibleIllumination() {
		return 35;
	}
	public int getLifeAlignment() {
		return 0;
	}
	public int getProgressAlignment() {
		return 0;
	}
		
	public static Race getInstance()
	{
		if(_instance == null) {
			_instance = new LargeBirdRace();
			_instance.start();
		}
		
		return _instance;
	}
	
	public int getBaseHpRegen()
	{
		return 35;
	}
	
}
