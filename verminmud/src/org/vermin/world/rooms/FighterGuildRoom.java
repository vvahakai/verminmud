/* FighterGuildRoom.java
	23.3.2002	VV
	
*/

package org.vermin.world.rooms;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.battle.ControlledMeleeBattleStyle;

public class FighterGuildRoom extends GuildRoom
{

	private static final byte[] physicalStrengthBonus = { 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 3, 0, 0, 0, 6, 3, 0, 0, 7 };

	private static final byte[] physicalConstitutionBonus = { 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 3, 0, 5, 0, 4, 0, 0, 7};

	private static final byte[] physicalDexterityBonus = { 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 3, 0, 0, 0, 2, 0, 3, 0, 3};

	private static final String[] guildTitles = { null, "Private", null, null, null, "Corporal", null, null, null, null, "Sergeant", null, null, null, null, "Lieutenant", null, null, null, null, "Captain"};

	public byte getPhysicalStrengthBonus(int level) { return physicalStrengthBonus[level]; }
	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }

	public FighterGuildRoom()
	{
		super("imperial infantry");

		setDescription("The training halls of the Imperial infantry in Vermincity");
		setLongDescription("The walls of this large hall are made of stone and the roof is supported by large " + 
							"stone pillars. The constant traffic of soldiers walking through this hall is " +
							"enormous and they come in and go out through different hallways that lead east, " +
							"south, north and the hallway that leads west out to the Vermincity streets. " +
							"You have to come here when you want to advance your level and train in the infantry.\n" +
							"Commands: advance, join, train, list, cost");
		this.guildLevels = 20;

		//implemented
		addTrainable("fighting", 
		new byte[] { 0, 0, 11, 25, 35, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 70},
		new byte[] { 0, 49, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100} 		
		, 5000);

		//implemented
		addTrainable("controlled melee",
		new byte[] { 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 15, 15, 15, 15, 15, 30, 30, 30, 30, 30, 50},
		new byte[] { 0, 0, 0, 0, 0, 7, 12, 12, 23, 23, 28, 31, 35, 38, 42, 51, 60, 64, 69, 75, 100}
		, 9000);

		//implemented
		addTrainable("consider", 
		new byte[] { 0, 0, 0, 0, 20, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}, 
		new byte[] { 0, 0, 37, 69, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		//implemented
		addTrainable("create torch", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 0, 40, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 1000);

		//implemented
		addTrainable("swimming", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 0, 0, 0, 10, 25, 45, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 1000);

		//implemented
		addTrainable("bludgeons", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 30, 60, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("push", 
		new byte[] { 0, 0, 0, 0, 0, 0, 15, 29, 43, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 25, 50, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		//implemented
		addTrainable("single hand combat", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 15, 15, 30, 30, 30, 30}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 7, 12, 12, 23, 23, 28, 31, 35, 38, 42, 51, 60, 64, 80, 100 }
		, 3000);

		//implemented
		addTrainable("multi hand combat", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 23, 28, 31, 36, 41, 47, 54, 61, 70, 81, 100 }
		, 3000);

		//implemented
		addTrainable("daggers", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 25, 54, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6000);

		//implemented
		addTrainable("swords",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 38, 62, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("parry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 7, 14, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 50}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 27, 52, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 10000);

		addTrainable("stun",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 9, 15, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 23, 51, 78, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6000);

		addTrainable("controlled withdrawal",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 42, 85, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		//implemented
		addTrainable("bash",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 36, 67, 89, 100, 100, 100, 100, 100 }
		, 5000);

		//implemented
		addTrainable("polearms",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 45, 67, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("warcry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 67, 91, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("critical",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 0, 25}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 35, 54, 85, 100, 100 }
		, 9000);

		addTrainable("iron will", //used to be stunned man...
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 50, 79, 100, 100 }
		, 10000);

		addTrainable("dirty fighting",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 21, 48, 69 }
		, 12000);

		//implemented
		addTrainable("find weakness",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 25}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 29, 58 }
		, 14000);

		//implemented
		addTrainable("riposte",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 79 }
		, 8000);
	
		addTrainable("phalanx",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35 }
		, 11000);

	}

	public BattleStyle[] getBattleStyles() {
		return new BattleStyle[] { new ControlledMeleeBattleStyle() };
	}

}
