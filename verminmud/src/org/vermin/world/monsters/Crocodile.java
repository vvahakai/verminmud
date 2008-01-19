package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.world.races.QuadrupleFactory;

public class Crocodile extends DefaultMonster {
	public Crocodile() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.TAIL,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(13500);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(40);
		setPhysicalConstitution(35);
		setPhysicalDexterity(30);
		setAggressive(false);
		setName("crocodile");
		setDescription("A crocodile");
		setLongDescription("At first, only a pair of bulging eyes can be seen drifting slowly on the surface. Then suddenly, with a powerful thrust of its tail the crocodile's great body thrusts trough the murky waters with ashtonising speed. Tough it is not as aggressive as its smaller alligator cousin, the crocodile shares the dreadful jaws and the vicious nature of it and is truly a beast to respect.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 40);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
