package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Fowl extends DefaultMonster {
       public Fowl() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(1000);
               setAggressive(false);
				setPhysicalStrength(5);
				setPhysicalConstitution(5);
				setPhysicalDexterity(5);
               setName("fowl");
               setDescription("A fowl");
               setLongDescription("This bird is a kind of feral chicken, though its coloration is much more muted than that of the domesticated bird.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 10);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
