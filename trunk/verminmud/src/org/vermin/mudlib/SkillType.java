package org.vermin.mudlib;

public interface SkillType {

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

	/**
	 * Called before concentration begins to check
	 * if the actor may use the skill.
	 *
	 * @param suc the skill usage context
	 */
	public boolean tryUse(SkillUsageContext suc);

	/**
	 * Called before the skill is applied.
	 * This is only called if the skill use has succeeded.
	 *
	 * @param suc skill usage context
	 */
	public void onUse(SkillUsageContext suc);

	/**
	 * Find the skill target.
	 * @param actor the living using the skill
	 * @param target the target name
	 * @return the target or null
	 */
	public MObject findTarget(Living actor, String target);
	
	/**
	 * Calculate the skill cost modifier.
	 * NOTE: Only the largest modifier of all types is used.
	 *
	 * @param suc the skill usage cost
	 * @param skillCost the skill cost in spell points
	 */
	public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost);
}
