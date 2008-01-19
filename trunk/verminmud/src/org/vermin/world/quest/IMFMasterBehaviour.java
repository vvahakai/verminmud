/*
 * Created on 27.5.2005
 */
package org.vermin.world.quest;

import java.util.Collection;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.Tick;
import org.vermin.mudlib.behaviour.BehaviourAdapter;
import org.vermin.mudlib.quest.DefaultQuestImpl.QuestBehaviour;
import org.vermin.util.DelayedAction;
import org.vermin.util.Print;

public class IMFMasterBehaviour extends BehaviourAdapter {
	
	
	private Collection<OutworldBattleQuest> quests;
	
	public IMFMasterBehaviour() {
	}
	
	private OutworldBattleQuest getQuest(Player who) {
		QuestBehaviour qb = (QuestBehaviour) who.findBehaviour(OutworldBattleQuest.thisQuestType());
		return qb==null ? null : (OutworldBattleQuest) qb.getQuest();
	}
	
	public void arrives(final Living who, Exit e) {
		
		if(!(who instanceof Player)) return;
		
		boolean hasQuest = false;
		boolean isComplete = false;
		
		final OutworldBattleQuest quest = getQuest((Player)who);
		final OutworldBattleJournal journal = quest == null ? null : (OutworldBattleJournal) quest.getJournal(who);
		
		if(journal == null) {
			// player does not have a quest, try to recruit people randomly :) 
			if(Dice.random() > 80) {
				new DelayedAction(1, Tick.BATTLE) {
					public void action() {
						owner.getRoom().say(owner, "Greetings "+Print.capitalize(who.getName())+"! Have you considered a career in the Imperial Mercenary Force?");
					}
				};
			}
		
		} else if(!journal.isComplete()) {
			// player has quest which is not yet completed
			new DelayedAction(1, Tick.BATTLE) {
				public void action() {
					owner.getRoom().say(owner, "What are you doing here, "+Print.capitalize(who.getName())+"? Your task remains unfinished!");
				}
			};
			
		} else {
			// player has a completed quest
			
			quest.completeQuest((Player)who);
			
			new DelayedAction(1, Tick.BATTLE) {
				public void action() {
					owner.getRoom().say(owner, quest.getDebriefing());
				}
			};
			
			((Player) who).addAvailableTitle("Mercenary");
			// TODO: perhaps reward the player?
			
		}
	}
	
	public void asks(Living asker, Living target, String subject) {
		if(!(asker instanceof Player)) return;
		
		Room r = target.getRoom();
		if(!subject.equalsIgnoreCase("quest") && !subject.equalsIgnoreCase("quests")) {
		    r.say(target, "Oh, I would't know anything about that, "+Print.capitalize(asker.getName())+".");
			return;
		}
		
		boolean hasQuest = false;
		boolean isComplete = false;
		final OutworldBattleQuest quest = getQuest((Player)asker);
		final OutworldBattleJournal journal = quest == null ? null : (OutworldBattleJournal) quest.getJournal(asker);
				
		
		/* Do not start a new quest if a journal exists. */
		if(journal != null) {
			r.say(target, "But "+asker.getName()+", you already have your assignment!");
			return;
		}
		
		r.say(target, "Thank you for your interest, "+asker.getName()+". The Imperial Mercenary Force needs bold adventuring "+asker.getRace().getName()+"s like you!");
			
		asker.notice("&B2;The Master of Mercenaries salutes, hands you an official order chit and says: 'Remember, it's "+Print.addArticle(asker.getRace().getName())+"'s life in the Imperial Mercenary Force!'&;");
		r.notice(asker, "The Master of Mercenaries salutes "+asker.getName()+", hands "+asker.getObjective()+" an official order chit and says: 'Remember, it's "+Print.addArticle(asker.getRace().getName())+"'s life in the Imperial Mercenary Force!'");
		
		OutworldBattleQuest[] available = (OutworldBattleQuest[]) quests.toArray(new OutworldBattleQuest[quests.size()]);
		OutworldBattleQuest obq = available[Dice.random(available.length)-1];
		obq.startQuest((Player) asker);
	}
	
	public void says(Living who, String what) {
		if(who == owner) {
			return;
		}
		if(what.toLowerCase().indexOf("quest") != -1) {
			who.getRoom().say(owner, "If you are looking for high adventure, the Imperial Mercenary Force is the place for you! Ask me for a quest!");
		}
	}
}
