package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class ForceGraspSkill extends Offensive {
   public ForceGraspSkill() {
     spCost = 52;
     rounds = 2;
     spellWords = "Kgah eufa a,nas";
     damageType = Damage.Type.PHYSICAL;
     damage = 210;
     name = "force grasp";
   }}
