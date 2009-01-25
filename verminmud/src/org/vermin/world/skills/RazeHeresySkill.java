package org.vermin.world.skills;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.vermin.mudlib.CorpseItem;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.items.TemplarPendant;

public class RazeHeresySkill extends BaseSkill {

	public void use(SkillUsageContext suc) {
       	Living who = suc.getActor();
    	int resorbSanctity = 1;
    	
		Iterator en = who.getRoom().findByType(Types.ITEM);
		Set<CorpseItem> remove = new HashSet();
		while(en.hasNext()) {
			Item l = (Item) en.next();
			CorpseItem corpse = (CorpseItem) l;
			if(l instanceof CorpseItem) {
				remove.add((CorpseItem)l);
			}
		}
		
		for(CorpseItem corpse : remove) {
			DefaultMonster m = corpse.getMonster();
			if(100-Dice.random(who.checkSkill("resorb sanctity")) < 50) {
				resorbSanctity = 2;
			}
			
			TemplarPendant p = (TemplarPendant) suc.getActor().findByNameAndType("templar_guild_object", Types.ITEM);
			p.setVirtue((int) (p.getVirtue()+(m.getExperienceWorth()/100*resorbSanctity)));
			((Room) corpse.getParent()).remove(corpse);
		}

		suc.getActor().notice("You successfully raze all the herecy thoughout the room.");
	}
	public String getName() {
		return "raze heresy";
	}
    public int getCost(SkillUsageContext suc) {
        return 10;
    }
    
	public int getTickCount() {
		return 2+Dice.random(1);
	}

    public SkillType[] getTypes() {
        return new SkillType[] { SkillTypes.DIVINE };
    }

}
