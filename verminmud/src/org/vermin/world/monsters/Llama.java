package org.vermin.world.monsters;

import org.vermin.mudlib.*;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Llama extends DefaultMonster {
	public Llama() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES

				}));
		setExperienceWorth(8000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(30);
		setPhysicalConstitution(25);
		setPhysicalDexterity(25);
		setAggressive(false);
		setName("llama");
		setDescription("A llama");
		setLongDescription("It's a llama.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 20);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
