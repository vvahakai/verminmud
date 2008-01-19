package org.vermin.world.monsters;
import org.vermin.mudlib.*;

public class Shark extends DefaultMonster {
	public Shark() {
				setRace(org.vermin.world.races.LargeFishRace.getInstance());
				setExperienceWorth(35000);
				addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], 10));
				setPhysicalStrength(65);
				setPhysicalConstitution(55);
				setPhysicalDexterity(60);
				setAggressive(Dice.random() < 50);
				setName("shark");
				setDescription("A gray shark");
				setLongDescription("It's a huge shark, it has a big fin.");
				setBattleStyle(new DefaultBattleStyle(this));
				setSkill("fighting", 50);
                getMoney().add(Money.Coin.BRONZE, Dice.random());
				start();
	}
}
