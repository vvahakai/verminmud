/*
 * Created on 18.2.2005
 */
package org.vermin.mudlib;

import org.vermin.driver.AbstractPropertyProvider;
import static org.vermin.mudlib.RoomProperty.*;
/**
 * @author Jaakko Pohjamo
 */
public class RoomWeatherPropertyProvider extends AbstractPropertyProvider<RoomProperty> {
	
	private Room room;
	private Weather weather;
	
	public RoomWeatherPropertyProvider(Room room, Weather weather) {
		this.room = room;
		this.weather = weather;
	}
	
	public boolean provides(RoomProperty property) {
		if(property == RAINS && weather.getHumidity(room) > 49) {
			return true;
		}
		if(property == DAY && weather.isSunAboveHorizon(room)) {
			return true;
		}
		return false;
	}
}
