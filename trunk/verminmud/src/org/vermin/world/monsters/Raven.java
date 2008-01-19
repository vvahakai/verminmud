package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Raven extends DefaultMonster {
       public Raven() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(750);
               setAggressive(false);
   			   setPhysicalStrength(7);
			   setPhysicalConstitution(7);
			   setPhysicalDexterity(7);
               setName("raven");
               setDescription("A raven");
               setLongDescription("It's a raven. It mostly looks like a bird.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 7);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
