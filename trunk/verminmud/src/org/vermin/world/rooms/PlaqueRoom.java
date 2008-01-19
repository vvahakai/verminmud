/*
 * Created on 13.5.2006
 */
package org.vermin.world.rooms;

import org.vermin.mudlib.Plaque;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexRoom;
import org.vermin.mudlib.World;
import org.vermin.mudlib.Plaque.PlaqueEntry;
import static org.vermin.mudlib.Plaque.Type;
import org.vermin.util.Print;
import org.vermin.util.Table;

public class PlaqueRoom extends RegexRoom {

	private Plaque plaque;
	
	private Plaque.Type type;
	private String plaqueTitle;
	private String plaqueValueTitle;
	
	private int showTop;
	private int showBefore;
	private int showAfter;
	
	public PlaqueRoom() {
		plaque = World.getPlaque();
	}
	
	public String[] getDispatchConfiguration() {
		return new String[] {
				"look at plaque => viewPlaque(actor)",
				"look at (.+) on plaque => viewPlaque(actor, 1)"
		};
	}
	
	public void viewPlaque(Player observer) {
		view(observer, observer.getId(), observer.getName());
	}
	
	public void viewPlaque(Player observer, String who) {
		String playerId = World.getPlayerIdByName(who);
		if(playerId == null) {
			observer.notice("You scan the plaque, but find no mention of '"+who+"'.");
		} else {
			view(observer, playerId, who);
		}
	}
	
	public void view(Player observer, String playerId, String playerName) {
		PlaqueEntry[] entries = plaque.showTop(showTop, type);
		
		Table t = new Table();
		t.addHeader("#", 4, Table.ALIGN_RIGHT);
		t.addHeader(plaqueTitle, 35, Table.ALIGN_MIDDLE);
		t.addHeader(plaqueValueTitle, 15, Table.ALIGN_RIGHT);
		t.addRow();
		
		boolean found = false;
		int index = 1;
		for(PlaqueEntry e : entries) {
			t.addColumn(Integer.toString(index), 4, Table.ALIGN_RIGHT);
			t.addColumn(e.getName());
			if(type == Type.DELTA) {
				t.addColumn(Print.humanReadable(e.delta()), 15, Table.ALIGN_RIGHT);
			} else {
				t.addColumn(Print.humanReadable(e.getExperience()), 15, Table.ALIGN_RIGHT);				
			}
			t.addRow();
			if(playerName.equals(e.getName())) {
				found = true;
			}
			index++;
		}
		
		if(found) {
			observer.notice(t.render());
			return; 
		}
		
		
		/*t = new Table();
		t.addHeader("#", 4, Table.ALIGN_RIGHT);
		t.addHeader(plaqueTitle, 35, Table.ALIGN_MIDDLE);
		t.addHeader(plaqueValueTitle, 15, Table.ALIGN_RIGHT);*/
		//t.addRow();
		t.addColumn("...", 4, Table.ALIGN_RIGHT);
		t.addColumn("");
		t.addColumn("", 15, Table.ALIGN_RIGHT);
		t.addRow();
		entries = plaque.showPlayer(playerId, showBefore, showAfter, type);
		index = plaque.find(playerId, type)-showBefore;
		boolean foundOnPlaque = false;
		for(PlaqueEntry e : entries) {
			foundOnPlaque = true;
			t.addColumn(Integer.toString(index), 4, Table.ALIGN_RIGHT);
			t.addColumn(e.getName());
			t.addColumn(Print.humanReadable(e.getExperience()), 15, Table.ALIGN_RIGHT);
			t.addRow();
			index++;
		}
		if(foundOnPlaque) {
			observer.notice(t.render());
		}
	}
	
	
}
