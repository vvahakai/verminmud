package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import java.util.Iterator;

/* base class for generated offensive spells */
public class BashSkill extends BaseSkill {

	protected String name = "bash";
	protected int rounds = 3;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes.clone();
	}

	public String getName() {
		return name;
	}	
	
	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}

	public int getTickCount() {
		return rounds;
	}

	protected int getDamage(Living who, Living tgt) {
		int resistance = tgt.getResistance(damageType);
		return ((who.getPhysicalStrength() * 2));
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();
		
		if(!isTargetInRoom(who, target))
			return;

		Living tgt = (Living) target;
		
		if(success <= 0) {
			
			who.notice("You try to bash " + tgt.getName() + " but fail to hit " +tgt.getObjective()+ ".");
			who.getRoom().notice(who, who.getName()+" tries to bash you but fails.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random();

			String message = "";
			message = "You gather your strength and bash " + tgt.getName();
			if(dices < 25)
				message = message + ", making " + tgt.getObjective() + " sway.";
			else if(dices < 50)
				message = message + ", causing some bruises.";
			else if(dices < 75)
				message = message + ", hurting " + tgt.getObjective() + " severely.";
			else
				message = message + ", sending " + tgt.getObjective() + " flying to the ground.";

//			tgt.subHp(maxdamage * (dices / 1000), Damage.STUN);
			
			who.notice(message);
			tgt.notice(who.getName()+" bashes you.");
			String msg = who.getName()+" bashes "+tgt.getName()+".";
			
			Iterator en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
			Damage dam = new Damage();
			dam.damage = maxdamage * dices / 100;
			dam.type = Damage.Type.CRUSHING;
			tgt.subHp(dam, who);
		}
		tgt.addAttacker(who);
	}
}
