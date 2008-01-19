package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Lark extends DefaultMonster {
       public Lark() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(500);
               setAggressive(false);
   			   setPhysicalStrength(5);
			   setPhysicalConstitution(5);
			   setPhysicalDexterity(5);
               setName("lark");
               setDescription("A lark");
               setLongDescription("It's a lark.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 5);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
