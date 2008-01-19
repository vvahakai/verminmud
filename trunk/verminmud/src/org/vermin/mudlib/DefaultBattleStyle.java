package org.vermin.mudlib;

import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;
import org.vermin.mudlib.battle.Attack;
import org.vermin.mudlib.battle.AttackPrevented;
import org.vermin.mudlib.battle.Effector;
import org.vermin.mudlib.battle.FailedAttack;
import org.vermin.mudlib.battle.GenericBattleStyle;
import org.vermin.mudlib.battle.Reaction;
import org.vermin.world.skills.Dodge;
import org.vermin.world.skills.Parry;
import org.vermin.world.skills.Riposte;

public class DefaultBattleStyle extends GenericBattleStyle {

	public DefaultBattleStyle() {}

	public String getName() {
		return "fighting";
	}

   public DefaultBattleStyle(Living owner) {
		super(owner);
	}
   
   public boolean tryUse() {
      return true;
   }

	public static class WeaponSkillEffector implements Effector {
		public int getEffectiveness() { return 1; }
		public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
			return actor.getSkill(weapon.getWeaponType().getSkillName());
		}
	}
	public static class MultiLimbEffector implements Effector {
		public int getEffectiveness() { return 1; }
		public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
			Wieldable[] wielded = actor.getWieldedItems(false);
			int count=0;
			for(int i=0; i<wielded.length; i++)
				if(wielded[i] == weapon) count++;
			
			return actor.getSkill(count > 1 ? "multi hand combat" : "single hand combat");
		}
	}

	public Effector[] getHitEffectors() {
		return new Effector[] {
			SKILL(2, "fighting", true),
			STAT(1, PHYS_DEX, true)/*,
			new WeaponSkillEffector(),
			new MultiLimbEffector()*/
		};
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {
			STAT(1, PHYS_STR, true),
			WEAPONDAM(0),
			new WeaponSkillEffector(),
			DICE(1),
			// 100 - hitLoc
		};
	}

	public Effector[] getDefensiveHitEffectors() {
		return new Effector[0];
	}


	public Reaction handleAttack(Attack attack) {

		Reaction reaction = null;

		if(attack instanceof FailedAttack)
			return null;


	react: {

			/* try to prevent the attack */
		prevent_attack: {
				if(Dodge.mayDodge(owner)) {
					reaction = Dodge.dodge(owner, attack, 1.0);
					if(reaction != null) break prevent_attack;
				}
				
				if(Parry.mayParry(owner)) {
					reaction = (Reaction) Parry.parry(owner, attack, 1.0);
					if(reaction != null) break prevent_attack;
				}
			}

			/* If attack was prevented, try to riposte */ 
			if(reaction instanceof AttackPrevented) {
				if(Riposte.mayRiposte(owner, attack)) Riposte.riposte(owner, attack.attacker, reaction);
				break react;
			}
			
			/* No riposte, take the damage */	
			reaction = takeDamage(attack);
		}

		return reaction;
	}

}
