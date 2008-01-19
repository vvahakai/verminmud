/*
 * Created on 29.1.2005
 *
 */
package org.vermin.world.skills;

import org.vermin.driver.AbstractPropertyProvider;
import org.vermin.driver.Driver;
import org.vermin.driver.PropertyProvider;
import org.vermin.driver.Queue;
import org.vermin.driver.Tickable;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultRoomImpl;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.mudlib.Tick;

import static org.vermin.mudlib.LivingProperty.*;

/**
 * @author tadex
 *
 */
public class BanishAbomination extends BaseSkill {

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#getCost(org.vermin.mudlib.SkillUsageContext)
     */
    public int getCost(SkillUsageContext suc) {
        return 350;
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#getName()
     */
    public String getName() {
        return "banish abomination";
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#getTickCount()
     */
    public int getTickCount() {
        return 3+Dice.random(2);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#getTypes()
     */
    public SkillType[] getTypes() {
        return new SkillType[] { SkillTypes.LOCAL, SkillTypes.MAGICAL, SkillTypes.OFFENSIVE };
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#tryUse(org.vermin.mudlib.SkillUsageContext)
     */
    public boolean tryUse(SkillUsageContext suc) {
        if( !((Living)suc.getTarget()).providesAny(UNDEAD, EXTRAPLANAR) ||
            suc.getTarget() instanceof Player ) {
            suc.getActor().notice(suc.getTarget().getName()+" doesn't seem like an abomination");
            return false;
        } else
            return true;
    }
            
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Skill#use(org.vermin.mudlib.SkillUsageContext)
     */
    public void use(final SkillUsageContext suc) {
        synchronized(suc.getActor().getRoom()) {
            
            final Living tgt = (Living) suc.getTarget();
            
            final Room old = tgt.getRoom();
            final Room r = new DefaultRoomImpl();
            
           
            
            if(tgt.isDead())
                return;
            
            old.remove(tgt);
            r.add(tgt);
            suc.getActor().notice("The target vanishes to another dimension.");
            
            final PropertyProvider<LivingProperty> provider =
                new AbstractPropertyProvider<LivingProperty>() {
                    public boolean provides(LivingProperty p) {
                        return p == LivingProperty.NO_REGENERATION;
                    }
            };
            tgt.addProvider(provider);
            
            final int[] count = new int[] { Dice.random(9) + 8 };
            Tickable t = new Tickable() {
                public boolean tick(Queue queue) {

                    boolean again = --count[0] > 0;
                    if(!again) {
                        tgt.removeProvider(provider);
                        synchronized(old) {
                            if(tgt.isDead()) {
                                suc.getActor().notice("The corpse of "+tgt.getName()+" returns from another dimension.");
                                old.add(r.findByName("corpse"));
                            } else {
                                suc.getActor().notice(tgt.getName()+" is unleashed from another dimension.");
                                old.add(tgt);
                                tgt.subHp(new Damage(Damage.Type.MAGICAL, 123), suc.getActor());
                            }
                        }
                    } else
                        suc.getActor().notice(tgt.getName()+" is bound to another dimension.");
                    return again;
                }
            };
            Driver.getInstance().getTickService().addTick(t, Tick.BATTLE);   
                
            
        }
    }
}
