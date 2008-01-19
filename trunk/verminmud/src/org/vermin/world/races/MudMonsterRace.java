package org.vermin.world.races;

import java.util.EnumSet;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.LivingProperty;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.world.items.DemonClaw;

public class MudMonsterRace extends DefaultRaceImpl implements Singleton {
	
		private static DemonClaw claw = new DemonClaw();
		
		private static DemonClaw claw2 = new DemonClaw();
		
		protected static MudMonsterRace _instance = null;
		
		private String name = new String("mud monster");
		
		Slot[] slots = new Slot[] {};
		
		public EnumSet<LivingProperty> getRaceProperties() {
			return EnumSet.of(LivingProperty.UNDEAD, LivingProperty.DOES_NOT_BREATHE);
		}
		
		public String getHitLocation(int pos) { return "torso"; }
		
		public String getName() { return this.name; }
		
		public String getLimbName(int limb)
		{
			switch(limb)
			{
				case 0: return "right claw";
				case 1: return "left claw";
				default: return "left butt cheek";
			}
		}
		
		public int getLifeAlignment() {
			return -1000;
		}
		
		public Item getLimb(int limb)
		{
			if (limb == 0)
				return MudMonsterRace.claw;

			if (limb == 1)
				return MudMonsterRace.claw2;
		
			return MudMonsterRace.claw;
		}
		
		public int getLimbCount()
		{
			return 2;
		}
		
		public static Race getInstance()
		{
			if(_instance == null) {
				_instance = new MudMonsterRace();
				_instance.start();
			}
			
			return _instance;
		}
		
		public int getBaseHpRegen()
		{
			return 55;
		}
		
		public int getBaseSpRegen()
		{
			return 42;
		}
		
		public int getSize()
		{
			return 68;
		}
		
		public int getExpRate()
		{
			return 78;
		}
		
		public int getMaxPhysicalStrength() { return 80; }
		public int getMaxMentalStrength() { return 60; }
		public int getMaxPhysicalConstitution() { return 65; }
		public int getMaxMentalConstitution() { return 55; }
		public int getMaxPhysicalCharisma() { return 22; }
		public int getMaxMentalCharisma() { return 34; }
		public int getMaxPhysicalDexterity() { return 45; }
		public int getMaxMentalDexterity() { return 66; }

		/* Statcosts for this race (0-10) */
		public int getPhysicalStrengthCost() { return 3; }
		public int getMentalStrengthCost() { return 5; }
		public int getPhysicalConstitutionCost() { return 4; }
		public int getMentalConstitutionCost() { return 6; }
		public int getPhysicalCharismaCost() { return 8; }
		public int getMentalCharismaCost() { return 6; }
		public int getPhysicalDexterityCost() { return 6; }
		public int getMentalDexterityCost() { return 5; }

		
}
