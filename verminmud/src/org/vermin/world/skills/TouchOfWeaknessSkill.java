package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class TouchOfWeaknessSkill extends Offensive {
   public TouchOfWeaknessSkill() {
     spCost = 400;
     rounds = 5;
     spellWords = "#null";
     damageType = Damage.Type.PHYSICAL;
     damage = 1;
     name = "touch of weakness ";
   }}
