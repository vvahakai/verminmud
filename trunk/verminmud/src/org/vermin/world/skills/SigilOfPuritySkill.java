package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class SigilOfPuritySkill extends Offensive {
   public SigilOfPuritySkill() {
     spCost = 212;
     rounds = 3;
     spellWords = "Extra ecclesiam nulla salus!";
     damageType = Damage.Type.MAGICAL;
	 /* FIXME: t�m� tekee sitten vain unlifeille damaa */
     damage = 615;
     name = "sigil of purity";
	 skillTypes = new SkillType[] { SkillTypes.TEMPLAROFFENSIVE, SkillTypes.DIVINE, SkillTypes.OFFENSIVE, SkillTypes.FIRE, SkillTypes.LOCAL };
   }
}
