/*
 * Created on 17.1.2005
 *
 */
package org.vermin.world.skills;


import org.vermin.mudlib.CorpseItem;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.mudlib.SkillTypes;

/**
 * @author tadex
 *
 */
public class LiberateSpiritSkill extends BaseSkill {

    public SkillType[] getTypes() {
        return new SkillType[] { SkillTypes.DIVINE, SkillTypes.LOCAL };
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#use(org.vermin.mudlib.SkillUsageContext)
     */
    public void use(SkillUsageContext suc) {
    	
        CorpseItem corpse = (CorpseItem) suc.getTarget();
        DefaultMonster m = corpse.getMonster();
    	
        if(suc.getActor().getRoom().contains(corpse)) { 
        	((Room) corpse.getParent()).remove(corpse);
        	suc.getActor().notice("You let the bothered soul free.");
        } 
        else {
            suc.getActor().notice("Use liberate spirit at what?");
        }
        
    }

    public boolean tryUse(Living actor, MObject target) {
        if(target == null || !(target instanceof CorpseItem) || ((CorpseItem) target).getMonster() == null) {
            actor.notice("You can only use liberate spirit on a corpse.");
            return false;           	
        }
        else return true;
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#getName()
     */
    public String getName() {
        return "liberate spirit";
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#getTickCount()
     */
    public int getTickCount() {
        return 1;
    }

   
}
