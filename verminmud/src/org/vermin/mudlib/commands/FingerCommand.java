/* FingerCommand.java
	16.2.2002
	
*/

package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;
import org.vermin.wicca.Session;

import java.util.Vector;
import org.vermin.driver.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class FingerCommand extends TokenizedCommand {
	
	public FingerCommand() {}

	public void run(Living who, Vector params) {
		org.vermin.mudlib.DefaultPlayerImpl tgt;	// target player
		
		if(params.size() < 2) {
			who.notice("Usage: finger <player>");
		} else {
			String id = "players/"+params.get(1).toString().toLowerCase();
			
			try {
				tgt = (org.vermin.mudlib.DefaultPlayerImpl) World.get(id);
				
				if (tgt == null) {
					who.notice("No such player: "+params.get(1));
				} else {
					StringBuffer sb = new StringBuffer();
					Session c = World.getSession(params.get(1).toString());
					
					String onlineTime = " is not online";
					String idleTime = "";
					if(c != null) {
						onlineTime = " has been on for "+ formatTime(c.getDuration());
						idleTime = (c.getIdleTime()<1000) ? " Not idle." : " Idle "+formatTime(c.getIdleTime())+".";
					}
					sb.append("\n");
					sb.append(tgt.getName()+" ");
					sb.append((tgt.getSurname().equals("") || tgt.getSurname() == null) ? "" : tgt.getSurname()+" ");
					sb.append("the " + capitalize(tgt.getRace().getName())+" ");
					sb.append((tgt.getTitle().equals("") || tgt.getTitle() == null) ? "\n" : tgt.getTitle()+"\n");
					Date d = tgt.getCreated();

					sb.append("Level: "+tgt.getLevel()+" Age: "+parseAge(d)+" Created: ");
					if(d != null) {
						DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("fi", "FI"));
						sb.append(df.format(d));
					} else {
						sb.append("before 13.6.2004");
					}
					sb.append("\n");
					
					sb.append(capitalize(tgt.getPronoun()) + onlineTime + "."+idleTime+"\n");
					if(tgt.getBestSoloKillDescription() != null) {
						sb.append("Best solo kill: ");
						sb.append(tgt.getBestSoloKillDescription());
						sb.append(" - ");
						sb.append(Long.toString(tgt.getBestSoloKillExperience()));
						sb.append(" xp.\n");
					}
					if(tgt.getBestPartyKillDescription() != null) {
						sb.append("Best party kill: ");
						sb.append(tgt.getBestPartyKillDescription());
						sb.append(" - ");
						sb.append(Long.toString(tgt.getBestPartyKillExperience()));
						sb.append(" xp.\n");
					}
					
					String plan = tgt.getPlan();
					if(plan == null)
						sb.append("No plan.");
					else {
						sb.append("Plan:\n");
						sb.append(plan);
					}
					who.notice(sb.toString());
					who.notice("\n");
				}
			} catch (LoadException e) {
				e.printStackTrace(System.out);
				who.notice("No such player: "+params.get(1));			
			}
		}
	}

	private String parseAge(Date created) {
		long dur = System.currentTimeMillis() - created.getTime();
		
		
		return (dur / (1000 * 60 * 60 * 24))+"d "
		      +(dur / (1000 * 60 * 60))%24+"h "
		      +(dur / (1000 * 60))%60+"m "
		      +(dur / 1000)%60+"s";
	}
	
	protected String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();	
	}
	
	protected String formatTime(long time) {
		StringBuffer sb = new StringBuffer();
		long secs = time/1000;
		
		sb.append((secs >= 86400) ? (secs/86400 +"d ") : "");
		secs %= 86400;
		sb.append((secs >= 3600) ? (secs/3600 +"h ") : "");
		secs %= 3600;
		sb.append((secs >= 60) ? (secs/60 +"m ") : "");
		secs %= 60;
		sb.append((secs != 0) ? (secs +"s") : "");
		
		return sb.toString();
	}

}
