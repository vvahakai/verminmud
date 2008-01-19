/*
 * InfantryPhalanxGuildRoom.java
 * Created on 20.2.2005
 */
package org.vermin.world.rooms;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.battle.ControlledMeleeBattleStyle;

/**
 * @author Ville
 *
 */
public class InfantryPhalanxGuildRoom extends GuildRoom {

	private static final byte[] physicalStrengthBonus = { 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 3, 0, 0, 0, 6, 3, 0, 0, 7 };

	private static final byte[] physicalConstitutionBonus = { 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 3, 0, 5, 0, 4, 0, 0, 7};

	private static final byte[] physicalDexterityBonus = { 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 3, 0, 0, 0, 2, 0, 3, 0, 3};

	private static final String[] guildTitles = { null, "Private", null, null, null, "Corporal", null, null, null, null, "Sergeant", null, null, null, null, "Lieutenant", null, null, null, null, "Captain"};

	public byte getPhysicalStrengthBonus(int level) { return physicalStrengthBonus[level]; }
	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }	
	
	public InfantryPhalanxGuildRoom() {
		super("Imperial Infantry Phalanx");
		
		setDescription("The training halls of the Imperial phalanx infantry in Vermincity");
		setLongDescription("The walls of this large hall are made of stone and the roof is supported by large " + 
							"stone pillars. The constant traffic of soldiers walking through this hall is " +
							"enormous and they come in and go out through different hallways that lead east, " +
							"south, north and the hallway that leads west out to the Vermincity streets. " +
							"You have to come here when you want to advance your level and train in the infantry.\n" +
							"Commands: advance, join, train, list, cost");
		this.guildLevels = 20;

		joinGuildRequirements.put("Imperial Infantry Recruits", 10);
		
		//implemented
		addTrainable("coordinated melee",
		new byte[] { 0, 0, 0, 0, 0, 0,  15, 15, 15, 15, 15, 30, 30, 30, 30, 30, 50, 50, 50, 50, 50 },
		new byte[] { 7, 12, 12, 23, 23, 28, 31, 35, 38, 42, 51, 60, 64, 69, 75, 100, 100, 100, 100, 100, 100 }
		, 9000);

		//implemented
		addTrainable("single hand combat", 
		new byte[] { 0, 0, 0, 0, 15, 15, 15, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 28, 31, 35, 38, 42, 51, 60, 64, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		//implemented
		addTrainable("multi hand combat", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20 }, 
		new byte[] { 31, 36, 41, 47, 54, 61, 70, 81, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		//implemented
		addTrainable("swords",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 21, 39, 54, 69, 82, 92, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("parry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 7, 14, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 50 }, 
		new byte[] { 0, 0, 0, 0, 0, 10, 27, 45, 52, 70, 87, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 10000);

		//implemented
		addTrainable("stun",
		new byte[] { 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 30, 0, 0 }, 
		new byte[] { 24, 30, 36, 42, 48, 54, 60, 66, 72, 78, 84, 90, 96, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6000);

		//implemented
		addTrainable("bash",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 67, 89, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 5000);

		//implemented
		addTrainable("polearms",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 27, 45, 67, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("find weakness",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 67, 91, 100, 100, 100, 100 }
		, 14000);

		//implemented
		addTrainable("critical",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 35, 54, 85, 100, 100, 100, 100, 100, 100 }
		, 9000);

		addTrainable("iron will", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 50, 79, 100, 100 }
		, 10000);

		addTrainable("advanced maneuvers",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 21, 48, 69, 81, 100 }
		, 15324);

		//implemented
		addTrainable("riposte",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 79, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);
	
		addTrainable("phalanx",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 35, 50, 63, 71, 80, 87, 94, 100 }
		, 11000);

	}

	public BattleStyle[] getBattleStyles() {
		return new BattleStyle[] { new ControlledMeleeBattleStyle() };
	}


}
