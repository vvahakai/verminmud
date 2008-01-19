/*
 * Created on 2.4.2005
 */
package org.vermin.world.quest;

import java.util.HashMap;

import org.vermin.mudlib.Behaviour;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.quest.DefaultQuestImpl;
import org.vermin.mudlib.quest.Journal;
import org.vermin.util.Functional.Predicate;

/**
 * Implements a simple quest where the Master of Mercenaries
 * of the imperial infantry sends a player to search and
 * destroy some monsters in the outworld.
 * 
 * @author Jaakko Pohjamo
 */
public class OutworldBattleQuest extends DefaultQuestImpl {
	
	private String briefing;
	private String debriefing;
	private String info;
	
	private HashMap<String,Integer> killObjectives;
	
	// Maps monster id to a description
	private HashMap<String,String> monsterDescriptions;
	
	public OutworldBattleQuest() {
	}
	
	
	public String getBriefing() {
		return briefing;
	}
  
	public String getDebriefing() {
		return debriefing;
	}
	
	public String getTitle() {
		return "A mission for the Imperial Mercenary Force";
	}
	
	public String getDescription() {
		return briefing;
	}
	
	
	
	public void abandonQuest(Player who) {
		super.abandonQuest(who);
		who.notice("Sod this for a game of soldiers! You tear up the mercenary order chit.");
	}

	

	@Override
	public void dies(QuestInfo qi, Living victim, Living killer) {
		OutworldBattleJournal obj = (OutworldBattleJournal) qi.journal;
		System.out.println("KILLER: "+killer+", VICTIM: "+victim+", qi.player: "+qi.player);
		
		if(killer == qi.player)
			obj.noteSlaying(victim.getId());
	}


	@Override
	protected Journal makeJournal(Player who) {
		OutworldBattleJournal obj = new OutworldBattleJournal();
		for(String id : killObjectives.keySet())
			obj.addKillObjective(id, killObjectives.get(id));
		return obj;
	}
	
	/**
	 * A predicate for filtering behaviours that are the
	 * quest behaviours of this type of quest.
	 * @return
	 */
	public static Predicate<Behaviour> thisQuestType() {
		return new Predicate<Behaviour>(){
			@Override
			public boolean call(Behaviour arg) {
				return arg instanceof QuestBehaviour &&
					((QuestBehaviour)arg).getQuest() instanceof OutworldBattleQuest;
			}};
	}


	@Override
	public String describeObjectives(Player player, Journal journal) {
		StringBuilder sb = new StringBuilder();
		
		OutworldBattleJournal obj = (OutworldBattleJournal) journal;
		HashMap<String,OutworldBattleJournal.Entry> objectives = obj.getKillObjectives();
		for(String key : objectives.keySet()) {
			String desc = monsterDescriptions.get(key);
			OutworldBattleJournal.Entry objective = objectives.get(key);
			sb.append("Kill ");
			sb.append(objective.required);
			sb.append(" ");
			sb.append(desc);
			sb.append(". ");
			if(objective.killed < objective.required) {
				sb.append("(");
				sb.append(objective.required-objective.killed);
				sb.append(" more)\n");
				sb.append(info);
			} else
				sb.append("&B4;[Complete]&;");
			sb.append("\n");
		}
		return sb.toString();
	}

	
}
