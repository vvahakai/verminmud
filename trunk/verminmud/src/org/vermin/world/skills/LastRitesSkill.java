/*
 * Created on 16.1.2005
 *
 */
package org.vermin.world.skills;


import org.vermin.mudlib.CorpseItem;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.SkillTypes;
import org.vermin.world.items.TemplarPendant;

/**
 * @author tadex
 *
 */
public class LastRitesSkill extends BaseSkill {

    public SkillType[] getTypes() {
        return new SkillType[] { SkillTypes.DIVINE, SkillTypes.LOCAL };
    }
    
    public int getCost(SkillUsageContext suc) {
        return 10;
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#use(org.vermin.mudlib.SkillUsageContext)
     */
    public void use(SkillUsageContext suc) {
    	
    	Living who = suc.getActor();
    	int resorbSanctity = 1;
    	
		if(100-Dice.random(who.checkSkill("resorb sanctity")) < 30) {
    		resorbSanctity = 2;
    	}
    	else {
    		resorbSanctity = 1;
    	
    	}
		        
        CorpseItem corpse = (CorpseItem) suc.getTarget();
        DefaultMonster m = corpse.getMonster();
        
        if(suc.getActor().getRoom().contains(corpse)) {
            TemplarPendant p = (TemplarPendant) suc.getActor().findByNameAndType("templar_guild_object", Types.TYPE_ITEM);
            
            p.setVirtue((int) (p.getVirtue()+(m.getExperienceWorth()/100*resorbSanctity)));
            ((Room) corpse.getParent()).remove(corpse);
            suc.getActor().notice("You free the soul.");

        } else {
            suc.getActor().notice("Use last rites on what?");
        }
        
    }

    public boolean tryUse(Living actor, MObject target) {
        Object p = actor.findByNameAndType("templar_guild_object", Types.TYPE_ITEM);
        if(target == null || !(target instanceof CorpseItem) || ((CorpseItem) target).getMonster() == null) {
            actor.notice("You can only use last rites on a corpse.");
            return false;
        } else if(p == null || !(p instanceof TemplarPendant)) {
            actor.notice("You must have the sacred pendant to use this skill.");
            return false;
        }
        else return true;
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#getName()
     */
    public String getName() {
        return "last rites";
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.skills.BaseSkill#getTickCount()
     */
    public int getTickCount() {
        return 1;
    }

   
}
