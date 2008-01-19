/* PrepareShemannaSkill.java
		12.1.2005 MV

	Skill to create shemanna powder, which can be used to heal yourself.
*/
package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.world.items.Shemanna;


public class PrepareShemannaSkill extends BaseSkill {

	public SkillType[] getTypes() {
		return new SkillType[0];
	}

	public String getName() {
		return "prepare shemanna";
	}

	/* Try to use this skill. */
	public boolean tryUse(Living who, MObject target) {
		if(!who.getRoom().isOutdoor())
		{
			who.notice("There are no herbs to create the powder in this area.");
			return false;
		}
		return true;
	}

   public int getTickCount() {
      return 5 + Dice.random(3);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		Shemanna shemanna = new Shemanna();
		String message = new String();

		if(success > 0)
		{
				int quality = success;
				shemanna.setPower(quality+(Math.max(0, who.checkSkill("arcane knowledge")))/4);

				if(quality < 10)
					message = "You find some herbs and make a lousy mix of shemanna.";
				else if(quality < 25)
					message = "You manage to create a pouch of decent looking shemanna powder.";
				else if(quality < 75)
					message = "You find good herbs, and succesfully create a fine blend of shemanna powder.";
				else if(quality < 95)
					message = "You find almost all of the herbs a perfect shemanna would include and make awesome an job of preparing the shemanna.";
				else 
					message = "You prepare shemanna that would make VerminCity guild of Alchemists jealous!";

				who.add(shemanna);
		}
		else
			message = "You fail to find herbs to prepare shemanna powder.";

		who.notice(message);
	}
}
