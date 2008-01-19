/*
 * Created on 29.1.2005
 */
package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Print;


/**
 * @author Ville
 */
public class WeatherCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"weather => weather(actor)"
			};
	}

	public void weather(Player actor) {
		WeatherService ws = WeatherService.getInstance();
		StringBuilder sb = new StringBuilder(200);
		if(ws.getWind(actor.getRoom()) > 85) {
			sb.append("Storm winds howl around you, and ");
		} else if(ws.getWind(actor.getRoom()) > 60) {
			sb.append("Strong winds are blowing, and ");			
		} else if(ws.getWind(actor.getRoom()) > 40) {
			sb.append("There is a light wind, and ");			
		} else if(ws.getWind(actor.getRoom()) > 15) {
			sb.append("There's almost no wind, and ");			
		} else {
			sb.append("The air is very still, and ");
		}		

		if(ws.getTemperature(actor.getRoom()) > 85) {
			sb.append("a searing heat assails you.");
		} else if(ws.getTemperature(actor.getRoom()) > 60) {
			sb.append("it's very hot.");
		} else if(ws.getTemperature(actor.getRoom()) > 40) {
			sb.append("the temperature is mild.");
		} else if(ws.getTemperature(actor.getRoom()) > 15) {
			sb.append("the air is cool.");
		} else {
			sb.append("it's freezing cold.");
		}		
		
		String rainwerb = null;
		if(ws.getTemperature(actor.getRoom()) < 35) {
			rainwerb = "snowing";
		} else if(ws.getTemperature(actor.getRoom()) < 45) {
			rainwerb = "sleeting";
		} else {
			rainwerb = "raining";
		}
		String rainnoun = null;
		if(ws.getTemperature(actor.getRoom()) < 35) {
			rainnoun = "snow";
		} else if(ws.getTemperature(actor.getRoom()) < 45) {
			rainnoun = "sleet";
		} else {
			rainnoun = "water";
		}
		
		if(ws.getHumidity(actor.getRoom()) > 85) {
			sb.append("\n"+Print.capitalize(rainnoun)+" is falling from the sky in great quantities.");
		} else if(ws.getHumidity(actor.getRoom()) > 60) {
			sb.append("\nIt's "+rainwerb+".");
		} else if(ws.getHumidity(actor.getRoom()) > 40) {
			sb.append("\nHeavy clouds hang in the sky, it looks like it might start "+rainwerb+" at any time.");
		} else if(ws.getHumidity(actor.getRoom()) > 30) {
		} else {
			sb.append("\nThe air feels very dry.");
		}	
		actor.notice(sb.toString());
		if(actor.getRoom().provides(RoomProperty.DAY)) {
			actor.notice("The sun is above the horizon.");
		}
		
		return;
		
	}

}
