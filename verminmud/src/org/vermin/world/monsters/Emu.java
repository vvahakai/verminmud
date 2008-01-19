package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Emu extends DefaultMonster {
       public Emu() {
               setRace(org.vermin.world.races.LargeBirdRace.getInstance());
               setExperienceWorth(8000);
               setAggressive(false);
				setPhysicalStrength(25);
				setPhysicalConstitution(25);
				setPhysicalDexterity(20);
               setName("emu");
               setDescription("An emu");
               setLongDescription("This large flightless bird truly lacks the grace characteristic to other members of its breed. Its bulky body is a mess of dirty feathers, decorated by two crooked legs and a long neck ending in a hideously ugly head. The legs are capable of delivering powerful kicks despite their crookedness, so watch your step.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 40);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
