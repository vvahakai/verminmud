package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Bullfinch extends DefaultMonster {
       public Bullfinch() {
               setRace(org.vermin.world.races.BirdRace.getInstance());
               setExperienceWorth(500);
			   setPhysicalStrength(5);
			   setPhysicalConstitution(5);
			   setPhysicalDexterity(5);
               setAggressive(false);
               setName("bullfinch");
               setDescription("A bullfinch");
               setLongDescription("This cute red-breasted little bird seems to enjoy inhabiting the light forests streching across large areas of this part of the continent. It darts rapidly from branch to branch amongst the trees looking for insects to eat, presumably. The bird seems to be so busy with this, that it ignores your presence completely.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 5);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
