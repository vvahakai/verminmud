package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Cockerel extends DefaultMonster {
       public Cockerel() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(1000);
               setAggressive(false);
               setName("cockerel");
				setPhysicalStrength(10);
				setPhysicalConstitution(10);
				setPhysicalDexterity(10);
               setDescription("A cockerel");
               setLongDescription("This bird is easilly recognizable as a cockerel by its characteristic fiery red comb and wattles. It is spending its time wandering slowly around amongst the undergrowth, stopping every now and then to dig the ground with its feet. Tough it gives you a couple of glances in your direction as you approach, it seems to mind little of your presence.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 10);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
