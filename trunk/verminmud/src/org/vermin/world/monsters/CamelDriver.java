/*
 * Created on 19.2.2005
 */
package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Money;

/**
 * @author Jaakko Pohjamo
 */
public class CamelDriver extends DefaultMonster {
	public CamelDriver() {
		setRace(org.vermin.world.races.HumanRace.getInstance());
		setExperienceWorth(12000);
		setPhysicalStrength(35);
		setPhysicalConstitution(35);
		setPhysicalDexterity(30);
		setName("A camel driver");
		addAlias("driver");
		addAlias("camel driver");
		addAlias("man");
		setDescription("A camel driver");
		setLongDescription("This is one of the famous camel drivers of the desert of Homo. They like to travel in pairs for safety and companionship.");
		setBattleStyle(new DefaultBattleStyle(this));
		setSkill("fighting", 40);
		getMoney().add(Money.Coin.BRONZE, Dice.random());
		
		start();
	}
}
