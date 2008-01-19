package org.vermin.mudlib;

public class SkillsProvider implements Skills {
	
	public Skill get(String name) {
		Skill s = (Skill) World.get("skills/"+name);
		if(s == null) {
			return new Skill(){
				public void onStart(SkillUsageContext suc) {}
				public void onTick(SkillUsageContext suc) {}
				public boolean tryUse(SkillUsageContext suc) {
					suc.getActor().notice("This skill is not implemented yet or corrupt, please inform a Wizard.");
					return false;
				}
				public int checkSuccess(Living actor) {	return 0;}
				public int getCost(SkillUsageContext suc) {	return 0; }
				public SkillType[] getTypes() {	return new SkillType[0]; }
				public int getTickCount() {	return 0; }
				public void use(SkillUsageContext suc) {}
				public String getName() { return "unimplemented skill";	}
			};
		}
		return s;
	}
}