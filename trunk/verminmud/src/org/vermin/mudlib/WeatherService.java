/*
 * Created on Jun 19, 2004
 */
package org.vermin.mudlib;

import java.awt.Point;

import org.vermin.mudlib.outworld.OutworldRoom;
import org.vermin.world.calendar.*;

/**
 * @author Jaakko Pohjamo
 */
public class WeatherService implements Weather {

	protected static WeatherService instance = new WeatherService();
	
	//FIXME: should get these from outworld loader
	private static final int MAP_LIMIT_X = 900;
	private static final int MAP_LIMIT_Y = 500;
	private static final int MAP_LIMIT_DISTANCE = (int) Math.sqrt(Math.pow(MAP_LIMIT_X/2,2)+Math.pow(MAP_LIMIT_Y,2));
	
	protected WeatherService() {
	}
	
	public static WeatherService getInstance() {
		return instance;
	}
	
	
	/**
	 * Returns true if r is instance of OutworldRoom.
	 * 
	 * @param r  the room to check for compatibility with WeatherService
	 * @return  true if r is instance of OutworldRoom
	 */
	public boolean canProvideFor(Room r) {
		return r instanceof OutworldRoom;
	}
	
	/**
	 * Returns the current absolute humidity of the
	 * weather on a scale from 0 to 100. When humidity
	 * reaches 50, it starts to rain. A weather offset
	 * in game time minutes may be specified.
	 * 
	 * @param offset  the desired weather offset in game time
	 * 					minutes
	 * @return  the humidity of the weather, on a scale from
	 * 		 	0 to 100, where 0 is dry and 100 is a deluge
	 */
	public int getHumidity(Room r) {
		
		if(!(r instanceof OutworldRoom)) {
			World.log("WeatherService: attempt to find weather for non OutworldRoom");
			return 50;
		}
		
		OutworldRoom owr = (OutworldRoom) r;
		
		int absoluteY = owr.getCoordinates().y;
		
		World.log("WeatherService: getting Humidity for room at x,"+absoluteY);			
		return calculateHumidity(absoluteY, 0);
	}
	
	private int calculateHumidity(int absoluteY, int minOffset) {
		Point sun = calculateSunPosition(minOffset);	
		int humidity;
		int sunY = (int) sun.getY();
		int deltaY = Math.abs(sunY - absoluteY);

		if(deltaY > MAP_LIMIT_Y/2) {
			deltaY = MAP_LIMIT_Y/2 - (deltaY-MAP_LIMIT_Y/2);
		}		
		
		humidity = 25*deltaY/MAP_LIMIT_Y;
		humidity = humidity + calculateButterflyEffect(VerminCalendar.MIN_PER_YEAR/4+minOffset)/2;
		
		return humidity;		
	}
	
	/**
	 * Returns the current absolute temperature of the
	 * weather on a scale from 0 to 100.  A weather offset
	 * in game time minutes may be specified.
	 * 
	 * @param offset  the desired weather offset, on a scale
	 * 					from 0 to 100
	 * @return  the warmth of the weather, on a scale from
	 * 		 	0 to 100, where 0 is freezing cold and 100
	 * 			is searing heat
	 */
	public int getTemperature(Room r) {
		
		if(!(r instanceof OutworldRoom)) {
			World.log("WeatherService: attempt to find weather for non OutworldRoom");
			return 50;
		}

		OutworldRoom owr = (OutworldRoom) r;
		
		return calculateTemperature(owr.getCoordinates().x, owr.getCoordinates().y, 0);
	}
	
	private int calculateTemperature(int absoluteX, int absoluteY, int minOffset) {
		World.log("WeatherService: getting Temperature for room at "+absoluteX+","+absoluteY);			
		
		int distance = calculateDistance(absoluteX, absoluteY);
		
		int temperature = 80*distance/MAP_LIMIT_DISTANCE;
		
		temperature += calculateButterflyEffect(VerminCalendar.MIN_PER_YEAR/3)/5;
		
		return temperature;
	}
	
	/**
	 * Returns the current absolute wind force on a scale
	 * from 0 to 100.  A weather offset in game time minutes 
	 * may be specified.
	 * 
	 * @return 
	 */
	public int getWind(Room r) {
		if(!(r instanceof OutworldRoom)) {
			World.log("WeatherService: attempt to find weather for non OutworldRoom");
			return 50;
		}
		
		OutworldRoom owr = (OutworldRoom) r;		
		
		int absoluteY = owr.getCoordinates().y;
		int absoluteX = owr.getCoordinates().x;		
		
		World.log("WeatherService: getting Wind for room at "+absoluteX+","+absoluteY);		
		return calculateWind(owr.getCoordinates().x, owr.getCoordinates().y, 0);
	}
	
