package org.vermin.world.skills;

import org.vermin.mudlib.skills.*;
import org.vermin.mudlib.*;
import org.vermin.world.items.TemplarPendant;

public class FlowOfVirtueSkill extends BaseSkill {

	public void use(SkillUsageContext suc) {
		
		String name = suc.getTargetName();
		if(name == null){
			suc.getActor().notice("Flow virtue at what?");
			return;
		}
		TemplarPendant p = (TemplarPendant) suc.getActor().findByNameAndType("templar_guild_object", Types.ITEM);
		if(name.equalsIgnoreCase("morale")) {
			if(nextLevel(p, p.getMorale(), name.toLowerCase())) {
				p.increaseMorale();
				return;
			}
		} else if(name.equalsIgnoreCase("deliverance")) {
			if(nextLevel(p, p.getDeliverance(), name.toLowerCase())) {
				p.increaseDeliverance();
				return;
			}
		} else if(name.equalsIgnoreCase("communion")) {
			if(nextLevel(p, p.getCommunion(), name.toLowerCase())) {
				p.increaseCommunion();
				return;
			}
		} else if(name.equalsIgnoreCase("smiting")) {
			if(nextLevel(p, p.getSmiting(), name.toLowerCase())) {
				p.increaseSmiting();
				return;
			}	
		} else {
			suc.getActor().notice("Flow virtue at what?");
			return;
		}

		suc.getActor().notice("Your pendant doesn't have enough virtue points.");
	}

	private boolean nextLevel(TemplarPendant p, int level, String target) {
		
		if(level == 25)
			return false;

		int l = level+1;
		int required = (int) ((1.4-Math.log10((double) (26-l))) * l * 1234);
		if(p.getVirtue() < required)
			return false;
		else {
            ((Living)p.getParent()).notice("You feel the divine power flow from your pendant granting you increased "+target+".");
			p.setVirtue(p.getVirtue() - required);
			return true;
        }
	}
	
	public String getName() {
		return "flow of virtue";
	}

   public int getTickCount() {
		return 3;
	}

   public SkillType[] getTypes() {
		return new SkillType[] {
			SkillTypes.DIVINE
		};
	}

	public int getCost(SkillUsageContext suc) {
		return 40 + Dice.random(20);
	}

	public boolean tryUse(Living actor, MObject target) {
		Object obj = actor.findByNameAndType("templar_guild_object", Types.ITEM);
		if(obj != null && obj instanceof TemplarPendant)
			return true;
		else {
			actor.notice("You must have the sacred pendant to do that.");
			return false;
		}
	}

}
