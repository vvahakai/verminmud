package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Ostrich extends DefaultMonster {
       public Ostrich() {
               setRace(org.vermin.world.races.LargeBirdRace.getInstance());
               setExperienceWorth(8000);
               setAggressive(false);
               setName("ostrich");
				setPhysicalStrength(25);
				setPhysicalConstitution(25);
				setPhysicalDexterity(15);
               setDescription("An ostrich");
               setLongDescription("It's an ostrich.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 40);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
