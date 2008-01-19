package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Hawk extends DefaultMonster {
       public Hawk() {
               setRace(org.vermin.world.races.LargeBirdRace.getInstance());
               setExperienceWorth(5800);
               setAggressive(false);
               setName("hawk");
				setPhysicalStrength(20);
				setPhysicalConstitution(15);
				setPhysicalDexterity(20);
               setDescription("A hawk");
               setLongDescription("It's a hawk.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
