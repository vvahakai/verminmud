
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
import org.vermin.mudlib.WebAffliction;
import org.vermin.util.Print;

public class WebSkill extends Offensive {
   public WebSkill() {
	 spCost = 99;
	 rounds = 2;
	 spellWords = "shxhhihshhxhxshix";
	 name = "web";
	 damageType = Damage.Type.ASPHYXIATION;
	 skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ASPHYXIATION, SkillTypes.LOCAL };

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
			
			   suc.getActor().notice("You chant '"+spellWords+"' and a volley of sticky webs fly forth from your hands towards "+tgt.getName()+".");
			   tgt.notice(Print.capitalize(who.getName())+" chants '"+spellWords+"' and a volley of sticky webs fly towards you from "+who.getPossessive()+" hands.");
			   String msg = Print.capitalize(who.getName())+" chants '"+spellWords+"' and a volley of sticky webs fly towards "+tgt.getName()+" from "+who.getPossessive()+" hands.";
			
			   Iterator en = who.getRoom().findByType(Types.LIVING);
			   while(en.hasNext()) {
				   Living l = (Living) en.next();
				   if(l != who && l != tgt)
					   l.notice(msg);
			   }

				int webStrength = suc.getActor().getMentalStrength() + suc.getActor().getSkill("web");
			   if(Math.abs(suc.getSkillEffectModifier()) > 0) {
					webStrength = webStrength + ( webStrength * suc.getSkillEffectModifier() / 100 );
				   if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
					   suc.getActor().notice(suc.getSkillEffectModifierMessage());
			   }
			   tgt.addAffliction(new WebAffliction(suc.getActor(), webStrength));
			   
				Damage dam = new Damage();
				dam.damage = who.getMentalStrength();
				dam.type = damageType;

				tgt.subHp(dam, who);
		   }
	   }
   }   
}
