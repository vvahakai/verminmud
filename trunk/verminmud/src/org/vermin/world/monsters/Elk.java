package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Elk extends DefaultMonster {
	public Elk() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(6000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -30));
		setPhysicalStrength(20);
		setPhysicalConstitution(25);
		setPhysicalDexterity(20);
		setAggressive(false);
		setName("elk");
		setDescription("An elk");
		setLongDescription("A much more powerful looking animal than its cousin, the deer. The elk has an almost brutish air around it.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 20);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
