/**
 * Summary.java
 * 26.10.2003 Tatu Tarvainen
 *
 * Command to show session summary.
 */
package org.vermin.mudlib.commands;

import org.vermin.mudlib.CentralBank;
import org.vermin.mudlib.Command;
import org.vermin.mudlib.DefaultMoneyImpl;
import org.vermin.mudlib.DefaultPlayerImpl;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Money;

public class Summary implements Command { 

	public boolean action(Living actor, String params) {

		/* Summary items:
			- experience gained: total, rate
			- money gained: total, rate
			- rooms explored: total
			- monsters killed: total
			- time spent
		*/
		
		DefaultPlayerImpl p = (DefaultPlayerImpl) actor;
		long xpGained = (p.getTotalExperience() + p.getExperience()) - p.getSummaryInitialExperience();
		long duration = (System.currentTimeMillis() - p.getSummarySessionStart()) / 60000;
		
		Money mi = p.getMoney();
		DefaultMoneyImpl bmi = ((CentralBank) CentralBank.getInstance()).getAccount(p).getValue();
		
		long currentMoney = (mi.getValue() + bmi.getValue());
		long moneyGained = currentMoney - p.getSummaryInitialMoney();
		
		duration = duration == 0 ? 1 : duration;

		actor.notice("Session duration: "+duration+" minutes.");
		actor.notice("Experience gained: "+xpGained+", "+xpGained/duration+"/min.");
		actor.notice("Money gained: "+moneyGained+", "+moneyGained/duration+"/min.");
		actor.notice("Rooms explored: "+p.getSummaryRoomsExplored());
		actor.notice("Kills: "+p.getSummaryKills());
        return false;
	}

}
