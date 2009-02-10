package org.vermin.mudlib.battle;

import org.vermin.mudlib.*;

import java.util.Vector;
import java.util.Iterator;

import static org.vermin.mudlib.LivingProperty.*;
import org.vermin.util.Print;

public abstract class GenericBattleStyle implements BattleStyle {

	protected Living owner;

	protected transient Living target;
	
	protected transient float[] wieldedAccumulatedSpeeds;
	protected transient Wieldable[] previousWeapons; 

	protected transient Order order;
	
   public GenericBattleStyle(Living owner) {
		this.owner = owner;
	}

	// 0-arg constructor serialization
	public GenericBattleStyle() {}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public void setOwner(Living owner) {
		this.owner = owner;
	}
	public void setTarget(Living target) {
		this.target = target;
	}

	public Living getOwner() {
		return owner;
	}

   /**
    * This method is called in every attack skill use.
    * It should return whether this style is usable in the
    * current status of the Living.
    * (eg. correct weapons wielded etc.)
    * 
    * @return Can this style be used.
    */
	public boolean tryUse() {
		return true;
	}

	/**
	 * Returns an array of effectors that affect
	 * ability to hit targets.
	 *
	 * @return an array of <code>Effector</code>s
	 */
	public abstract Effector[] getHitEffectors();

	/**
	 * Returns an array of effectors that affect
	 * melee damage.
	 *
	 * @return an array of <code>Effector</code>s
	 */
	public abstract Effector[] getDamageEffectors();

	/**
	 * Returns an array of effectors that affect
	 * the attacker's hit chance.
	 *
	 * @return an array of <code>Effector</code>s
	 */
	public abstract Effector[] getDefensiveHitEffectors();

	/**
	 * Handle a single attack message.
	 *
	 * @param attack the received attack message
	 * @return the appropriate reaction
	 */
	public abstract Reaction handleAttack(Attack attack);

	/**
	 * Check for critical hit and return damage factor.
	 *
	 * @return damage factor on successfull critical, or null
	 */
	public Double calculateCritical() {
		int crit = owner.getPhysicalDexterity();
		crit += owner.getSkill("critical");
		crit /= 30; // tune point

		if(Dice.random() < crit)
			return 3.0;
		else
			return null;
	}

	public int calculateHitModifier() {

		Effector[] de = getDefensiveHitEffectors();

		int totalEffectiveness = 0;
		
		// calculate total effectiveness of all damage effectors
		for(int z=0; z<de.length; z++)
			totalEffectiveness += de[z].getEffectiveness();
		
		// sum up the effects of all effectors in proportion
		int percent = 0;
		for(int z=0; z<de.length; z++) 
			percent += de[z].getEffectiveness() * de[z].calculateEffect(owner, null, null, Damage.Type.PHYSICAL) / totalEffectiveness;

		// These are tunes to make combat more interesting
		percent -= Math.max(0, 50 - owner.getPhysicalDexterity());
		percent -= Math.max(-25, Math.min(25, owner.getSize()-50));
		
		return percent;
	}

	/**
	 * This is called when someone attacks the
	 * owner of this battle style.
	 *
	 * NOTE: You can't override this method.
	 * Override <code>handleAttack</code> method instead.
	 *
	 * @param attacks an array of attacks
	 * @return reactions to the attacks
	 */
	public final Message[] accept(Message[] attacks) {
		
		//owner.notice("attacks: "+attacks);
		//owner.notice("attacks[0].attacker: "+ ((Attack)attacks[0]).attacker);
		
		owner.getBattleGroup().addHostileGroup( ((Attack) attacks[0]).attacker.getBattleGroup() );
		
		Reaction[] reactions = new Reaction[attacks.length];
		for(int i=0; i<attacks.length; i++) {
			Attack a = (Attack) attacks[i];
			Reaction reaction = handleAttack(a);
			reactions[i] = reaction;
		}

		return reactions;
	}

