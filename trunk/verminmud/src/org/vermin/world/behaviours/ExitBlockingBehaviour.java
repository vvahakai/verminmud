/*
 * Created on 11.2.2006
 */
package org.vermin.world.behaviours;

import java.util.LinkedList;
import java.util.List;

import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Modifier;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.behaviour.BehaviourAdapter;
import org.vermin.util.Print;

public class ExitBlockingBehaviour extends BehaviourAdapter {

	private class ExitBlockingModifier implements Modifier {
		private Object[] blockedRoomArgument;
		private boolean active = true;
		private String message;
		
		public ExitBlockingModifier(Room current, Exit blocked, Living l) {
			this.blockedRoomArgument = new Object[] { current.getId() };
			this.message = Print.capitalize(l.getName())+" blocks your way to "+blocked.getDirection(current.getId())+".";
		}
		
		public void deActivate() {
			active = false;
		}

		public Object[] getArguments() {
			return blockedRoomArgument;
		}

		public String getDescription() {
			return message;
		}

		public ModifierTypes getType() {
			return ModifierTypes.BARRIER;
		}

		public boolean isActive() {
			return active;
		}

		public int modify(MObject target) {
			return -100;
		}
	}
	
	private String[] directions;
	private List<ExitBlockingModifier> currentBlocks;
	
	public ExitBlockingBehaviour() {
		
	}
	
	public ExitBlockingBehaviour(String[] directions) {
		this.directions = directions;
		this.currentBlocks = new LinkedList();
	}
	
	@Override
	public void arrives(Living who) {
		if(currentBlocks == null) { currentBlocks = new LinkedList(); }
		if(who == owner) {
			currentBlocks.clear();
			
			Room r = owner.getRoom();
			Exit[] exits = r.getExits();
			for(String d : directions) {
				for(Exit e : exits) {
					if(e.getDirection(r.getId()).equals(d)) {
						ExitBlockingModifier ebm = new ExitBlockingModifier(r, e, owner);
						currentBlocks.add(ebm);
						e.addModifier(ebm);
					}
				}
			}
		}
	}

	@Override
	public void dies(Living victim, Living killer) {
		if(victim == owner) {
			clearBlocks();
		}
	}

	@Override
	public void leaves(Living who) {
		if(who == owner) {
			clearBlocks();
		}
	}

	private void clearBlocks() { 
		for(ExitBlockingModifier ebm : currentBlocks) {
			ebm.deActivate();
		}
	}
	
}
