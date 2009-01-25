package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import static org.vermin.mudlib.LivingProperty.*;

import java.util.Iterator;

/* base class for generated offensive spells */
public class SmiteSkill extends BaseSkill {

	protected String name = "smite";
	protected int rounds = 4;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public String getName() {
		return name;
	}	
	
	public boolean tryUse(Living who, MObject target) {
		Wieldable[] wielded = who.getWieldedItems(false);
		if(wielded == null)
			return false;
		
		boolean haveWeapon = false;
		
		
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}

	public int getTickCount() {
		return rounds;
	}

	protected int getDamage(Living who, Living tgt) {
		return ((who.getPhysicalStrength() * 5)); // Korjaa damage
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
	
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();
		
		if(!isTargetInRoom(who, target))
			return;
		Living tgt = (Living) target;
		
		if(success <= 0) {
			
			who.notice("You try to smite " + tgt.getName() + " but fail to hit " +tgt.getObjective()+ ".");
			who.getRoom().notice(who, who.getName()+" tries to smite you but fails.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random();

			String message = new String();
			message = "You gather your strength and smite " + tgt.getName();
			if(dices < 25)
				message = message + ", making " + tgt.getObjective() + " sway.";
			else if(dices < 50)
				message = message + ", causing some bruises.";
			else if(dices < 75)
				message = message + ", hurting " + tgt.getObjective() + " severely.";
			else
				message = message + ", sending " + tgt.getObjective() + " flying to the ground.";

//			tgt.subHp(maxdamage * (dices / 1000), Damage.STUN);
				
			if(who.checkSkill("divine wrath") > 0 && (tgt.provides(UNDEAD) || tgt.provides(EXTRAPLANAR))) {
				int divinedice = Dice.random(who.getSkill("divine wrath"));
				if(divinedice >= 0) {
					message += message + " DIVINE WRATH \\o/";			
				}
				else {
					message += message + "voi vitun vittu t�t�kin";
				}
				maxdamage = maxdamage+100+Dice.random(who.getSkill("divine wrath"))+Dice.random(who.getSkill("arcane knowledge"));
					
			}
			
			who.notice(message);
			tgt.notice(who.getName()+" smites you.");
			String msg = who.getName()+" smites "+tgt.getName()+".";
			
			Iterator en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
			Damage dam = new Damage();
			dam.damage = (maxdamage / 2) + (((maxdamage /2) * dices )/ 100);
			dam.type = Damage.Type.CRUSHING;
			tgt.subHp(dam, who);
		}
		tgt.addAttacker(who);
	}
}
