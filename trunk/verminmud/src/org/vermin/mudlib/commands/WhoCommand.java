/* WhoCommand.java
	2.4.2002	Tatu Tarvainen
	21.9.2003       Vili Lang

	Implements the who command.
*/
package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;

import java.util.Iterator;
import java.util.Vector;

public class WhoCommand extends TokenizedCommand {
	
	public WhoCommand() {}
	
	public void run(Living who, Vector params) {
		String cmd;
		
		Iterator it = World.getPlayers();
		
		StringBuffer sb = new StringBuffer();
		sb.append("Players currently online:\n");		
		int plrCount = 0;
		org.vermin.mudlib.DefaultPlayerImpl plr;
		while(it.hasNext()) {
		    plr = (org.vermin.mudlib.DefaultPlayerImpl)it.next();
		    plrCount++;
		    sb.append((plr instanceof Wizard) ? ("<" + plr.getLevel() + "> ") : ("[" + plr.getLevel() + "] "));
		    sb.append((plr.getLevel() < 10) ? " " : "");
		    sb.append(plr.getName() + " ");
		    sb.append((plr.getSurname().equals("")) ? "" : plr.getSurname() + " ");
		    sb.append("the " + plr.getRace().getName() + " " + plr.getTitle()+"\n");
		}
		sb.append("----\n" +plrCount + " players total.\n");
		who.notice(sb.toString());
	}
	
	protected String vectorToString(Vector v, int start) {
		StringBuffer sb = new StringBuffer();
		for(int i=start; i<v.size(); i++) {
			sb.append(v.get(i).toString().toLowerCase() + (i==v.size()-1 ? "" : " "));
		}
		return sb.toString();
	}
}
