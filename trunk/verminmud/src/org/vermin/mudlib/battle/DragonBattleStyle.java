package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class DragonBattleStyle extends MartialArtsBattleStyle {

	public DragonBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public DragonBattleStyle() {}

	public String getName() {
		return "dragon";
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(6, PHYS_STR, true),
			STAT(2, PHYS_DEX, true),
			SKILL(5, "iron palm training",true),
			SKILL(5, "martial arts",true),
			SKILL(5, "combat orientation",true),
			SKILL(0, "focus strength",true),
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
				msgs[0] = "You throw a spinning backhand slam connecting "+subject.getName()+"'s "+hitloc+" hard.";
				msgs[1] = ""+getOwner().getName()+" throws a spinning backhand slam that connects your "+hitloc+".";
			} else if(d < 20) {
				msgs[0] = "You execute a rising dragon backhand blow at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+"'s rising dragon backhand blow hits you to the "+hitloc+".";
			} else if(d < 30) {
				msgs[0] = "You deliver a whipping knifehand strike hitting "+subject.getName()+" to the "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" delivers a whipping knifehand strike to your "+hitloc+".";
			} else if(d < 40) {
				msgs[0] = "Your devastating dragon tail spin kick bashes "+subject.getName()+" to the ground.";
				msgs[1] = ""+getOwner().getName()+" executes a dragon tail spin kick knocking you down.";
			} else if(d < 50) {
				msgs[0] = "You jump into the air and land a flying axe kick on "+subject.getName()+".";
				msgs[1] = ""+getOwner().getName()+" jumps into the air and lands a flying axe kick on you.";
			} else if(d < 60) {
				msgs[0] = "Your fast crouching dragon strike catches "+subject.getName()+" by surprise.";
				msgs[1] = "You are surprised by "+getOwner().getName()+"'s fast crouching dragon strike.";
			} else if(d < 70) {
				msgs[0] = "Your fierce body uppercut nearly knocks "+subject.getName()+" out cold.";
				msgs[1] = ""+getOwner().getName()+"'s fierce body uppercut slams you almost unconscious.";
			} else if(d < 80) {
				msgs[0] = "You leap forward with blinding speed and land a flying dragon kick at "+subject.getName()+".";
				msgs[1] = "Suddenly "+getOwner().getName()+" flies through the air and lands a flying dragon kick on you.";
			} else if(d < 90) {
				msgs[0] = "You deliver a devastating roundhouse kick maiming "+subject.getName()+"'s "+hitloc+" brutally.";
				msgs[1] = ""+getOwner().getName()+" delivers a devastating roundhouse kick at your "+hitloc+".";
			} else {
				msgs[0] = "Your vicious double upper kick nastily connects "+subject.getName()+" to the "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+"'s vicious double upper kick connects your "+hitloc+" nastily.";
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
