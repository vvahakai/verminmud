package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;

public class Stats extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"stats => stats(actor)"
		};
	}

	public void stats(Living actor) {

		StringBuffer sb = new StringBuffer();
		sb.append("Physical (Str: ");
		sb.append(Integer.toString(actor.getPhysicalStrength()));
		sb.append(", Dex: ");
		sb.append(Integer.toString(actor.getPhysicalDexterity()));
		sb.append(", Con: ");
		sb.append(Integer.toString(actor.getPhysicalConstitution()));

		sb.append(") Mental (Str: ");
		sb.append(Integer.toString(actor.getMentalStrength()));
		sb.append(", Dex: ");
		sb.append(Integer.toString(actor.getMentalDexterity()));
		sb.append(", Con: ");
		sb.append(Integer.toString(actor.getMentalConstitution()));
		sb.append(")");

		String report = sb.toString();
		actor.notice("Stats: '"+report+"'.");
//		actor.getRoom().notice(actor, actor.getName()+" reports '"+report+"'.");
	}

}
