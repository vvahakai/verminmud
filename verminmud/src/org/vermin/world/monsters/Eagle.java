package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Eagle extends DefaultMonster {
       public Eagle() {
               setRace(org.vermin.world.races.LargeBirdRace.getInstance());
               setExperienceWorth(6000);
               setAggressive(false);
				setPhysicalStrength(20);
				setPhysicalConstitution(20);
				setPhysicalDexterity(20);
               setName("eagle");
               setDescription("A eagle");
               setLongDescription("Usually these large hunting birds are seen high in the sky, floating on top of upward drafts, looking for pray. This one seems to have decided to visit the ground for a change. The eagle is often called the king of the birds, and the large talons and the powerful beak they are armed with seel well suited to settle any dispute over the rightfulness of the title.");
               setBattleStyle(new DefaultBattleStyle(this));
               setSkill("fighting", 30);
               getMoney().add(Money.Coin.BRONZE, Dice.random());
               start();
       }
}
