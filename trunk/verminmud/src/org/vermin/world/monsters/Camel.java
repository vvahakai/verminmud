package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class Camel extends DefaultMonster {
	public Camel() {
		setRace((Race) QuadrupleFactory.getInstance().create(
				new Object[] { QuadrupleFactory.QuadrupleOption.PAWS

				}));
		setExperienceWorth(8000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -20));
		setPhysicalStrength(30);
		setPhysicalConstitution(25);
		setPhysicalDexterity(25);
		setAggressive(Dice.random() < 1);
		setName("camel");
		setDescription("A camel");
		setLongDescription("This four-legged beast of the desert was badly beaten with an ugly-stick when it was created by the gods. Many of its brethren get beaten with real sticks by camel-drivers as well but some, such as this one, manage to escape and wander across the desert aimlessly looking to make their living off the unfertile dunes. Luckily, surviving in the deep desert is something these animals do very well.");
		setBattleStyle(new BestialBattleStyle(this));
		setSkill("fighting", 30);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
