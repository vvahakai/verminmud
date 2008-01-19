package org.vermin.world.skills;
public class CureSeriousWoundsSkill extends GenericHealing {
   public CureSeriousWoundsSkill() {
     spCost = 22;
     rounds = 2;
     spellWords = "Jrzi abtum";
     healAmount = 82;
     name = "cure serious wounds";
	 MentalDexEffect = true;
   }
   
   }
