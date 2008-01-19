package org.vermin.mudlib;

import java.util.LinkedList;

public class DefaultWieldableImpl extends DefaultItemImpl implements Wieldable
{
	private static final String[] CRUSHING_VERBS = new String[] {"hit", "slam", "bash", "smash", "crush", "strike"};
	private static final String[] PIERCING_VERBS = new String[] {"hit", "pierce", "lunge", "thrust", "stab"};
	private static final String[] CHOPPING_VERBS = new String[] {"hit", "cleave", "hack", "hew"};
	private static final String[] SLASHING_VERBS = new String[] {"hit", "cut", "slash", "slice"};

	private static final String[] CRUSHING_VERBS_S = new String[] {"hits", "slams", "bashes", "smashes", "crushes", "strikes"};
	private static final String[] PIERCING_VERBS_S = new String[] {"hits", "pierces", "lunges", "thrusts", "stabs"};
	private static final String[] CHOPPING_VERBS_S = new String[] {"hits", "cleaves", "hacks", "hews"};
	private static final String[] SLASHING_VERBS_S = new String[] {"hits", "cuts", "slashes", "slices"};
	
	
   protected Damage[] hitDamageTypes;

   protected float hitSpeed = 1.0f;

	protected LinkedList defenseModifiers;
	protected LinkedList[] damageModifiers;
   
   public DefaultWieldableImpl() {
      Damage dmg = new Damage();
      dmg.type = Damage.Type.CRUSHING;
      dmg.damage = 1;

      hitDamageTypes = new Damage[] { dmg };
      
   }
      

   public Damage[] getHitDamage(Living target) {
      return hitDamageTypes;
   }
   
   public Damage[] getProjectileDamage(Living target) {
	   // default implementation just returns the same as hit damage
	   return getHitDamage(target);
   }
   
   public void setHitDamageTypes(Damage[] types) {
      this.hitDamageTypes = types;
   }
   
   public String getObjectHitVerb(Damage.Type type) {
	
      switch(type) {
		  case CRUSHING:
			  return oneOf(CRUSHING_VERBS);
		  case PIERCING:
			  return oneOf(PIERCING_VERBS);
		  case CHOPPING:
			  return oneOf(CHOPPING_VERBS);
		  case SLASHING:
			  return oneOf(SLASHING_VERBS);
		  case POISON:
			  return "poison";
		  case FIRE:
			  return "scorch";
		  case COLD:
			  return "freeze";
		  case ASPHYXIATION:
			  return "choke";
		  case PSIONIC:
			  return "mindblast";
		  case ACID:
			  return "corrode";
		  case MAGICAL:
			  return "zap";
		  case RADIATION:
			  return "radiate";
		  case SONIC:
			  return "blast";
		  case STUN:
			  return "stun";
		  default:
			  return "congratulate";
      }
   }
	
   public String getSubjectHitVerb(Damage.Type type) {
	
      switch(type) {
		  case CRUSHING:
			  return oneOf(CRUSHING_VERBS_S);
		  case PIERCING:
			  return oneOf(PIERCING_VERBS_S);
		  case CHOPPING:
			  return oneOf(CHOPPING_VERBS_S);
		  case SLASHING:
			  return oneOf(SLASHING_VERBS_S);
		  case POISON:
			  return "poisons";
		  case FIRE:
			  return "scorches";
		  case COLD:
			  return "freezes";
		  case ASPHYXIATION:
			  return "chokes";
		  case PSIONIC:
			  return "mindblasts";
		  case ACID:
			  return "corrodes";
		  case MAGICAL:
			  return "zaps";
		  case RADIATION:
			  return "radiates";
		  case SONIC:
			  return "blasts";
		  case STUN:
			  return "stuns";
		  default:
			  return "congratulates";
      }
   }


   public String getHitNoun(Damage.Type type) {
	   String noun = null;
		
	   switch(type) {
		   case CRUSHING:
			   noun = oneOf(CRUSHING_VERBS);
		   break;
		   case PIERCING:
			   noun = oneOf(PIERCING_VERBS);
		   break;
		   case CHOPPING:
			   noun = oneOf(CHOPPING_VERBS);
		   break;
		   case SLASHING:
			   noun = oneOf(SLASHING_VERBS);
		   break;
		   default:
		   noun = "hit";
	   }
		
	   return noun;
   }
   
	public WeaponType getWeaponType() { return WeaponType.NONE; }
   
   public int getDefensiveValue() { return 1; } 

   public boolean tryWield(Living who) { return true; }
   
   public boolean tryUnwield(Living who) { return true; }
   
   public void onWield(Living who) { }
   
   public void onUnwield(Living who) { }
   
   public void hitReact() { }
   
   public boolean hitOverride(Living attacker, Living target) { return false; }

	public void addModifier(Modifier m) {
		switch(m.getType()) {

		  case DEFENSE:
			  if(defenseModifiers == null)
				  defenseModifiers = new LinkedList();
			  defenseModifiers.add(m);
			  break;

		  case DAMAGE:

			  int damageType = (Integer) m.getArguments()[0];
			  if(damageModifiers == null)
				  damageModifiers = new LinkedList[Damage.NUM_TYPES];
			  if(damageModifiers[damageType] == null)
				  damageModifiers[damageType] = new LinkedList();
			  damageModifiers[damageType].add(m);
			  break;

		  default: super.addModifier(m);
		}
	}

	/**
	 * A convinience method for randomly selecting a string
	 * from an array.
	 * 
	 * @param strings  an array of strings to choose from
	 * @return  a randomly chosen string from the passed array
	 */
	protected String oneOf(String[] strings) {
		return strings[Dice.random(strings.length)-1];
	}

	public float getSpeed() {
		return hitSpeed;
	}
}
