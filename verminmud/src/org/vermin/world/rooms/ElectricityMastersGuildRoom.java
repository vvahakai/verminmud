/* ElectricityMastersGuildRoom.java
	15.1.2005 MV
	
*/

package org.vermin.world.rooms;


public class ElectricityMastersGuildRoom extends GuildRoom
{

	private static final byte[] mentalStrengthBonus = { 0, 1, 2, 1, 0, 2}; 

	private static final byte[] mentalConstitutionBonus = { 0, 1, 0, 2, 0, 2};

	private static final byte[] mentalDexterityBonus = { 0, 2, 1, 0, 1, 1};

	private static final byte[] physicalConstitutionBonus = { 0, 0, 0, 2, 0, 1};

	private static final String[] guildTitles = { null, null, null, null, null, "Electricity Master"};

	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalDexterityBonus(int level) { return mentalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }
	
	public ElectricityMastersGuildRoom()
	{
		super("electricity masters");

		setDescription("Electricity masters guild class room");
		setLongDescription("This is a class room in the guild of electricity masters. This is where the master mages of the guild" + 
							"give training in ways of art.\n"+
							"You have to come here when you want to advance your level and train in the electric masters guild\n" +
							"Commands: advance, join, train, list, cost");
		this.guildLevels = 5;
		
		addTrainable("electricity",
		new byte[] { 0, 0, 15, 20, 30, 50}, 
		new byte[] { 0, 45, 100, 100, 100, 100 }
		, 7500);

		addTrainable("lightning bolt",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 25, 60, 95, 100, 100 }
		, 9500);

		addTrainable("electrocution",
		new byte[] { 0, 0, 0, 0, 0, 24}, 
		new byte[] { 0, 0, 10, 35, 60, 100 }
		, 11500);

		addTrainable("electricity lore",
		new byte[] { 0, 0, 0, 0, 10, 20}, 
		new byte[] { 0, 10, 30, 65, 85, 100 }
		, 13500);

		addTrainable("electricity shield",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 30, 65, 100, 100, 100 }
		, 8500);

		addTrainable("summon storm",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 25, 40, 60, 75, 100 }
		, 9000);

		addTrainable("summon lightning",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 20, 40, 65, 100 }
		, 12000);

		addTrainable("electric force",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 20, 65, 100 }
		, 14000);

		addTrainable("electric preparation",
		new byte[] { 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 30, 75 }
		, 15500);

	}

}
