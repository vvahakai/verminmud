/*
 * Created on 2.4.2005
 *
 */
package org.vermin.world.skills;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.minion.DefaultMinionImpl;
import org.vermin.mudlib.minion.Leash;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.races.GolemRace;
import org.vermin.world.races.HumanRace;

/**
 * @author Matti V�h�kainu / Vermin ry
 *
 */
public class CreateClockworkSentinelSkill extends BaseSkill {
	
	protected SkillType[] skillTypes2 = new SkillType[] { SkillTypes.ARCANE };

	public String getName() {
		return "create clockwork sentinel";
	}
	

	public int getTickCount() {
		return 15+Dice.random(5);
	}
	
	public int getCost(SkillUsageContext suc) {
		return 300;
	}

	public int minionMax(Living master){
		return Leash.getMinionCount(master);
	}
	
	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.SUMMONING, SkillTypes.MAGICAL };
	
	public SkillType[] getTypes() { return skillTypes; }

	
	public void use(SkillUsageContext suc) {
		int sentinelSuccess = suc.getSkillSuccess();
		if (sentinelSuccess <= 0) {
			suc.getActor().notice("You fail the skill.");
		}
		else if (minionMax(suc.getActor()) > 2) {
			suc.getActor().notice("You fail to summon more minions.");
		}
		
		else {	
			suc.getActor().notice("You quickly assemble a small sentinel and chant 'It's aliiiiivveeeee!'.\nThe construct stands up and is ready to serve you.");
			DefaultMinionImpl sentinel = new DefaultMinionImpl(suc.getActor());
			sentinel.setName("sentinel");
			sentinel.setLongDescription("This tiny construct has been assembled to serve "+suc.getActor().getName()+" and "+suc.getActor().getName()+" only.");
			sentinel.setDescription("a small clockwork sentinel");
			sentinel.setFollowing(true);
			sentinel.setRace(new GolemRace(HumanRace.getInstance()));
			sentinel.setMaxHp(1000 + (sentinelSuccess));
			sentinel.setHp(1000 + (sentinelSuccess));
			sentinel.setPhysicalConstitution(50);
			sentinel.setPhysicalStrength(50 + ((int) sentinelSuccess / 5));
			sentinel.setPhysicalDexterity(50 + ((int) sentinelSuccess / 5));
			sentinel.addCommand("report");
			sentinel.addCommand("get");
			sentinel.addCommand("take");			
			sentinel.addCommand("drop");
			sentinel.addCommand("put");
			sentinel.addCommand("eq");			
			sentinel.addCommand("score");
			sentinel.addCommand("look");
			sentinel.addCommand("loot");
			sentinel.addCommand("stats");			
			suc.getActor().getParent().add(sentinel);
			Leash l = Leash.get(suc.getActor());
			if(l == null) {
				l = new Leash();
				suc.getActor().add(l);				
			}		
			sentinel.start();
			l.addMinion(sentinel);
		}
		
	}


}
