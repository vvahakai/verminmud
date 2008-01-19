package org.vermin.mudlib;

public class SkillTypeAdapter implements SkillType {

	public void onTick(SkillUsageContext suc) {}
	public void onStart(SkillUsageContext suc) {}
	public boolean tryUse(SkillUsageContext suc) { return true; }
	public void onUse(SkillUsageContext suc) {}
	public MObject findTarget(Living actor, String target) {
		return null;
	}
	public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { }
}
