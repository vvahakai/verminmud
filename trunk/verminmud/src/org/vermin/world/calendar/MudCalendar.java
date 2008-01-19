package org.vermin.world.calendar;

/**
 * This interface defines an object capable
 * of mapping real-world timestamps to mud
 * dates. Instances of MudCalendar also offer
 * a convinient formatting method, similar to the
 * unix date command.
 *
 * NOTE: In addition to the format method,
 *       <code>MudCalendars</code> should override
 *       the toString method to return a standard
 *       string representation of the date.
 */

public interface MudCalendar {
      
   /**
    * Returns a string describing this date object.
    * The actual syntax of the format string is
    * dependant on the implementation, but should
    * be as much like the unix date command's format
    * syntax as possible.
    *
    * @param formatString  the string to format date with
    *
    * @return  a date string formatted by <code>formatString</code> 
    */
   public String format(String formatString);
   
   /**
    * Sets the real-world time this calendar represents.
    *
    * @param timeStamp  the new time
    */
   public void setTimeStamp(long timeStamp);
   
   /**
    * Returns the time stamp of the real-world time this
    * calendar represents.
    *
    * @return  the timestamp
    */
   public long getTimeStamp();
}