	/**
	 * Use this battle style.
	 * This is meant to be called on every battle tick.
	 *
	 * An attack is possible if a combatant opponent is found in the room and
	 * the owner is combatant (in a party position where attacking is possible).
	 *
	 * @return true if an attack was possible, false otherwise
	 */
	public final boolean use() {

		// if owner has NO_BATTLE property, don't attack
		if(owner.provides(NO_BATTLE))
			return true; // return true in order to keep on tickin'		
		
		Order ord = order;
		
		// execute standing orders, if any
		if(order != null) {
			boolean orderCompleted = order.execute(owner);
			if(orderCompleted)
				order = null;
		}
		
		Living subject = null;
		Message[] attackMessages = null;
		
		if(ord != null) {
			subject = ord.getTarget();
			attackMessages = ord.getAttackMessages(subject);			
			if(attackMessages == null || attackMessages.length == 0)
				return true;
		} else {
			// if owner is not combatant, don't attack
			if(!owner.getBattleGroup().isCombatant(owner.getRoom(), owner))
				return false;

			subject = getOpponent();
			if(subject == null) {
				return false;
			}

			// if opponent is found, it must be combatant
			attackMessages = generateAttackMessages(subject);
		}
		
		setTarget(subject);		
		
		// If no attacks succeeded, just return
		if(attackMessages == null || attackMessages.length == 0)
			return true;
		
		// Relay the messages to the opponents battlestyle
		Message[] reactions = subject.getBattleStyle().accept(attackMessages);
		
		for(int i=0; i<reactions.length; i++) {
			
			Attack attack = (Attack) attackMessages[i]; // the attack that caused this reaction
			Reaction reaction = (Reaction) reactions[i];
			
			handleReaction(attack, reaction, subject);

			if(subject.isDead())
				break;
		}

		return true;
	}
	
	/**
	 * Generate 3 hit messages for the given attack, reaction and target.
	 * Returns an array of messages, where the first one is for the actor,
	 * the second for the target and the third for spectators.
	 *
	 * @param attack the attack message
	 * @param reaction the reaction to the attack
	 * @param subject target of the attack
	 * @return an array of messages
	 */
	protected String[] generateAttackMessages(Attack attack, Reaction reaction, Living subject) {

		String[] msgs = new String[3];
		
		if(attack instanceof FailedAttack) {
			msgs[0] = "You miss "+subject.getName()+" with your "+attack.weapon.getName()+".";
			msgs[1] = Print.capitalize(owner.getName())+" misses you.";
			msgs[2] = Print.capitalize(owner.getName())+" misses "+subject.getName()+".";
		} else if(reaction instanceof DamageTaken) {
			DamageTaken dt = (DamageTaken) reaction;
			
			if(attack instanceof ProjectileAttack) {
				msgs[0] = "The "+attack.weapon.getName()+" you just threw hits "+subject.getName();
				msgs[1] = "The "+attack.weapon.getName()+" just thrown by "+attack.attacker.getName()+" hits you.";
				msgs[2] = attack.attacker.getName()+"'s projectile hits "+subject.getName()+".";
			} else {
				msgs[0] = (attack.critical?"You score a CRITICAL hit.\n":"")+"You "+attack.weapon.getObjectHitVerb(dt.mainDamage.type)+" "+subject.getName()+" with your "+attack.weapon.getName()+".\n"+dt.attackerGore;
				msgs[1] = Print.capitalize(attack.attacker.getName())+" "+attack.weapon.getSubjectHitVerb(dt.mainDamage.type)+" you with "+attack.attacker.getPossessive()+" "+attack.weapon.getName()+".\n"+dt.targetGore;
				msgs[2] = Print.capitalize(attack.attacker.getName())+" hits "+subject.getName()+".";
			}
		}
		return msgs;
	}
	
