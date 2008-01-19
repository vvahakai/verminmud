package org.vermin.wicca.web;

import org.vermin.mudlib.Player;

public class State extends AbstractWebComponent implements org.vermin.wicca.remote.State {

	
	public State(WebClientOutput output) {
		super(output);
	}

	public void update(Player who) {
		StringBuilder sb = new StringBuilder();
		sb.append("{'hp':"); sb.append(who.getHp());
		sb.append(",'maxhp':"); sb.append(who.getMaxHp());
		sb.append(",'sp':"); sb.append(who.getSp());
		sb.append(",'maxsp':"); sb.append(who.getMaxSp());
		sb.append(",'exp':"); sb.append(who.getExperience());
		sb.append("}");
		
		write((short) 3, (byte) 1, Integer.toString(who.getHp()), 
				Integer.toString(who.getMaxHp()), Integer.toString(who.getSp()),
				Integer.toString(who.getMaxSp()), Long.toString(who.getExperience())); 

	}

}
