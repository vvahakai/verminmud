/* Skill.java
	19.1.2002	Tatu Tarvainen / Council 4
	
	Defines the skill (and spell) interface.
*/

package org.vermin.mudlib;


public interface Skill {

	/* Return the skills name */
	public String getName();

	/**
	 * Use this skill. 
	 * This is called when the concentration is done and
	 * the skill is fired.
	 */
	public void use(SkillUsageContext suc);

   /* Get preparation time for this skill in ticks. */
   public int getTickCount();

   /* Get skill types for this skill */
   public SkillType[] getTypes();

	/* Get the skill cost */
	public int getCost(SkillUsageContext suc);

	/** 
	 * Check success.
	 * Must return a positive integer on success.
	 * The value is how well the skill was performed.
	 * On failure this method must return <= 0.
	 */
	public int checkSuccess(Living actor);

    /**
     * Check if this skill can be used in the given context.
     * If the skill cannot be used, an informative message
     * should be shown to the actor.
     * 
     * @param suc the skill usage context
     * @return true if the skill can be used, false otherwise
     */
    public boolean tryUse(SkillUsageContext suc);
    
	/**
	 * Called on each tick.
	 *
	 * @param suc skill usage context
	 */
	public void onTick(SkillUsageContext suc);

	/**
	 * Called when concentration on the skill begins.
	 *
	 * @param suc the skill usage context
	 */
	public void onStart(SkillUsageContext suc);    
}
