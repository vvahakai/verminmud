package org.vermin.world.rooms;

import org.vermin.mudlib.*;


public class CityRoom extends DefaultRoomImpl {

	protected byte type;

	public CityRoom() {
		this.type = '.';
		illumination = 50;
	}

	public CityRoom(byte[] type) {
		this.type = type[0];
		illumination = 50;
	}

	// total of monster spawned
	private static int monstersSpawned = 0;

	// how many monsters are currently alive in the city (controls spawning)
	private static int monstersAlive = 0;

	// maximum number of monsters wanted
	private static int maxMonstersAlive = 100;

	public void start() {
		super.start();
		spawn();
	}

	public boolean action(MObject actor, String cmd) {
		if(type == 'C' && cmd.equalsIgnoreCase("reinc")) {
			Player p = (Player) actor;
			p.getRoom().remove(p);
			Room r = new org.vermin.mudlib.rooms.Reincarnation(p);
			//p.setRoom(r);
			p.setParent(r);
			return true;
		}
		return false;
	}

	public void spawn() {

		if(monstersAlive == maxMonstersAlive)
			return;

		boolean maybe = Dice.random() < 40; // maxMonstersAlive - monstersAlive;

		if(maybe) {

			// spawn a monster

			//int type = Dice.random(5);
			
			Living monster = null;

			// do a switch/case to select proper monster type
			monster = new Pigeon(); 

			add(monster);
			monstersAlive++;
		}
	}

	public static class Pigeon extends DefaultMonster {

		public Pigeon() {
			super();
			setRace(org.vermin.world.races.BirdRace.getInstance());
			setExperienceWorth(100);
			setAggressive(Dice.random(1) == 0);
			setName("pigeon");
			setDescription("A gray pigeon");
			setLongDescription("It's a perfectly normal looking pigeon. It looks delicious.");
			setPhysicalStrength(3);
			setPhysicalConstitution(3);
			setPhysicalDexterity(3);
			setMaxHp(10);
			setHp(10);
			setBattleStyle(new DefaultBattleStyle(this));
			setSkill("fighting", 10);
			start();
		}

		public void dumpCorpse() {
			monstersAlive = monstersAlive-1;
			super.dumpCorpse();
		}
	}

	public String getDescription() {
		switch(type) {
		  case '.': return "Vermin city street.";
		  case '%': return "Vermin city gates.";
		  case 'P': return "Vermin city park.";
		  case 'C': return "Central square of Vermin city.";
		  case '+': return "Peaceful Grove Graveyards.";
		}
		return "";
	}
	public String getLongDescription() {
		switch(type) {
		  case '.': return "You are on the streets of Vermin city.";
		  case '%': return "You are standing between the colossal gates of Vermin city.";
		  case 'P': return "You are in a city park. It is very beautiful here.";
		  case 'C': return "This the central square of Vermincity. You can see a reincing machine next to a fountain.\n"+
				"You may reincarnate yourself by typing 'reinc'.";
		  case '+': return "This is the Peaceful Grove Graveyards. People are buried here.";
		}
		return "";
	}
	
	public String getExtendedDescription(String what) {
		if(type == 'C' && what.equalsIgnoreCase("fountain")) {
			return "This is the famous central fountain of Vermincity. Too bad it's non-functional at the moment.";
		}
		return null;
	}
	public void explore(Living explorer) {}
}

