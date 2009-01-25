
/**
 * FriendsCommand.java
 * 21.9.2003 Tatu Tarvainen
 * 20.2.2004 Ville V�h�kainu
 *
 * Command to list, add and remove your friends.
 */
package org.vermin.world.commands;

import org.vermin.mudlib.*;
import org.vermin.util.Table;
import org.vermin.wicca.Session;
import org.vermin.driver.*;

import java.util.HashSet;
import java.util.Iterator;

public class FriendsCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] {
			"friends => showFriends(actor)",
			"friends -a (\\w+) (\\w+) => addFriend(actor, 1 ,2)",
			"friends -a (\\w+) => addFriend(actor, 1)",
			"friends -r (\\w+) (\\w+) => removeFriend(actor, 1, 2)",
			"friends -r (\\w+) => removeFriend(actor, 1)",
			"friends -l (\\w+) => listFriends(actor, 1)",
			"friends -l => listFriends(actor)",
			"friends (\\w+) => showFriends(actor, 1)"
		};
	}
	
	public void showFriends(Player who) {
		showFriends(who, "friends");
	}

	public void showFriends(Player who, String list) {
		Loader l = Driver.getInstance().getLoader();
		HashSet friends = who.getFriends(list);
		if(friends != null) {
			Iterator it = friends.iterator();
			String name;
			Living tgt;
			boolean tableContainsStuff = false;		

			Table t = new Table();
			t.addHeader("Name", 17, Table.ALIGN_LEFT);
			t.addHeader("Online time", 13, Table.ALIGN_LEFT);
			t.addHeader("Idle time", 13, Table.ALIGN_LEFT);
			t.addHeader("Flags", 7, Table.ALIGN_LEFT);
			t.addHeader("Level", 7, Table.ALIGN_LEFT);

			while(it.hasNext()) {
				name = (String) it.next();
				String id = "players/"+name.toLowerCase();
				if(World.isLoggedIn(name)) {
					try {
						tgt = (org.vermin.mudlib.DefaultPlayerImpl) l.get(id);
						Session c = World.getSession(tgt.getName().toString());
						String onlineTime = formatTime(c.getDuration());
						String idleTime = (c.getIdleTime()<1000) ? "Not idle" : formatTime(c.getIdleTime());
						StringBuffer sb = new StringBuffer();

						if(tgt.findByNameAndType("_party", Types.ITEM) != null) sb.append('P');
						else sb.append(' ');
						if(isIdle(tgt)) sb.append('I');
						else sb.append(' ');
						if(tgt.isDead()) sb.append('D');
						else sb.append(' ');

						t.addRow();
						t.addColumn(tgt.getName(), 17, Table.ALIGN_LEFT);
						t.addColumn(onlineTime, 13, Table.ALIGN_RIGHT);
						t.addColumn(idleTime, 13, Table.ALIGN_RIGHT);
						t.addColumn(sb.toString(), 7, Table.ALIGN_MIDDLE);
						t.addColumn(((Player) tgt).getLevel(), 7, Table.ALIGN_RIGHT);
						tableContainsStuff = true;
					} catch (Exception e) {
						System.err.println(e.toString());
						continue;			
					}
				}
			}
			if(tableContainsStuff)
				who.notice(t.render());
			else who.notice("You have no friends (in-game).");
		}
	}
	
	public boolean isIdle(Living l) {
		Session c = World.getSession(l.getName());
		if(c.getIdleTime() < 60000)
			return false;
		else
			return true;
	}

    protected String formatTime(long time) {
        StringBuffer sb = new StringBuffer();
        long secs = time/1000;

	sb.append((secs >= 86400) ? (secs/86400 +"d ") : "");
        secs %= 86400;
        sb.append((secs >= 3600) ? (secs/3600 +"h ") : "");
        secs %= 3600;
        sb.append((secs >= 60) ? (secs/60 +"m ") : "");
        secs %= 60;
        sb.append((secs != 0) ? (secs +"s") : "");

	return sb.toString();
    }

	public void addFriend(Player who, String name) {
		addFriend(who, name, "friends");
	}

	public void addFriend(Player who, String name, String list) {
		who.addFriend(name, list);
		who.notice("Added "+name+" to your friends '"+list+" 'list");
	}

	public void removeFriend(Player who, String name) {
		removeFriend(who, name, "friends");
	}

	public void removeFriend(Player who, String name, String list) {
		if(who.removeFriend(name, list))
			who.notice("Removed "+name+" from your friends '"+list+" 'list");
	}

	public void listFriends(Player who) {
		listFriends(who,"friends");
	}

	public void listFriends(Player who, String list) {
		Loader l = Driver.getInstance().getLoader();
		HashSet friends = who.getFriends(list);
		if(friends != null) {
			Iterator it = friends.iterator();
			String name;
			Living tgt;
			while(it.hasNext()) {
				name = (String) it.next();
				String id = "players/"+name.toLowerCase();
				try {
					tgt = (org.vermin.mudlib.DefaultPlayerImpl) l.get(id);
				} catch (Exception e) {
					continue;			
				}
				who.notice(tgt.getName()+"\n");
			}
		}
	}

	public void usage(Living who) {
		who.notice("Usage: friends [-a | -r | -l] [name] [list]");
	}

}
