/* EqCommand.java
	16.2.2002
	
*/

package org.vermin.world.commands;

import org.vermin.mudlib.*;

import java.util.Vector;

public class EqCommand extends TokenizedCommand {

	public void run(Living who, Vector params) {
		run(who, who, params);
	}
	public void run(Living who, Living outputTo, Vector params) {
		if(params.size() == 2 && params.get(1).equals("free")) {
			showFree(who);
			return;
		}

		Item[] wielded = who.getWieldedItems(true);
		Item[] worn = who.getWornItems();
		Race r = who.getRace();
		StringBuffer sb = new StringBuffer();

		boolean first = true;
		boolean previouslyShown = false;
		for(int i=0; i<wielded.length; i++) {
			if(wielded[i] != null && wielded[i] != r.getLimb(i) && wielded[i].isVisible()) {
				if(first) {
					sb.append("Equipment:\n");
					first = false;
				}
				sb.append("Wielded in "+r.getLimbName(i)+": "+wielded[i].getDescription()+"\n");
			}
		}
		
		if(first != false) {
			String msg = who.getBattleStyle().describeWeaponProficiency();
			sb.append(msg);
		}

		for(int i=0; i<worn.length; i++) {
			if(worn[i] != null) {
				previouslyShown = false;
				if(first) {
					sb.append("Equipment:\n");
					first = false;
				}
				String delim = "";
				StringBuffer slotb = new StringBuffer();
				for(int j=0;j<worn.length;j++) {
					if(worn[i] == worn[j]) {
						if(j < i) previouslyShown = true;
						slotb.append(delim + r.getSlots()[j].name);
						delim = ", ";
					}
				}
				if(!previouslyShown)
					sb.append("Worn in "+slotb.toString()+": "+worn[i].getDescription()+"\n");
			}
		}
		
		if(first) sb.append("No equipment.");
		outputTo.notice(sb.toString());
		
	}


	private void showFree(Living who) {
		Item[] wielded = who.getWieldedItems(true);
		Item[] worn = who.getWornItems();
		Race r = who.getRace();
		StringBuffer sb = new StringBuffer();

		boolean firstwield = true;
		boolean firstslot = false;
		String delim = "";
		for(int i=0; i<wielded.length; i++) {
			if(wielded[i] == null) {
				if(firstwield) {
					sb.append("Free limbs: ");		
					firstwield = false;
				}
				sb.append(delim+r.getLimbName(i));
				delim = ", ";
			}
		}

		if(!firstwield) {
			sb.append("\n");
		}

		delim = "";

		for(int i=0; i<worn.length; i++) {
			if(worn[i] == null) {
				if(firstslot) {
					sb.append("Free slots: ");
					firstslot = false;
				}
				sb.append(delim+r.getSlots()[i].name);
				delim = ", ";
			}
		}

		if(!firstslot) {
			sb.append("\n");
		}
		
		if(firstwield && firstslot) sb.append("You have no free slots.");
		who.notice(sb.toString());

	}
}
