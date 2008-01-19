package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.WeaponType;

import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.World;
import org.vermin.world.skills.Dodge;
import org.vermin.world.skills.UnarmedParry;

public class MartialArtsBattleStyle extends GenericBattleStyle {

	public MartialArtsBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public MartialArtsBattleStyle() {}

	public String getName() {
		return "martial arts";
	}

	public Effector[] getHitEffectors() {
		return new Effector[] {
			SKILL(5, "martial arts",true),
			SKILL(3, "combat orientation",true),
			STAT(5, PHYS_DEX, true)
		};
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {
			STAT(1, PHYS_STR, true),
			STAT(1, PHYS_DEX, true),
			//TODO: add WEAPONDAM effector (if wanted)
			SKILL(1, "iron palm training",true),
			SKILL(1, "martial arts",true),
			SKILL(1, "combat orientation",true),
			DICE(1)
		};
	}
	
	public Effector[] getDefensiveHitEffectors() {
		return new Effector[0];
	}

	public boolean canHitAgain(int hits, Living target) {
		if(hits == 1 && owner.checkSkill("sticky hands") > 0) {
			if(Dice.random(4) >= 3) {
				World.log("STICKY");
				return true;
			}
		} else if(hits == 2 && owner.checkSkill("rolling punches") > 0) {
			if(Dice.random(5) >= 4) {
				World.log("ROLLING");
				return true;

			}
		}

		return false;
	}

	public Reaction handleAttack(Attack attack) {
		if(attack instanceof FailedAttack) 
			return null;
		
		Message reaction = null;

	react: {
			if(Dodge.mayDodge(owner)) {
				double factor = 1.0 + owner.getSkill("combat orientation")/500;
				reaction = Dodge.dodge(owner, attack, factor);
				if(reaction != null)
					break react;
			}
			
			if(UnarmedParry.mayParry(owner)) {
				double factor = 1.0 + owner.getSkill("combat orientation")/500;
				reaction = UnarmedParry.parry(owner, attack, factor);
				if(reaction != null)
					break react;
			}

			reaction = takeDamage(attack);
		}

		return (Reaction) reaction;
	}

   /**
	 * Check that the user is not wielding any weapons
	 * (other than natural weapons).
    * 
    * @return Can this style be used.
    */
   public boolean tryUse() {
		Wieldable[] wielded = owner.getWieldedItems(true);
		if(wielded == null)
			return true;

		for(int i=0; i<wielded.length; i++) {
			if(wielded[i].getWeaponType() != WeaponType.NONE) {
				owner.notice("&3;You revert to uneducated fighting.");
				return false;
			}
		}
		return true;
	}

	protected String[] generateAttackMessages(Attack attack, Reaction reaction, Living subject) {

		String[] msgs = new String[3];

		if(attack instanceof FailedAttack) {

			msgs[0] = "Your kungfu is not strong enough to cause any effect on "+subject.getName()+".";
			msgs[1] = getOwner().getName()+"'s attempted martial arts move against you has failed.";
			msgs[2] = getOwner().getName()+"'s attempted martial arts move against "+subject.getName()+" has failed.";

		} else if(reaction instanceof DamageTaken) {
			
			DamageTaken dt = (DamageTaken) reaction;

			int d = Dice.random();

			String hitloc = subject.getRace().getHitLocation(attack.hitLocation);

			if(d < 10) {
				msgs[0] = "Your devastating fujin strike knocks "+subject.getName()+" down hard.";
				msgs[1] = getOwner().getName()+"'s devastating fujin strike knocks you down.";
			} else if(d < 20) {
				msgs[0] = "Your enthusiastic front kick smashes into "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s front kick smashes into your "+hitloc+".";
			} else if(d < 30) {
				msgs[0] = "You spin around and place a kakato-nagi elbow strike deep into "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" throws a kakato-nagi elbow strike to your "+hitloc+".";
			} else if(d < 40) {
				msgs[0] = "You turn and throw a roundhouse kick that whams into "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s ruthless roundhouse kick whams into your "+hitloc+".";
			} else if(d < 50) {
				msgs[0] = "You swing a brutal backhand strike to the "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" swings a brutal backhand strike at your "+hitloc+".";
			} else if(d < 60) {
				msgs[0] = "You execute a perfect tobi-ushiro-geri jump kick bashing "+subject.getName()+" around the place.";
				msgs[1] = getOwner().getName()+"'s tobi-ushiro-geri jump kick bashes you around the place.";
			} else if(d < 70) {
				msgs[0] = "You chamber your fist and deliver a fujo-fudin blow that sinks into "+subject.getName()+".";
				msgs[1] = getOwner().getName()+" delivers a fujo-fudin blow into your "+hitloc+".";
			} else if(d < 80) {
				msgs[0] = "You deliver a lightning-fast side kick that connects with "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s quick side kick connects with your "+hitloc+".";
			} else if(d < 90) {
				msgs[0] = "Your double-handed morote-zuki blow hits "+subject.getName()+"'s "+hitloc+" leaving "+subject.getName()+" breathless.";
				msgs[1] = getOwner().getName()+" hits your "+hitloc+" with a double-handed morote-zuki blow.";
			} else {
				msgs[0] = "You execute an awesome cartwheel kick that hits "+subject.getName()+"'s "+hitloc+" with force.";
				msgs[1] = getOwner().getName()+" executes an awesome cartwheel kick that connects your "+hitloc+" with force.";
			}

			if(attack.critical)
				msgs[0] = "You score a CRITICAL hit!\n"+msgs[0];

			msgs[0] = msgs[0]+"\n"+dt.attackerGore;
			msgs[1] = msgs[1]+"\n"+dt.targetGore;

			msgs[2] = getOwner().getName()+" performs a martial arts move on "+subject.getName()+".";
		}

		return msgs;
	}
}
