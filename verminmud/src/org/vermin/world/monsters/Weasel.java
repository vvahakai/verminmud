package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.world.races.QuadrupleFactory;

public class Weasel extends DefaultMonster {
	public Weasel() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(1000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -43));
		setPhysicalStrength(10);
		setPhysicalConstitution(7);
		setPhysicalDexterity(10);
		setAggressive(false);
		setName("weasel");
		setDescription("A weasel");
		setLongDescription("It's a weasel.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 10);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
