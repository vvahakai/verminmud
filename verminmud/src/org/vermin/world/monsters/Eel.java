package org.vermin.world.monsters;
import org.vermin.mudlib.*;
public class Eel extends DefaultMonster {
	public Eel() {
				setRace(org.vermin.world.races.SnakeRace.getInstance());
				setExperienceWorth(2000);
				setAggressive(false);
				setPhysicalStrength(10);
				setPhysicalConstitution(10);
				setPhysicalDexterity(5);
				setName("eel");
				setDescription("An eel");
				setLongDescription("It's a disgusting eel, of the worst bloodsucking variety. It moves swiftly in the water, but you know well that its kind can also hide amongst the stones, their natural camouflage rendering them almost invisible. Yes, you can spot this one because of its movement, but there might be dozens more of the accursed creatures around you in the water, waiting stealthily for the right moment to attach themselves upon your skin.");
				setBattleStyle(new DefaultBattleStyle(this));
                getMoney().add(Money.Coin.BRONZE, Dice.random());
				start();
	}
}
