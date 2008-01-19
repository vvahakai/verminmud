package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Gnu extends DefaultMonster {
	public Gnu() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(8000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -25));
		setPhysicalStrength(30);
		setPhysicalConstitution(30);
		setPhysicalDexterity(25);
		setAggressive(false);
		setAggressive(false);
		setName("gnu");
		setDescription("A gnu");
		setLongDescription("It was created by a man called Richard Stallman.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 20);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
