package org.vermin.mudlib.commands;

import org.vermin.mudlib.BattleStyle;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;

public class Battlestyle extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"battlestyle => listStyles(actor)",
			"battlestyle (\\d+) => setStyle(actor, 1)",
			"battlestyle => descStyle(actor)"
		};
	}
	
	public void listStyles(Player actor) {
		int i = 1;
		actor.notice("Your available battlestyles (currently using: "+actor.getBattleStyle().getName()+")");
		for(BattleStyle bs : actor.listAvailableBattleStyles()) {
			actor.notice(i+". "+bs.getName());
			i++;
		}
	}

	public void setStyle(Player actor, int style) {
		int i = 1;
		try {
			BattleStyle s = actor.listAvailableBattleStyles().get(style-1);
			actor.setBattleStyle(s);
			actor.notice("You start using the '"+s.getName()+"' style.");
		} catch(IndexOutOfBoundsException oob) {
			actor.notice("No such style.");
		}
	}
	
	public void descStyle(Player actor) {
		BattleStyle s = actor.getBattleStyle();
		actor.notice("You are currently using '"+s.getName()+"'.");
		actor.notice("You evaluate your currently wielded weaponry:");
		actor.notice(s.describeWeaponProficiency());
	}
}
