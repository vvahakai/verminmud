package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Salmon extends DefaultMonster {
       public Salmon() {
               setRace(org.vermin.world.races.FishRace.getInstance());
               setExperienceWorth(1200);
               setAggressive(false);
				setPhysicalStrength(5);
				setPhysicalConstitution(5);
				setPhysicalDexterity(5);
               setName("salmon");
               setDescription("A blue salmon");
               setLongDescription("It's a salmon. Trying to swim against the flow.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
