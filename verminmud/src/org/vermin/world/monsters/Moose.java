package org.vermin.world.monsters;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Moose extends DefaultMonster {
	public Moose() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(7000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -25));
		setPhysicalStrength(20);
		setPhysicalConstitution(25);
		setPhysicalDexterity(20);
		setAggressive(false);
		setAggressive(false);
		setName("moose");
		setDescription("A moose");
		setLongDescription("It's a moose.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 20);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
