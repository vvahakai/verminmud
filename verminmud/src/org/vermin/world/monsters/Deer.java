package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Deer extends DefaultMonster {
	public Deer() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(5000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -30));
		setPhysicalStrength(20);
		setPhysicalConstitution(20);
		setPhysicalDexterity(20);
		setAggressive(false);
		setName("deer");
		setDescription("A deer");
		setLongDescription("The deer is a lithe looking animal. It moves amongst the forests' trees gracefully and carefully, with a poised-to-flee look typical to its herbivorous kind. Its acute senses have, no doubt, detected your arrival long before, but it doesn't seem to consider you a threat yet. It is a fairly large animal after all, and the large horns crowing its head represent a considerable defensive option to be used instead of fleeing.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 10);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
