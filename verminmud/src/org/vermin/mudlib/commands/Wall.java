package org.vermin.mudlib.commands;

import java.util.Iterator;

import org.vermin.driver.Driver;
import org.vermin.driver.Loader;
import org.vermin.mudlib.Command;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.World;

public class Wall implements Command {
	
	public boolean action(Living who, String params) {
		Driver d = Driver.getInstance();
		Loader l = d.getLoader();
		Iterator it = World.getPlayers();
		String msg = who.getName()+" tells everyone: '"+params+"'";
		while(it.hasNext()) {
			Player p = (Player) it.next();
			p.notice(msg);
		}
		return true;
	}
}
