package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class ForceFistSkill extends Offensive {
   public ForceFistSkill() {
     spCost = 210;
     rounds = 4;
     spellWords = "koro ragha tko";
     damageType = Damage.Type.PHYSICAL;
     damage = 700;
     name = "force fist";
	  skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.LOCAL };

   }}
