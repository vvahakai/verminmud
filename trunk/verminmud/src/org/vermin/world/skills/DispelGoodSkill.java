package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class DispelGoodSkill extends Offensive {
   public DispelGoodSkill() {
     spCost = 100;
     rounds = 4;
     spellWords = "#null";
     damageType = Damage.Type.MAGICAL;
     damage = 510;
     name = "dispel good";
   }}
