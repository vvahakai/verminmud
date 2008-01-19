package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.world.races.QuadrupleFactory;

public class Alligator extends DefaultMonster {
	public Alligator() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.TAIL,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(12000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(35);
		setPhysicalConstitution(35);
		setPhysicalDexterity(30);
		setAggressive(Dice.random() < 30);
		setName("alligator");
		setDescription("An alligator");
		setLongDescription("This is a large alligator lazily swmming around in the shallow foul-smelling swamp waters. Your attention is immediately attracted to the size of its jaws, which look like they could swallow a medium sized dwarf whole, chainmail and all. A few strings of decaying meat hang from the alligators' crooked teeth, adding to the already unpleasant sight.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 40);
		getMoney().add(Money.Coin.BRONZE, Dice
				.random());
		start();
	}
}
