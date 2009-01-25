package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.Wizard;
import org.vermin.mudlib.skills.BaseSkill;

/* base class for generated offensive spells */
public class Offensive extends BaseSkill {

	protected String name = "FIXME!";
	protected int spCost = 1;
	protected int rounds = 1;
	protected String spellWords = "FIXME!";
	protected Damage.Type damageType = Damage.Type.PHYSICAL;
	protected int damage = 1;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL, SkillTypes.OFFENSIVE, SkillTypes.ARCANE };

	public SkillType[] getTypes() {
		return skillTypes;
	}
	
	public String getName() { return name; }

	public int getCost(SkillUsageContext suc) { return spCost; }

	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target) && 
			isTargetInRoom(who, target);
	}
	
	public int getTickCount() {
		return rounds;
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		if(!isTargetInRoom(who, target))
			return;

		Living tgt = (Living) target;		
		
		if(success <= 0) {
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		} else { 

			BattleStyle bs = tgt.getBattleStyle();
			if(bs.canTumbleSpell()) {
				suc.getActor().notice(bs.getAttackerTumbleSpellMessage());
				tgt.notice(bs.getOwnerTumbleSpellMessage());

			} else {
			
				suc.getActor().notice("You chant '"+spellWords+"' and hit "+tgt.getName()+" with your "+name+".");
				tgt.notice(who.getName()+" chants '"+spellWords+"' and hits you with "+who.getPossessive()+" "+name+".");
				String msg = who.getName()+" chants '"+spellWords+"' and hits "+tgt.getName()+" with "+who.getPossessive()+" "+name+".";
			
				Iterator en = who.getRoom().findByType(Types.LIVING);
				while(en.hasNext()) {
					Living l = (Living) en.next();
					if(l != who && l != tgt)
						l.notice(msg);
				}

				Damage dam = new Damage();
				dam.damage = damage;
				dam.type = damageType;

				if(Math.abs(suc.getSkillEffectModifier()) > 0) {
					dam.damage = ( damage * suc.getSkillEffectModifier() / 100 );
					if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
						suc.getActor().notice(suc.getSkillEffectModifierMessage());
				}			

				tgt.subHp(dam, who);
				if (suc.getActor() instanceof Wizard) {
					suc.getActor().notice("Damage done with "+suc.getSkill().getName()+": "+dam.damage);
				}
			}
		}
	}
}
