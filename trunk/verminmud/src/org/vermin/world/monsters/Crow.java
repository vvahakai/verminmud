package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Crow extends DefaultMonster {
       public Crow() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(750);
			   setPhysicalStrength(7);
			   setPhysicalConstitution(7);
			   setPhysicalDexterity(7);			  
               setAggressive(false);
               setName("crow");
               setDescription("A crow");
               setLongDescription("This small black bird silently sits on the branch of a nearby tree. At first you have trouble preceiving any detail because of its sheer blackness, but then you notice the faint glint of a pair of beady eyes staring at you. Some beleive that this intelligent bird is the messenger of gods, but it surely doesn't seem to have anything to say to you.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 7);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
