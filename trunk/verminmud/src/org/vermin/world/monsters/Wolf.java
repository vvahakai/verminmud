package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Wolf extends DefaultMonster {
	public Wolf() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(12000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(35);
		setPhysicalConstitution(30);
		setPhysicalDexterity(40);
		setAggressive(Dice.random() < 10);
		setName("wolf");
		setDescription("A wolf");
		setLongDescription("It's a wolf. It eyes you hungrily.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 35);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
