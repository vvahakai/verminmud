package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import java.util.Iterator;

/* base class for generated offensive spells */
public class WarcrySkill extends BaseSkill {

	protected String name = "warcry";
	protected int rounds = 5;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public String getName() {
		return name;
	}	
	
	public boolean tryUse(Living who, MObject target) {
		if(who.inBattle()) {
			who.notice("You are already fighting.\n");
			return false;
		}
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}

	public int getTickCount() {
		return rounds;
	}

	protected int getDamage(Living who, Living tgt) {
		int resistance = tgt.getResistance(damageType);
		return ((who.getPhysicalStrength() * 3)+Dice.random(50));
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();
		
		if(!isTargetInRoom(who, target))
			return;

		if(who.inBattle()) {
			who.notice("You are already fighting.\n");
			return;
		}

		Living tgt = (Living) target;
		
		if(success <= 0) {
			
			who.notice("Your warcry fails to catch " + tgt.getName() + " by suprise.");
			who.getRoom().notice(who, who.getName()+" shouts a weak warcry.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random();

			String message = "";
			message = "You let out a powerful warcry catching " + tgt.getName() + " by suprise and attack " + tgt.getObjective();
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
			tgt.notice(who.getName()+" shouts a powerful warcry and attacks you.");
			String msg = who.getName()+" shouts a powerful warcry and attacks "+tgt.getName()+".";
			
			Iterator en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
			Damage dam = new Damage();
			dam.damage = (int) ((maxdamage * 0.75) + maxdamage * dices / 400);
			dam.type = Damage.Type.CRUSHING;
			tgt.subHp(dam, who);
			
			dam.type = Damage.Type.STUN;
			tgt.subHp(dam, who);
			
		}
		tgt.addAttacker(who);
	}
}
