package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Wolverine extends DefaultMonster {
	public Wolverine() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(3500);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -35));
		setPhysicalStrength(15);
		setPhysicalConstitution(15);
		setPhysicalDexterity(15);
		setAggressive(Dice.random() < 35);
		setName("wolverine");
		setDescription("A wolverine");
		setLongDescription("It's a wolverine.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 10);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
