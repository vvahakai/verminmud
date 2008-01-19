package org.vermin.wicca.telnet;

import java.util.StringTokenizer;

import org.vermin.mudlib.Player;

public class State extends AbstractTelnetComponent implements org.vermin.wicca.remote.State {

	public State(TelnetClientOutput tco) {
		super(tco);
	}
	
	public void update(Player who) {
		// FIXME: move old player "prompt" code to here.
		//getClientOutput().prompt()
		StringTokenizer st = new StringTokenizer(who.getPromptString(), "$");
		StringBuffer sb = new StringBuffer();

		while(st.hasMoreTokens())
			sb.append( promptReplace(who, st.nextToken()) );
		
		getClientOutput().prompt(sb.toString());
	}
	
	private String promptReplace(Player who, String str) {
		if(str.equalsIgnoreCase("hp")) {
			return Integer.toString(who.getHp());
      } else if(str.equalsIgnoreCase("maxhp")) {
			return Integer.toString(who.getMaxHp());
      } else if(str.equalsIgnoreCase("sp")) {
			return Integer.toString(who.getSp());
      } else if(str.equalsIgnoreCase("maxsp")) {
			return Integer.toString(who.getMaxSp());
      } else if(str.equalsIgnoreCase("exp")) {
			return Long.toString(who.getExperience());
      } else if(str.equalsIgnoreCase("n")) {
         return "\n";
      } else {
         return str;
      }
	}

}
