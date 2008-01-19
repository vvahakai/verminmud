package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class SigilOfHolyLightSkill extends Offensive {
   public SigilOfHolyLightSkill() {
     spCost = 263;
     rounds = 4;
     spellWords = "Extra ecclesiam nulla salus!";
     damageType = Damage.Type.MAGICAL;
	 /* FIXME: t�m� tekee sitten vain unlifeille damaa */
     damage = 826;
     name = "sigil of holy light";
	 skillTypes = new SkillType[] { SkillTypes.TEMPLAROFFENSIVE, SkillTypes.DIVINE, SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.LOCAL };
   }
}
