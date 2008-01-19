package org.vermin.world.monsters;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Lynx extends DefaultMonster {
	public Lynx() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(10000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(30);
		setPhysicalConstitution(30);
		setPhysicalDexterity(30);
		setAggressive(false);
		setAggressive(false);
		setName("lynx");
		setDescription("A lynx");
		setLongDescription("It's a lynx.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 30);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
