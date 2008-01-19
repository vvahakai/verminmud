/*
 * Created on 29.1.2005
 */
package org.vermin.world.commands;

import java.util.Iterator;

import org.vermin.mudlib.Item;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Types;
import org.vermin.util.Table;
import org.vermin.world.items.GuildItem;

/**
 * This command allows a player to list the guilds
 * he has joined and the levels achieved in those
 * guilds.
 * 
 * @author Jaakko Pohjamo
 */
public class GuildsCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
				"guilds => guilds(actor)"
			};
	}

	public void guilds(Player actor) {
		Iterator items = actor.findByType(Types.TYPE_ITEM);
		boolean hadGuilds = false;
		
		Table t = new Table();
		t.addHeader("Guild", 16, Table.ALIGN_LEFT);
		t.addHeader("Level", 3, Table.ALIGN_LEFT);
		
		while(items.hasNext()) {
			Item it = (Item) items.next();
			if(it instanceof GuildItem) {
				GuildItem git = (GuildItem) it;
				hadGuilds = true;
				t.addRow();
				t.addColumn(git.getGuildName(), 16, Table.ALIGN_LEFT);
				t.addColumn(git.getLevel(), 3, Table.ALIGN_RIGHT);
			}
		}
		
		if(!hadGuilds) {
			actor.notice("You haven't joined any guilds yet.");
		} else {
			actor.notice(t.render());
		}
	}

}
