package org.vermin.world.skills;
import java.util.Iterator;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.PoisonAffliction;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
public class PoisonSkill extends Offensive {
   public PoisonSkill() {
     spCost = 99;
     rounds = 1;
     spellWords = "Umm yamee!";
     damageType = Damage.Type.POISON;
     damage = 20;
     name = "poison";
	skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.POISON, SkillTypes.LOCAL };

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
			
			   Iterator en = who.getRoom().findByType(Types.TYPE_LIVING);
			   while(en.hasNext()) {
				   Living l = (Living) en.next();
				   if(l != who && l != tgt)
					   l.notice(msg);
			   }

				int poisonDamage = suc.getActor().getMentalStrength() + suc.getActor().getSkill("poison");

			   if(Math.abs(suc.getSkillEffectModifier()) > 0) {
					poisonDamage = damage + ( damage * suc.getSkillEffectModifier() / 100 );
				   if(suc.getSkillEffectModifierMessage() != null && suc.getSkillEffectModifierMessage().length() > 0)
					   suc.getActor().notice(suc.getSkillEffectModifierMessage());
			   }

			   tgt.addAffliction(new PoisonAffliction(suc.getActor(), poisonDamage));
		   }
	   }
   }   
}
