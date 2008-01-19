package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
public class DeathWishSkill extends Offensive {
   public DeathWishSkill() {
     spCost = 357;
     rounds = 4;
     spellWords = "Asf morf zocra'pfee zocra'pfee FBER!";
     damageType = Damage.Type.MAGICAL;
     damage = 1623;
     name = "death wish";
	skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.LOCAL };

   }}
