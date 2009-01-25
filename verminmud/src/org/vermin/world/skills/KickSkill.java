package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;

public class KickSkill extends BaseSkill {

	protected String name = "kick";
	protected int rounds = 2;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getTickCount() {
		return rounds;
	}
	
	protected int getDamage(Living who, Living tgt) {
		return (who.getPhysicalStrength()*2);
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();		

		if(!isTargetInRoom(who, target))
			return;

		Living tgt = (Living) target;
		
		if(success <= 0) {
			who.notice("You try to kick " + tgt.getName() + " but fail to hit.");
			tgt.notice(who.getName()+" tries to kick you but fails miserably.");
			
//			who.notice("You try to push " + tgt.getName() + " but fail to hit " +tgt.getObjective()+ ".");
//			who.getRoom().notice(who, who.getName()+" tries to push you but fails.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random();

			String message = new String();
			message = "You gather all your strength and kick " + tgt.getName();
			if(dices < 25)
				message = message + ", making " + tgt.getObjective() + " sway.";
			else if(dices < 50)
				message = message + ", causing some minor bruises.";
			else if(dices < 75)
				message = message + ", hurting " + tgt.getObjective() + " severely.";
			else
				message = message + ", sending " + tgt.getObjective() + " flying to the air.";

			
			who.notice(message);
			tgt.notice(who.getName()+" kicks you.");
			String msg = who.getName()+" kicks "+tgt.getName()+".";
			
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


