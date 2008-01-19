package org.vermin.mudlib.skills;

import org.vermin.driver.Tickable;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DamageListener;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Tick;
import org.vermin.mudlib.World;

/**
 *
 */
public class LifeLink extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL, SkillTypes.DIVINE };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public int getTickCount() {
		return 7;
	}

	public String getName() {
		return "life link";
	}

	public void use(SkillUsageContext suc) {
		
		final Living actor = suc.getActor();
		final Living target = (Living) suc.getTarget();

		int s = suc.getSkillSuccess();

		if(s <= 0) {
			actor.notice("You try to link your lives, but fail.");
			return;
		}

		actor.notice("You establish a life link with "+target.getName());
		target.notice(actor.getName()+" has established a life link with you.");
		DmgListener dl = new DmgListener(actor, target);
		World.addRegenTick(dl);
		actor.addDamageListener(dl);
	}
	
	private static class DmgListener implements DamageListener, Tickable<Tick> {
		
		private transient Living actor, target;
		private double actorPercent, targetPercent;
		private int rounds;
		private String targetName, actorName;
		
		public DmgListener() {}
		
		public DmgListener(Living a, Living t) {
			actor = a;
			target = t;
			actorPercent = 
			rounds = 30 + Dice.random(4) * 10;
			targetName = target.getName();
			actorName = actor.getName();
			double total = actor.getMaxHp()+target.getMaxHp();
			actorPercent = actor.getMaxHp()/total;
			targetPercent = target.getMaxHp()/total;

		}
		
		public boolean tick(Tick q) {
			if(q == Tick.REGEN) {
				World.log("LIFELINK tick: "+rounds);
				rounds--;
				if(rounds == 0) {
					if(actor != null)
						actor.notice("You life link on "+targetName+" has faded.");
					if(target != null)
						target.notice(actorName+"'s life link on you has faded.");
					return false;
				}
				return true;
			} else
				return false;
		}
		public Damage onSubHp(Damage dmg, Living attacker, int hitLocation) {				 
				target.subHp(new Damage(dmg.type, (int) (dmg.damage*targetPercent)));
				return new Damage(dmg.type, (int) (dmg.damage * actorPercent));
		}
		
		public boolean isActive() {
			if(rounds > 0 && target != null && actor != null)
				return true;
			else
				return false;
		}
		
	};
	
}
