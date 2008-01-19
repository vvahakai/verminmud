/**
 * Reincarnation.java
 * 25.10.2003 Tatu Tarvainen
 *
 * Room that handles reincarnation (and part of player creation).
 */
package org.vermin.mudlib.rooms;

import java.util.Vector;

import org.vermin.mudlib.Commander;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.RegexRoom;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.World;
import org.vermin.world.commands.HelpCommand;

public class Reincarnation extends RegexRoom {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"race (.+) => setRaceName(1)",
			"race => listRaces()",
			"accept => accept()",
			"look => look()",
			"help race (.+) => helpRace(1)"
		};
	}

	private Player actor;

	private Race race;
	
	public Reincarnation() {}
	
	public Reincarnation(Player who) {
		actor = who;
		look();
	}

	public void look() {
		actor.notice("You are floating in a realm of possibilities.\n"+
						 "You must select your physical form for your next life.\n"+
						 "Use 'race <racename>' to set your race.\n"+
						 "Use 'help race <racename>' to get a description of a race.\n"+
						 "Use 'accept' when you are satisfied with your selections.");
		listRaces();
	}

	public void listRaces() {
		
		StringBuffer sb = new StringBuffer("Available races: ");
        boolean first = true;
        for(String name : World.getPlayableRaceNames()) {
            sb.append(first ? "" : ", ");
            first = false;
            sb.append(name);
		}
        sb.append(".");
		actor.notice(sb.toString());
	}

	public void setRaceName(String r) {
		String raceName = r.trim().toLowerCase();
		Race raceC = World.getPlayableRaceByName(raceName);

		if(raceC == null) {
			actor.notice("No such race.");
		} else {
		    race = raceC;
        	actor.notice("You have selected the "+race.getName()+" race.");
        }
	}

	public void helpRace(String race) {
		Vector v = new Vector();
		v.add("help");
		v.add(race);
		((HelpCommand) Commander.getInstance().get("help")).run(actor, v);
	}
	public void accept() {
		if(race == null)
			actor.notice("You must first choose your race.");
		else
			reincarnate();
	}

	private void reincarnate() {

		actor.clearAvailableTitles();
		actor.clearAvailableBattleStyles();
		
		actor.putItemsToInventory();
		actor.clearInventory();
		actor.setExperience(actor.getTotalExperience()+actor.getExperience());
		actor.setTotalExperience(0l);
		actor.setRace(race);
		actor.setLevel(0);
		actor.clearSkills();
		actor.setFreeStatPoints(0);
		actor.clearUsedStatPoints();


		Race r = actor.getRace();

		actor.setPhysicalStrength(Math.round((float) r.getMaxPhysicalStrength() / (float) 10.0));
		actor.setPhysicalDexterity(Math.round((float) r.getMaxPhysicalDexterity() / (float) 10.0));
		actor.setPhysicalConstitution(Math.round((float) r.getMaxPhysicalConstitution() / (float) 10.0));
		actor.setPhysicalCharisma(Math.round((float) r.getMaxPhysicalCharisma() / (float) 10.0));
		actor.setMentalStrength(Math.round((float) r.getMaxMentalStrength() / (float) 10.0));
		actor.setMentalDexterity(Math.round((float) r.getMaxMentalDexterity() / (float) 10.0));
		actor.setMentalConstitution(Math.round((float) r.getMaxMentalConstitution() / (float) 10.0));
		actor.setMentalCharisma(Math.round((float) r.getMaxMentalCharisma() / (float) 10.0));
		actor.updateStats();
		actor.setHp(actor.getMaxHp());
		actor.setSp(actor.getMaxSp());
		actor.save();

		Room room = (Room) World.get("city:0,0,0");
		room.add(actor);
		room.notice(actor, actor.getName()+" has been reincarnated.");
		actor.notice("You have been reincarnated.");
	}

	public boolean mayTeleport(Living who) {
		return false;
	}
}
