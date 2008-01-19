/* SmitersGuildRoom.java
	4.12.2004	JT & MV
	
*/

package org.vermin.world.rooms;



public class SmitersGuildRoom extends GuildRoom {

	private static final byte[] physicalStrengthBonus = { 0, 1, 1, 2, 0, 1, 1, 2, 0, 1, 3 };

	private static final byte[] physicalConstitutionBonus = { 0, 0, 1, 0, 2, 0, 0, 0, 2, 0, 2 };
	
	private static final byte[] physicalResistanceBonus = { 0, 0, 0, 0, 2, 0, 0, 0, 3, 0, 2 };
	
	private static final byte[] physicalDexterityBonus = { 0, 0, 0, 0, 0, 1, 0, 1, 0, 2, 0 };
	
	private static final String[] guildTitles = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};


	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getPhysicalResistanceBonus(int level) { return physicalResistanceBonus[level]; }
	public byte getPhysicalStrengthBonus(int level) { return physicalStrengthBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }

	public String getGuildTitle(int level) { return guildTitles[level]; }


	public SmitersGuildRoom()
	{
		super("smiters");

		setDescription("The Holy of the Holies");
		setLongDescription("This room is very austere and ascetic in appearance. In the back wall of the room " +
							"is an altar made out of two granite slabs and decorated with a marble plaque " +
							"depicting a group of men with halos around their head. There are two candlesticks " +
							"on the both sides of the altar. Above the altar on the wall there is a beautiful " +
							"but seemingly ancient triptych. The adjoining walls are covered with gobelin " +
							"tapestries that show templar knights in various heroic acts and battles. There " +
							"are no benches on the cold stone floor, but even so, the atmosphere feels very " +
							"serene and holy in this room.\n" +
							"Commands: advance, join, train, list");
		this.guildLevels = 10;
	
	
		
		addTrainable("consider", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 1240);
		
		addTrainable("axes", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		addTrainable("swords", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);
		
		addTrainable("bludgeons", 
		new byte[] { 0, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		addTrainable("bash", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 6800);

		addTrainable("parry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 7500);

		addTrainable("iron will",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 90, 90, 95, 95, 100, 100, 100, 100, 100, 100}
		, 12000);

		addTrainable("dodge",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 50, 60, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 10000);

		addTrainable("kick",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 6000);

		addTrainable("sword and shield techniques",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 14000);

		addTrainable("combat discipline",
		new byte[] { 0, 30, 35, 40, 0, 45, 50, 0, 0, 0, 0}, 
		new byte[] { 0, 75, 75, 75, 90, 95, 95, 100, 100, 100, 100}
		, 14000);

		addTrainable("righteous fervor",
		new byte[] { 0, 30, 35, 0, 40, 45, 0, 60, 0, 0, 0}, 
		new byte[] { 0, 55, 60, 65, 70, 85, 90, 90, 90, 95, 100}
		, 15000);

		addTrainable("stun",
		new byte[] { 0, 0, 0, 50, 60, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 50, 55, 60, 70, 70, 80, 85, 80, 90, 100}
        , 6000);
		
		addTrainable("critical",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 50, 50, 50, 60, 60, 60, 70, 80, 90, 100}
        , 9000);
		
		addTrainable("two weapon combat",
		new byte[] { 0, 0, 0, 20, 25, 30, 35, 40, 45, 50, 60},
		new byte[] { 0, 0, 40, 50, 55, 60, 65, 75, 85, 90, 100 }
        , 14000);
		
		addTrainable("bludgeon mastery",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30},
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 40, 70 }
        , 20000);
		
		addTrainable("divine wrath",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70 }
        , 20000);
		
		addTrainable("smite",
		new byte[] { 0, 0, 0, 0, 0, 0, 10, 20, 30, 40, 50},
		new byte[] { 0, 0, 0, 0, 0, 20, 30, 40, 50, 70, 90 }
	    , 16000);
				
		
	}

}
