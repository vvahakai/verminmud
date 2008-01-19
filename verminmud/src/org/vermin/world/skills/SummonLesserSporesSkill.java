package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class SummonLesserSporesSkill extends Offensive {
   public SummonLesserSporesSkill() {
     spCost = 120;
     rounds = 3;
     spellWords = "Awen alsf Hisssa Sauan";
     damageType = Damage.Type.POISON;
     damage = 421;
     name = "summon lesser spores";
   }}
