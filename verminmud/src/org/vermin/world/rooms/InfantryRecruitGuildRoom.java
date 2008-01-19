/*
 * InfantryRecruitGuildRoom.java
 * Created on 19.2.2005
 */
package org.vermin.world.rooms;


/**
 * @author Ville
 */
public class InfantryRecruitGuildRoom extends GuildRoom {

	private static final byte[] physicalStrengthBonus =     { 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2 };

	private static final byte[] physicalConstitutionBonus = { 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2};

	private static final byte[] physicalDexterityBonus =    { 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0};

	private static final String[] guildTitles = { null, "Private", null, null, null, null, null, null, null, null, "Corporal"};
	
	public byte getPhysicalStrengthBonus(int level) { return physicalStrengthBonus[level]; }
	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }	
	
	public InfantryRecruitGuildRoom() {
		super("Imperial Infantry Recruits");
		
		setDescription("The training halls of the recruits of Imperial infantry in Vermincity");
		setLongDescription("The walls of this large hall are made of stone and the roof is supported by large " + 
							"stone pillars. The constant traffic of soldiers walking through this hall is " +
							"enormous and they come in and go out through different hallways that lead east, " +
							"south, north and the hallway that leads west out to the Vermincity streets. " +
							"Recruits come here when they want to train their skills.\n" +
							"Commands: advance, join, train, list, cost");
		this.guildLevels = 10;	

		
		addTrainable("fighting", 
		new byte[] { 0, 0, 11, 25, 35, 45, 45, 45, 45, 45, 45 },
		new byte[] { 0, 49, 75, 100, 100, 100, 100, 100, 100, 100, 100 } 		
		, 4563);
		
		addTrainable("consider", 
		new byte[] { 0, 0, 0, 0, 20, 30, 30, 30, 30, 30, 30 }, 
		new byte[] { 0, 0, 37, 69, 100, 100, 100, 100, 100, 100, 100 }
		, 1864);

		addTrainable("create torch", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		new byte[] { 0, 0, 0, 40, 75, 100, 100, 100, 100, 100, 100 }
		, 1021);		
		
		addTrainable("swimming", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		new byte[] { 0, 0, 0, 0, 0, 10, 25, 45, 70, 100, 100 }
		, 1234);

		addTrainable("bludgeons", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35 }, 
		new byte[] { 0, 0, 0, 0, 30, 60, 100, 100, 100, 100, 100 }
		, 6875);

		addTrainable("push", 
		new byte[] { 0, 0, 0, 0, 0, 0, 15, 29, 43, 49, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 25, 50, 100, 100, 100, 100 }
		, 3194);

		addTrainable("single hand combat", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 7, 12, 12, 23, 23 }
		, 3456);

		addTrainable("multi hand combat", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 11, 23, 28 }
		, 3742);

		addTrainable("daggers", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35 }, 
		new byte[] { 0, 0, 0, 0, 0, 25, 54, 100, 100, 100, 100 }
		, 6132);
		
		addTrainable("tinning", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 18, 41, 69, 90, 100, 100, 100, 100, 100, 100, }
		, 945);	
		
		addTrainable("stun",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 6, 12, 18 }
		, 5974);
		
		addTrainable("bash",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 36 }
		, 5000);
		
		
	}

	

}