	private void handleReaction(Attack attack, Reaction reaction, Living subject) {
		
		Vector spectMessage = new Vector();

		spectMessage.add(Print.capitalize(owner.getName()) + " attacks " + subject.getName()+".");

		int totalDamage = 0;

		String[] msgs = generateAttackMessages(attack, reaction, subject);

		if(attack instanceof FailedAttack) {
			owner.notice(msgs[0]);
			subject.notice(msgs[1]);
			noticeOthers(msgs[2], owner, subject);
		}
		
		if(reaction != null) {
			if(reaction instanceof DamageTaken) {
				DamageTaken dt = (DamageTaken) reaction;
				dt.attackerMessage = msgs[0];
				dt.targetMessage = msgs[1];
				dt.spectatorMessage = msgs[2];
				for(int d=0; d<dt.damage.length; d++) {
					
					//if(subject.subHp(owner, dt.damage[d].damage)) // on death, break
					//	break;
					if(!dt.damage[d].type.isAffliction())
						totalDamage += dt.damage[d].damage;
				}
			}
				
			owner.notice(reaction.attackerMessage);
			subject.notice(reaction.targetMessage);
			noticeOthers(reaction.spectatorMessage, owner, subject);
		}
		
		if(owner instanceof Player) {
			((Player) owner).getClientOutput().battle().attack(attack, reaction, subject);
		}
		if(subject instanceof Player) {
			((Player) subject).getClientOutput().battle().defend(attack, reaction, subject);
		}
		
		if(reaction != null) {
			if(reaction.counterAttack != null)
				handleCounterAttack(subject, reaction.counterAttack);
	
			if(reaction instanceof DamageTaken) {
				DamageTaken dt = (DamageTaken) reaction;
				for(Damage d : dt.damage) {
					if(d.type.isAffliction())
						subject.addAffliction(createAffliction(d.type.getAfflictionType(), d.damage));
					else
						subject.subHp(d, attack.hitLocation, owner);
				}
			}
		}
	}

	double calculateHit(Living subject, Wieldable weapon) {
		// ### 1. Calculate hit chance ###
		double totalEff = 0d;
		Effector[] he = (Effector[]) getHitEffectors();
			
		// calculate total effectiveness of hit effectors
		for(int z=0; z<he.length; z++) 
			totalEff += he[z].getEffectiveness();
			
		// sum up the effects of all effectors in proportion
		double hit = 0.0d;
		for(int z=0; z<he.length; z++)
			hit += (double) he[z].getEffectiveness() * (double) he[z].calculateEffect(owner, subject, weapon, Damage.Type.PHYSICAL) / totalEff;

		// scale down hit with defender's defensive modifier
		hit = (100-subject.getBattleStyle().calculateHitModifier()) * hit / 100;

		return globalTuneHit(hit);
	}

	// GLOBAL TUNE POINT
	private double globalTuneHit(double original) {
		return original * 3.2f;
	}
	
	// GLOBAL TUNE POINT
	private int globalTuneDamage(int original) {
		return original/2;
	}


