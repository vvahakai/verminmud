package org.vermin.mudlib;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.vermin.mudlib.Stat.*;

import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.GoreMessageProvider;
import org.vermin.mudlib.battle.GoreMessageProviderFactory;
import org.vermin.mudlib.battle.GoreProperty;
import static org.vermin.mudlib.battle.GoreProperty.HAS_BLOOD;


public class DefaultRaceImpl implements Race {
   
   protected String[] hitLocations;
   protected String name;
   protected String[] limbNames;
   protected Wieldable[] limbs;
   protected Slot[] slots;
   protected Map<Integer, Slot> hitlocSlotMap;
   protected EnumMap<Stat, Integer> stats;
   protected EnumMap<Stat, Integer> statCosts;
   protected int baseHpRegen;
   protected int baseSpRegen;
   protected int size;
   protected int expRate;
   protected String[] genders;
   protected int minimumVisibleIlluminationLevel;
   protected int maximumVisibleIlluminationLevel = 100;
   protected int alignmentProgress, alignmentLife;
   protected String leader, leaderDescription;
   
   protected transient GoreMessageProvider goreMessageProvider;

   private static final String[] DEFAULT_GENDERS = new String[] {"male", "female"};
   private static final Slot[] DEFAULT_SLOTS = new Slot[0];
   private static final Wieldable[] DEFAULT_LIMBS = new Wieldable[0];

   private HashMap<String, Integer> intrinsicSkills = new HashMap<String, Integer>();
   private EnumSet<LivingProperty> raceProperties = EnumSet.noneOf(LivingProperty.class);
   private EnumSet<GoreProperty> goreFlags = EnumSet.of(GoreProperty.HAS_BLOOD);
   
   
   
   public DefaultRaceImpl() {
   }
      
   public void start() {
//   	System.out.println("race "+this+" startattu");
      if(getGoreFlags() != null) {
         goreMessageProvider = GoreMessageProviderFactory.getInstance().findProvider(getGoreFlags());
      } else {
         goreMessageProvider = GoreMessageProviderFactory.getInstance().findProvider(EnumSet.of(HAS_BLOOD));
      }
      
      if(statCosts == null) {
         statCosts = new EnumMap<Stat, Integer>(Stat.class);
         for(Stat s : EnumSet.allOf(Stat.class)) {
            statCosts.put(s, 0);
         }
      }
   }
   
	public int getLifeAlignment() {
		return alignmentLife;
	}
	public int getProgressAlignment() {
		return alignmentProgress;
	}

	public EnumSet<LivingProperty> getRaceProperties() {
		return raceProperties;
	}

	public EnumSet<GoreProperty> getGoreFlags() {
		return goreFlags;
	}
   /**
	 * Describes a hit location.
	 * Smaller hit location number means better hit location.
	 * 0 = best hit location, 100 = worst hit location
	 *
	 * @param pos the hit location (0 - 100)
	 * @return the name of the location
	 */
    public String getHitLocation(int pos) {
       return hitLocations[pos];
    }
	
	/**
	 * Returns the name of the race.
	 *
	 * @return the name
	 */
    public String getName() {
       return name;
    }
	
	/**
	 * Returns the name of the given limb.
	 *
	 * @param limb the limb number
	 * @return the name of the limb
	 */
    public String getLimbName(int limb) {
       return limbNames[limb];
    }

	/**
	 * Returns the given limb.
	 *
	 * @param limb the limb number
	 * @return the limb item
	 */
    public Item getLimb(int limb) {
       return limbs[limb];
    }

	/**
	 * Returns the number of limbs this race has.
	 *
	 * @return the number of limbs
	 */
    public int getLimbCount() {
       if(limbs == null) {
          return 0;
       } else {
          return limbs.length;
       }
    }
	
   /**
	 * Returns an array of armor slots.
	 *
	 * @return an array of <code>Slot</code>s
	 */
    public Slot[] getSlots() {
       if(slots == null) {
          return DEFAULT_SLOTS;
       } else {
          return slots;
       }
    }
   
   /**
	 * Returns the slot associated with the given
	 * hit location.
	 *
	 * @param hitLoc the desired hit location (0 - 100)
	 * @return the <code>Slot</code> or <code>null</code> if there is
	 *         no slot for the location
	 */
    public Slot getSlotForLocation(int hitLoc) {
       if(hitlocSlotMap == null) {
          return null;
       }
       return hitlocSlotMap.get(hitLoc);
    }

