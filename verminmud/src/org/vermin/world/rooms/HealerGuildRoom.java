/* HealerGuildRoom.java
	18.7.2002	VV & MV
	
*/

package org.vermin.world.rooms;


public class HealerGuildRoom extends GuildRoom
{

	private static final byte[] mentalStrengthBonus = { 0, 2, 0, 2, 0, 1, 0, 2, 0, 0, 0, 1, 0, 2, 3, 0, 0, 0, 0, 1, 4 };

	private static final byte[] mentalConstitutionBonus = { 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 2, 0, 1, 3};

	private static final byte[] mentalDexterityBonus = { 0, 2, 1, 0, 1, 3, 0, 0, 0, 0, 4, 0, 0, 2, 0, 2, 1, 1, 3, 1, 5};

	private static final byte[] physicalConstitutionBonus = { 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2};

	private static final String[] guildTitles = { null, "Aspirant Healer", null, null, null, "Licensed Healer", null, null, null, null, "Healer Chaplain", null, null, null, null, "Master Healer", null, null, null, null, "Chaplain Lord"};


	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalDexterityBonus(int level) { return mentalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }

	public HealerGuildRoom()
	{
		super("healer");

		setDescription("Main hall of the healer's guild");
		setLongDescription("You are in the main hall of the healer's guild of Vermin city. Many doors leave to classrooms where the masters of the guild teach aspiring healers in various magical and mundane healing skills. This is the place to come when you want to train and advance in the healer's guild.\n"+
	                       "Commands: advance, join, train, list, cost");
		this.guildLevels = 20;

		//implemented
		addTrainable("essence eye", 
		new byte[] { 0, 0, 10, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 40, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100} 		
		, 2000);

		//implemented
		addTrainable("fighting", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 30, 50, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		//implemented
		addTrainable("quick chant", 
		new byte[] { 0, 0, 0, 0, 0, 0, 13, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 0, 0, 0, 30, 30, 45, 45, 70, 70, 70, 70, 85, 85, 85, 100, 100, 100, 100, 100}
		, 10000);

		//implemented
		addTrainable("bludgeons", 
		new byte[] { 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 30, 30, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("prayer", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 45, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 45, 45, 70, 70, 85, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);

		//implemented
		addTrainable("bless", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 40, 40, 40, 75, 90, 90, 100, 100, 100, 100 }
		, 6000);

		addTrainable("remove poison",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 55, 55, 55, 55, 75, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("healing efficiency",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 28, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 50, 70, 80, 90, 100 }
		, 10000);

		//implemented
		addTrainable("flamestrike",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 50, 55, 70, 70, 90, 90, 100, 100 }
		, 11000);

		//implemented
		addTrainable("mental regeneration",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 35, 35, 35, 35, 50, 70, 85, 100, 100, 100 }
		, 6000);

		//implemented
		addTrainable("physical regeneration",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 35, 35, 35, 35, 50, 70, 85, 100, 100, 100 }
		, 13000);

		addTrainable("light",
		new byte[] { 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 50, 70, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 5000);

		addTrainable("remove scar",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 60, 80, 80, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		addTrainable("sex change",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 45, 45, 60, 60, 75, 95, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 7000);

		//implemented
		addTrainable("heal",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 30, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 55, 70, 70, 100, 100, 100, 100 }
		, 9000);

		//implemented
		addTrainable("major heal",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 0, 60}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 45, 65, 80, 100 }
		, 10000);

		//implemented
		addTrainable("cure small wounds",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 40, 40, 60, 75, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 12000);

		//implemented
		addTrainable("cure serious wounds",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 35, 35, 55, 55, 70, 90, 100, 100, 100, 100, 100, 100, 100 }
		, 14000);

		//implemented
		addTrainable("cause small wounds",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 40, 40, 60, 75, 75, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 8000);

		//implemented
		addTrainable("cause serious wounds",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 35, 35, 55, 55, 70, 90, 100, 100, 100, 100, 100, 100, 100 }
		, 11000);

		addTrainable("holy hand",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 40, 55, 70, 85, 100, 100, 100 }
		, 12000);

		//implemented
		addTrainable("reincarnation",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 21, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 75, 100 }
		, 13400);

		//implemented
		addTrainable("resurrection",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 40, 40, 60, 60, 70, 70, 100, 100 }
		, 13000);

		addTrainable("life link",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 45, 60, 70, 80, 95, 100 }
		, 12000);

		addTrainable("true life link",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 70, 84 }
		, 14900);

		//implemented
		addTrainable("mastery of healing",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 70 }
		, 15000);

	}

}
