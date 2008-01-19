/* CorpseItem.java
	3.2.2002
	
	A corpse.
*/

package org.vermin.mudlib;

import org.vermin.driver.*;
import org.vermin.world.races.UndeadRace;

public class CorpseItem extends ContainerItem {

	protected boolean turnUndead;
	
	protected int decomposeTicks;
	protected int rounds;
	
	protected DefaultMonster monster;
	
	private Race race;

	public CorpseItem() {}

	public CorpseItem(Living who, boolean turnUndead) {
		this.turnUndead = turnUndead;
		
		decomposeTicks = (Dice.random(10) + 2);
		rounds = 0;
		
		race = who.getRace();

		try {
			monster = (DefaultMonster) who;
		} catch(ClassCastException e) {
			monster = null;
		}
		
		Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
	}

	public Race getRace() {
		return race;
	}

	
	public boolean tick(short type) {
		rounds++;
		
		if(rounds == decomposeTicks) {
			getParent().remove(this);
			return false;
		} else if(turnUndead && rounds > 2) {
			if(Dice.random() > 90) {
				//World.log("Tämä oli sitä corpse tiedoston parsetusta, jotta saataisiin selville... mutta ei saatu");

				if(getParent() instanceof Room && monster != null && monster.provides(LivingProperty.UNDEAD)) {
					Room r = (Room) getParent();
					getParent().remove(this);
					
					monster.setAggressive(true);

					String d = monster.getDescription();
					if(d.endsWith("."))
						d = d.substring(d.length()-1) + " (undead).";
					else
						d = d + " (undead).";

					monster.setDescription(d);
					
					Race race = monster.getRace();
					monster.setRace(new UndeadRace(race));
					
					monster.setHp(monster.getMaxHp());
					
					r.add(monster);
					return false;
				}
			}
		}
		return true;
	}
	
    public DefaultMonster getMonster() {
        return monster;
    }
    
}
