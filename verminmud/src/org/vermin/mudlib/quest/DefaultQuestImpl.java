package org.vermin.mudlib.quest;

import java.util.Collection;

import org.vermin.mudlib.Behaviour;
import org.vermin.mudlib.DefaultPersistentImpl;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.Skill;
import org.vermin.mudlib.World;
import org.vermin.mudlib.behaviour.BehaviourAdapter;

import static org.vermin.util.Functional.*;

public class DefaultQuestImpl extends DefaultPersistentImpl implements Quest {

	public static class QuestInfo {
		public Journal journal;
		public Player player;
		public QuestInfo(Journal j, Player p) {
			journal = j;
			player = p;
		}
	};
	
	protected String title;
	protected String description;
	
	
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Journal getJournal(Living who) {
		QuestBehaviour qb = (QuestBehaviour) who.findBehaviour(thisQuest());
		if(qb == null) 
			return null;
		return qb.journal;
	}
	
	public void startQuest(Player who) {
		QuestBehaviour qb = new QuestBehaviour(this, makeJournal(who)); 
		who.addBehaviour(qb);
		qb.setOwner(who);
	}

	protected Predicate<Behaviour> thisQuest() {
		final Quest thisQuest = this;
		return new Predicate<Behaviour>() { public boolean call(Behaviour o) {
			return (o instanceof QuestBehaviour &&
					((QuestBehaviour) o).quest == thisQuest);
		}};
	}
	
	public void abandonQuest(Player who) {
		who.removeBehaviour(thisQuest());
	}
	
	public void completeQuest(Player who) {
		who.removeBehaviour(thisQuest());
	}
	protected Journal makeJournal(Player who) {
		return new DefaultJournalImpl(getTitle());
	}
	
	/* Quest callback hooks, to be overwritten by quest logic */
	
	public void  onRegenTick (QuestInfo qi, Living who) {}
	public void onBattleTick (QuestInfo qi, Living who) {}
	public void         asks (QuestInfo qi, Living asker, String subject) {}
	public void         dies (QuestInfo qi, Living victim, Living killer) {}
	public void         says (QuestInfo qi, Living who, String what) {}
	public void     unwields (QuestInfo qi, Living who, Item what) {}
	public void       wields (QuestInfo qi, Living who, Item what) {}
	public void        drops (QuestInfo qi, Living who, Item what) {	}
	public void        takes (QuestInfo qi, Living who, Item what) {}
	public void  startsUsing (QuestInfo qi, Living who, Skill skill) {}
	public void       leaves (QuestInfo qi, Living who, Exit to) {}
	public void      arrives (QuestInfo qi, Living who, Exit from) {}
	public void      arrives (QuestInfo qi, Living who) {}
	public void       leaves (QuestInfo qi, Living who) {}
	
	public static class QuestBehaviour extends BehaviourAdapter {
		
		private Journal journal;
		private DefaultQuestImpl quest;
		
		public QuestBehaviour() {}
		
		public QuestBehaviour(DefaultQuestImpl q, Journal j) {
			quest = q;
			journal = j;
		}
		
		public Quest getQuest() {
			return quest;
		}
		public Journal getJournal() {
			return journal;
		}
		
		public void onRegenTick(Living who) {
			quest.onRegenTick(new QuestInfo(journal,(Player)owner), who);
		}
	
		public void onBattleTick(Living who) {
			quest.onBattleTick(new QuestInfo(journal,(Player)owner), who);
		}
	
		public void asks(Living asker, Living target, String subject) {
			quest.asks(new QuestInfo(journal,(Player)owner), asker, subject);
		}
	
		@Override
		public void dies(Living victim, Living killer) {
			World.log("QuestBehaviour.dies");
			quest.dies(new QuestInfo(journal,(Player)owner), victim, killer);
		}
	
		public void says(Living who, String what) {
			quest.says(new QuestInfo(journal,(Player)owner), who, what);
		}
	
		public void unwields(Living who, Item what) {
			quest.unwields(new QuestInfo(journal,(Player)owner), who, what);
		}
	
		public void wields(Living who, Item what) {
			quest.wields(new QuestInfo(journal,(Player)owner), who, what);
	
		}
	
		public void drops(Living who, Item what) {
			quest.drops(new QuestInfo(journal,(Player)owner), who, what);
		}
	
		public void takes(Living who, Item what) {
			quest.takes(new QuestInfo(journal,(Player)owner), who, what);
		}
	
		public void startsUsing(Living who, Skill skill) {
			quest.startsUsing(new QuestInfo(journal,(Player)owner), who, skill);
		}
	
		public void leaves(Living who, Exit to) {
			quest.leaves(new QuestInfo(journal,(Player)owner), who, to);
		}
	
		public void arrives(Living who, Exit from) {
			quest.arrives(new QuestInfo(journal,(Player)owner), who, from);
		}
	
		public void arrives(Living who) {
			quest.arrives(new QuestInfo(journal,(Player)owner), who);
		}
	
		public void leaves(Living who) {
			quest.leaves(new QuestInfo(journal,(Player)owner), who);
		}
		
		public void command(Object ... args) {
			if(args != null && args.length == 2 && "quest-behaviour-list".equals(args[0]))
				((Collection<QuestBehaviour>)args[1]).add(this);
		}
	}
	
	public static Quest getQuestByClass(Player p, final Class<?> c) {
		QuestBehaviour qb = (QuestBehaviour) p.findBehaviour(new Predicate<Behaviour>() {
			@Override
			public boolean call(Behaviour arg) {
				return (arg instanceof QuestBehaviour && ((QuestBehaviour)arg).quest.getClass() == c);
			}});
		return qb == null ? null : qb.quest; 
	}

	public String describeObjectives(Player player, Journal journal) {
		// Default implementation can't describe objectives
		return null;
	}
}
