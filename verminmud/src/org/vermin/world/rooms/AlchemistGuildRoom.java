/* AlchemistGuildRoom.java
	2.4.2005	JT & MV
	
*/

package org.vermin.world.rooms;

import org.vermin.world.items.GolemWorkbench;

public class AlchemistGuildRoom extends GuildRoom {

	private static final byte[] physicalStrengthBonus = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0 };

	private static final byte[] physicalDexterityBonus = { 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0};

	private static final byte[] physicalConstitutionBonus = { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2};

	private static final byte[] mentalConstitutionBonus = { 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 2, 0, 0, 2, 0, 0, 1, 0, 2};

	private static final byte[] mentalStrengthBonus = { 0, 0, 2, 0, 2, 0, 1, 0, 2, 0, 3, 0, 2, 0, 2, 0, 3, 0, 3, 1, 4};

	private static final byte[] mentalDexterityBonus = { 0, 1, 0, 2, 0, 2, 0, 3, 0, 2, 0, 2, 0, 1, 0, 2, 0, 3, 0, 3, 4};

	private static final String[] guildTitles = { null, "Researcher", null, "Student", null, "Adept", null, "Dabbler", null, "Apotechary", null, "Reader of Arcana", null, null, "Aether Traveller", null, null, "Lion of Knowledge", null, null, "Dragon of Alchemy"};


	public byte getPhysicalConstitutionBonus(int level) { return physicalConstitutionBonus[level]; }
	public byte getPhysicalStrengthBonus(int level) { return physicalStrengthBonus[level]; }
	public byte getMentalConstitutionBonus(int level) { return mentalConstitutionBonus[level]; }
	public byte getMentalStrengthBonus(int level) { return mentalStrengthBonus[level]; }
	public byte getPhysicalDexterityBonus(int level) { return physicalDexterityBonus[level]; }
	public byte getMentalDexterityBonus(int level) { return mentalDexterityBonus[level]; }
	public String getGuildTitle(int level) { return guildTitles[level]; }


	public AlchemistGuildRoom()
	{
		super("alchemist");

		setDescription("The Arcane Laboratory");
		setLongDescription("This old hall is permeated with strange odours and a tang of electricity in the " +
							"air. The space is illuminated by flickering candles that create dancing shadows to " +
							"the corners, adding to the eerie atmosphere. There seems to be a skull of some " +
							"unidentifiable creature on the nearby wooden table with a dripping candle on top. " +
							"There is some strange equipment on the tables, different-shaped glass bottles " +
							"containing liquids in variety of colours and vapours and distillations bubbling " +
							"through tubes and hoses. Thick metal chains hang from the darkness above, and the " +
							"walls are lined with bookshelves filled with rows of dusty tomes.\n" +
							"Commands: advance, join, train, list");
		this.guildLevels = 20;
		
		this.add(new GolemWorkbench());
		addTrainable("ancient languages", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 20, 30, 35, 0, 40, 50, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 30, 40, 50, 60, 70, 80, 80, 80 }
		, 14000);
		
		addTrainable("ancient rites", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 30, 35, 0, 40, 50, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 30, 40, 50, 60, 70, 80, 80, 80 }
		, 14000);
		
		addTrainable("fire powder", 
		new byte[] { 0, 0, 15, 20, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 30, 50, 60, 70, 85, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 2500);
		
		addTrainable("polearms", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 30, 50, 60, 70, 85, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 2000);

		addTrainable("consider", 
		new byte[] { 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 40, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100} 		
		, 1240);

		addTrainable("create torch", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 30, 35, 40, 40, 40, 40, 45, 45, 70, 70, 70, 70, 85, 85, 85, 100, 100, 100, 100, 100}
		, 1100);
		
		addTrainable("create food", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 30, 35, 40, 40, 40, 40, 45, 45, 70, 70, 70, 70, 85, 85, 85, 100, 100, 100, 100, 100}
		, 1100);
		
		addTrainable("create money", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 30, 35, 40, 40, 40, 40, 45, 45, 70, 70, 70, 70, 85, 85, 85, 100, 100, 100, 100, 100}
		, 1100);
		
		addTrainable("darkness", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 30, 50, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}
		, 2000);
		
		addTrainable("essence eye", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 30, 40, 50, 65, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 3000);
	
		//implemented
		addTrainable("tinning", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 10, 20, 30, 30, 70, 70, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 1050);
		
		addTrainable("light", 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		new byte[] { 0, 0, 20, 40, 50, 70, 80, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100} 		
		, 1240);

		addTrainable("gather reagents",
		new byte[] { 0, 0, 0, 0, 0, 15, 20, 30, 0, 0, 40, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 20, 25, 35, 45, 60, 75, 80, 95, 100, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 4500);

		addTrainable("herbal knowledge",
		new byte[] { 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 20, 30, 40, 45, 55, 60, 65, 70, 85, 90, 95, 100, 100, 100, 100, 100, 100 }
		, 7000);

		addTrainable("prepare potion",
		new byte[] { 0, 0, 0, 0, 0, 15, 20, 0, 30, 40, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 20, 30, 40, 50, 60, 70, 80, 90, 95, 100, 100, 100, 100, 100, 100, 100, 100 }
		, 6500);

		addTrainable("create clockwork sentinel",
		new byte[] { 0, 0, 0, 0, 0, 0, 15, 20, 25, 30, 35, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 90, 100, 100 }
		, 7500);

		addTrainable("hover",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 20, 30, 40, 50, 60, 70, 80, 90, 95, 100, 100, 100, 100, 100, 100 }
		, 4000);

		addTrainable("quick chant",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 20, 25, 30, 35, 40, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 20, 30, 40, 50, 60, 70, 80, 90, 95, 100, 100, 100, 100 }
		, 7000);

		addTrainable("carpentry",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 45, 55, 65, 75 }
		, 10000);

		addTrainable("mineralogy",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 40, 45, 65, 75 }
		, 10000);

		addTrainable("gemcutting",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 65 }
		, 12000);

		addTrainable("metallurgy",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 55, 65 }
		, 12000);

		addTrainable("golem creation",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 30}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 55, 70 }
		, 12000);

		addTrainable("prepare script",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 30, 45 }
		, 14000);
		
		addTrainable("material processing",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 30}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 15, 20, 40, 55, 70, 80 }
		, 8000);		

		addTrainable("golem repair",
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
		new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20 }
		, 16000);

	}


}
