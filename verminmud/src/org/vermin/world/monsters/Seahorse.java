package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Seahorse extends DefaultMonster {
	public Seahorse() {
				setRace(org.vermin.world.races.FishRace.getInstance());
				setExperienceWorth(200);
				addModifier(new DefaultModifierImpl(ModifierTypes.SIZE, new Object[0], -8));
				setPhysicalStrength(2);
				setPhysicalConstitution(2);
				setPhysicalDexterity(2);
				setAggressive(false);
				setName("sea-horse");
				setDescription("A colourful sea-horse");
				setLongDescription("It's a tiny sea-horse. You wonder if you can kill such a cute animal.");
				setBattleStyle(new DefaultBattleStyle(this));
                getMoney().add(Money.Coin.BRONZE, Dice.random());
				start();
	}
}
