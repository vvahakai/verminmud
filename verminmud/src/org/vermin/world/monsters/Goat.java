package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.world.races.QuadrupleFactory;

public class Goat extends DefaultMonster {
	public Goat() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(6000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -30));
		setPhysicalStrength(25);
		setPhysicalConstitution(20);
		setPhysicalDexterity(20);
		setAggressive(false);
		setAggressive(false);
		setName("goat");
		setDescription("A goat");
		setLongDescription("It's a goat.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 20);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
