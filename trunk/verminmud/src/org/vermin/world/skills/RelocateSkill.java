package org.vermin.world.skills;


import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.util.Print;

public class RelocateSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.REMOTE, SkillTypes.TELEPORTATION };
	
	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Ye can run but ye can't hide darling.";

	public String getName() {
		return "relocate";
	}

	public boolean tryUse(Living who, MObject target) {
		return 
			hasLivingTarget(who, target);
	}

	public int getCost(SkillUsageContext suc) { return 52; }

   public int getTickCount() {
      return 2 + Dice.random(1);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Living target = (Living) suc.getTarget();
		int success = suc.getSkillSuccess();

		if(success > 0) {

			if(!who.getRoom().mayTeleport(who) || !target.getRoom().mayTeleport(who)) {
				who.notice("You chant '"+spellWords+"' but the ether will not bend to your will.");
				who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"'.");
			} else {
				if(!World.isLoggedIn(target.getName())) {
					who.notice(Print.capitalize(target.getName())+" is no longer present.");
					return;
				}

				who.notice("You chant '"+spellWords+"' and teleport to " + target.getName() + ".");
				who.getRoom().notice(who, who.getName() + " disappears.");
				target.getRoom().notice(who, who.getName() + " materializes before your eyes.");
				
				who.getRoom().remove(who);
				//who.setRoom(target.getRoom());
				//who.setParent(target.getRoom());
				target.getRoom().add(who);
			}

		} else {
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
