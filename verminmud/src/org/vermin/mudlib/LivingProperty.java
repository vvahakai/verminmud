package org.vermin.mudlib;


/**
 * Enumerates possible properties that <code>Living</code>s can
 * have.
 */
public enum LivingProperty {

	/**
	 * This living does not have to breathe in order to survive.
	 */
	DOES_NOT_BREATHE,

	/** 
	 * Allows breathing underwater.
	 */
	BREATHES_WATER,

	/**
	 * This living has the ability to fly.
	 */
	FLIGHT,

	/**
	 * Cold creatures can not be seen by infrared vision.
	 */
	COLD,

	/**
	 * Mindless creatures can not be detected by or affected 
	 * with psionic powers.
	 */
	MINDLESS,
	
	/**
	 * This creature is considered to be and undead.
	 */
	UNDEAD,
	
	/**
	 * The creature is not native to the physical plane of existence.
	 */
	EXTRAPLANAR,
	
	/**
	 * This creature has been constructed out of inorganic components.
	 */
	CONSTRUCT,
	
	/**
	 * This creature is capable of detecting objects by sonic sense
	 * even without visible illumination.
	 */
	SONIC_SENSE,

	/**
	 * This creature can warm objects without visible illumination.
	 */
	INFRAVISION,
	
	/**
	 * This creature cannot be detected by visual means.
	 */
	INVISIBLE,


	/**
	 * This creature is allergic to water.
	 */
	ALLERGY_WATER,

	/**
	 * This creature is allergic to illumination levels
	 * above the racial maximum visible illumination.
	 */
	ALLERGY_LIGHT,

	/**
	 * This living cannot use skills.
	 */
	NO_SKILLS,

	/**
	 * This living cannot do battle.
	 */
	NO_BATTLE,

	/**
	 * This living is immobilized. All voluntary movement
	 * is restricted.
	 */
	IMMOBILIZED,

	/**
	 * This living cannot teleport or be magically summoned.
	 */
	NO_TELEPORT,
	
	/**
	 * This living does not regenerate normally.
	 */
	NO_REGENERATION,
	
	/**
	 * This living hovers slightly above surface.
	 * This enables walking on water.
	 */
	HOVER,

	/**
	 * This living is swarm composed of multiple creatures
	 */
	SWARM
}
			
