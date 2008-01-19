package org.vermin.mudlib.battle;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.Stat.PHYS_DEX;
import static org.vermin.mudlib.Stat.PHYS_STR;

public class TigerBattleStyle extends MartialArtsBattleStyle {

	public TigerBattleStyle(Living owner) {
		super(owner);
	}

	// 0-arg constructor serialization
	public TigerBattleStyle() {}

	public String getName() {
		return "tiger";
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
				msgs[0] = "You deliver a to-sho palm strike that hits "+subject.getName()+" like a hammer.";
				msgs[1] = getOwner().getName()+" delivers a to-sho palm strike pounding you almost to the ground.";
			} else if(d < 20) {
				msgs[0] = "You wham your knee violently to the "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" rams "+getOwner().getPossessive()+" knee at your "+hitloc+".";
			} else if(d < 30) {
				msgs[0] = "You rush forward and bash "+subject.getName()+" hard with your sen-shippo strike.";
				msgs[1] = getOwner().getName()+" bashes you hard with a sen-shippo strike.";
			} else if(d < 40) {
				msgs[0] = "You ram a fierce oda palm strike into the "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" executes an oda palm strike at your "+hitloc+".";
			} else if(d < 50) {
				msgs[0] = "You swing an ugyu-haito haymaker blow connecting "+subject.getName()+"'s "+hitloc+" hard.";
				msgs[1] = getOwner().getName()+"'s ugyu-haito haymaker blow connects your "+hitloc+" hard.";
			} else if(d < 60) {
				msgs[0] = "You leap into the air and land a han-senpu jump kick at "+subject.getName()+".";
				msgs[1] = getOwner().getName()+" jumps into the air and lands a han-senpu jump kick on you.";
			} else if(d < 70) {
				msgs[0] = "You execute a furious spinning senpu-zenso-tai double kick at "+subject.getName()+".";
				msgs[1] = getOwner().getName()+"'s spinning senpu-zenso-tai double kick damages you badly.";
			} else if(d < 80) {
				msgs[0] = "Your devastating ryukei-kakuo uppercut slams into "+subject.getName()+"'s "+hitloc+".";
				msgs[1] = getOwner().getName()+" throws a ryukei-kakuo uppercut to your "+hitloc+".";
			} else if(d < 90) {
				msgs[0] = "You scream and execute a flying sohi-kyaku double kick hitting "+subject.getName()+" off balance.";
				msgs[1] = getOwner().getName()+"'s flying sohi-kyaku double kick hits you and topples you over.";
			} else {
				msgs[0] = "You chamber your palms and thrust out a lethal byakko-soha double palm strike.";
				msgs[1] = getOwner().getName()+" executes a lethal byakko-soha double palm strike maiming you brutally.";
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