	private Message[] generateAttackMessages(Living subject) {
		
		Vector messages = new Vector();

		Wieldable[] wielded = owner.getWieldedItems(true);
		Wieldable weapon;
		Damage[] dmg;
		
		boolean[] handled = new boolean[wielded.length];
		
		boolean limb = false;
		for(int i=0; i<wielded.length; i++) {
			
			weapon = wielded[i];
			
			if(handled[i])
				continue;
		        
			//System.out.println("LIMB "+i);
			/* If the same weapon is in more than one limb, mark those Wieldables as handled */
			for(int j=0; j<wielded.length; j++) {
				if(wielded[j] == weapon) {
					handled[j] = true;
				}
			}
			
			
			dmg = weapon.getHitDamage(subject);
			
			if(wieldedAccumulatedSpeeds == null) {
				wieldedAccumulatedSpeeds = new float[wielded.length];
			}
			
			float weaponSpeed;	
			int finalWeaponSpeed;
		
			weaponSpeed = calculateWeaponSpeed(weapon);

			if(previousWeapons != null && previousWeapons[i] == weapon) {
				finalWeaponSpeed = (int) (weaponSpeed+wieldedAccumulatedSpeeds[i]);
				wieldedAccumulatedSpeeds[i] = (weaponSpeed+wieldedAccumulatedSpeeds[i]) - finalWeaponSpeed;
			}
			else {
				finalWeaponSpeed = (int) weaponSpeed;
				wieldedAccumulatedSpeeds[i] = weaponSpeed - finalWeaponSpeed;				
			}
			
			
			/* Calculate hit success */

			int hits=0;
			while(hits < finalWeaponSpeed || (hits >= finalWeaponSpeed && canHitAgain(hits-(int) weaponSpeed+1, subject)))
			{
				hits++;
				//System.out.println("HITS: "+hits);
				double hitDice = Dice.random();
				double hit = calculateHit(subject, weapon);
				
				if(hitDice >= hit) { 
					/* Hit MISSED */
					FailedAttack fa = new FailedAttack();
					fa.attacker = owner;
					fa.weapon = weapon;
					fa.hitLocation = Dice.random();
					messages.add(fa);

					if(hits == 1) // 1st hit must succeed in order to have bonus hits 
						break;

				} else {
			
					/* Hit was successfull */
					
					// Calculate hit location according to hit succesfullnes
					int hitLoc = (100-(int) Math.min(hit, 100)) + Dice.random(40)-25;
					if(hitLoc < 0) hitLoc = 0;
					if(hitLoc > 100) hitLoc = 100;
				
					/* 2. Calculate potential damage */
					Damage[] potential = new Damage[dmg.length];
					int potentialSum = 0;

					boolean critical = false;
					Double criticalFactor = calculateCritical();
					if(criticalFactor != null)
						critical = true;
					
					for(int k = 0; k < dmg.length; k++) {
					
						potential[k] = new Damage();
					
						if(dmg[k].type == Damage.Type.CRUSHING ||
							dmg[k].type == Damage.Type.PIERCING ||
							dmg[k].type == Damage.Type.SLASHING ||
							dmg[k].type == Damage.Type.CHOPPING ||
							dmg[k].type == Damage.Type.STUN) {
						
							float damage = (float) 0.0;
							Effector[] de = (Effector[]) getDamageEffectors();
							int totalEffectiveness = 0;

							// calculate total effectiveness of all damage effectors
							for(int z=0; z<de.length; z++)
								totalEffectiveness += de[z].getEffectiveness();

							for(int z=0; z<de.length; z++) {
								if(de[z].getEffectiveness() > 0) {
									// sum up the effects of all effectors in proportion
									damage += de[z].getEffectiveness() * de[z].calculateEffect(owner, subject, weapon, dmg[k].type) / totalEffectiveness;
								} else {
									// add bonus effects (where effectiveness is zero)
									damage += de[z].calculateEffect(owner, subject, weapon, dmg[k].type);
								}
							}

							potential[k].damage = globalTuneDamage((int) damage);
							potential[k].type = dmg[k].type;

						} else {
							potential[k].damage = globalTuneDamage(dmg[k].damage * (80 + Dice.random(40)) / 100);
							potential[k].type = dmg[k].type;
						}

						// account for critical hits
						if(critical)
							potential[k].damage = (int) (potential[k].damage * criticalFactor);

						potentialSum += potential[k].damage;
					}

					Attack a = new Attack();
					a.critical = critical;
					a.attacker = owner;
					a.weapon = weapon;
					a.damage = potential;
					a.hitLocation = hitLoc;

					messages.add(a);
				}
			}
		}
		// remember the weapons used this round for comparison next round
		previousWeapons = wielded;
		
		return (Message[]) messages.toArray(new Message[messages.size()]);
	}

	public float getWeaponSpeedWeightModifier(Wieldable weapon) {
		int weaponWeight = weapon.getSize()*weapon.getMaterial().getWeight();
		float weightScale =  (float) weaponWeight / (float) (owner.getPhysicalStrength() * 100);
		return weightScale;
	}
	
	public float getWeaponSpeedSizeModifier(Wieldable weapon) {
		return (float) owner.getSize()*15 / (float) weapon.getSize();
	}
	
