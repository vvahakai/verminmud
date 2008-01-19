package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class GreaterCurseSkill extends Offensive {
   public GreaterCurseSkill() {
     spCost = 388;
     rounds = 5;
     spellWords = "#null";
     damageType = Damage.Type.PHYSICAL;
     damage = 1;
     name = "greater curse";
   }}
