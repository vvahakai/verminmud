/* CreateMoneySkill.java
	22.9.2003	VV
	
	skill for creating lots of money
*/
package org.vermin.world.skills;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.DefaultMoneyImpl;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;

public class CreateMoneySkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.ARCANE };
	
	public SkillType[] getTypes() { return skillTypes; }	

	private String spellWords = "Allasf ranamas";

	public String getName() {
		return "create money";
	}

	public int getCost(SkillUsageContext suc) { return 500; } 

   public int getTickCount() {
      return 25 + Dice.random(25); 
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		MObject target = suc.getTarget();
		int success = suc.getSkillSuccess();

		

		if(success > 0)
		{

				int quality = success;
                Money mi = new DefaultMoneyImpl();
				mi.addValue((long) ( (quality * 3.141596) * ( who.getMentalDexterity() + who.getMentalStrength() ) ));

				who.notice("You chant '"+spellWords+"' and a pile of coins magically appears.");
				who.notice("You create " + mi.getWorth());
				who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' and creates a pile of coins.");
				mi.moveAll(who.getMoney());
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
