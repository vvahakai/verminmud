package org.vermin.world.calendar;

import java.util.TreeMap;
import java.util.Hashtable;
import java.lang.reflect.*;

import java.util.GregorianCalendar;

public class VerminCalendar implements MudCalendar {
   
   private long timeStamp;
   private VerminCalendar.MethodMap methodMap;
   private StringStatics stringStatics;
   private static final long dayZero;
   
   static {
      dayZero = new GregorianCalendar(2002, 1, 5).getTimeInMillis();
   }
	
   public static final long MS_PER_MIN = 10000L;
   public static final long MS_PER_HOUR = MS_PER_MIN*60;
   public static final long MS_PER_DAY = MS_PER_HOUR*24;
   public static final long MS_PER_WEEK = MS_PER_DAY*7;
   public static final long MS_PER_MONTH = MS_PER_WEEK*4;
   public static final long MS_PER_YEAR = MS_PER_MONTH*13+MS_PER_DAY;
   
   public static final int MIN_PER_DAY = (int) (MS_PER_DAY/MS_PER_MIN);
   public static final int DAY_PER_YEAR = (int) (MS_PER_YEAR/MS_PER_DAY);   
   public static final int MIN_PER_YEAR = MIN_PER_DAY*DAY_PER_YEAR;
   
   private static final class MethodMap extends TreeMap {
      
      private static Hashtable instances = new Hashtable();
      private VerminCalendar calendar;
            
      private MethodMap(VerminCalendar calendar) {
         this.calendar = calendar;
         Class base = VerminCalendar.class;
         Method[] methods = base.getMethods();
         for(int i=0; i<methods.length; i++) {
				//System.out.println("Found method: "+methods[i].getName());
            if(methods[i].getName().length() == 1) {
					//System.out.println("Mapped method: "+methods[i].getName());
               this.put(new Character(methods[i].getName().charAt(0)), methods[i]);
            }
         }
      }
      
      public static VerminCalendar.MethodMap getInstance(VerminCalendar calendar) {
         if(instances.get(calendar) == null) {
            VerminCalendar.MethodMap newMap = new VerminCalendar.MethodMap(calendar);
            instances.put(calendar, newMap);
            return newMap;
         } else {
            return (VerminCalendar.MethodMap) instances.get(calendar);
         }
      }
      
      public String invoke(char what) {
         try {
            return (String) ((Method) this.get(new Character(what))).invoke(calendar);
         } catch (IllegalAccessException iae) {
            System.err.println("[org.vermin.world.VerminCalendar] Method.invoke() threw the following... the bastard.");
            iae.printStackTrace();
         } catch (InvocationTargetException iae) {
            System.err.println("[org.vermin.world.VerminCalendar] Method.invoke() threw the following... the bastard.");
            iae.printStackTrace();
         }
         
         return null;
      }
   }
   
   private static final class StringStatics {
      
      private static StringStatics instance = new StringStatics();
      
      public static StringStatics getInstance() {
         return instance;
      }
      
      public static final String[] monthNames = new String[] {
         null,
         "Month of the Slaughtered Orc".intern(),
         "Month of the Critical Hit".intern(),
         "Month of the Longsword".intern(),
         "Month of the Roosting Dragon".intern(),
         "Month of the Overzealous Paladin".intern(),
         "Month of the Wizard's Staff".intern(),
         "Month of the Scantly Clad Berserker".intern(),
         "Month of the Coffee Elemental".intern(),
         "Month of the Crosseyed Basilisk".intern(),
         "Month of the Wicked Witch".intern(),
         "Month of the Bald Werewolf".intern(),
         "Month of the Madly Rotating Buccaneer".intern(),
         "Month of the Sneaky Rat".intern(),
         "Month of the Hangover Day".intern()
      };
      
      public static final String[] monthAbbreviations = new String[] {
         null,
         "Orc".intern(),
         "Crit".intern(),
         "Sword".intern(),
         "Dragon".intern(),
         "Paladin".intern(),
         "Staff".intern(),
         "Berserker".intern(),
         "Elemental".intern(),
         "Basilisk".intern(),
         "Witch".intern(),
         "Werewolf".intern(),
         "Buccaneer".intern(),
         "Rat".intern(),
         "Hangover".intern()
      };
      
      public static final String[] dayNames = new String[] {
         null,
         "Alpha".intern(),
         "Beta".intern(),
         "Delta".intern(),
         "Epsilon".intern(),
         "Gamma".intern(),
         "Iota".intern(),
         "Omega".intern()
      };
      
      public static final String[] dayAbbreviations = new String[] {
         null,
         "Alp".intern(),
         "Bet".intern(),
         "Del".intern(),
         "Eps".intern(),
         "Gam".intern(),
         "Iot".intern(),
         "Ome".intern()
      };
   }
         

   protected VerminCalendar(VerminCalendar extender, long timeStamp) {
      if(extender == null) {
         extender = this;
      }
      this.timeStamp = timeStamp;
      this.methodMap = VerminCalendar.MethodMap.getInstance(extender);
      this.stringStatics = VerminCalendar.StringStatics.getInstance();
   }
   
   public static VerminCalendar getInstance() {
      return new VerminCalendar(null, System.currentTimeMillis());
   }
   
   public static VerminCalendar getInstance(long timeStamp) {
      VerminCalendar calendar = new VerminCalendar(null, timeStamp);
      calendar.setTimeStamp(timeStamp);
      return calendar;
   } 
   
   public void setTimeStamp(long timeStamp) {
      this.timeStamp = timeStamp;
   }
   
   public long getTimeStamp() {
      return timeStamp;
   }
   
