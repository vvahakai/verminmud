package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Buffalo extends DefaultMonster {
	public Buffalo() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
						QuadrupleFactory.QuadrupleOption.HORNS

				}));
		setExperienceWorth(10000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -15));
		setPhysicalStrength(30);
		setPhysicalConstitution(30);
		setPhysicalDexterity(30);
		setAggressive(false);
		setName("buffalo");
		setDescription("A buffalo");
		setLongDescription("Herds of these great dark brown beasts wander around the arid lands around you, grazing on the few tufts of grass pushing trough the dry ground here and there. It's a wonder that animals of such size can survive on so poor foodstuffs.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 20);
		getMoney().add(Money.Coin.BRONZE, Dice.random());

		start();
	}
}
