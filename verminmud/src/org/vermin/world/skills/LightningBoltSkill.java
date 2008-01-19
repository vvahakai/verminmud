package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class LightningBoltSkill extends Offensive {
   public LightningBoltSkill() {
     spCost = 120;
     rounds = 3;
     spellWords = "Awen alsf Aueh pookojoko!";
     damageType = Damage.Type.ELECTRIC;
     damage = 421;
     name = "lightning bolt";
   }}