	public float calculateWeaponSpeed(Wieldable weapon) {
		
		if(weapon.getWeaponType() == WeaponType.NONE)
			return 1;
		
		float weaponSpeed = weapon.getSpeed();
		
		float sizeScale = getWeaponSpeedSizeModifier(weapon);
		float weightScale = getWeaponSpeedWeightModifier(weapon);
		
		if(sizeScale > 1.0) {
			weaponSpeed += (weaponSpeed * owner.getPhysicalDexterity() / 100 
							 * sizeScale) / 2;
		}

		if(weightScale > 1.0) {
			weaponSpeed = (weaponSpeed / weightScale); 
		}
		
		if(weaponSpeed > 2)
			weaponSpeed = 2;
		
		if(weaponSpeed < 0.2)
			weaponSpeed = 0.2f;
		
		return weaponSpeed;
	}

	
	/**
	 * Check if another hit is possible.
	 *
	 * @param hits number of hits so far
	 * @param target the object of aggression
	 * @return true, if another hit is possible, false otherwise
	 */
	protected boolean canHitAgain(int hits, Living target) {
		return false;
	}

	/**
	 * Check if the owner succeeds in tumbling a physical attack.
	 *
	 * @return true if tumble succeeds, false otherwise
	 */
	public boolean canTumbleSkill() {
		return false;
	}

	/**
	 * Check if the owner succeeds in tumbling a magical attack.
	 *
	 * @return true if tumble succeeds, false otherwise
	 */
	public boolean canTumbleSpell() {
		return Dice.random() < owner.getSkill("tumble")/10;
	}

	/**
	 * Get message for tumbling a spell.
	 */
	public String getAttackerTumbleSpellMessage() {
		return owner.getName()+" tumbles the spell.";
	}
	/**
	 * Get message for tumbling a spell.
	 */
	public String getOwnerTumbleSpellMessage() {
		return "You tumble the spell.";
	}
	/**
	 * Get message for tumbling a skill.
	 */
	public String getAttackerTumbleSkillMessage() {
		return owner.getName()+" tumbles the attack.";
	}
	/**
	 * Get message for tumbling a skill.
	 */
	public String getOwnerTumbleSkillMessage() { 
		return "You tumble the attack.";
	}

	protected final Living getOpponent() {
		BattleGroup grp = owner.getBattleGroup();
		
		return target != null ? 
			grp.getOpponent(owner.getRoom(), target.getName()) :
			grp.getOpponent(owner.getRoom());
	}
	
	protected void handleCounterAttack(Living subject, Attack attack) {
		Damage[] dmg = attack.damage;
		for(int i=0; i<dmg.length; i++) {
			owner.subHp(dmg[i], subject);
		}
	}

	protected void noticeOthers(String msg, Living excl1, Living excl2) {
		Iterator en = excl1.getRoom().findByType(Types.LIVING);
		while(en.hasNext()) {
			Living l = (Living) en.next();
			if(l != excl1 && l != excl2)
				l.notice(msg);
		}
	}

	protected DamageTaken takeDamage(Attack attack) {
		
		DamageTaken taken = new DamageTaken();
		taken.damage = new Damage[attack.damage.length];

		int total=0;
		int maxDamageIndex = 0; //Etsit��n t�h�n se kovimman damagen tehneen tyypin indeksi ja tehd��n messut sen perusteella.
		int maxDamage = 0;
		for(int i=0; i<attack.damage.length; i++) {
			taken.damage[i] = new Damage();
			taken.damage[i].damage = 
				attack.damage[i].damage * (100 - owner.getResistance(attack.damage[i].type)) / 100;
			taken.damage[i].type = attack.damage[i].type;
			
			total += taken.damage[i].damage;

			if(!taken.damage[i].type.isAffliction() && 
				(taken.damage[i].damage > maxDamage || taken.mainDamage == null)) {
				maxDamage = taken.damage[i].damage;
				taken.mainDamage = taken.damage[i];
			}
		}

		// FIXME: messaget tehd��n l�hteneen damagen (joka ei ole Affliction) perusteella, ei DamageTaken arvojen perusteella,
		//        eli resistancet / muut ei vaikuta :/
		String[] msgs = getDamageMessages(owner, attack, attack.hitLocation);
               
                
		taken.attackerGore = msgs[0];
		taken.targetGore = msgs[1];
                
		return taken;
	}
	
