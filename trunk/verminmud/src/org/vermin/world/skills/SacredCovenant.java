/*
 * Created on 11.3.2005
 *
 */
package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DamageListener;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.behaviour.BehaviourAdapter;
import org.vermin.mudlib.skills.BaseSkill;

/**
 * @author tadex
 *
 */
public class SacredCovenant extends BaseSkill {
    
    public int getCost(SkillUsageContext suc) {
        return 667;
    }
    public String getName() {
        return "sacred covenant";
    }
    
    public int getTickCount() {
        return Dice.random(4) + 6;
    }
    
    public SkillType[] getTypes() {
        return new SkillType[] {
                SkillTypes.SELF,
                SkillTypes.DIVINE
        };
    }
	
	public boolean tryUse(SkillUsageContext suc) {
		if(suc.getActor().inBattle()) {
			suc.getActor().notice("You cannot perform the elaborate rituals while in battle.");
			return false;			
		}
		return true;
	
	}
    
    public void use(SkillUsageContext suc) {
		int arcane = 0;
		Living who = suc.getActor();
		if(who.checkSkill("arcane knowledge") > 0) {
			arcane = Dice.random(2);
		}
			
        Covenant c = new Covenant();
        c.ticks = 3 + Dice.random(3)+arcane;
        c.owner = suc.getActor();
        
        suc.getActor().addBehaviour(c);
        suc.getActor().addDamageListener(c);
    }
    
    public class Covenant extends BehaviourAdapter implements DamageListener {
        int ticks;
        Living owner;
        
        public void onBattleTick(Living who) {
            ticks--;
        }
        public Damage onSubHp(Damage dmg, Living attacker, int hitloc) {
            if(attacker != null && attacker.getLifeAlignment() > -1000) {
                Damage d = new Damage();
                d.damage = dmg.damage;
                d.type = dmg.type;
                return d;
            }
            return new Damage(dmg.type, 0);
        }

        public boolean isActive() {
            if(ticks == 0)
                owner.removeBehaviour(this);
            return ticks > 0;
        }
    };
    
}
