package org.vermin.mudlib.skills;
import org.vermin.mudlib.*;


/**
 * Base class for all skills.
 */
public abstract class BaseSkill implements Skill {
	
	public BaseSkill() {}

/*	private final int calculateSkillCost(SkillUsageContext suc) {
		SkillType[] subTypes = getTypes();
		for(int i=0; i<subTypes.length; i++)
			subTypes[i].calculateSkillCostModifier(suc, getCost(suc));
		
		suc.getActor().notice(suc.getSkillCostModifierMessage());
		return getCost(suc) + suc.getSkillCostModifier();
		
	}*/

	// METHODS THAT CAN BE SPECIALIZED
	public int checkSuccess(Living actor) {
		return actor.checkSkill(getName());
	}
	public boolean tryUse(Living actor, MObject target) {
		return true;
	}
	
    public boolean tryUse(SkillUsageContext suc) {
        return tryUse(suc.getActor(), suc.getTarget());
    }
   
	public void onTick(SkillUsageContext suc) {
		return;
	}
	public void onStart(SkillUsageContext suc) {
		return;
	}

    
	// METHODS TO BE IMPLEMENTED IN THE ACTUAL SKILLS
	public int getCost(SkillUsageContext suc) { return 0; }
	public abstract void use(SkillUsageContext suc);
	public abstract String getName();
   public abstract int getTickCount();
   public abstract SkillType[] getTypes();
   
	/* Convenient checking methods */
	protected final boolean hasLivingTarget(Living who, MObject target) {
		if(target == null) {
			who.notice("Use "+getName()+" on who?");
		} else if(!(target instanceof Living)) {
			who.notice("Can't use "+getName()+" on inanimate objects.");
		} else {
			return true;
		}
		return false;
	}

	protected final boolean isTargetInRoom(Living who, MObject target) {
		if(target == null)
			return false;
		System.out.println("isTargetInRoom: "+target.getName()+" "+target.getType());
		MObject t = who.getRoom().findByNameAndType(target.getName(), target.getType());
		return t == target;
	}
	
}
