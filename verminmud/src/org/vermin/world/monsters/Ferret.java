package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.world.races.QuadrupleFactory;

public class Ferret extends DefaultMonster {
	public Ferret() {
		setRace(org.vermin.world.races.QuadrupleRace.getInstance());
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS,
						QuadrupleFactory.QuadrupleOption.JAWS

				}));
		setExperienceWorth(1000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -40));
		setPhysicalStrength(10);
		setPhysicalConstitution(5);
		setPhysicalDexterity(10);
		setAggressive(false);
		setName("ferret");
		setDescription("A ferret");
		setLongDescription("These small four-legged predatory animals are usually wary of anything considerably larger than themselves, but this individual doesn't seem to be in a hurry to hide. Instead, it arches its back and hisses at you from between its small but cruel looking teeth.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 10);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
