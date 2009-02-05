/* OutworldRoom.java
	20.3.2002	Tatu Tarvainen
	
	Outer world room implementation.
*/
package org.vermin.mudlib.outworld;

import java.awt.Point;

import org.vermin.mudlib.*;

import org.vermin.world.monsters.*;

public class OutworldRoom extends DefaultRoomImpl
{
	private int mapX, mapY;
	private OutworldLoader loader;
	
	protected TerrainType terrain;
	
	private String location;

	public OutworldRoom(int type, OutworldLoader loader, int x, int y) {
		switch(type) {
		  case 'f':  this.terrain = TerrainType.LIGHT_FOREST; break;
		  case 'r':  this.terrain = TerrainType.SMALL_RIVER; break;
		  case 'p':  this.terrain = TerrainType.GRASSLANDS; break;
		  case 'R':  this.terrain = TerrainType.DEEP_RIVER; break;
		  case 'F':  this.terrain = TerrainType.DENSE_FOREST; break;
		  case 'h':  this.terrain = TerrainType.HILLS; break;
		  case '~':  this.terrain = TerrainType.OCEAN; break;
		  case '|':  this.terrain = TerrainType.ROAD; break;
		  case '\\': this.terrain = TerrainType.ROAD; break;
		  case '-':  this.terrain = TerrainType.ROAD; break;
		  case '/':  this.terrain = TerrainType.ROAD; break;
		  case '=':  this.terrain = TerrainType.BRIDGE; break;
		  case 's':  this.terrain = TerrainType.SWAMP; break;
		  case 'b':  this.terrain = TerrainType.BADLANDS; break;
		  case 'd':  this.terrain = TerrainType.DESERT; break;
		  case 'j':  this.terrain = TerrainType.JUNGLE; break;
		  case '%':  this.terrain = TerrainType.RUINS; break;
		  case 't':  this.terrain = TerrainType.TUNDRA; break;
		  case 'V':  this.terrain = TerrainType.VOLCANIC; break;
		  case 'l':  this.terrain = TerrainType.LAKE; break;
		  case 'y':  this.terrain = TerrainType.FIELDS; break;
		  case 'z':  this.terrain = TerrainType.SHORE; break;
		  case '"':  this.terrain = TerrainType.SHALLOWS; break;
		  case 'i':  this.terrain = TerrainType.HIGHLANDS; break;
		  case 'w':  this.terrain = TerrainType.WATERFALLS; break;
		  case 'M':  this.terrain = TerrainType.THE_LONE_MOUNTAIN; break;
		  case '+':  this.terrain = TerrainType.CROSSING; break;
		  case '.':  this.terrain = TerrainType.AIR; break;
		  case ',':  this.terrain = TerrainType.HIGH_AIR; break;
		  case '\'': this.terrain = TerrainType.ABOVE_CLOUDS; break;
		  case 'm':  this.terrain = TerrainType.PASSABLE_MOUNTAIN; break;
		  case 'T':  this.terrain = TerrainType.TREETOPS; break;
		  case 'o':  this.terrain = TerrainType.HILLTOPS; break;
		  case '_':  this.terrain = TerrainType.WATER_BOTTOM; break;
		  case ';':  this.terrain = TerrainType.DEEP_WATER_BOTTOM; break;
		  case ':':  this.terrain = TerrainType.SEA_BOTTOM; break;
		  case '{':  this.terrain = TerrainType.UNDERWATER; break;
		  case '}':  this.terrain = TerrainType.DEEP_UNDERWATER; break;

		  default: 
			  this.terrain = TerrainType.LIGHT_FOREST; 
			  System.out.println("Kartta bugaa: "+type);
			  break;
		}
		this.loader = loader;
		this.mapX = x;
		this.mapY = y;
		this.propertyProvider.addProvider(new RoomWeatherPropertyProvider(this, WeatherService.getInstance()));
		setOutdoor(true);
		setIllumination(50);
	}
	
	public TerrainType getTerrainType() {
		return terrain;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String loc) {
		this.location = loc;
	}

	public Point getCoordinates() {
		return new Point(mapX, mapY);
	}
	
	public OutworldLoader getLoader() {
		return loader;
	}
	
	public int getWaterLevel() {
		return terrain.waterLevel;
		//PENDING: modifiers to these?
	}

	public boolean requiresAviation() {
		return terrain.requireAviation;
	}

	public String getDescription() {
		return terrain.desc;
	}
	
	public String getLongDescription() {
		return terrain.longdesc;
	}
	
	public void start() {
		super.start();
		spawn();
	}

