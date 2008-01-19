package org.vermin.mudlib.skills;

import org.vermin.mudlib.*;

import java.util.*;

/**
 * Skill that allows one to turn corpses into cans
 * of edible food.
 *
 */
public class Tinning extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.LOCAL, new TinningSkillType() };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	public static class TinningSkillType extends SkillTypeAdapter {
		public boolean tryUse(SkillUsageContext suc) {
			MObject target = suc.getTarget();
			Player p = (Player) suc.getActor();
			if(!(target instanceof CorpseItem)) {
				p.notice("You can't tin "+target.getDescription()+".");
				return false;
			}

			TinningKit kit = null;
			Iterator it = p.findByType(Types.TYPE_ITEM);
			while(it.hasNext()) {
				MObject obj = (MObject) it.next();
				if(obj instanceof TinningKit) {
					kit = (TinningKit) obj;
					break;
				}
			}
			if(kit == null) {
				p.notice("You need a tinning kit in order to do that.");
				return false;
			}

			return true;
		}}

	public static class TinningKit extends DefaultItemImpl {
		
		public TinningKit() {
			material = "steel";
			size = 1;
			dp = 100;
			maxDp = 100;
			name = "tinning kit";
			description = "a tinning kit";
			longDescription = "It is an intricate mechanical device made of steel. "+
				"It can be used to create cans of foodstuff.";
			aliases = new Vector();
			aliases.add("kit");
		}
	}

	public void use(SkillUsageContext suc) {
		int s = suc.getSkillSuccess();
		Player p = (Player) suc.getActor();
		CorpseItem corpse = (CorpseItem) suc.getTarget();

		if(s <= 0) {
			p.notice("You try to create a can from "+corpse.getDescription()+" but fail to operate the complex tinning kit.");
		} else {
			
			Race race = corpse.getRace();
			String meat = race.getName();
			DefaultEdibleImpl edible = new DefaultEdibleImpl(10*race.getSize()*s);
			
			edible.setName("can");
			edible.addAlias("can of meat");
			edible.addAlias(meat+" meat");
			edible.setDescription("can of "+meat+" meat");
			edible.setLongDescription("It is a can made of tin.");

			String message = null;
			if(s < 10)
				message = "You scavenge the corpse and create a small can of "+meat+" meat.";
			else if(s < 25)
				message = "You create a decent can of "+meat+" meat.";
			else if(s < 75)
				message = "After some preparation, you manage to create a large can full of "+meat+" meat.";
			else
				message = "You skillfully operate the tinning kit and create a HUGE can of "+meat+" meat that will "+
					"satiate for a long time.";

			p.notice(message);
			p.add(edible);
			((Room) corpse.getParent()).remove(corpse);
		}
			
	}

	public String getName() {
		return "tinning";
	}
   public int getTickCount() {
		return 6;
	}
}
