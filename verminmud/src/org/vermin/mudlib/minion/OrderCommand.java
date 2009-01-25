package org.vermin.mudlib.minion;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Types;

public class OrderCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
        return new String[] {
				"order ([^\\s]+) to follow => follow(actor, 1)",
				"order ([^\\s]+) to unfollow => unfollow(actor, 1)",	
				"order ([^\\s]+) to front => moveToFront(actor, living 1)",
                "order ([^\\s]+) to list commands => listCommands(actor, living 1)",			
                "order ([^\\s]+) to (\\w+) (.+) => order(actor, 1, 2, 3)",
                "order ([^\\s]+) to (\\w+) => order(actor, 1, 2)",
                "order ([^\\s]+) as (\\w+) => name(actor, living 1, 2)",                
                "minions => minions(actor)"
        };
	}

    public boolean order(Living actor, String target, String command) {
		return order(actor,target,command,"");
    }
	
	public boolean follow(Living actor, String target) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.follow(target);
		return false;		
	}

	public boolean unfollow(Living actor, String target) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.unfollow(target);
		return false;		
	}	
	
    public boolean order(Living actor, String target, String command, String action) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.order(target,command,action);
		return false;
    }
	
	public boolean minions(Living actor) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.minions((Player) actor);
		return false;
	}	
	
	public boolean name(Living actor, Living target, String name) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.name(target,name);
		return false;		
	}
	
	public boolean moveToFront(Living actor, Living target) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.moveToFront(target);
		return false;	
	}
	
	public boolean listCommands(Living actor, Living target) {
		Leash l = (Leash) actor.findByNameAndType("_minion_leash", Types.ITEM);
		if(l != null)
			return l.listCommands(target);
		return false;	
	}
}
