package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class VacuumBlastSkill extends Offensive {
   public VacuumBlastSkill() {
     spCost = 246;
     rounds = 4;
     spellWords = "Zeme asphyxiation asm!";
     damageType = Damage.Type.ASPHYXIATION;
     damage = 812;
     name = "vacuum blast";
   }}
