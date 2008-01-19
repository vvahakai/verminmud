package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Duck extends DefaultMonster {
       public Duck() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(1500);
               setAggressive(false);
				setPhysicalStrength(5);
				setPhysicalConstitution(5);
				setPhysicalDexterity(5);
               setName("duck");
               setDescription("A duck");
               setLongDescription("Hoarse interrogative quacking sounds erupt from the throat of this waddling bird as you arrive near it, apparently too near for its comfort. It tilts its head as it stares at you, obviously considering you an unwelcome intruder who has already lingered around well past his welcome. Perhaps you should just leave this harmless animal alone.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 20);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
