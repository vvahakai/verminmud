package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Cuckoo extends DefaultMonster {
       public Cuckoo() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(1500);
               setAggressive(false);
				setPhysicalStrength(5);
				setPhysicalConstitution(5);
				setPhysicalDexterity(5);
               setName("cuckoo");
               setDescription("A cuckoo");
               setLongDescription("You have heard the characteristic sound of this bird ringing through the forest long before seeing the source. Then you spot it, a gray bird hanging on a branch, curiously unimpressive compared to its voice. As you move closer it quiets down and observes you warily, waiting to see if you will leave peacefully.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
