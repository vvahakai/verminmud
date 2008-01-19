package org.vermin.mudlib.commands;

import org.vermin.mudlib.*;

public class Report extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"report => report(actor)",
			"report stats => reportStats(actor)"
		};
	}

	public void report(Living actor) {
		String report = makeReportNotice(actor);
		actor.notice("You report '"+report+"'.");
		actor.getRoom().notice(actor, actor.getName()+" reports '"+report+"'.");
	}

	public static String makeReportNotice(Living actor) {
		StringBuffer sb = new StringBuffer();
		sb.append("Hp: ");
		sb.append(Integer.toString(actor.getHp()));
		sb.append("/");
		sb.append(Integer.toString(actor.getMaxHp()));
		sb.append(", Sp: ");
		sb.append(Integer.toString(actor.getSp()));
		sb.append("/");
		sb.append(Integer.toString(actor.getMaxSp()));
		return sb.toString();
	}

	public void reportStats(Living actor) {

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
		actor.notice("You report '"+report+"'.");
		actor.getRoom().notice(actor, actor.getName()+" reports '"+report+"'.");
	}

}
