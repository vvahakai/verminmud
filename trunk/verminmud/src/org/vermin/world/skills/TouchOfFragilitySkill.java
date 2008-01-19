package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class TouchOfFragilitySkill extends Offensive {
   public TouchOfFragilitySkill() {
     spCost = 400;
     rounds = 5;
     spellWords = "#null";
     damageType = Damage.Type.PHYSICAL;
     damage = 1;
     name = "touch of fragility";
   }}
