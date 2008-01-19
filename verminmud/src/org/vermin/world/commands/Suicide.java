/*
 * Created on 10.2.2006
 */
package org.vermin.world.commands;

import org.vermin.driver.AuthenticationProvider;
import org.vermin.driver.Driver;
import org.vermin.mudlib.ActionHandler;
import org.vermin.mudlib.Command;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.World;

public class Suicide implements Command {
	
	public Suicide() {
	}

	private class Suicider implements ActionHandler<MObject> {
		
		public boolean action(final MObject actor, String command) {
			final Player p = (Player) actor;
			AuthenticationProvider auth = Driver.getInstance().getAuthenticator();

			String[] params = command.split(" +");
			StringBuffer sb = new StringBuffer();
			boolean nameFound = false;
			for(int i=1; i<params.length; i++) {
				if(!params[i].equalsIgnoreCase(p.getName())) {
					sb.append(params[i]).append(" ");
				} else {
					nameFound = true;
				}
			}
			if(sb.length() > 0) {
				sb.deleteCharAt(sb.length()-1);
			}
			if(nameFound && auth.authenticate(p.getName(), sb.toString()) != null) {
				p.notice("So be it.");
				World.suicidePlayer(p);
			} else {
				p.notice("You cannot bring yourself to do it.");
				p.removeHandler(this);

			}
			
			return true;
		}
	}
	
	public boolean action(Living who, String params) {
		Player p = (Player) who;
		
		p.notice("You start to contemplate suicide.");
		p.notice("&B2;Note that this process is immediate and irreversible.&;");
		p.notice("Type 'suicide <character name> <password>' to delete this character.");
		

		p.addHandler("suicide", new Suicider());
		return true;
	}

}