	private void spawn() {
		if(Dice.random() < 30) {
			switch(terrain) {
			  case SWAMP: {
				  switch(Dice.random(3)) {
					 case 1: add(new Alligator()); break;
					 case 2: add(new Crocodile()); break;
					 case 3: add(new DungBeetle()); break;
				  }
				  break;
			  }
			  case LIGHT_FOREST: {
				  switch(Dice.random(12)) {
					 case 1: add(new Owl()); break;
					 case 2: add(new Duck()); break;
					 case 3: add(new Raven()); break;
					 case 4: add(new Bullfinch()); break;
					 case 5: add(new Cuckoo()); break;
					 case 6: add(new Cockerel()); break;
					 case 7: add(new Lark()); break;
					 case 8: add(new Fowl()); break;
					 case 9: add(new Crow()); break;
					 case 10: add(new Squirrel()); break;
					 case 11: add(new Hawk()); break;
					 case 12: add(new Ferret()); break;
					 default: break;
				  }
				  break;
			  }
			  case DENSE_FOREST: {
				  switch(Dice.random(13)) {
					 case 1: add(new Owl()); break;
					 case 2: add(new Cuckoo()); break;
					 case 3: add(new Lark()); break;
					 case 4: add(new Fowl()); break;
					 case 5: add(new Bear()); break;
					 case 6: add(new Hawk()); break;
					 case 7: add(new Deer()); break;
					 case 8: add(new Elk()); break;
					 case 9: add(new Moose()); break;
					 case 10: add(new Ostrich()); break;
					 case 11: //fallthru
					 case 12: //fallthru
					 case 13: add(new Squirrel()); break;
					 default: break;
				  }
				  break;
			  }
			  case JUNGLE: {
				  switch(Dice.random(2)) {
					 case 1: add(new Tiger()); break;
					 case 2: add(new Snake()); break;
					 default: break;
				  }
				  break;
			  }
			  case GRASSLANDS: {
				  switch(Dice.random(4)) {
					 case 1: add(new Snake()); break;
					 case 2: add(new Lion()); break;
					 case 3: add(new Crow()); break;
					 case 4: add(new Gnu()); break;
					 default: break;
				  }
				  break;
			  }
			  case HILLS: {
				  switch(Dice.random(4)) {
					 case 1: add(new Eagle()); break;
					 case 2: add(new Goat()); break;
					 case 3: add(new Llama()); break;
					 case 4: add(new Weasel()); break;
					 default: break;
				  }
				  break;
			  }
			  case BADLANDS: {
				  switch(Dice.random(2)) {
					 case 1: add(new Buffalo()); break;
					 case 2: add(new Vulture()); break;
					 default: break;
				  }
				  break;
			  }
			  case SMALL_RIVER: {
				  switch(Dice.random(2)) {
					 case 1: add(new Pike()); break;
					 case 2: add(new Duck()); break;
					 default: break;
				  }
				  break;
			  }
			  case DEEP_RIVER: {
				  switch(Dice.random(2)) {
					 case 1: add(new Salmon()); break;
					 case 2: add(new Eel()); break;
					 default: break;
				  }
				  break;
			  }
			  case DESERT: {
				  switch(Dice.random(2)) {
					 case 1: add(new Camel()); break;
						 //case 2: add(new Scopion()); break;
					 default: break;
				  }
				  break;
			  }
			  case VOLCANIC: add(new Raven()); break;
			  case OCEAN: {
				  switch(Dice.random(2)) {
					 case 1: add(new Shark()); break;
					 case 2: add(new Seahorse()); break;
					 default: break;
				  }
				  break;
			  }
			  case TUNDRA: {
				  switch(Dice.random(5)) {
					 case 1: add(new Wolf()); break;
					 case 2: add(new Fox()); break;
					 case 3: add(new Lynx()); break;
					 case 4: add(new Reindeer()); break;
					 case 5: add(new Wolverine()); break;
					 default: break;
				  }
				  break;
			  }
			  case RUINS: add(new Rat()); break;
				  
			}
		}
		if(Dice.random() < 30) {
			spawn();
		}
		/**
		 * SPAWNING RULES (from Siggy) (13.9.2003)
		 * swamp - crocodiles, alligators
		 * light forest - bullfinches, cockerels, ducks, fowls, hawks, larks, squirrels, ferret, owl
		 * dense forest - bears, cuckoo, hawks, fowls, larks, deers, elks, moose, ostrich, owl
		 * jungle - tigers, snakes
		 * grassland - snakes, lions, crows, gnu
		 * hills - eagles, goats, llama
		 * badlands - buffalos, vultures
		 * small river - pikes*, ducks
		 * deep river - salmons*, eels*
		 * desert - camels, scorpions*
		 * volcanic - ravens
		 * ocean - sharks*, sea-horse*
		 * tundra - wolves, foxes, lynx, reindeer, wolverine
		 * road - none
		 * bridge - none
		 * ruins - rats*}
		 */
	}
	
	public static class Cow extends DefaultMonster {
		public Cow() {
			setRace(org.vermin.world.races.QuadrupleRace.getInstance());
			setExperienceWorth(200);
			setAggressive(false);
			setName("cow");
			setDescription("A spotted cow");
			setLongDescription("It's a large cow. It looks delicious.");
			setPhysicalDexterity(20);
			setPhysicalConstitution(20);
			setMaxHp(20);
			setHp(20);
			setBattleStyle(new DefaultBattleStyle(this));
			setSkill("fighting", 20);
			getMoney().add(Money.Coin.BRONZE, Dice.random());
			start();
		}
	}

	public void explore(Living explorer) {}
	
	public int getIllumination() {
		if (provides(RoomProperty.DAY)) {
			illumination = 75;
		}
		else {
			illumination = 40;
		}
		return super.getIllumination();
	}

	@Override
	public void save() {}
	
	
	
}
