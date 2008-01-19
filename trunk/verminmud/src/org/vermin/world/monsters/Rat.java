package org.vermin.world.monsters;

import org.vermin.mudlib.*;
import org.vermin.world.races.QuadrupleFactory;

public class Rat extends DefaultMonster {
	public Rat() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(300);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -45));
		setPhysicalStrength(5);
		setPhysicalConstitution(5);
		setPhysicalDexterity(5);
		setAggressive(Dice.random() < 5);
		setName("rat");
		setDescription("A rat");
		setLongDescription("It's a small rat eating cheese.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 5);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
