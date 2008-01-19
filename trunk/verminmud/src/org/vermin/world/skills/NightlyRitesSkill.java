package org.vermin.world.skills;
import org.vermin.mudlib.Damage;
public class NightlyRitesSkill extends Offensive {
   public NightlyRitesSkill() {
     spCost = 512;
     rounds = 7;
     spellWords = "Ooo Scythe of the Reaper!";
     damageType = Damage.Type.MAGICAL;
     damage = 7223;
     name = "nightly rites";
   }}
