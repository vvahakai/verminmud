/*
 *  ConcreteQuadrupleRace.java
 *
 * A wrapper class around QuadrupleRace.
 * Defines fine details, such as what natural
 * weapons this quadruple uses (hooves, paws and/or jaws)
 * and modifies hit locations as needed.
 */
package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.mudlib.Item;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.RaceAdapter;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.QuadrupleHoof;
import org.vermin.world.items.QuadrupleHorns;
import org.vermin.world.items.QuadruplePaw;

import static org.vermin.mudlib.battle.GoreProperty.HAS_BLOOD;
import static org.vermin.mudlib.battle.GoreProperty.HAS_BONES;
import static org.vermin.mudlib.battle.GoreProperty.HAS_INTERNAL_ORGANS;
import static org.vermin.mudlib.battle.GoreProperty.IS_ANIMAL;

public class ConcreteQuadrupleRace extends RaceAdapter {
 
   private Wieldable[] limbs;
   
   private boolean hasHorns;
   private boolean hasPaws;
   private boolean hasHooves;
   
   public ConcreteQuadrupleRace(Race delegate, Wieldable[] limbs) {
      super(delegate);
      
      this.limbs = limbs;
      for(int i=0; i<limbs.length; i++) {
         if(limbs[i] instanceof QuadrupleHoof) hasHooves = true;
         if(limbs[i] instanceof QuadruplePaw) hasPaws = true;
         if(limbs[i] instanceof QuadrupleHorns) hasHorns = true;
      }
   }

	public int getLifeAlignment() {
		return 0;
	}
	public int getProgressAlignment() {
		return 0;
	}

	public int getIntrinsicSkill(String name) {
		return 0;
	}

	public int getMaximumVisibleIllumination() {
		return 100;
	}

	public EnumSet<LivingProperty> getRaceProperties() {
		return EnumSet.noneOf(LivingProperty.class);
	}

	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(HAS_BLOOD, HAS_BONES, HAS_INTERNAL_ORGANS, IS_ANIMAL);
	}
	
	public int getMinimumVisibleIllumination() {
		return 35;
	}

   /**
    * Returns a string describing a hitlocation
    * in the specified percent.
    *
    * @param pos  a percentage describing the 'goodness' of the hitlocation
    * @return     the hitLocation value
    */
   public String getHitLocation(int pos) {
      if(hasHorns && pos > 1 && pos < 4)
         return "horns";
      
      if(pos == 96) {
         if(hasPaws)
            return "right rear paw";
         else
            return "right rear hoof";
      }
         
      if(pos == 98) {
         if(hasPaws)
            return "left rear paw";
         else
            return "left rear hoof";
      }
         
      if(pos == 92) {
         if(hasPaws)
            return "right front paw";
         else
            return "right front hoof";
      }
         
      if(pos == 90) {
         if(hasPaws)
            return "left front paw";
         else
            return "left front hoof";
      }
         
      return super.getHitLocation(pos);
   }


   /**
    * Returns a string describing the name of a limb in
    * the specified index. (example: "hoof")
    *
    * @param limb  the index of the limb to return name of
    * @return      the name of the limb
    */
   public String getLimbName(int limb) {
      return limbs[limb].getName();
   }


   /**
    * Returns the limb item in the specified index.
    *
    * @param limb  
    * @return      The limb value
    */
   public Item getLimb(int limb) {
      return limbs[limb];
   }


   /**
    * Returns the limb count of this Quadruple.
    *
    * @return   The limbCount value
    */
   public int getLimbCount() {
      return limbs.length;
   }

   public void changeLeader(String leaderId, String leaderDescription) {
	   throw new UnsupportedOperationException();
   }

}

