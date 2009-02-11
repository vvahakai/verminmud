/*
 * Created on 29.1.2005
 */
package org.vermin.mudlib.battle;

import java.util.ArrayList;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.util.Print;

/**
 * This implementation of <code>BattleStyle</code>
 * is intended for four-legged beast monsters,
 * such as bears, bulls and whatnot.
 * 
 * Features include: missed combat rounds because
 * of animalistic behaviour, custom dodge messages
 * and a chance of BERSERK FURY when user is abused
 * enough.
 * 
 * @author Jaakko Pohjamo
 */
public class BestialBattleStyle extends DefaultBattleStyle {
	
	private boolean isBerserk = false;
	
	public BestialBattleStyle() {
		
	}
	
    public BestialBattleStyle(Living owner) {
		super(owner);
	}
    
	public Reaction handleAttack(Attack attack) {

		if(attack instanceof FailedAttack)
			return null;

		Reaction reaction = null;

		if(!isBerserk) { // no dodging when berserking
			if(owner.checkSkill("dodge") > 0) {
				reaction =  new AttackPrevented();
				switch(Dice.random(3)) {
				case 1:
					((AttackPrevented) reaction).attackerMessage = Print.capitalize(owner.getName())+" avoids your strike with animal cunning.";
			        ((AttackPrevented) reaction).targetMessage = "";
			        ((AttackPrevented) reaction).spectatorMessage = Print.capitalize(owner.getName())+" avoids "+attack.attacker.getName()+"'s strike with animal cunning.";
			        break;
				case 2:
					((AttackPrevented) reaction).attackerMessage = Print.capitalize(owner.getName())+"'s agile body evades your blow.";
			        ((AttackPrevented) reaction).targetMessage = "";
			        ((AttackPrevented) reaction).spectatorMessage = Print.capitalize(owner.getName())+"'s agile body evades "+attack.attacker.getName()+"'s blow.";
					break;
				case 3:
					((AttackPrevented) reaction).attackerMessage = Print.capitalize(owner.getName())+" dodges your attack with a snarl.";
			        ((AttackPrevented) reaction).targetMessage = "";
			        ((AttackPrevented) reaction).spectatorMessage = Print.capitalize(owner.getName())+" dodges "+attack.attacker.getName()+"'s attack with a snarl.";
					break;
				}
			}	         
		}

		if(reaction == null) {
			reaction = takeDamage(attack);
		}

		return reaction;
	}

