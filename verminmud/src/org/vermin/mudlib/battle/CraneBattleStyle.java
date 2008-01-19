package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class CraneBattleStyle extends MartialArtsBattleStyle {

	public CraneBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public CraneBattleStyle() {}

	public String getName() {
		return "crane";
	}
	
	public boolean canTumbleSpell() {
		return Dice.random() < (owner.getSkill("tumble")+owner.getSkill("flowing movement"))/10;
	}

	public boolean canTumbleSkill() {
		return Dice.random() < (owner.getSkill("anticipate moves")+owner.getSkill("flowing movement"))/10;
	}

	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(2, PHYS_STR, true),
			STAT(6, PHYS_DEX, true),
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
				msgs[0] = "Your upward palm force thrust connects "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" connects your "+hitloc+" with an upward palm force thrust.";
			} else if(d < 20) {
				msgs[0] = "You neatly step sideways and hit "+subject.getName()+" with a spinning backhand strike.";
				msgs[1] = ""+getOwner().getName()+" sidesteps and hits you with a spinning backhand strike.";
			} else if(d < 30) {
				msgs[0] = "You leap forward and deliver a shashu-ki-kyaku kick at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" jumps forward and delivers a shashu-ki-kyaku kick at your "+hitloc+".";
			} else if(d < 40) {
				msgs[0] = "Your whipping uryu-banda whirlwind strike whacks "+subject.getName()+" brutally.";
				msgs[1] = ""+getOwner().getName()+"'s whipping uryu-banda whirlwind strike whacks you brutally.";
			} else if(d < 50) {
				msgs[0] = "You bash "+subject.getName()+" to the ground with your saho-soheki-sho blow.";
				msgs[1] = ""+getOwner().getName()+"'s saho-soheki-sho blow bashes you to the ground.";
			} else if(d < 60) {
				msgs[0] = "You slam your opponent around with a devastating soheki-sho double wing strike.";
				msgs[1] = ""+getOwner().getName()+" slams you around with a devastating soheki-sho double wing strike.";
			} else if(d < 70) {
				msgs[0] = "Your smooth rigo-tai double kick catches "+subject.getName()+" viciously to the "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+"'s rigo-tai double kick catches you to the "+hitloc+".";
			} else if(d < 80) {
				msgs[0] = "You nearly pierce "+subject.getName()+" with your furious spearhand strike to the "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+"'s furious spearhand strike almost pierces your "+hitloc+".";
			} else if(d < 90) {
				msgs[0] = "You whirl around and plant a crushing reversed roundhouse kick to "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" whirls around and throws a reversed roundhouse kick at your "+hitloc+".";
			} else {
				msgs[0] = "Your fierce crane wing backhand slam almost decimates "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = ""+getOwner().getName()+" nearly decimates your "+hitloc+" with a crane wing backhand slam.";
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
