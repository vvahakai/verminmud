/* ZealotsGuildRoom.java
	4.12.2004	JT & MV
	
*/

package org.vermin.world.rooms;



public class ZealotsGuildRoom extends GuildRoom {

	private static final byte[] mentalStrengthBonus = { 0, 1, 1, 2, 0, 1, 1, 2, 0, 1, 3 };

	private static final byte[] physicalConstitutionBonus = { 0, 0, 1, 0, 1, 0, 0, 0, 2, 0, 2 };
	
	private static final byte[] mentalConstitutionBonus = { 0, 0, 0, 0, 2, 0, 0, 0, 3, 0, 2 };
	
	private static final byte[] physicalDexterityBonus = { 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0 };
	
	private static final String[] guildTitles = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};


	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }

	public String getGuildTitle(int level) { return guildTitles[level]; }


	public ZealotsGuildRoom()
	{
		super("zealots");

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
		
		addTrainable("polearms", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);

		addTrainable("swords", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);
		
		addTrainable("push", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 3000);

		addTrainable("parry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 7500);

		addTrainable("iron will",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 90, 90, 95, 95, 100, 100, 100, 100, 100, 100}
		, 12000);

		addTrainable("dodge",
		new byte[] { 0, 0, 0, 40, 50, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 10000);

		addTrainable("banish abomination",
		new byte[] { 0, 40, 50, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 7500);

		addTrainable("last rites",
		new byte[] { 0, 40, 50, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 30, 50, 70, 80, 95, 100 }
		, 7600);

		addTrainable("sword and shield techniques",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 14000);

		addTrainable("combat discipline",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 75, 75, 75, 90, 95, 95, 100, 100, 100, 100}
		, 14000);

		addTrainable("righteous fervor",
		new byte[] { 0, 30, 40, 50, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 55, 60, 65, 70, 85, 90, 90, 90, 95, 100}
		, 15000);
		
		addTrainable("consecrate location",
		new byte[] { 0, 0, 0, 10, 15, 20, 25, 30, 35, 40, 45},
		new byte[] { 0, 0, 10, 25, 35, 55, 55, 70, 80, 85, 100}
		, 10000);
		
		addTrainable("prepare shemanna",
		new byte[] { 0, 0, 0, 0, 10, 15, 20, 25, 30, 35, 40},
		new byte[] { 0, 0, 0, 50, 60, 60, 65, 75, 90, 100, 100}
        , 14000);
		
		addTrainable("arcane knowledge",
		new byte[] { 0, 0, 0, 0, 0, 5, 10, 20, 30, 40, 50},
		new byte[] { 0, 0, 0, 0, 20, 40, 55, 65, 70, 70, 80}
		, 20000);
			
		addTrainable("purify weapon",
		new byte[] { 0, 0, 0, 0, 0, 0, 10, 20, 30, 40, 50},
		new byte[] { 0, 0, 0, 0, 0, 20, 45, 55, 70, 85, 100}
       , 14000);
		
		addTrainable("sigil of holy light",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 10, 20, 40, 50},
		new byte[] { 0, 0, 0, 0, 0, 0, 35, 55, 70, 75, 80}
        , 16000);
		
		addTrainable("sigil of purity",
		new byte[] { 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
        , 16000);
		
		addTrainable("purgatory",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20},
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 30, 50, 75}
        , 16000);
		
		addTrainable("sacred covenant",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50}
        , 30000);
		
		
	}

}
