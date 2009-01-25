package org.vermin.world.skills;

import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.minion.DefaultMinionImpl;
import org.vermin.mudlib.minion.Leash;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.races.HumanRace;
import org.vermin.world.races.UndeadRace;

public class SummonSkeletonSkill extends BaseSkill {

	public String getName() {
		return "summon skeleton";
	}

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.ARCANE };
	
	public SkillType[] getTypes() { return skillTypes; }	
	
	public int getTickCount() {
		return 3;
	}

	public void use(SkillUsageContext suc) {
		DefaultMinionImpl skeleton = new DefaultMinionImpl(suc.getActor());
		skeleton.setName("skeleton");
		skeleton.setDescription("boner skeleton");
		skeleton.setLongDescription("This skeleton is very boney.");
		skeleton.setHp(1009);
		skeleton.setMaxHp(1009);		
		skeleton.setFollowing(true);
		skeleton.setIllumination(20);
		skeleton.setRace(new UndeadRace(HumanRace.getInstance()));
		skeleton.setStatsToMax();
		skeleton.setObjective("to kill and destroy summoner's enemies.");
		suc.getActor().getRoom().add(skeleton);
		Leash l = (Leash) suc.getActor().findByNameAndType("_minion_leash", Types.ITEM);
		if(l == null) {
			l = new Leash();
			suc.getActor().add(l);
		}
		skeleton.start();
		l.addMinion(skeleton);		
		suc.getActor().notice("Whee teit skeletonin!");
	}	
	
}
