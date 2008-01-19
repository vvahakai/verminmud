/* Slot.java
 * 13.6.2002 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

public class Slot
{

	/* Constants for type */
	public static final String HEAD    = "head";
	public static final String NECK    = "neck";
	public static final String AMULET  = "amulet";
	public static final String TORSO   = "torso";
	public static final String ARM     = "arm";
	public static final String HAND    = "hand";
	public static final String LEG     = "leg";
	public static final String FOOT    = "foot";
	public static final String CLOAK   = "cloak";
	public static final String BELT    = "belt";
	public static final String TAIL    = "tail";
	public static final String FINGER  = "finger";
	public static final String WRIST   = "wrist";
	public static final String WING    = "wing";

   public String type; // eg. leg
   public String name; // eg. left leg

   public Slot(String type, String name) {
      this.type = type;
      this.name = name;
   }

	public Slot(String type) {
		this.type = type;
	}

	public Slot() {}

}
