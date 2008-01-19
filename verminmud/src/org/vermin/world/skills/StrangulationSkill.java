package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class StrangulationSkill extends Offensive {
   public StrangulationSkill() {
     spCost = 52;
     rounds = 3;
     spellWords = "Heh bitch can'tgetair!";
     damageType = Damage.Type.ASPHYXIATION;
     damage = 210;
     name = "strangulation";
   }}
