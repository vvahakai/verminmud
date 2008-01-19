/* BattleMageGuildRoom.java
	2.8.2003	VV
	
*/

package org.vermin.world.rooms;



public class BattleMageGuildRoom extends GuildRoom
{

	private static final byte[] mentalStrengthBonus = { 0, 1, 0, 1, 0, 2, 0, 1, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 3, 8}; 

	private static final byte[] mentalConstitutionBonus = { 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 2, 0, 1, 3, 2};

	private static final byte[] mentalDexterityBonus = { 1, 0, 1, 0, 2, 0, 1, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 3, 0, 5};

	private static final byte[] physicalConstitutionBonus = { 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 2};

	private static final byte[] magicResistanceBonus = { 1, 0, 0, 1, 0, 0, 1, 0, 0, 2, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 5};

	private static final String[] guildTitles = { null, "Battlemage apprentice", null, null, null, null, null, null, null, null, "Battlemage", null, null, null, null, "High mage", null, null, null, null, "Mage lord"};

	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalDexterityBonus(int level) { return mentalDexterityBonus[level]; }
	public byte getMagicResistanceBonus(int level) { return magicResistanceBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }
	
	public BattleMageGuildRoom()
	{
		super("battlemage");

		setDescription("Battlemage guild class room");
		setLongDescription("This is a class room in the guild of Battlemages. This is where the master mages of the guild" + 
							"give training in ways of art.\n"+
							"You have to come here when you want to advance your level and train in the battlemage guild\n" +
							"Commands: advance, join, train, list, cost");
		this.guildLevels = 20;
		
		//implemented
		addTrainable("essence eye", 
		new byte[] { 0, 0, 14, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 25, 55, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100} 		
		, 5000);

		//implemented
		addTrainable("quick chant", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 13, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 55},
		new byte[] { 0, 0, 0, 13, 13, 23, 35, 45, 48, 70, 73, 79, 82, 85, 89, 94, 100, 100, 100, 100, 100}
		, 10000);

		//implemented
		addTrainable("fighting", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 30, 50, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		//implemented
		addTrainable("polearms", 
		new byte[] { 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 30, 30, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("consider", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 12, 23, 34, 45, 54, 70, 77, 85, 93, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 1843);

		//implemented
		addTrainable("magic lore", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 40, 40, 40, 75, 90, 90, 100, 100, 100, 100 }
		, 6000);

		//implemented
		addTrainable("create food",
		new byte[] { 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 30, 30, 50, 70, 80, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 1235);

		//implemented
		addTrainable("create money",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 30, 30, 50, 55, 70, 70, 90, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3467);

		addTrainable("darkness",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 50, 70, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 4000);

		addTrainable("light",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 50, 70, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 4000);
		
		//implemented
		addTrainable("magic missile",
		new byte[] { 0, 0, 20, 34, 42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 50, 70, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 5430);

		addTrainable("nightvision",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 32, 56, 65, 74, 85, 92, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 5132);
 
		//implemented
		addTrainable("shelter",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 20, 35, 35, 35, 35, 50, 70, 85, 100, 100, 100, 100, 100, 100, 100 }
		, 6000);

		addTrainable("hover",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 20, 35, 35, 35, 35, 50, 70, 85, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3256);

		addTrainable("avoidance",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 60, 80, 80, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		addTrainable("teleport",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 45, 45, 60, 60, 75, 95, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("force globe",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 30, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 55, 70, 70, 100, 100, 100, 100 }
		, 9000);

		addTrainable("faerie fire",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 0, 60}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 45, 65, 80, 100 }
		, 10000);

		//implemented
		addTrainable("enfeeblement",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 40, 40, 60, 75, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 12000);

		//implemented
		addTrainable("golden arrow",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 30, 45, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 40, 40, 60, 75, 75, 100, 100, 100, 100, 100, 100, 100 }
		, 9999);
	
		//implemented
		addTrainable("forget",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 35, 35, 55, 55, 70, 90, 100, 100, 100, 100, 100, 100, 100 }
		, 11000);

		addTrainable("invisibility",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 40, 55, 70, 85, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6543);

		/*addTrainable("know alignment",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
		new byte[] { 0, 0, 0, 35, 55, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3400);*/

		addTrainable("mirror image",
		new byte[] { 0, 0, 0, 12, 24, 44, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 25, 40, 40, 60, 60, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		addTrainable("web",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 45, 60, 70, 80, 95, 100, 100, 100, 100, 100 }
		, 12000);

		//implemented
		addTrainable("haste",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 40, 55, 70, 85, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6943);

		//implemented
		addTrainable("relocate",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 70, 84, 100}
		, 10900);

		addTrainable("lead weight",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 25, 45, 60, 70, 80, 95, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7450);

		addTrainable("feather weight",
		new byte[] { 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 25, 45, 60, 70, 80, 95, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7450);

		//somewhat implemented (poison damage type sucks)
		addTrainable("poison",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 25, 45, 60, 70, 80, 95, 100, 100, 100, 100 }
		, 7450);

	}

}
