package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Snake extends DefaultMonster {
	public Snake() {
				setRace(org.vermin.world.races.SnakeRace.getInstance());
				setExperienceWorth(2000);
				setAggressive(false);
				setPhysicalStrength(10);
				setPhysicalConstitution(5);
				setPhysicalDexterity(10);
				setName("snake");
				setDescription("A green snake");
				setLongDescription("It's a rather long snake. ");
				setBattleStyle(new DefaultBattleStyle(this));
				setSkill("fighting", 5);
                getMoney().add(Money.Coin.BRONZE, Dice.random());
				start();
	}
}