	private int calculateWind(int absoluteX, int absoluteY, int minOffset) {
		int distance = calculateDistance(absoluteX, absoluteY);
		
		if(distance > MAP_LIMIT_DISTANCE/2) {
			distance = MAP_LIMIT_DISTANCE-distance;
		}
		
		int wind = 80*distance/MAP_LIMIT_DISTANCE;
		
		wind = wind + (int) (calculateButterflyEffect(VerminCalendar.MIN_PER_YEAR/2)*0.6);
		
		return wind;
	}
	
	public int getHumidityChange(Room r) {
		OutworldRoom owr = (OutworldRoom) r;
		return calculateHumidity(owr.getCoordinates().y, -5) - calculateHumidity(owr.getCoordinates().y, 0);
	}
	
	public int getTemperatureChange(Room r) {
		OutworldRoom owr = (OutworldRoom) r;
		return calculateTemperature(owr.getCoordinates().x, owr.getCoordinates().y, -5) - calculateTemperature(owr.getCoordinates().x, owr.getCoordinates().y, 0);
	}
	
	public int getWindChange(Room r) {
		OutworldRoom owr = (OutworldRoom) r;
		return calculateWind(owr.getCoordinates().x, owr.getCoordinates().y, -5) - calculateWind(owr.getCoordinates().x, owr.getCoordinates().y, 0);
	}
	
	public boolean isSunAboveHorizon(Room r) {
		OutworldRoom owr = (OutworldRoom) r;
		int hoplaa = calculateDistance(owr.getCoordinates().x, owr.getCoordinates().y);
		return hoplaa < MAP_LIMIT_DISTANCE/4;
	}
	
	//approximates map position distance from sun
	//on the sufrace of a donut shaped world
	private int calculateDistance(int mapX, int mapY) {
		Point sun = calculateSunPosition(0);		
		// super wrap!
		int sunX = (int) sun.getX();
		int sunY = (int) sun.getY();
		int deltaX = Math.abs(sunX - mapX);
		int deltaY = Math.abs(sunY - mapY);
		int distance;
		
		if(deltaX > MAP_LIMIT_X/2) {
			deltaX = MAP_LIMIT_X/2 - (deltaX-MAP_LIMIT_X/2);
		}
	
		if(deltaY > MAP_LIMIT_Y/2) {
			deltaY = MAP_LIMIT_Y/2 - (deltaY-MAP_LIMIT_Y/2);
		}
		
		//super stretch!
		deltaX = (Math.abs(sunY*2-MAP_LIMIT_Y)*deltaX)/MAP_LIMIT_Y;
		
		distance = (int) Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
		
		World.log("WeatherService: calculateDistance == "+distance);
		
		return distance;
	}

	
	/**
	 * Provides an useful value which fluctuates between 0 and 100
	 * over time in a manner designed to imitate random changes in
	 * weather (think multiple sinewaves combined). Use this value
	 * to modify base values calculated by your implementation to
	 * produce more 'random' weather effects.
	 * 
	 * @param offset  an offset in game minutes for the time to provide
	 *                butterfly effect for
	 * @return a semi-random integer value useful in providing more
	 *         realistic weather data
	 */
	protected int calculateButterflyEffect(int minOffset) {
		minOffset += VerminCalendar.getInstance().getMinuteOfYear();
		minOffset = minOffset % VerminCalendar.MIN_PER_YEAR;
		
		double offset = Math.toRadians(360f * (float) minOffset / (float) VerminCalendar.MIN_PER_YEAR);
		int effect = (int) ((Math.sin(offset*Math.PI*138)+1)*33.3/2
				  + (Math.sin(offset*Math.PI*300)+1)*33.3/2
				  + (Math.sin(offset*Math.PI*600)+1)*33.3/2);
		
		World.log("WeatherService: Butterfly effect == "+effect);
		
		return effect;
	}
	
	private Point calculateSunPosition(int offset) {
		offset = offset / VerminCalendar.MIN_PER_DAY;
		
		int dayOfYear = VerminCalendar.getInstance().getDayOfYear()+(offset%VerminCalendar.DAY_PER_YEAR);
		int minuteOfDay = VerminCalendar.getInstance().getMin();
		int sunX, sunY;
		
		sunX = VerminCalendar.DAY_PER_YEAR*dayOfYear/MAP_LIMIT_X;
		sunY = VerminCalendar.MIN_PER_DAY*minuteOfDay/MAP_LIMIT_Y;
		
		World.log("WeatherService: position of sun == "+sunX+","+sunY);
		
		return new Point(sunX, sunY);
	}
	
	public static void main(String[] args) {
		WeatherService erno = new WeatherService();
		try {
			while(true) {
				System.out.println(MAP_LIMIT_DISTANCE/4);
				System.out.println(erno.calculateDistance(700, 300));
				System.out.println(VerminCalendar.getInstance().format("Current date is: %d. %B, year %Y, %T"));
				Thread.sleep(10000);
			}
		} catch(InterruptedException ie) {
			
		}
	}
	
}