	protected String[] generateAttackMessages(Attack attack, Reaction reaction, Living subject) {
		
		if(attack.attacker.getHp() < attack.attacker.getMaxHp()/3) {
			if(Dice.random(10) == 1 && !isBerserk) {
				isBerserk = true;
				attack.attacker.getRoom().notice(attack.attacker, Print.capitalize(attack.attacker.getName())+" goes into berserk fury!");
			}
		}
		
		String[] msgs = new String[3];

		if(attack instanceof FailedAttack) {

			int msgNum = Dice.random(9);
			String hitloc = subject.getRace().getHitLocation(attack.hitLocation);
			String weapon = attack.weapon.getName();
			String Attacker = Print.capitalize(attack.attacker.getName());
			String attacker = attack.attacker.getName();
			
			switch(msgNum) {
				case 1:
					msgs[0] = ""; // no message needed for monsters
					msgs[1] = Attacker+" makes a cautious lunge at you, but misses.";
				break;
				case 2:
					msgs[0] = "";
					msgs[1] = "You avoid "+attacker+"'s swinging "+weapon+".";
				break;
				case 3:
					msgs[0] = "";
					msgs[1] = Attacker+" swiftly charges at you, but you manage to avoid it by inches.";
				break;
				case 4:
					msgs[0] = "";
					msgs[1] = Attacker+" crouches and growls deeply.";
				break;
				case 5:
					msgs[0] = "";
					msgs[1] = Attacker+" rears and brandishes "+attack.attacker.getPossessive()+" "+weapon+".";
				break;
				case 6:
					msgs[0] = "";
					msgs[1] = Attacker+" suddenly jumps backwards.";
				break;
				case 7:
					msgs[0] = "";
					msgs[1] = Attacker+"'s "+weapon+" almost connects your "+hitloc+".";
				break;
				case 8:
					msgs[0] = "";
					msgs[1] = Attacker+" roars and jumps up and down.";
				break;
				case 9:
					msgs[0] = "";
					msgs[1] = Attacker+" bares "+attack.attacker.getPossessive()+" teeth and hisses.";
				break;
				default: // This should not happen
					msgs[0] = "You fail to beat Quakka with your long stick.";
					msgs[1] = getOwner().getName()+"'s attack on you fails.";
			}
			
			msgs[2] = Attacker+" misses "+subject.getName()+".";
			
		} else if(reaction instanceof DamageTaken) {
			
			DamageTaken dt = (DamageTaken) reaction;

			// random(1 - fighting) + damage percentage of max hp 
			int d = Dice.random(getOwner().getSkill("fighting")+1)+attack.getMaxDamage().damage * 100 / Math.max(getOwner().getMaxHp(),1);

			String hitloc = subject.getRace().getHitLocation(attack.hitLocation);
			String weapon = attack.weapon.getName();
			String attacker = Print.capitalize(attack.attacker.getName());
			String hits = attack.weapon.getSubjectHitVerb(dt.mainDamage.type);
			String hit = attack.weapon.getObjectHitVerb(dt.mainDamage.type);
			String its = attack.attacker.getPossessive();
			
			if(d < 10) {
				msgs[0] = "";
				msgs[1] = attacker+" "+hits+" you with "+its+" "+weapon+".";
			} else if(d < 20) {
				msgs[0] = "";
				msgs[1] = attacker+" swiftly charges at you, connecting "+its+" "+weapon+" with your "+hitloc+".";
			} else if(d < 30) {
				msgs[0] = "";
				msgs[1] = attacker+" strikes at your "+hitloc+" with blinding quickness.";
			} else if(d < 40) {
				msgs[0] = "";
				msgs[1] = attacker+" crouches, then lunges at you sinking "+its+" "+weapon+" into your "+hitloc+".";
			} else if(d < 50) {
				msgs[0] = "";
				msgs[1] = attacker+" hisses and splutters, flailing "+its+" "+weapon+" at your "+hitloc+".";
			} else if(d < 60) {
				msgs[0] = "";
				msgs[1] = attacker+" charges roaring, "+its+" "+weapon+" into your "+hitloc+".";
			} else if(d < 70) {
				msgs[0] = "";
				msgs[1] = attacker+" bares "+its+" teeth and aims a quick "+hit+" at you.";
			} else if(d < 80) {
				msgs[0] = "";
				msgs[1] = attacker+" crouches, then lunges at you sinking "+its+" "+weapon+" into your "+hitloc+".";
			} else if(d < 90) {
				msgs[0] = "";
				msgs[1] = attacker+" attacks you with a quick successsion of "+hits+".";
			} else {
				msgs[0] = "";
				msgs[1] = attacker+"'s "+weapon+" connects your "+hitloc+" with surprising strenght.";
			}

			String berserkMessage = "";
			if(isBerserk && Dice.random(5) == 1) {
				switch(Dice.random(3)) {
				case 1:
					berserkMessage = attacker+" rages and foams!\n";
					break;
				case 2:
					berserkMessage = attacker+" roars in berserk fury!\n";
					break;
				case 3:
					berserkMessage = attacker+" shudders with wild rage!\n";
					break;
				}
			}
			msgs[0] = berserkMessage+msgs[0]+"\n"+dt.attackerGore;
			msgs[1] = berserkMessage+msgs[1]+"\n"+dt.targetGore;
			msgs[2] = berserkMessage+attacker+" is in melee with "+subject.getName()+".";
		}

		return msgs;
	}
	
	/**
	 * Bestial battlestyle gains additional attacks when in berserk.
	 *
	 * @param hits number of hits so far
	 * @param target the object of aggression
	 * @param messages attack messages so far this turn
	 * @return true, if another hit is possible, false otherwise
	 */
	@Override
	protected boolean canHitAgain(int hits, Living target, ArrayList<Message> messages) {
		if(isBerserk && hits == 1) {
			for (Message m : messages) {
				if(!(m instanceof FailedAttack)) {
					return true;
				}
			}
		}
		return false;
	}
}
