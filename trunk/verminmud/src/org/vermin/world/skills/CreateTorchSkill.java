/* CreateTorchSkill.java
		VV
	
	Skill to create torches that give light. The player must be outdoors to use this skill.	
*/
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.world.items.Torch;


public class CreateTorchSkill extends BaseSkill {

	public SkillType[] getTypes() {
		return new SkillType[0];
	}

	public String getName() {
		return "create torch";
	}

	/* Try to use this skill. */
	public boolean tryUse(Living who, MObject target) {
		if(!who.getRoom().isOutdoor())
		{
			who.notice("There are no materials here to create a torch from.");
			return false;
		}
		return true;
	}

   public int getTickCount() {
      return 4 + Dice.random(4);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		Torch torch = new Torch();
		String message = "";

		if(success > 0)
		{
				int quality = success;
				torch.setFuel(quality);

				if(quality < 10)
					message = "You gather some twigs and create a pathetic torch.";
				else if(quality < 25)
					message = "You manage to create a decent looking torch.";
				else if(quality < 75)
					message = "After some hunting for materials, you have created a torch that will burn for a long time.";
				else if(quality < 95)
					message = "You have created a torch that surpasses the specifications of the Torchmakers association of VerminCity.";
				else 
					message = "You have created a torch fit for KINGS!";

				who.add(torch);
		}
		else
			message = "You fail to create a torch";

		who.notice(message);
	}
}
