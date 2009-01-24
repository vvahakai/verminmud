/*
 * Created on 10.11.2006
 */
package org.vermin.world.behaviours;

import org.vermin.mudlib.Behaviour;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.WeatherService;
import org.vermin.mudlib.behaviour.BehaviourAdapter;
import org.vermin.mudlib.outworld.OutworldRoom;

public class WeatherObservationBehaviour extends BehaviourAdapter implements
		Behaviour {
	
	private String previousRoomId = "";
	private int msgCount = 0;
	
	public void onRegenTick(Living who) {
		if(who.getRoom() instanceof OutworldRoom &&
				who.getRoom().getId().equals(previousRoomId) &&
				!who.inBattle() &&
				msgCount < 2) {
			doMessage(who);
			return;
		}
		
		if(!who.getRoom().getId().equals(previousRoomId)) {
			msgCount = 0;
		}
		previousRoomId = who.getRoom().getId();
	}
	
	private void doMessage(Living who) {
		WeatherService weatherService = WeatherService.getInstance();
		String rainwerb = null;
		if(weatherService.getTemperature(who.getRoom()) < 35) {
			rainwerb = "snowing";
		} else if(weatherService.getTemperature(who.getRoom()) < 45) {
			rainwerb = "sleeting";
		} else {
			rainwerb = "raining";
		}
		String rainnoun = null;
		if(weatherService.getTemperature(who.getRoom()) < 35) {
			rainnoun = "snow";
		} else if(weatherService.getTemperature(who.getRoom()) < 45) {
			rainnoun = "sleet";
		} else {
			rainnoun = "water";
		}
		
		int humidity = weatherService.getHumidity(who.getRoom());
		
		if(humidity > 40 && Dice.random(3) == 1) {
			if(humidity > 90) {
				who.notice("Huge amounts of "+rainnoun+" is whipping around you.");
				msgCount++;
			} else if(humidity > 70) {
				who.notice("It's "+rainwerb+" heavily.");
				msgCount++;
			} else if(humidity > 50) {
				who.notice("Some "+rainnoun+" is dropping from the sky.");
				msgCount++;
			} else if(humidity > 40) {
				who.notice("Heavy clouds are rolling across the sky.");
				msgCount++;
			}
		}
	}
}
