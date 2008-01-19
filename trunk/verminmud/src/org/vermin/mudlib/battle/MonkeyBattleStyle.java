package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class MonkeyBattleStyle extends MartialArtsBattleStyle {

	public MonkeyBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public MonkeyBattleStyle() {}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(7, PHYS_STR, true),
			STAT(9, PHYS_DEX, true),
			SKILL(9, "iron palm training",true),
			SKILL(9, "martial arts",true),
			SKILL(9, "combat orientation",true),
			SKILL(0, "chi flow",true),
			DICE(9)
		};
	}
	
	public boolean canTumbleSpell() {
		return Dice.random() < (owner.getSkill("tumble")+owner.getSkill("drunken movement"))/10;
	}

	public boolean canTumbleSkill() {
		return Dice.random() < (owner.getSkill("anticipate moves")+owner.getSkill("drunken movement"))/10;
	}

	public String getName() {
		return "monkey";
	}

	protected String[] generateAttackMessages(Attack attack, Reaction reaction, Living subject) {

		String[] msgs = new String[3];

		if(attack instanceof FailedAttack) {

			msgs[0] = "Your kungfu is not strong enough to cause any effect on "+subject.getName()+".";
			msgs[1] = getOwner().getName()+"'s attempted martial arts move against you has failed.";
			msgs[2] = getOwner().getName()+"'s attempted martial arts move against "+subject.getName()+" has failed.";;

		} else if(reaction instanceof DamageTaken) {
			
			DamageTaken dt = (DamageTaken) reaction;

			int d = Dice.random();

			String hitloc = subject.getRace().getHitLocation(attack.hitLocation);

			if(d < 10) {
				msgs[0] = "You execute a merciless kosotai leg sweep at "+subject.getName()+".";
				msgs[1] = getOwner().getName()+"'s merciless kosotai leg sweep catches you off balance.";
			} else if(d < 20) {
				msgs[0] = "You lean inside "+subject.getName()+"'s defence and throw a vicious koso elbow thrust.";
				msgs[1] = getOwner().getName()+"'s vicious koso elbow thrust breaks through your defence.";
			} else if(d < 30) {
				msgs[0] = "You rise from a half-crouch and slam a koshu palm strike to "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s koshu palm strike wounds your "+hitloc+" badly.";
			} else if(d < 40) {
				msgs[0] = "You spring forward and head-butt "+subject.getName()+" hard to the "+hitloc+".";
				msgs[1] = getOwner().getName()+" utterly surprises you by head-butting your "+hitloc+".";
			} else if(d < 50) {
				msgs[0] = "You deliver a ren-koshutai heel kick to "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" delivers a ren-koshutai heel kick at your "+hitloc+".";
			} else if(d < 60) {
				msgs[0] = "You deliver a ren-chowan double strike at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" throws a ren-chowan double strike at your "+hitloc+".";
			} else if(d < 70) {
				msgs[0] = "You spin around and throw a tohon-kishin backflip kick at "+subject.getName()+".";
				msgs[1] = getOwner().getName()+"'s tohon-kishin backflip kick maims you brutally.";
			} else if(d < 80) {
				msgs[0] = "You slide near "+subject.getName()+" and launch a fierce zenso-tai spinning leg sweep.";
				msgs[1] = getOwner().getName()+" launches a zenso-tai spinning leg sweep tripping you over.";
			} else if(d < 90) {
				msgs[0] = "You swing around and ram a brutal haymaker blow to the "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s brutal haymaker blow connects your "+hitloc+".";
			} else {
				msgs[0] = "You leap forward and execute a senpu-zenku doubleflip kick at "+subject.getName()+".";
				msgs[1] = getOwner().getName()+" jumps forward and executes a senpu-zenku doubleflip kick at you.";
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
