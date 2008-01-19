package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Tiger extends DefaultMonster {
	public Tiger() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(16000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -10));
		setPhysicalStrength(40);
		setPhysicalConstitution(40);
		setPhysicalDexterity(35);
		setAggressive(false);
		setAggressive(false);
		setName("tiger");
		setDescription("A tiger");
		setLongDescription("It's a large tiger.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 45);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
