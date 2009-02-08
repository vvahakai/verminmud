/* ScoreCommand.java
 16.2.2002
 
 */

package org.vermin.world.commands;

import org.vermin.mudlib.*;

import java.util.Vector;


public class ScoreCommand extends TokenizedCommand
{
	public void run(Living who, Vector params)
	{
		if(!(who instanceof Player)) {
			return;
		}
		Player actor = (Player) who;
		CentralBank cb = (CentralBank) CentralBank.getInstance();
		CentralBank.Account acc = (CentralBank.Account) (cb.getAccount(actor));
		Money mi_cash = actor.getMoney();
		long cash = 0;
		long bank = 0;
		int explength = new Long(actor.getExperience()).toString().length();
		String exptabs = "\t\t\t";
		if (explength >= 3)
			exptabs = "\t\t";
		if (explength >= 11)
			exptabs = "\t";
		if (explength >= 19)
			exptabs = "";
		Money mi_bank = null;
		if(acc != null)
			mi_bank = (acc.getValue());
		if(mi_cash != null)
			cash = mi_cash.getValueAs(Money.Coin.COPPER);
		if(mi_bank != null)
			bank = mi_bank.getValueAs(Money.Coin.COPPER);
		int moneylength = new Long(cash).toString().length();
		String moneytabs = "\t\t";
		if (moneylength >= 3)
			moneytabs = "\t";
		if (moneylength >= 11)
			moneytabs = "";
		
		StringBuffer sb = new StringBuffer();

		String surnameSeparator = "";
		if(actor.getSurname().length() != 0) surnameSeparator = " ";

		sb.append("Name: "+actor.getName()+surnameSeparator+actor.getSurname()+" the "+actor.getRace().getName()+" "+actor.getTitle()+"\n");
		sb.append("Level: "+actor.getLevel()+"\n");
		
//		---------------------	
		/*
		 sb.append("Guild levels: ");
		 Iterator items = who.findByType(Types.TYPE_ITEM);
		 while(items.hasNext()) {
		 Item it = (Item) items.next();
		 if(it instanceof GuildItem) {
		 GuildItem gi = (GuildItem) it;
		 
		 sb.append(Print.capitalize( gi.getName().substring(0, gi.getName().length()-13).replace('_', ' ') ));
		 sb.append(" (");
		 sb.append(Integer.toString(gi.getLevel()));
		 sb.append(")");
		 sb.append(items.hasNext() ? ", " : ".");
		 }
		 }
		 sb.append("\n");
		 */
//		--------------------
		sb.append("Exp: "+actor.getExperience()+exptabs+"Total: "+actor.getTotalExperience()+"\n");
		sb.append("Cash: "+cash+" copper"+moneytabs+"In bank: "+bank+" copper\n");
		int explorePercent = (int) Math.round(actor.getExploreCount()*100d/World.getExplorableRoomCount());
		sb.append("Hitpoints: "+actor.getHp()+"("+actor.getMaxHp()+")\tSpellpoints: "+actor.getSp()+"("+actor.getMaxSp()+")\n");
		
		sb.append("Physical: Str("+who.getPhysicalStrength()+") Dex("+who.getPhysicalDexterity()+") Con("+who.getPhysicalConstitution()+") Cha("+who.getPhysicalCharisma()+")\n");
		sb.append("Mental:   Str("+who.getMentalStrength()+") Dex("+who.getMentalDexterity()+") Con("+who.getMentalConstitution()+") Cha("+who.getMentalCharisma()+")\n");
		sb.append("Size: ");
		if(who.getSize() >= 90) {
			sb.append("huge");
		} else if(who.getSize() >= 80) {
			sb.append("very large");
		} else if(who.getSize() >= 70) {
			sb.append("large");
		} else if(who.getSize() >= 55) {
			sb.append("quite large");
		} else if(who.getSize() >= 45) {
			sb.append("medium");
		} else if(who.getSize() >= 30) {
			sb.append("quite small");
		} else if(who.getSize() >= 20) {
			sb.append("small");
		} else if(who.getSize() >= 10) {
			sb.append("very small");
		} else if(who.getSize() >= 0) {
			sb.append("tiny");
		} else {
			sb.append("LOL WTF!?");
		}
		sb.append("\n");
		
		sb.append("Explored rooms: "+actor.getExploreCount()+" ("+explorePercent+"%)\n");
		String hungry = "";
		if (who.getSustenance() > 9000) hungry = "You are full.";
		else if (who.getSustenance() <= 9000 && who.getSustenance() > 5000) hungry = "You are satiated.";
		else if (who.getSustenance() <= 5000 && who.getSustenance() > 3000) hungry = "You are not hungry.";
		else if (who.getSustenance() <= 3000 && who.getSustenance() > 2000) hungry = "You feel a bit hungry.";
		else if (who.getSustenance() <= 2000 && who.getSustenance() > 1000) hungry = "You feel hungry.";
		else if (who.getSustenance() <= 1000 && who.getSustenance() > 500) hungry = "You feel very hungry.";
		else if (who.getSustenance() <= 500 && who.getSustenance() > 400) hungry = "You are ravenous.";
		else if (who.getSustenance() <= 400 && who.getSustenance() > 300) hungry = "Your stomach hurts.";
		else if (who.getSustenance() <= 300 && who.getSustenance() > 200) hungry = "You feel faint.";
		else if (who.getSustenance() <= 200 && who.getSustenance() > 100) hungry = "Your vision dims... you need food!";
		else if (who.getSustenance() <= 100 && who.getSustenance() > 0) hungry = "You are starving.";
		else if (who.getSustenance() == 0) hungry = "You are STARVING.";
		else hungry = "You are bloated.";
		
		sb.append(hungry);
		//sb.append(getStatTable(actor));
		/*
		 sb.append("Stat\t\tPhysical\tMental\n");
		 sb.append("Strength\t"+who.getPhysicalStrength()+"\t\t"+who.getMentalStrength()+"\n");
		 sb.append("Dexterity\t"+who.getPhysicalDexterity()+"\t\t"+who.getMentalDexterity()+"\n");
		 sb.append("Constitution\t"+who.getPhysicalConstitution()+"\t\t"+who.getMentalConstitution()+"\n");						
		 sb.append("Charisma\t"+who.getPhysicalCharisma()+"\t\t"+who.getMentalCharisma()+"\n");
		 */
		actor.notice(sb.toString());
		
	}
	
