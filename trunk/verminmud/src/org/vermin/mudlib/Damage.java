/* Damage.java
	19.1.2002	TT & VV & JP
	
	Damage type structure.
*/

package org.vermin.mudlib;

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
}
