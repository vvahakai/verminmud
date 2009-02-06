package org.vermin.mudlib;

public class SkillUsageContext {

	private Living actor;
	private MObject target;
	private int ticksLeft;
	private Skill skill;
	private SkillType[] types;
	private boolean aborted;
	private int skillSuccess = 0;

	private String targetName; // the name used for the target at cast time

	//skill cost modifier (absolute modifier, -100 lowers cost by 100 etc.)
	private int skillCostModifier = 0;
	private String skillCostModifierMessage = "";

	//skill effect modifier (percentage, 200 doubles effect, 50 halves effect)
	//skill can use this
	private int skillEffectModifier = 100;
	private String skillEffectModifierMessage = "";

	public SkillUsageContext(Living a, MObject t, Skill s) {
		skill = s;
		actor = a;
		target = t;
		types = skill.getTypes();
		ticksLeft = skill.getTickCount();
		aborted = false;
	}

	public Skill getSkill() {
		return skill;
	}
	
	public Living getActor() {
		return actor;
	}
	public MObject getTarget() {
		return target;
	}

	public String getTargetName() {
		return targetName;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public int getSkillSuccess() {
		return skillSuccess;
	}

	public void setTicksLeft(int tl) {
		ticksLeft = tl;
	}

	public void setSkillSuccess(int success) {
		skillSuccess = success;
	}

	public void setSkillCostModifier(int modifier, String message)
	{
		if(Math.abs(modifier) > Math.abs(skillCostModifier)) {
			skillCostModifier = modifier;
			skillCostModifierMessage = message;
		}
	}

    public void setTarget(MObject target) {
        this.target = target;
    }
    
	public void setTargetName(String name) {
		targetName = name;
	}

	public int getSkillCostModifier() { return skillCostModifier; }
	public String getSkillCostModifierMessage() { return skillCostModifierMessage; }

	public void setSkillEffectModifier(int modifier, String message)
	{
		if(Math.abs(modifier) > Math.abs(skillEffectModifier)) {
			skillEffectModifier = modifier;
			skillEffectModifierMessage = message;
		}
	}

	public int getSkillEffectModifier() { return skillEffectModifier; }
	public String getSkillEffectModifierMessage() { return skillEffectModifierMessage; }


	/**
	 * Stop using this skill.
	 */
	public void abort() {
		aborted = true;
	}

	public boolean isAborted() {
		return aborted;
	}

}
