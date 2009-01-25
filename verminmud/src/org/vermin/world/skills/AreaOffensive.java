package org.vermin.world.skills;

import java.util.ArrayList;
import java.util.Iterator;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;

/* base class for generated offensive spells */
public class AreaOffensive extends BaseSkill {

	protected String name = "FIXME!";
	protected int spCost = 1;
	protected int rounds = 1;
	protected String spellWords = "FIXME!";
	protected Damage.Type damageType = Damage.Type.PHYSICAL;
	protected int damage = 1;

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.AREA, SkillTypes.OFFENSIVE };

	public SkillType[] getTypes() {
		return skillTypes;
	}
	
	public String getName() { return name; }

	public int getCost(SkillUsageContext suc) { return spCost; }

	public boolean tryUse(Living who, MObject target) {
		return true;
	}
	
	public int getTickCount() {
		return rounds;
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		int success = suc.getSkillSuccess();
		
		if(success <= 0) {
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		} else { 
			
				who.notice("You chant '"+spellWords+"' and conjure a devastating "+name+".");
				who.getRoom().notice(who, who.getName()+ " chants '"+spellWords+"' and conjures a devastating "+name+".");
			
				Damage dam = new Damage();
				dam.damage = damage;
				dam.type = damageType;

				if(Math.abs(suc.getSkillEffectModifier()) > 0) {
					dam.damage = damage + ( damage * suc.getSkillEffectModifier() / 100 );
					if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
						suc.getActor().notice(suc.getSkillEffectModifierMessage());
				}

				Iterator<MObject> en = who.getRoom().findByType(Types.LIVING);
				ArrayList<Living> targets = new ArrayList<Living>(); 
				while(en.hasNext()) {
					Living l = (Living) en.next();
					if(l != who && !who.getBattleGroup().contains(l.getLeafBattleGroup()))
						targets.add(l);
				}
				
				if(targets.size() == 0)
					return;
				
				dam.damage = dam.damage / targets.size();
			    dam.damage += dam.damage * Math.max(who.getSkill("mastery of the universe"), targets.size()) / 20; //Tuo 20 on sit sitä tunea.
				
				for(Living l : targets)
					l.subHp(dam, who);
				
			}
		}
}