	/*
	public String getStatTable(Player who) {
		
		Table t = new Table();
		t.addHeader("Stat", 14, Table.ALIGN_MIDDLE);
		t.addHeader("Physical", 8, Table.ALIGN_MIDDLE);
		t.addHeader("Mental", 8, Table.ALIGN_MIDDLE);
		
		t.addRow();
		t.addColumn("Strength", 14, Table.ALIGN_LEFT);
		t.addColumn(Integer.toString(who.getPhysicalStrength()), 8, Table.ALIGN_RIGHT);
		t.addColumn(Integer.toString(who.getMentalStrength()), 8, Table.ALIGN_RIGHT);
		
		t.addRow();
		t.addColumn("Dexterity", 14, Table.ALIGN_LEFT);
		t.addColumn(Integer.toString(who.getPhysicalDexterity()), 8, Table.ALIGN_RIGHT);
		t.addColumn(Integer.toString(who.getMentalDexterity()), 8, Table.ALIGN_RIGHT);
		
		t.addRow();
		t.addColumn("Constitution", 14, Table.ALIGN_LEFT);
		t.addColumn(Integer.toString(who.getPhysicalConstitution()), 8, Table.ALIGN_RIGHT);
		t.addColumn(Integer.toString(who.getMentalConstitution()), 8, Table.ALIGN_RIGHT);
		
		t.addRow();
		t.addColumn("Charisma", 14, Table.ALIGN_LEFT);
		t.addColumn(Integer.toString(who.getPhysicalCharisma()), 8, Table.ALIGN_RIGHT);
		t.addColumn(Integer.toString(who.getMentalCharisma()), 8, Table.ALIGN_RIGHT);
		
		return t.render();
	}*/
}
