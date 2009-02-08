/* Damage.java
	19.1.2002	TT & VV & JP
	
	Damage type structure.
*/

package org.vermin.mudlib;

import java.util.ArrayList;

public class Damage {

	public static final int NUM_TYPES		= 12;

	/**
	 * Enumeration of different damage types.
	 * A damage type may have an affliction type, which means
	 * that the type does not take HP, but causes the specified
	 * type of affliction with the damage amount.
	 */
	public enum Type {
		
		PHYSICAL,	
		ELECTRIC,
		POISON(Affliction.Type.POISON),
		FIRE,
		COLD,
		ASPHYXIATION,
		PSIONIC,
		ACID,
		MAGICAL,
		RADIATION,
		SONIC,
		STUN(Affliction.Type.STUN),

		/* SUB-TYPES FOR PHYSICAL */
			CRUSHING(PHYSICAL),
			PIERCING(PHYSICAL),
		CHOPPING(PHYSICAL),
		SLASHING(PHYSICAL);

		private Affliction.Type afflicts = null;
		private Type resistance = null;

		Type(Affliction.Type afflicts) {
			this.afflicts = afflicts;
		}

		Type(Damage.Type resistance) {
			this.resistance = resistance;
		}
	
		Type() {}

		public Type getResistanceType() {
			return resistance == null ? this : resistance;
		}

		public Affliction.Type getAfflictionType() {
			return afflicts;
		}

		public boolean isAffliction() {
			return afflicts != null;
		}
	};
	
	
	
	public Type type;
	public int damage;

	public Damage() {}
	public Damage(Type type, int damage) {
		this.type = type;
		this.damage = damage;
	}
	
	public static class DamageBuilder {
		private ArrayList<Damage> dmg = new ArrayList<Damage>();
		public DamageBuilder physical(int value) { return add(Damage.Type.PHYSICAL, value);	}
		public DamageBuilder electric(int value) { return add(Damage.Type.ELECTRIC, value); }
		public DamageBuilder poison(int value) { return add(Damage.Type.POISON, value); }
		public DamageBuilder fire(int value) { return add(Damage.Type.FIRE, value); }
		public DamageBuilder cold(int value) { return add(Damage.Type.COLD, value); }
		public DamageBuilder asphyxiation(int value) { return add(Damage.Type.ASPHYXIATION, value); }
		public DamageBuilder psionic(int value) { return add(Damage.Type.PSIONIC, value); }
		public DamageBuilder acid(int value) { return add(Damage.Type.ACID, value); }
		public DamageBuilder magical(int value) { return add(Damage.Type.MAGICAL, value); }
		public DamageBuilder radiation(int value) { return add(Damage.Type.RADIATION, value); }
		public DamageBuilder sonic(int value) { return add(Damage.Type.SONIC, value); }
		public DamageBuilder stun(int value) { return add(Damage.Type.STUN, value); }

		/* SUB-TYPES FOR PHYSICAL */
		public DamageBuilder crushing(int value) { return add(Damage.Type.CRUSHING, value); }
		public DamageBuilder piercing(int value) { return add(Damage.Type.PIERCING, value); }
		public DamageBuilder chopping(int value) { return add(Damage.Type.CHOPPING, value); }
		public DamageBuilder slashing(int value) { return add(Damage.Type.SLASHING, value); }

		public DamageBuilder add(Damage.Type t, int value) {
			dmg.add(new Damage(t, value));
			return this;
		}
		public Damage[] dmg() {
			return (Damage[]) dmg.toArray(new Damage[dmg.size()]);
		}
	}
	
	public static DamageBuilder build() {
		return new DamageBuilder();
	}
}
