package org.vermin.world.monsters;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Reindeer extends DefaultMonster {
	public Reindeer() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(5000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -30));
		setPhysicalStrength(20);
		setPhysicalConstitution(20);
		setPhysicalDexterity(20);
		setAggressive(false);
		setAggressive(false);
		setName("reindeer");
		setDescription("A reindeer");
		setLongDescription("It's a reindeer.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 10);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
