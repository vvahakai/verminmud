/*
 * Created on 2.4.2005
 */
package org.vermin.world.monsters;

import org.vermin.mudlib.DefaultBattleStyle;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SpawningRule;
import org.vermin.mudlib.battle.BestialBattleStyle;
import org.vermin.world.races.QuadrupleFactory;

public class RogueSpawningRule implements SpawningRule {
	
	public void spawn(Room room) {
		if(Dice.random() < 5) {
			switch(Dice.random(3)) {
			case 1:
				room.add(new DungBeetle());
				break;
			case 2:
				room.add(new GridBug());
				break;
			case 3:
				room.add(new Rothe());
				break;
			}
		}
	}

	public void unspawn(MObject what) {

	}

	private class GridBug extends DefaultMonster {

		public GridBug() {
			setRace(org.vermin.world.races.InsectoidRace.getInstance());
			setPhysicalStrength(15);
			setPhysicalConstitution(18);
			setPhysicalDexterity(30);
			setName("grid bug");
			addAlias("bug");
			addAlias("insect");
			setDescription("A grid bug");
			setLongDescription("This is a small green bug which often lives in the dungeons of Vermin world.");
			setBattleStyle(new DefaultBattleStyle(this));
			setSkill("fighting", 30);
			start();
		}
	}
	private class Rothe extends DefaultMonster {
		public Rothe() {
			setRace((Race) QuadrupleFactory.getInstance().create(
					new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES,
							QuadrupleFactory.QuadrupleOption.HORNS

					}));
			setPhysicalStrength(50);
			setPhysicalConstitution(30);
			setPhysicalDexterity(30);
			setAggressive(true);
			setName("rothe");
			setDescription("A rothe");
			setLongDescription("This huge buffalo-like animal is often the demise of young adventurers who stray too deep in these dangerous dungeons.");
			setBattleStyle(new BestialBattleStyle(this));
			setSkill("fighting", 34);
			start();
		}
	}
}
