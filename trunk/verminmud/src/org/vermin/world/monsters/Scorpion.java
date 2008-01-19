package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultModifierImpl;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.ModifierTypes;
import org.vermin.mudlib.Money;
import org.vermin.world.races.InsectoidRace;

public class Scorpion extends DefaultMonster {
	public Scorpion() {
		setRace(InsectoidRace.getInstance());
		setExperienceWorth(1000);
		addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -40));
		setPhysicalStrength(10);
		setPhysicalConstitution(10);
		setPhysicalDexterity(10);
		setAggressive(false);
		setName("scorpion");
		setDescription("A scorpion");
		setLongDescription("It's a tiny scorpion.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 5);
        getMoney().add(Money.Coin.BRONZE, Dice.random());
		start();
	}
}
