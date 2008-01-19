package org.vermin.mudlib.commands;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.RoomProperty;
import org.vermin.mudlib.World;
import org.vermin.mudlib.outworld.OutworldRoom;

public class ClimbCommand extends RegexCommand {

	public String[] getDispatchConfiguration() {
		return new String[] { "climb (.*) => climb(actor, any 1)" };
	}

	public void climb(Living actor, MObject target) {
		if(target instanceof Living) {
			actor.notice("You try to climb on top of "+target.getName() +".");
			((Living) target).notice(actor.getName()+" tries to climb on top you.");
		}
		else if(target instanceof Item) {
			String royaltitle = actor.getPronoun().equals("she") ? "queen" : "king";
			actor.notice("You climb on top of "+target.getName()+" and proclaim yourself the "+royaltitle+" of the world.");
			actor.getRoom().notice(actor, actor.getName()+" climbs on top of "+target.getName()+" and proclaims "+actor.getObjective()+"self the "+royaltitle+" of the world.");
		}
		else if(target instanceof Exit) {
			if(actor.getRoom() instanceof OutworldRoom) {
				Exit e = (Exit) target;
				OutworldRoom startRoom = (OutworldRoom) actor.getRoom();
				Room targetRoom = (Room) World.get(e.getTarget(startRoom.getId()));
				if(targetRoom instanceof OutworldRoom && targetRoom.provides(RoomProperty.REQUIRES_AVIATION)) {
					// TODO: If someone implements a flying castle, take that into account!
					OutworldRoom finalRoom = findTarget(actor, (OutworldRoom) targetRoom);
					startRoom.remove(actor);
					startRoom.leave(actor, e);
					actor.setParent(finalRoom);
					finalRoom.enter(actor, finalRoom.getExitTo("u"));
					actor.modifySustenance(-10);
					
				}
				else {
					actor.notice("You cannot climb there");
				}
			}
			else {
				actor.notice("You cannot climb there");
			}
		}
	}
	
	private OutworldRoom findTarget(Living actor, OutworldRoom owr) {
		while(owr.provides(RoomProperty.REQUIRES_AVIATION)) {
			if(Dice.random() > 70) {
				actor.notice("You lose your footing and fall.");
				return owr;
			}
			Room targetRoom = (Room) World.get(owr.getExitTo("d").getTarget(owr.getId()));
			if(targetRoom instanceof OutworldRoom) {
				owr = (OutworldRoom) targetRoom;
			}
		}
		actor.notice("You manage to get down safely.");
		return owr;
	}
}
