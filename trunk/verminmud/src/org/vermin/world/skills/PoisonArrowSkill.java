package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class PoisonArrowSkill extends Offensive {
   public PoisonArrowSkill() {
     spCost = 52;
     rounds = 3;
     spellWords = "Bite bitch purpleface!";
     damageType = Damage.Type.POISON;
     damage = 210;
     name = "poison arrow";
   }}
