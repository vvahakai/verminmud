package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class VacuumBoltSkill extends Offensive {
   public VacuumBoltSkill() {
     spCost = 120;
     rounds = 3;
     spellWords = "Awen alsf Gasp Gasp";
     damageType = Damage.Type.ASPHYXIATION;
     damage = 421;
     name = "vacuum bolt";
   }}
