/* QuestionVirtueSkill.java 
 * 17.1.2005 MV 
 */ 

package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.world.items.TemplarPendant;

public class QuestionVirtueSkill extends BaseSkill {

	public int virtue;
	public int morale;
	public int communion;
	public int smiting;
	public int deliverance;
		
	public SkillType[] getTypes() {
        return new SkillType[] { SkillTypes.DIVINE };
		}
	public void use(SkillUsageContext suc) {
            
            TemplarPendant p = (TemplarPendant) suc.getActor().findByNameAndType("templar_guild_object", Types.ITEM);
            
            virtue = p.getVirtue();
            morale = p.getMorale();
            communion = p.getCommunion();
            smiting = p.getSmiting();
            deliverance = p.getDeliverance();
            suc.getActor().notice("You question your honor and virtue, and receive information about the powers");      
            suc.getActor().notice("of the sacred pendant.");
            suc.getActor().notice("You have collected "+virtue+" points of Virtue.");
            suc.getActor().notice("You have collected "+morale+" points of Morale.");
            suc.getActor().notice("You have collected "+communion+" points of Communion.");
            suc.getActor().notice("You have collected "+smiting+" points of Smiting.");
            suc.getActor().notice("You have collected "+deliverance+" points of Deliverance.");         
            
    }
	
    public boolean tryUse(Living actor, MObject target) {
        Object p = actor.findByNameAndType("templar_guild_object", Types.ITEM);
        if(p == null || !(p instanceof TemplarPendant)) {
            actor.notice("You must have the sacred pendant to use this skill.");
            return false;
        }
        else return true;
    }
    
	public String getName() {
		return "question virtue";
	}
	
	public int getTickCount() {
		return 5;
	}

}
