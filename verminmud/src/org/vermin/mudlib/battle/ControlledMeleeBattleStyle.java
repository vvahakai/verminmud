package org.vermin.mudlib.battle;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;
import org.vermin.util.Print;

public class ControlledMeleeBattleStyle extends DefaultBattleStyle {

	public ControlledMeleeBattleStyle() {}

	public String getName() {
		return "coordinated melee";
	}

   public ControlledMeleeBattleStyle(Living owner) {
		super(owner);
	}
   
   public boolean tryUse() {
      return true;
   }

	public Effector[] getHitEffectors() {
		return new Effector[] {
			SKILL(2, "coordinated melee", true),
			STAT(2, PHYS_DEX, true),
			new WeaponSkillEffector(),
			new MultiLimbEffector()
		};
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {
			SCALE(2.0, SKILL(1, "find weakness", true)),
			SCALE(1.4, STAT(2, PHYS_STR, true)),
			WEAPONDAM(0),
			new WeaponSkillEffector(),
			DICE(1),
			// 100 - hitLoc
		};
	}

	public Effector[] getDefensiveHitEffectors() {
		return new Effector[0];
	}

	/*
	protected void handleCounterAttack(Living subject, Attack attack) {
		
		Reaction r = null;

		if(Dodge.mayDodge(owner)) {
			r = (Reaction) Dodge.dodge(owner, attack, 1.0);
			if(r instanceof AttackPrevented) {
				owner.notice("You nimbly dodge "+subject.getName()+"'s counter-attack.");
				subject.notice(Print.capitalize(owner.getName())+" nimbly dodges your counter-attack.");
				return;
			}
		}
		
		if(Parry.mayParry(owner)) {
			r = (Reaction) Parry.parry(owner, attack, 1.0);
			if(r instanceof AttackPrevented) {
				owner.notice("You skillfully parry "+subject.getName()+"'s counter-attack.");
				subject.notice(Print.capitalize(owner.getName())+" skillfully parries your counter-attack.");
				return;
			}
		}

		takeDamage((DamageTaken) r, attack);
		owner.notice(r.targetMessage);
		subject.notice(r.attackerMessage);

		}*/


	protected String[] generateAttackMessages(Attack attack, Reaction reaction, Living subject) {

		String[] msgs = new String[3];

		if(attack instanceof FailedAttack) {

			int msgNum = Dice.random(4);

			switch(msgNum) {
				case 1:
					msgs[0] = "You are forced on the defensive and cannot complete an effective attack on "+subject.getName()+".";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
					msgs[2] = getOwner().getName()+"'s attack on "+subject.getName()+" fails.";
				break;
				case 2:
					msgs[0] = "You fail to reach "+subject.getName()+" with your cautious attack.";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
					msgs[2] = getOwner().getName()+"'s attack on "+subject.getName()+" fails.";
				break;
				case 3:
					msgs[0] = "You attack on "+subject.getName()+" fails.";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
					msgs[2] = getOwner().getName()+"'s attack on "+subject.getName()+" fails.";
				break;
				case 4:
					msgs[0] = "You maneuver for a better position to attack "+subject.getName()+".";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
					msgs[2] = getOwner().getName()+"'s attack on "+subject.getName()+" fails.";
				break;
				default: // This should not happen
					msgs[0] = "You fail to beat Quakka with your long stick.";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
					msgs[2] = getOwner().getName()+"'s attack on "+subject.getName()+" fails.";
			}

		} else if(reaction instanceof DamageTaken) {
			
			DamageTaken dt = (DamageTaken) reaction;

			// (1 to controlled melee) + damage percentage of max hp 
			int d = Dice.random(getOwner().getSkill("coordinated melee")+1)+attack.getMaxDamage().damage * 100 / Math.max(getOwner().getMaxHp(),1);

			String hitloc = subject.getRace().getHitLocation(attack.hitLocation);
			String objectHitVerb = attack.weapon.getObjectHitVerb(dt.mainDamage.type);
			String subjectHitVerb = attack.weapon.getSubjectHitVerb(dt.mainDamage.type);

			if(d < 20) {
				msgs[0] = "You "+objectHitVerb+" "+subject.getName()+" with your "+attack.weapon.getName()+".";
				msgs[1] = Print.capitalize(attack.attacker.getName())+" "+subjectHitVerb+" you with "+attack.attacker.getPossessive()+" "+attack.weapon.getName()+".";
			} else if(d < 40) {
				msgs[0] = "Your skilled "+objectHitVerb+" hits "+subject.getName()+".";
				msgs[1] = Print.capitalize(attack.attacker.getName())+"'s skilled "+subjectHitVerb+" hits you.";
			} else if(d < 60) {
				msgs[0] = "Your careful maneuvering pays off as you "+objectHitVerb+" at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s careful maneuvering pays off as "+subject.getPronoun()+" "+subjectHitVerb+" at your "+hitloc+".";
			} else if(d < 80) {
				msgs[0] = "Your concentrated "+objectHitVerb+" on "+subject.getName()+" delivers a powerful blow.";
				msgs[1] = getOwner().getName()+"'s concentrated "+subjectHitVerb+" on you delivers a powerful blow.";
			} else {
				msgs[0] = "You perfectly execute a powerful "+objectHitVerb+" at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" perfectly executes a powerful "+subjectHitVerb+" at your "+hitloc+".";
			}

			if(attack.critical)
				msgs[0] = "Your well executed maneuvers cause a CRITICAL hit!\n"+msgs[0];

			msgs[0] = msgs[0]+"\n"+dt.attackerGore;
			msgs[1] = msgs[1]+"\n"+dt.targetGore;

			msgs[2] = Print.capitalize(getOwner().getName())+" is in melee with "+subject.getName()+".";
		}

		return msgs;
	}
}
