package org.vermin.mudlib.battle;

import java.util.EnumSet;

import org.vermin.mudlib.Dice;

public abstract class AbstractGoreMessageProvider implements GoreMessageProvider {
   
   /**
    * Contains the flags this <code>GoreMessageProvider</code>
    * provides messages for.
    */
   protected final EnumSet<GoreProperty> properties;
   
   protected AbstractGoreMessageProvider(EnumSet<GoreProperty> properties) {
      this.properties = properties;
   }
   
   public boolean hasProperty(GoreProperty p) {
      return properties.contains(p);
   }
   
   public boolean hasProperties(EnumSet<GoreProperty> p) {
      return properties.containsAll(p);
   }
   
   public EnumSet<GoreProperty> getProperties() {
      return properties;
   }
   
   /**
    * A convinience method for applying a random element to the
    * hitlocation scored.
    * 
    * @param hitLocation  int value describing a hit location
    * @return  the same value, with a random +/- 25 element added,
    * 			the extreme modifiers being less common
    */
   protected int randomizeHitLocation(int hitLocation) {
   	int random = 0;

		hitLocation = Math.max(hitLocation, 25);
		hitLocation = Math.min(hitLocation, 75);
   	
   	random += (Dice.random()/2)-25;	
		hitLocation += random;
		
		hitLocation = Math.max(hitLocation, 0);
		hitLocation = Math.min(hitLocation, 100);
		
   	return hitLocation;
   }
}
