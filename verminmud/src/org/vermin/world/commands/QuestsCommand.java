/*
 * Created on 2.4.2005
 */
package org.vermin.world.commands;

import java.util.ArrayList;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.World;
import org.vermin.mudlib.quest.Journal;
import org.vermin.mudlib.quest.Quest;
import org.vermin.mudlib.quest.DefaultQuestImpl.QuestBehaviour;

public class QuestsCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"quests => listQuests(actor)",
				"quests (\\d)+ => questInfo(1, actor)",
				"quests abandon (\\d) => questAbandon(1, actor)",
				"quests test => testQuest(actor)"
		};
	}
	
	public void testQuest(Player who) {
		Quest q = (Quest) World.get("common/quests/outworld_imf_ants");
		q.startQuest(who);
	}
	
	public static ArrayList<QuestBehaviour> getQuestBehaviours(Living who) {
		ArrayList<QuestBehaviour> qbs = new ArrayList<QuestBehaviour>();
		who.command("quest-behaviour-list", qbs);
		return qbs;
	}
	public void listQuests(Living who) {
		
		who.notice("Active quests:");
		int questIndex = 1;
		
		for(QuestBehaviour qb : getQuestBehaviours(who)) {
			Journal j = (Journal) qb.getJournal();
			who.notice(questIndex+". "+qb.getQuest().getTitle()+"");
			questIndex++;
		}
		if(questIndex == 1) {
			who.notice("none.");
		}
		
		who.notice("\nType 'quests <number>' to get information about a spesific quest.");
	}
	
	private QuestBehaviour getQuestByIndex(int index, Living who) {
		ArrayList<QuestBehaviour> qbs = getQuestBehaviours(who);
		
		if(index < 1 || index > qbs.size()) {
			who.notice("No such quest.");
			return null;
		}
		
		return qbs.get(index-1);
		
	}
	public void questInfo(int index, Living who) {
		
		QuestBehaviour qb = getQuestByIndex(index, who);
		if(qb == null) return;
		
		Journal j = (Journal) qb.getJournal();
		String q = qb.getQuest().getTitle();
		who.notice("Quest: '&B2;"+q+"&;'");
		who.notice("Description: "+qb.getQuest().getDescription());
		String objectives = qb.getQuest().describeObjectives((Player) who, j);
		if(objectives != null) {
			who.notice("Current objectives:");
			who.notice(objectives);
		}
	}
	
	public void questAbandon(int index, Living who) {
		
		QuestBehaviour qb = getQuestByIndex(index, who);
		if(qb == null) return;
		
		qb.getQuest().abandonQuest((Player) who);
	}
}
