package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;

public class TeleportSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.TELEPORTATION };
	boolean verminCityHasACentralSquare = true;

	public SkillType[] getTypes() { return skillTypes; }		

	private String spellWords = "Oh lemme land safely!";

	public String getName() {
		return "teleport";
	}

	public boolean tryUse(Living who, MObject target) {
		return verminCityHasACentralSquare;
	}

	public int getCost(SkillUsageContext suc) { return 150; }

	public int getTickCount() {
		return 10 + Dice.random(5);
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		int success = suc.getSkillSuccess();

		if(success > 0)
		{
				Room targetRoom = (Room) World.get("city:0,0,0");

				if(targetRoom == null || !who.getRoom().mayTeleport(who) || !targetRoom.mayTeleport(who)) {
					who.notice("You chant '"+spellWords+"' but the ether will not bend to your will.");
					who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"'.");
				} else {
					who.notice("You chant '"+spellWords+"' and teleport to Vermincity.");
					who.getRoom().notice(who, who.getName() + " disappears.");
					targetRoom.notice(who, who.getName() + " materializes before your collective eyes.");

					who.getRoom().remove(who);
					//who.setRoom(targetRoom);
					//who.setParent(targetRoom);
					targetRoom.add(who);
				}
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
