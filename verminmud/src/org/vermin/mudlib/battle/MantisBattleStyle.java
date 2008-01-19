package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class MantisBattleStyle extends MartialArtsBattleStyle {

	public MantisBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public MantisBattleStyle() {}

	public String getName() {
		return "mantis";
	}
	
	public Effector[] getDamageEffectors() {
		return new Effector[] {

			STAT(9, PHYS_STR, true),
			STAT(7, PHYS_DEX, true),
			SKILL(9, "iron palm training",true),
			SKILL(9, "martial arts",true),
			SKILL(9, "combat orientation",true),
			SKILL(0, "focus strength",true),
			DICE(9)
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
				msgs[0] = "You move near your opponent and bash "+subject.getName()+"'s "+hitloc+" with your knee.";
				msgs[1] = getOwner().getName()+" slides beside you and bashes you to the "+hitloc+" with "+getOwner().getPossessive()+" knee.";
			} else if(d < 20) {
				msgs[0] = "Your merciless praying mantis sweep kick connects with "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" executes a praying mantis sweep kick wounding you mercilessly.";
			} else if(d < 30) {
				msgs[0] = "Your vicious open-handed backhand strike crushes "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" crushes your "+hitloc+" with a vicious backhand strike.";
			} else if(d < 40) {
				msgs[0] = "You slip past your opponent's defence and wham your elbow to the "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" whams you with "+getOwner().getPossessive()+" elbow damaging your "+hitloc+".";
			} else if(d < 50) {
				msgs[0] = "Your rising praying mantis strike maims "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+"'s rising praying mantis strike connects with your "+hitloc+".";
			} else if(d < 60) {
				msgs[0] = "Your praying mantis snap kick nearly dislocates "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = "You nearly drop to the ground as "+getOwner().getName()+" kicks your "+hitloc+".";
			} else if(d < 70) {
				msgs[0] = "You crouch from the "+subject.getName()+"'s reach and slam a backhand fist to the "+hitloc+".";
				msgs[1] = getOwner().getName()+" crouches away from your reach and slams a backhand fist to your "+hitloc+".";
			} else if(d < 80) {
				msgs[0] = "You swing a reversed haymaker blow at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" swings your "+hitloc+" hard with a reversed haymaker blow.";
			} else if(d < 90) {
				msgs[0] = "Your praying mantis hook blow catches "+subject.getName()+" to the "+hitloc+".";
				msgs[1] = "You reel back as the "+getOwner().getName()+" whams your "+hitloc+" with a mantis hook blow.";
			} else {
				msgs[0] = "You place a praying mantis heel kick at "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" knocks the wind out of you with a brutal mantis heel kick.";
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
