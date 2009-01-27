package org.vermin.mudlib.commands;

import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Stat;

public class StatCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"stat train (.*) => train(actor, 1)",
				"stat cost => list(actor)",
				"stat => help(actor)"
			};
	}

	public void help(Player who) {
		who.notice("See 'help stat'");
	}
	
	public void train(Player who, String what) {
		Stat s = parseStat(what);
		if(s != null) {
			int cost = who.getStatPointCost(s);			
			if(cost > who.getFreeStatPoints()) {
				who.notice("You don't have enough free stat points for that. (Cost:"+cost+", Available:"+who.getFreeStatPoints()+")");
				return;
			}		
			who.useStatPoint(s);
			who.notice("You are now at: "+who.getStat(s)+". Stat points used: "+cost);
		}
		else
			who.notice("Train what? See 'help stat'");
	}
	
	public void list(Player who) {
		StringBuffer sb = new StringBuffer();
		sb.append("Stat costs for next point:\n");
		sb.append("Physical Strength, "+who.getStatPointCost(Stat.PHYS_STR)+"\n");
		sb.append("Physical Dexterity, "+who.getStatPointCost(Stat.PHYS_DEX)+"\n");
		sb.append("Physical Constitution, "+who.getStatPointCost(Stat.PHYS_CON)+"\n");
		sb.append("Physical Charisma, "+who.getStatPointCost(Stat.PHYS_CHA)+"\n");
		sb.append("Mental Strength, "+who.getStatPointCost(Stat.MENT_STR)+"\n");
		sb.append("Mental Dexterity, "+who.getStatPointCost(Stat.MENT_DEX)+"\n");
		sb.append("Mental Constitution, "+who.getStatPointCost(Stat.MENT_CON)+"\n");
		sb.append("Mental Charisma, "+who.getStatPointCost(Stat.MENT_CHA)+"\n");
		sb.append("Free stat points: "+who.getFreeStatPoints()+"\n");		
		who.notice(sb.toString());
	}
	
	private Stat parseStat(String what) {
		what = what.toLowerCase();
		if(what.equals("physical strength")     || what.equals("physical str")) return Stat.PHYS_STR;
		if(what.equals("physical constitution") || what.equals("physical con")) return Stat.PHYS_CON;
		if(what.equals("physical dexterity")    || what.equals("physical dex")) return Stat.PHYS_DEX;
		if(what.equals("physical charisma")     || what.equals("physical cha")) return Stat.PHYS_CHA;
		if(what.equals("mental strength")       || what.equals("mental str"))   return Stat.MENT_STR;
		if(what.equals("mental constitution")   || what.equals("mental con"))   return Stat.MENT_CON;
		if(what.equals("mental dexterity")      || what.equals("mental dex"))   return Stat.MENT_DEX;
		if(what.equals("mental charisma")       || what.equals("mental cha"))   return Stat.MENT_CHA;
		return null;
	}
}
