package org.vermin.mudlib.battle;

public enum GoreProperty {
   /** A value indicating that the subject has a skeletal structure.
    * The messages provided should feature bones breaking, snapping, fracturing etc.
    */
    HAS_BONES,
   /** A value indicating that the subject has blood. 
    * Messages provided should feature bleeding wounds, blood flying arouind etc.
    */
    HAS_BLOOD,
   /** A value indicating that the subject is covered with fur.
    * Messages provided should feature the fur being mutilated.
    */
    HAS_FUR,
   /** A value indicating that the subject is covered with scales.
    * Messages provided should feature the damaging and penetration of the protective scales.
    */
    HAS_SCALES,
   /** A value indicating that the subject has an exoskeleton.
    * Messages provided should feature the damaging and penetration of the exoskeleton.
    */
    HAS_EXOSKELETON,
   /** A value indicating that the subject has the standard set of internal organs.
    * Messages provided should feature more or less succesful attempts to damage internal organs.
    */
    HAS_INTERNAL_ORGANS,
	/** A value indicating that the subject is covered with feathers.
	 * Messages provided should feature the damaging of the feather layer.
	 */
    HAS_FEATHERS,
	/** A value indicating that the subject is an animal.
	 * Messages provided should feature standard animal reactions to pain, such as animal sounds,
	 * rolling on the ground, etc.
	 */
    IS_ANIMAL,
   /** A value indicating that the subject is a mechanical construct.
    * Messages provided should feature damaging the structure of the construct, springs and
    * cogwheels flying around, etc.
    */
    IS_MECHANICAL,
   /** A value indicating that the subject is undead.
    * Messages provided should indicate the indifference of undead beings towards pain.
    */
    IS_UNDEAD,
   /** A value indicating that the subject is an insect.
    * Messages provided should feature statndard insectoid reactions to pain, such as
    * shrieking sounds, body twitching, internal fluids bleeding, etc.
    */
    IS_INSECT,
   /** A value indicating that the subject represents a swarm of small creatures.
    * Messages provided should feature the dying of one or more members of the swarm.
    */
    IS_SWARM,
   /** A value indicating that the subject is, or is covered by, slimy gelatinous goo.
    * Messages provided should feature the damage and/or penetration of the silme.
    */
    IS_GELATINOUS,
   /** A value indicating that the subject is more or less a fungus.
   * Messages provided should feature spores flying around, chunks falling off etc.
   */
    IS_FUNGOID,
   /** A value indicating that the subject is capable of flying. 
    * Messages provided should feature the subject being thrown down from mid-flight etc.
    * NOTE: This does not indicate that the subject has wings.
    */
    IS_FLYING,
   /** A value indicating that the subject is of a magical persuasion. 
    * Messages provided should feature various magical effects of the magic
    * dissipating under stress.
    */
    IS_MAGICAL,
   /** A value indicating that the subject makes wild, unintelligible shrieks when
    * subject to large amounts of pain. */
    VOICE_SHRIEKS,
   /** A value indicating that the subject can speak, insult and 
    * beg for mercy etc. when subject to pain.
    */
    VOICE_SPEAKS,
   /** A value indicating that the subject can speak, insult and
    * beg for mercy in its own unintelligible language.
    */
    VOICE_FOREIGN_LANGUGE,
   /** A value indicating that the subject roars in loud voice
    * when subject to pain.
    */
    VOICE_ROARS
}
