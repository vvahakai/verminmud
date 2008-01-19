package org.vermin.world.monsters;
import org.vermin.mudlib.*;
import org.vermin.mudlib.Dice;

public class Vulture extends DefaultMonster {
       public Vulture() {
               setRace(org.vermin.world.races.LargeBirdRace.getInstance());
               setExperienceWorth(5800);
               setAggressive(Dice.random() < 5);
               setName("vulture");
				setPhysicalStrength(15);
				setPhysicalConstitution(20);
				setPhysicalDexterity(15);
               setDescription("A vulture");
               setLongDescription("It's a vulture. It eyes you in disturbing manner.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
