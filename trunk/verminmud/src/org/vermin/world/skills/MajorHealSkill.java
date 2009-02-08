package org.vermin.world.skills;
public class MajorHealSkill extends GenericHealing {
   public MajorHealSkill() {
     spCost = 85;
     rounds = 3;
     spellWords = "Orutom urbuzun";
     healAmount = 300;
     name = "major heal";
	 mentalDexEffect = true;
   }
   
   }
