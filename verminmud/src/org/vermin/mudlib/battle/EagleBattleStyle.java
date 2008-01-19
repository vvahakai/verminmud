package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class EagleBattleStyle extends MartialArtsBattleStyle {

	public EagleBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public EagleBattleStyle() {}

	public String getName() {
		return "eagle";
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(5, PHYS_STR, true),
			STAT(3, PHYS_DEX, true),
			SKILL(5, "iron palm training",true),
			SKILL(5, "martial arts",true),
			SKILL(5, "combat orientation",true),
			SKILL(0, "chi flow",true),
			DICE(6)
		};
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
				msgs[0] = "Your upward knifehand strike connects "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" executes an upward knifehand strike at your "+hitloc+".";
			} else if(d < 20) {
				msgs[0] = "You spin around and whack "+subject.getName()+" with your dokuja-hisho strike.";
				msgs[1] = ""+getOwner().getName()+"'s dokuja-hisho strike sends you reeling.";
			} else if(d < 30) {
				msgs[0] = "You deliver a thrusting koshu-tai kick at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" delivers a thrusting koshu-tai kick at your "+hitloc+".";
			} else if(d < 40) {
				msgs[0] = "You bash "+subject.getName()+" with a spinning backhand wing strike.";
				msgs[1] = ""+getOwner().getName()+"'s spinning backhand wing strike bashes you around.";
			} else if(d < 50) {
				msgs[0] = "You leap forward and execute a so-hien double kick at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" jumps at you and executes a so-hien double kick connecting your "+hitloc+".";
			} else if(d < 60) {
				msgs[0] = "Your fierce dokuja-koto strike almost pierces "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+"'s fierce dokuja-koto strike damages your "+hitloc+".";
			} else if(d < 70) {
				msgs[0] = "You chamber your hands and deliver a double-handed soja-tensho fingertip strike.";
				msgs[1] = ""+getOwner().getName()+" executes a double-handed soja-tensho fingertip strike at you.";
			} else if(d < 80) {
				msgs[0] = "You jump into the air and wham "+subject.getName()+" to the "+hitloc+" with a dokuja-hiten strike.";
				msgs[1] = ""+getOwner().getName()+" leaps into the air and lands a dokuja-hiten strike on your "+hitloc+".";
			} else if(d < 90) {
				msgs[0] = "Your lightning-fast san-to-ja fingertip strike nearly paralyzes "+subject.getName()+".";
				msgs[1] = "You blink as "+getOwner().getName()+" executes a san-to-ja fingertip strike wounding you badly.";
			} else {
				msgs[0] = "Your brutal toku-so-soku backflip double kick utterly crushes "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = "You fall down as "+getOwner().getName()+" throws a toku-so-soku backflip double kick crushing your "+hitloc+".";
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
