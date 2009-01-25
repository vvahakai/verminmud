package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

import java.util.Iterator;

public class PushSkill extends BaseSkill {

	protected String name = "push";
	protected int rounds = 2;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes;
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
		return (who.getPhysicalStrength());
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();		

		if(!isTargetInRoom(who, target))
			return;

		Living tgt = (Living) target;
		
		if(success <= 0) {
			who.notice("You try to push " + tgt.getName() + " but fail to hit " +tgt.getObjective()+ ".");
			tgt.notice(who.getName()+" tries to push you but fails to connect.");
			
//			who.notice("You try to push " + tgt.getName() + " but fail to hit " +tgt.getObjective()+ ".");
//			who.getRoom().notice(who, who.getName()+" tries to push you but fails.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random();

			String message = new String();
			message = "You gather your strength and push " + tgt.getName();
			if(dices < 25)
				message = message + ", making " + tgt.getObjective() + " sway.";
			else if(dices < 50)
				message = message + ", causing some bruises.";
			else if(dices < 75)
				message = message + ", hurting " + tgt.getObjective() + " severely.";
			else
				message = message + ", sending " + tgt.getObjective() + " flying to the ground.";

			
			who.notice(message);
			tgt.notice(who.getName()+" pushes you.");
			String msg = who.getName()+" pushes "+tgt.getName()+".";
			
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

			MonkSkills.pressurePointManipulation(who, tgt);
		}
		tgt.addAttacker(who);
	}
}
