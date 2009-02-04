package org.vermin.mudlib;

/**
 * AbstractRaceAdapter.java
 *
 * AbstractRaceAdapter is an utility class which makes
 * it easier to create classes defining slight
 * changes to an existing race without creating
 * a whole new race implementation.
 */

import org.vermin.mudlib.battle.Attack;

public abstract class RaceAdapter implements Race {
   
   protected Race delegate;
   
   public RaceAdapter(Race delegate) {
      this.delegate = delegate;
   }
   
   public String getHitLocation(int pos) {
      return delegate.getHitLocation(pos);
   }
   
   public String getName() {
      return delegate.getName();
   }
   
   public String getLimbName(int limb) {
      return delegate.getLimbName(limb);
   }
   
   public Item getLimb(int limb) {
      return delegate.getLimb(limb);
   }
   
   public int getLimbCount() {
      return delegate.getLimbCount();
   }
   
   public Slot[] getSlots() {
      return delegate.getSlots();
   }
   
   public Slot getSlotForLocation(int hitLoc) {
      return delegate.getSlotForLocation(hitLoc);
   }
   
   public int getMaxPhysicalStrength() {
      return delegate.getMaxPhysicalStrength();
   }
   
   public int getMaxMentalStrength() {
      return delegate.getMaxMentalStrength();
   }
   
   public int getMaxPhysicalConstitution() {
      return delegate.getMaxPhysicalConstitution();
   }
   
   public int getMaxMentalConstitution() {
      return delegate.getMaxMentalConstitution();
   }
   
   public int getMaxPhysicalCharisma() {
      return delegate.getMaxPhysicalCharisma();
   }
   
   public int getMaxMentalCharisma() {
      return delegate.getMaxMentalCharisma();
   }
   
   public int getMaxPhysicalDexterity() {
      return delegate.getMaxPhysicalDexterity();
   }
   
   public int getMaxMentalDexterity() {
      return delegate.getMaxMentalDexterity();
   }
   
   public int getPhysicalStrengthCost() {
      return delegate.getPhysicalStrengthCost();
   }
   
   public int getMentalStrengthCost() {
      return delegate.getMentalStrengthCost();
   }
   
   public int getPhysicalConstitutionCost() {
      return delegate.getPhysicalConstitutionCost();
   }
   
   public int getMentalConstitutionCost() {
      return delegate.getMentalConstitutionCost();
   }
   
   public int getPhysicalCharismaCost() {
      return delegate.getPhysicalCharismaCost();
   }
   
   public int getMentalCharismaCost() {
      return delegate.getMentalCharismaCost();
   }
   
   public int getPhysicalDexterityCost() {
      return delegate.getPhysicalDexterityCost();
   }
   
   public int getMentalDexterityCost() {
      return delegate.getMentalDexterityCost();
   }
   
   public int getBaseHpRegen() {
      return delegate.getBaseHpRegen();
   }
   
   public int getBaseSpRegen() {
      return delegate.getBaseSpRegen();
   }
   
   public int getSize() {
      return delegate.getSize();
   }
   
   public int getExpRate() {
      return delegate.getExpRate();
   }
   
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      return delegate.getAttackerGoreMessage(who, attack, hitLocation);
   }
   
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      return delegate.getSubjectGoreMessage(who, attack, hitLocation);
   }

   public String[] getGenders() {
	  return delegate.getGenders();
   }
   
   public String getLeader() {
	   return delegate.getLeader();
   }
   public String getLeaderDescription() {
	   return delegate.getLeaderDescription();
   }
   
}