   public String format(String formatString) {
      if(formatString.endsWith("%")) {
         return "[org.vermin.world.VerminCalendar] Warning: cannot parse format string ending with '%'.";
      }
      
      StringBuffer dateString = new StringBuffer();
      
      for(int i=0; i<formatString.length(); i++) {
         if(formatString.charAt(i) != '%') {
            dateString.append(formatString.charAt(i));
         } else {
            i++;
            if(formatString.charAt(i) != '%') {
               dateString.append(methodMap.invoke(formatString.charAt(i)));
            } else {
               dateString.append("%");
            }
         }
      }
      
      return dateString.toString();
   }
   
   public int getYear() {
      return (int) ((timeStamp-dayZero) / MS_PER_YEAR);
   }
   
   public int getMonth() {
      return (int) (((timeStamp-dayZero) % MS_PER_YEAR) / MS_PER_MONTH)+1;
   }
   
   public int getDayOfMonth() {
      long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;
      long monthSurplus = yearSurplus % MS_PER_MONTH;
      return (int) (monthSurplus / MS_PER_DAY)+1;
   }
   
   public int getDayOfYear() {
      return (int) (((timeStamp-dayZero) % MS_PER_YEAR) / MS_PER_DAY)+1;
   }
   
   public int getDayOfWeek() {
      long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;
      long monthSurplus = yearSurplus % MS_PER_MONTH;
      long weekSurplus = monthSurplus % MS_PER_WEEK;
      
      return (int) (weekSurplus / MS_PER_DAY)+1;
   }
   
   public int getWeekOfYear() {
      long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;

      return (int) (yearSurplus / MS_PER_WEEK)+1;
   }
   
   public int getHour() {
      long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;
      long monthSurplus = yearSurplus % MS_PER_MONTH;
      long daySurplus = monthSurplus % MS_PER_DAY;
      
      return (int) (daySurplus / MS_PER_HOUR);
   }
   
   public int getMin() {
      long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;
      long monthSurplus = yearSurplus % MS_PER_MONTH;
      long daySurplus = monthSurplus % MS_PER_DAY;
      long hourSurplus = daySurplus % MS_PER_HOUR;

      return (int) (hourSurplus / MS_PER_MIN);
   }
   
   public int getMinuteOfYear() {
   	long yearSurplus = (timeStamp-dayZero) % MS_PER_YEAR;
   	
   	return (int) (yearSurplus / MS_PER_MIN);
   	
   }
   
   // abbreviated weekday name (Sun..Sat)
   public String a() {
      return StringStatics.dayAbbreviations[getDayOfWeek()];
   }
   
   // full weekday name, variable (Sunday..Saturday)
   public String A() {
      return StringStatics.dayNames[getDayOfWeek()];
   }
   
   // abbreviated month name (Jan..Dec)
   public String b() {
      return StringStatics.monthAbbreviations[getMonth()];
   }
   
   // full month name (January..December)
   public String B() {
      return StringStatics.monthNames[getMonth()];
   }
   
   // date and time (ddd mmm dd hh:mm yyyy)
   public String c() {
      return null;
   }
   
   // day of month (01..28)
   public String d() {
      String monthStr = String.valueOf(getDayOfMonth());
      if(monthStr.length() == 1) {
         return "0"+monthStr;
      } else {
         return monthStr;
      }
   }
   
   // date (dd.mm.yyyy)
   public String D() {
      return d()+"."+m()+"."+Y();
   }
   
   // day of month, blank padded ( 1..28)
   public String e() {
      String monthStr = String.valueOf(getDayOfMonth());
      if(monthStr.length() == 1) {
         return " "+monthStr;
      } else {
         return monthStr;
      }
   }
   
   // same as %b
   public String h() {
      return b();
   }
   
   // hour (00..23)
   public String H() {
      String monthStr = String.valueOf(getHour());
      if(monthStr.length() == 1) {
         return "0"+monthStr;
      } else {
         return monthStr;
      }   
   }
   
   
   // day of year (000..365)
   public String j() {
      String year = String.valueOf(getDayOfYear());
      if(year.length() == 3) {
         return year;
      } else if(year.length() == 2) {
         return "0"+year;
      } else {
         return "00"+year;
      }
   }
   
   // hour ( 0..23)
   public String k() {
      String hourStr = String.valueOf(getHour());
      if(hourStr.length() == 1) {
         return " "+hourStr;
      } else {
         return hourStr;
      }   
   }
   
   // month (01..12)
   public String m() {
      String monthStr = String.valueOf(getMonth());
      if(monthStr.length() == 1) {
         return "0"+monthStr;
      } else {
         return monthStr;
      }
   }
   
   // minute (00..59)
   public String M() {
      String minStr = String.valueOf(getMin());
      if(minStr.length() == 1) {
         return "0"+minStr;
      } else {
         return minStr;
      }
   }
   
   // a newline
   public String n() {
      return "\n";
   }

   
   // time, 24 hour (hh:mm)
   public String T() {
      return H()+":"+M();
   }
   
   // week number of year (1..53)
   public String V() {
      return String.valueOf(getWeekOfYear());
   }
   
   // day of week (1..7)
   public String w() {
      return String.valueOf(getDayOfWeek());
   }
   
   // year (0..)
   public String Y() {
      return String.valueOf(getYear());
   }
   
   public static void main(String args[]) {
      VerminCalendar cal = VerminCalendar.getInstance();
      
      //for(int i=0; i<100; i++) {
         cal.setTimeStamp(System.currentTimeMillis());
         System.out.println(cal.format("Current time is: %d. %B, year %Y, %T"));
      //}
   }
   
}
