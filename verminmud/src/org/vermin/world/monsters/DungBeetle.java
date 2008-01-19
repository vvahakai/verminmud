/*
 * Created on Jul 17, 2004
 */
package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Money;

/**
 * @author Jaakko Pohjamo
 */
public class DungBeetle extends DefaultMonster {

	public DungBeetle() {
		setRace(org.vermin.world.races.InsectoidRace.getInstance());
		setExperienceWorth(12000);
		setPhysicalStrength(35);
		setPhysicalConstitution(35);
		setPhysicalDexterity(30);
		setAggressive(Dice.random() < 20);
		setName("dung beetle");
		addAlias("beetle");
		addAlias("insect");
		setDescription("A dung beetle");
		setLongDescription("This is a dung beetle about the size of a small dog. It seems to be slow and clumsy, but most adventurers know better and avoid traveling the swamps in fear of the beetles' razor sharp claws and venomous stinger. Not many people know, and even fewer want to know what is it in these swamps that make the beetles grow to have such an exceptional size and ferocity.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 40);
		getMoney().add(Money.Coin.BRONZE, Dice.random());
		
		start();
	}
}
