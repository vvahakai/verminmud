package org.vermin.mudlib.commands;

import org.vermin.mudlib.BattleGroup;
import org.vermin.mudlib.Living;
import static org.vermin.mudlib.LivingProperty.IMMOBILIZED;
import static org.vermin.mudlib.LivingProperty.NO_BATTLE;
import static org.vermin.mudlib.LivingProperty.NO_SKILLS;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Wizard;
import org.vermin.util.Print;

public class Scan extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"scan all => scanAll(actor)",
			"scan => scan(actor)",
			"scan (.*) => scan(actor, living 1)"
		};
	}

	public void scanAll(Player who ) {
		// FIXME: implement function with (i-am-the-collector)
		/*
		BattleGroup bg = who.getBattleGroup();
		for(BattleGroup hostile : bg.hostileGroups()) {
			hostile.getCombatants(who.getRoom());
			*/
	}
	
	public void scan(Player who) {
		BattleGroup bg = who.getLeafBattleGroup();
		
		Living attacker = bg.getOpponent(who.getRoom());
		
		if(attacker == null)
			for(BattleGroup hostile : bg.hostileGroups()) {
				attacker = hostile.getCombatant(who.getRoom());
				if(attacker != null) break;
			}
			
		if(attacker == null)
			who.notice("You are not fighting anyone.");
		else
			scan(who, attacker);

	}
	
	public void scan(Player who, Living target) {
		if(target == null) {
			who.notice("You can't scan that");
		} else {
			String stun = "";
			if(target.provides(IMMOBILIZED))
				stun = "knocked out and ";
			else if(target.provides(NO_BATTLE))
				stun = "stunned and ";
			else if(target.provides(NO_SKILLS))
				stun = "dazed and ";

			if(who instanceof Wizard)
				who.notice(Print.capitalize(target.getName()) + " is " + stun + target.getShape()+" ("+target.getHp()+"/"+target.getMaxHp()+").");
			else
				who.notice(Print.capitalize(target.getName()) + " is " + stun + target.getShape()+".");
		}
	}
}