	/**
	 * Returns the maximum amount of physical strength
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxPhysicalStrength() {
       return stats.get(PHYS_STR);
    }

	/**
	 * Returns the maximum amount of mental strength
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxMentalStrength() {
       return stats.get(MENT_STR);
    }

	/**
	 * Returns the maximum amount of physical constitution
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxPhysicalConstitution() {
       return stats.get(PHYS_CON);
    }
	/**
	 * Returns the maximum amount of mental constitution
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxMentalConstitution() {
       return stats.get(MENT_CON);
    }

	/**
	 * Returns the maximum amount of physical charisma
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxPhysicalCharisma() {
       return stats.get(PHYS_CHA);
    }

	/**
	 * Returns the maximum amount of mental charisma
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxMentalCharisma() {
       return stats.get(MENT_CHA);
    }

	/**
	 * Returns the maximum amount of physical dexterity
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxPhysicalDexterity() {
       return stats.get(PHYS_DEX);
    }

	/**
	 * Returns the maximum amount of mental dexterity
	 * instances of this race can have.
	 *
	 * @return a <code>int</code> value
	 */
    public int getMaxMentalDexterity() {
       return stats.get(MENT_DEX);
    }

	/**
	 * Returns the cost of one point of physical strength.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
    public int getPhysicalStrengthCost() {
       return statCosts.get(PHYS_STR);
    }

	/**
	 * Returns the cost of one point of mental strength.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
    public int getMentalStrengthCost() {
       return statCosts.get(MENT_STR);
    }

	/**
	 * Returns the cost of one point of physical constitution.
	 *
	 * @return a <code>int</code>
	 */
    public int getPhysicalConstitutionCost() {
       return statCosts.get(PHYS_CON);
    }

	/**
	 * Returns the cost of one point of mental constitution.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
    public int getMentalConstitutionCost() {
       return statCosts.get(MENT_CON);
    }

	/**
	 * Returns the cost of one point of physical charisma.
	 *
	 * @return a <code>int</code>  from 0 to 10
	 */
    public int getPhysicalCharismaCost() {
       return statCosts.get(PHYS_CHA);
    }

	/**
	 * Returns the cost of one point of mental charisma.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
    public int getMentalCharismaCost() {
       return statCosts.get(MENT_CHA);
    }

	/**
	 * Returns the cost of one point of physical dexterity.
	 *
	 * @return a <code>int</code> from 0 to 10
	 */
    public int getPhysicalDexterityCost() {
       return statCosts.get(PHYS_DEX);
    }

	/**
	 * Returns the cost of one point of mental dexterity.
	 *
	 * @return a <code>int</code> from 0 to 10 
	 */
    public int getMentalDexterityCost() {
       return statCosts.get(MENT_DEX);
    }

	/**
	 * Returns the base hit point regeneration rate.
	 *
	 * @return an <code>int</code> value
	 */

    public int getBaseHpRegen() {
       return baseHpRegen;
    }
	/**
	 * Returns the base spell point regeneration rate.
	 *
	 * @return an <code>int</code> value
	 */
    public int getBaseSpRegen() {
       return baseSpRegen;
    }
	
	/**
	 * Returns the size.
	 *
	 * @return a <code>int</code> value
	 */
    public int getSize() {
       return size;
    }

	/**
	 * Returns the experience rate percentage.
	 *
	 * @return a <code>int</code> value
	 */
    public int getExpRate() {
       return expRate;
    }
	
	/**
	 * Create a gore message for attacker.
	 * @param who the <code>Living</code> who got hurt
	 * @param attack the attack producing the damage
	 * @param hitLocation the hit location of the damage
	 * @return a <code>String</code> value
	 */
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      return goreMessageProvider.getAttackerGoreMessage(who, attack, hitLocation);
   }
           
	/**
	 * Create a gore message for sujbect.
	 * @param who the <code>Living</code> who got hurt
	 * @param attack the attack producing the damage
	 * @param hitLocation the hit location of the damage
	 * @return a <code>String</code> value
	 */
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      return goreMessageProvider.getSubjectGoreMessage(who, attack, hitLocation);
   }
   
	/**
	 * Returns the available genders for this race.
	 *
	 * @return an array of <code>String</code> values
	 */
    public String[] getGenders() {
       if(genders == null) {
          return DEFAULT_GENDERS;
       } else {
          return genders;
       }
    }

	/**
	 * Returns the minimum illumination level this Race's creatures
	 * can see in.
	 *
	 * @return the illumination level
	 */
    public int getMinimumVisibleIllumination() {
       return minimumVisibleIlluminationLevel;
    }

	/**
	 * Returns the maximum illumination level this Race's creatures
	 * can see in.
	 *
	 * @return the illumination level
	 */
    public int getMaximumVisibleIllumination() {
       return maximumVisibleIlluminationLevel;
    }

	/**
	 * Returns a skill value of an intrinsic skill if such exists.
	 *
	 * @param skillname the name of the intrinsic skill
	 * @return the skill value
	 */
	public int getIntrinsicSkill(String skillname) {
        if(intrinsicSkills.containsKey(skillname))
			return intrinsicSkills.get(skillname);
        else
            return 0;
	}

	public String getLeader() {
		return leader;
	}

	public String getLeaderDescription() {
		return leaderDescription;
	}

	public void changeLeader(String leaderId, String leaderDescription) {
		this.leader = leaderId;
		this.leaderDescription = leaderDescription;
	}

}
