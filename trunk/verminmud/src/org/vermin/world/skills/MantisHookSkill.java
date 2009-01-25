package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.util.Print;

import java.util.Iterator;

public class MantisHookSkill extends BaseSkill {

	protected String name = "mantis hook";
	protected int rounds = Dice.random(1)+3;
	protected Damage.Type damageType = Damage.Type.PHYSICAL;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.LOCAL /*, SkillTypes.MARTIAL*/ };

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
		//tuo kymmenen on sitten sit� perhanan damage tunea -Lord HighTuner
		return (int) (((((who.getPhysicalStrength()+who.getPhysicalDexterity())*2)+Math.max(0, who.checkSkill("martial arts"))))*3);
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();		

		if(!isTargetInRoom(who, target))
			return;

		Living tgt = (Living) target;
		
		if(success <= 0) {
			
			who.notice("You try to pin your opponent with a mantis hook but your grip slips.");
			tgt.notice(who.getName()+" tries to pin you but "+who.getPossessive()+" grip slips.");
		} else {

			int maxdamage = getDamage(who,tgt);
			int dices = Math.max(0, who.checkSkill("mantis hook"));

			Race r = tgt.getRace();
			String hitloc = r.getHitLocation(Dice.random()); // hitlocation
			String message = new String();
			String vmessage = new String();
			if(dices < 15) {
				message =  "You grab a feeble hold of "+tgt.getName()+"'s "+hitloc+".";
				vmessage = Print.capitalize(who.getName())+" grabs a feeble hold of your "+hitloc+".";
			} else if(dices < 35){
				message =  "You move aside from "+tgt.getName()+"'s attack and put "+tgt.getPossessive()+" "+hitloc+" in a lock.";
				vmessage = Print.capitalize(who.getName())+" evades your attack and puts your "+hitloc+" in a lock.";
			} else if(dices < 60){
				message =  "You move inside "+tgt.getName()+"'s defence and take a firm grip of "+tgt.getPossessive()+" "+hitloc+".";
				vmessage = Print.capitalize(who.getName())+" follows your movement and takes a firm grip of your "+hitloc+".";
			} 	else if(dices < 80){
				message =  "Your cunning feint throws "+tgt.getPossessive()+" off balance and into your tight hold.";
				vmessage = Print.capitalize(who.getName())+"'s cunning feint causes you to stumble into "+tgt.getPossessive()+" tight hold.";
			} 		else if(dices < 90){
				message =  "You dive inside "+tgt.getName()+"'s defence and grab "+tgt.getPronoun()+" into a LETHAL embrace.";
				vmessage = Print.capitalize(who.getName())+" lunges at you and grabs you into a LETHAL embrace.";
			} 		else {
				message =  "You twist away from "+tgt.getName()+"'s attack and PIN "+tgt.getPronoun()+" with a CHOKING grip!";
				vmessage = "You feel your eyes bulge as "+Print.capitalize(who.getName())+" PINS you with a CHOKING grip!";
			}
			who.notice(message);
			tgt.notice(vmessage);
			
			
			String msg = who.getName()+" hits "+tgt.getName()+" with "+who.getPossessive()+" fist.";
			
			Iterator en = who.getRoom().findByType(Types.LIVING);
			while(en.hasNext()) {
				Living l = (Living) en.next();
				if(l != who && l != tgt)
					l.notice(msg);
			}
				int mantisStun = suc.getActor().getPhysicalStrength() + dices + Math.max(0, suc.getActor().checkSkill("pinning techniques"));
					if(Math.abs(suc.getSkillEffectModifier()) > 0) {
					mantisStun = mantisStun + ( mantisStun * suc.getSkillEffectModifier() / 100 );
					if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
					   suc.getActor().notice(suc.getSkillEffectModifierMessage());
			   }
			
			tgt.addAffliction(new StunAffliction(suc.getActor(), mantisStun));

			Damage dam = new Damage();
			dam.damage = (maxdamage / 2 ) + ( (maxdamage * dices / 100) / 2);
			dam.type = Damage.Type.CRUSHING;
			tgt.subHp(dam, who);
		}
		tgt.addAttacker(who);
	}
}
