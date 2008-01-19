/*
 * Created on 7.1.2006
 */
package org.vermin.world.skills;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;

public class ElectricBurst extends AreaOffensive {

	public ElectricBurst() {
	     spCost = 321;
	     rounds = 8;
	     spellWords = "ezem ezem ezem";
	     damageType = Damage.Type.ELECTRIC;
	     damage = 1322;
	     name = "electric burst";
		  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.ELECTRIC, SkillTypes.AREA };

	}
}
