package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.mudlib.BattleGroup;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.skills.BaseSkill;

public class MirrorImageSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.ARCANE, SkillTypes.PROTECTIVE, SkillTypes.SELF };

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Kopiot roks!";

	public String getName() {
		return "mirror image";
	}

	public int getCost(SkillUsageContext suc) { return 51; }

	public int getTickCount() {
	  return 5 + Dice.random(2);
	}

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0 && !who.getBattleGroup().containsName("MirrorImageBattleGroup"))
		{
			int mirrors = Dice.random((success/10)+(success/10));
			who.notice("You chant '"+spellWords+"' and create " + mirrors + " mirror images of yourself.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' and creates " + mirrors + " mirror images of "+ who.getObjective() +"self.");
			
			MirrorImageBattleGroup mibg = new MirrorImageBattleGroup();
			mibg.setOwner(who);

			BattleGroup foundBattleGroup = who.getLeafBattleGroup();

			// Finding the child battlegroup of personal group that contains this player's leaf battlegroup
			Iterator it = who.getPersonalBattleGroup().children();
			while(it.hasNext()) {
				BattleGroup bg = (BattleGroup) it.next();
				if(bg.contains(who.getLeafBattleGroup())) {
					foundBattleGroup = bg;
					break;
				}
			}

			if(foundBattleGroup != null) {
				who.getPersonalBattleGroup().wrapChild(foundBattleGroup, mibg);		
				mibg.parentChanged(who.getPersonalBattleGroup());
			}

			for(int i=0; i<mirrors; i++) {
				MirrorImage mi = new MirrorImage();
				room.add(mi);
				//mi.setRoom(room);
				mi.setParent(room);
				mi.setRace(who.getRace());
				mi.setName(who.getName());
				mi.setMirrorImageBattleGroup(mibg);
				mi.setOwner(who);
				mibg.addChild(mi.getPersonalBattleGroup());
			}
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
