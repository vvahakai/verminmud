package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.StunAffliction;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.mudlib.skills.MonkSkills;
import org.vermin.util.Print;

public class AssaultScreamSkill extends BaseSkill {

	
	protected Damage.Type damageType = Damage.Type.PHYSICAL;
	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL /*, SkillTypes.MARTIAL*/ };

	public String getName() {
		return "assault scream";
	}

	public int getTickCount() {
		return 4;
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}
	
	public boolean tryUse(Living who, MObject target) {
		if (who.inBattle()) {
			return false;
		}
		else if 
		(hasLivingTarget(who, target) && isTargetInRoom(who, target)) {
			return true;
		}
		
		else return false;
		
	}
	
	
	protected int getDamage(Living who, Living tgt) {
		//tuo kymmenen on sitten sit� perhanan damage tunea -Lord HighTuner
		float flow = (float) (1.0 + MonkSkills.chiFlow(who)/100.0);
		return (int) (flow*((who.getPhysicalStrength()*1.5)+(who.getPhysicalDexterity()*1.5)+Math.max(0, who.checkSkill("martial arts"))));
	}

	
	public void use(SkillUsageContext suc) {
		
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();		
		
		if(!isTargetInRoom(who, target))
			return;
		
		Living tgt = (Living) target;
		
		if(success <= 0) {
			
			who.notice("You try to scare your opponent with your scream but your enemy hardly notices.");
			tgt.notice(who.getName()+" tries to scare you but "+who.getPossessive()+" fails miserably.");
		} else {
			
			int maxdamage = getDamage(who,tgt);
			int dices = Dice.random(who.getSkill("assault scream"));
			
			//Race r = tgt.getRace();
			//String hitloc = r.getHitLocation(Dice.random()); // hitlocation
			String message = new String();
			String vmessage = new String();
			if(dices < 25) {
				message =  "You are able to make "+tgt.getName()+" flinch.";
				vmessage = Print.capitalize(who.getName())+" makes you flinch a bit with a feeble scream.";
			} else if(dices < 65){
				message =  "You concentrate and let out an assault scream at "+tgt.getName()+" and make "+tgt.getPossessive()+" head spin.";
				vmessage = Print.capitalize(who.getName())+" makes your head spin with "+who.getPossessive()+" assault scream.";
			} else {
				message =  "You perform a series of maneuvers at "+tgt.getName()+" and suprise "+tgt.getObjective()+" attacking from behind!";
				vmessage = "You get suprised from behind by "+Print.capitalize(who.getName())+" as "+who.getPossessive()+" assault scream destroyes you mentally.";
			}
			who.notice(message);
			tgt.notice(vmessage);
			
			
			String msg = who.getName()+" hits "+tgt.getName()+" with "+who.getPossessive()+" fist.";
			
			Iterator<MObject> en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
			int assaultStun = suc.getActor().getPhysicalStrength() + suc.getActor().getSkill("mantis hook") + suc.getActor().getSkill("pinning techniques");
			if(Math.abs(suc.getSkillEffectModifier()) > 0) {
				assaultStun = (assaultStun + ( assaultStun * suc.getSkillEffectModifier() / 100 ))*2;
				if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
					suc.getActor().notice(suc.getSkillEffectModifierMessage());
			}
			
			tgt.addAffliction(new StunAffliction(suc.getActor(), assaultStun));
			
			Damage dam = new Damage();
			dam.damage = (maxdamage / 2 ) + ( (maxdamage * dices / 100) / 2);
			dam.type = Damage.Type.CRUSHING;
			tgt.subHp(dam, who);
		}
		tgt.addAttacker(who);
	}


}



