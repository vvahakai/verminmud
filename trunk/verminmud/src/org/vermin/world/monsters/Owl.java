package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Owl extends DefaultMonster {
       public Owl() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(1250);
               setAggressive(false);
   			   setPhysicalStrength(15);
			   setPhysicalConstitution(10);
			   setPhysicalDexterity(10);
               setName("owl");
               setDescription("An owl");
               setLongDescription("It's an owl.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 15);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
