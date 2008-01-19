package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Bear extends DefaultMonster {
	public Bear() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(25000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], 5));
		setPhysicalStrength(55);
		setPhysicalConstitution(55);
		setPhysicalDexterity(55);
		setAggressive(false);
		setName("bear");
		setDescription("A bear");
		setLongDescription("Bear is the king of the forest, or so they say, and this one is truly an awe-inspiring sight. A bulky but muscular body stands before you, equipped with claws which pronounce brute strength. In its dark eyes you see a strange glint of something akin to intelligence as it regards you stoicaly, waiting for you to make your next move.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 50);
        getMoney().add(Money.Coin.BRONZE, Dice
				.random());
		start();
	}
}
