package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Lion extends DefaultMonster {
	public Lion() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(20000);
		setPhysicalStrength(50);
		setPhysicalConstitution(50);
		setPhysicalDexterity(50);
		setAggressive(Dice.random() < 15);
		setAggressive(false);
		setName("lion");
		setDescription("A lion");
		setLongDescription("It's a lion.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 50);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
