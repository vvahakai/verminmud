/*
 * Created on 30.1.2005
 */
package org.vermin.mudlib;

/**
 * Weather defines an object which knows about the 
 * weather conditions in a specified set of rooms.
 * 
 * @author Jaakko Pohjamo
 */
public interface Weather {
	
	/**
	 * Returns whether this implementation of Weather
	 * can provide accurate weather data for the
	 * specified Room. If any of the other methods are
	 * passed a Room for which this mehtod returns false,
	 * they may throw an IllegalArgumentException to
	 * indicate that data is impossible to provide, or
	 * provide inappropriate data.
	 * 
	 * @param r  the room to test for compliance with
	 *  		 this implementation of Weather
	 * @return  boolean value indicating whether this Weather
	 * 		    can provide accurate weather data for room
	 */
	public boolean canProvideFor(Room room);
	
	/**
	 * Returns the current absolute humidity of the
	 * weather on a scale from 0 to 100. When humidity
	 * reaches 50, it starts to rain.
	 * 
	 * @param r  the room to gain humidity information of
	 * @return  the humidity of the weather, on a scale from
	 * 		 	0 to 100, where 0 is desert-dry and 100 is a
	 * 			vertical river. rain starts at 50.
	 */
	public int getHumidity(Room room);
	
	/**
	 * Returns the current direction and speed of change
	 * of humidity conditions in the room, on a scale of -100
	 * to 100. Note that the extremes of this scale are only
	 * theoretical maximums, and are unlikely to be returned
	 * in practice. A negative sign indicates a decrease in
	 * humidity, and positive an increase.
	 * 
	 * @param r the room to gain humidity change information of
	 * @return  the direction and speed of humidity change, on a
	 * 			scale of -100 to 100
	 */
	public int getHumidityChange(Room r);
	
	/**
	 * Returns the current absolute temperature of the
	 * weather on a scale from 0 to 100, where 0 is
	 * freezing cold and 100 is scorching. Water is frozen
	 * in temperatures of 20 and below. 20-30 is considered
	 * something in between (ice and snow exist, but also
	 * liquid running water). Above 30 all water is liquid.
	 * 
	 * @param r  the room to gain temperature information of
	 * @return  the warmth of the weather, on a scale from
	 * 		 	0 to 100, where 0 is freezing cold and 100
	 * 			is searing heat
	 */

	public int getTemperature(Room r);
	/**
	 * Returns the current direction and speed of change
	 * of temperature conditions in the room, on a scale of -100
	 * to 100. Note that the extremes of this scale are only
	 * theoretical maximums, and are unlikely to be returned
	 * in practice. A negative sign indicates a decrease in
	 * temperature, and positive an increase.
	 * 
	 * @param r the room to gain temperature change information of
	 * @return  the direction and speed of temperature change, on a
	 * 			scale of -100 to 100
	 */
	public int getTemperatureChange(Room r);

	
	/**
	 * Returns the current absolute wind force on a scale
	 * from 0 to 100.
	 * 
	 * @param r  the room to gain temperature information of
	 * @return  the absolute force of wind, on a scale from
	 * 			0 to 100
	 */
	public int getWind(Room r);
	
	/**
	 * Returns the current direction and speed of change
	 * of wind conditions in the room, on a scale of -100
	 * to 100. Note that the extremes of this scale are only
	 * theoretical maximums, and are unlikely to be returned
	 * in practice. A negative sign indicates a decrease in
	 * wind, and positive an increase.
	 * 
	 * @param r  the room to gain wind change information of
	 * @return  the direction and speed of wind change, on a
	 * 			scale of -100 to 100
	 */
	public int getWindChange(Room r);
	
	/**
	 * Returns whether the sun is currently percieved to be 
	 * above the horizon from the passed room. Note that
	 * clouds, indoorness or other such things are not 
	 * considered here.
	 * 
	 * @param r  the room to gain information of
	 * @return  true if the sun is currently above horizon in r
	 */
	public boolean isSunAboveHorizon(Room r);
}
