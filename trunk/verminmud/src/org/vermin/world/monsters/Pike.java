package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Pike extends DefaultMonster {
       public Pike() {
               setRace(org.vermin.world.races.FishRace.getInstance());
               setExperienceWorth(1200);
               setAggressive(false);
               setName("pike");
				setPhysicalStrength(5);
				setPhysicalConstitution(10);
				setPhysicalDexterity(5);
               setDescription("A dark gray pike");
               setLongDescription("It's a large pike. It is about to eat a smaller fish.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
