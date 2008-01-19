/* InnerCircleGuildRoom.java
	4.12.2004	JT & MV
	
*/

package org.vermin.world.rooms;




public class InnerCircleGuildRoom extends GuildRoom {


	private static final byte[] mentalStrengthBonus = { 0, 1, 1, 0, 0, 1, 0, 2, 0, 1, 3 };
	
	private static final byte[] physicalStrengthBonus = { 0, 2, 0, 0, 0, 0, 1, 0, 0, 0, 2 };

	private static final byte[] physicalConstitutionBonus = { 0, 0, 1, 0, 3, 0, 0, 0, 2, 0, 2 };
	
	private static final byte[] mentalConstitutionBonus = { 0, 0, 0, 0, 2, 0, 0, 0, 3, 0, 2 };
	
	private static final byte[] mentalDexterityBonus = { 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 1 };
		
	private static final String[] guildTitles = { null, null, null, null, null, null, null, null, null, null, null};


	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getMentalDexterityBonus(int level) { return mentalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }


	public InnerCircleGuildRoom()
	{
		super("inner circle");

		setDescription("The Sacred Brotherhood of Inner Circle");
		setLongDescription("You have entered the house of the defenders of this hell mouth. The sanctity of this place " +
							"is beyond understanding. High powers are behind this huge structure. The walls and the roof " +
							"are filled with numerous glass and crystal ornaments, which seem to consist their beliefs.\n" +
							"Commands: advance, join, train, list");
		this.guildLevels = 10;
	
		addTrainable("unveil evil", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 20, 45, 60, 80, 100, 100, 100, 100, 100, 100 }
		, 4000);
		
		addTrainable("disclosure of unlife", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 20, 40, 55, 70, 90, 100, 100, 100, 100 }
		, 6500);
		
		addTrainable("liberate spirit", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 15, 30, 55, 75, 90, 100, 100, 100 }
		, 5500);

		addTrainable("resorb sanctity", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 20, 40, 60, 80, 90, 100 }
		, 16000);

		addTrainable("saintly shielding", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 30, 55, 70, 85, 100, 100 }
		, 8000);
		
		addTrainable("prayer of inner circle", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 15, 30, 55, 80, 100 }
		, 9500);
		
		addTrainable("raze heresy", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 25, 45, 70, 100 }
		, 10000);
		
		addTrainable("flow of virtue", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 65, 70, 70, 75, 80, 90, 95, 100, 100, 100 }
		, 10000);

		addTrainable("question virtue", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 60, 65, 70, 85, 95, 95, 100, 100, 100, 100 }
		, 6700);

		addTrainable("arcane knowledge",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 80, 80, 85, 85, 90, 95, 95, 100, 100, 100}
		, 20000);
		
		addTrainable("sigil of holy light",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 80, 90, 100, 100, 100, 100, 100, 100, 100, 100}
	    , 16000);
				
		addTrainable("purgatory",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 75, 75, 75, 85, 85, 95, 95, 100, 100, 100}
		, 16000);
				
		addTrainable("sacred covenant",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 50, 55, 60, 75, 80, 85, 85, 90, 95, 100}
        , 30000);

	}

}