	private String[] getDamageMessages(Living who, Attack attack, int hitloc) {
		Race race = who.getRace();
		return new String[] {
			race.getAttackerGoreMessage(who, attack, hitloc),
			race.getSubjectGoreMessage(who, attack, hitloc)
		};
	}

	public Affliction createAffliction(Affliction.Type type, int amount) {
		switch(type) {
		  case STUN:
			  return new StunAffliction(owner, amount);
		  case POISON:
		  	  return new PoisonAffliction(owner, amount);
		  default:
			  throw new UnsupportedOperationException("FIXME: Implement more afflictions.");
		}
	}

	/* Convenience functions that return effectors */

	public static Effector STAT(final int e, final Stat stat, final boolean mods) {
		return new Effector() {
				public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
					return actor.getStat(stat,mods);
				}
				public int getEffectiveness() {
					return e;
				}
			};
	}

	public static Effector SKILL(final int e, final String skill, final boolean mods) {
		return new Effector() {
				public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
					return actor.getSkill(skill,mods);
				}
				public int getEffectiveness() {
					return e;
				}
			};
	}

	public static Effector WEAPONDAM(final int e) {
		return new Effector() {
				public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
					Damage[] dmgs = weapon.getHitDamage(target);
					int amount = 0;
					for(int i=0;i<dmgs.length;i++)
						if(dmgs[i].type == dt)
							amount += dmgs[i].damage;
					return amount;
				}
				public int getEffectiveness() {
					return e;
				}
			};
	}
	
	
	public static Effector DICE(final int e) {
		return new Effector() {
				public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
					return Dice.random();
				}
				public int getEffectiveness() {
					return e;
				}
			};
	}

	/**
	 * Convenience method to scale another effector with a given factor.
	 */
	public static Effector SCALE(final double factor, final Effector old) {
		return new Effector() {
				public int calculateEffect(Living actor, Living target, Wieldable weapon, Damage.Type dt) {
					return (int) (factor * old.calculateEffect(actor, target, weapon, dt));
				}
				public int getEffectiveness() {
					return old.getEffectiveness();
				}
			};
	}

	public void onBattleStop() {
		wieldedAccumulatedSpeeds = null;
	}
	
	public String describeWeaponProficiency() {
		StringBuffer desc = new StringBuffer();

		Wieldable weapons [] = owner.getWieldedItems(false);
		for(int i=0;i<weapons.length;i++) {
			if(weapons[i] == null) continue;
			float speed = calculateWeaponSpeed(weapons[i]);
			if(speed < 0.5) {
				desc.append("You are very slow with your " +weapons[i].getDescription()+".\n");			
			}
			else if(speed < 0.75) {
				desc.append("You are quite slow with your " +weapons[i].getDescription()+".\n");
			}
			else if(speed < 0.90) {
				desc.append("You are slightly slow with " +weapons[i].getDescription()+".\n");
			}
			else if(speed < 1.10) {
				desc.append("You use your " +weapons[i].getDescription()+ " at an average speed.\n");
			}
			else if(speed < 1.25) {
				desc.append("You are slightly fast with " +weapons[i].getDescription()+".\n");
			}
			else if(speed < 1.50) {
				desc.append("You are quite fast with your " +weapons[i].getDescription()+".\n");
			}
			else {
				desc.append("You are very fast with your " +weapons[i].getDescription()+".\n");
			}
			if(getWeaponSpeedSizeModifier(weapons[i]) < 0.5) {
				desc.append("This weapon is way too large for you to wield properly.\n");
			} else if(getWeaponSpeedSizeModifier(weapons[i]) < 0.8) {
				desc.append("This weapon slightly too large for you to wield properly.\n");
			}
			if(getWeaponSpeedWeightModifier(weapons[i]) < 0.5) {
				desc.append("This weapon is way too heavy for you to wield properly.\n");
			} else if(getWeaponSpeedWeightModifier(weapons[i]) < 0.8) {
				desc.append("This weapon slightly too heavy for you to wield properly.\n");
			}
		}
		

		return desc.toString();
	}
	
}
