/* DefaultMonster.java
	2.2.2002	Tatu Tarvainen & Ville V�h�kainu / Council 4
	
	This is a base implementation for monsters with pluggable
    behaviour modules.
*/

package org.vermin.mudlib;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.vermin.driver.Queue;
import org.vermin.mudlib.behaviour.Aggressive;
import org.vermin.util.Functional.Predicate;

public class DefaultMonster extends DefaultLivingImpl {

	protected Map<String,Integer> skills = new HashMap();
	protected transient SkillObject skillObject;
	protected long experienceWorth;
	protected Object raceOption;
		
	public DefaultMonster() { 
		super();
		skillObject = new SkillObject();
	}
	
	public void start() { 
      		
		super.start();
		
		//System.out.println("M�K�: "+getName()+" "+getMaxHp()+" / "+getMaxSp());
		setHp(getMaxHp());
		setSp(getMaxSp());
		
		for(String skillName : skills.keySet()) {
			skillObject.setSkill(skillName, skills.get(skillName));
		}
		
		wearAll();
		wieldAll();
				
	}
	
	public void setStatsToMax() {
		physicalStr = race.getMaxPhysicalStrength();
		mentalStr = race.getMaxMentalStrength();
		physicalCon = race.getMaxPhysicalConstitution();
		mentalCon = race.getMaxMentalConstitution();
		physicalDex = race.getMaxPhysicalDexterity();
		mentalDex = race.getMaxMentalDexterity();
		physicalCha = race.getMaxPhysicalCharisma();
		mentalCha = race.getMaxMentalCharisma();
	}

	public void updateStats() {
		super.updateStats();
		hp = maxHp;
		sp = maxSp;
	}

	@Override
	protected void doRegen() {
		// Added division by 10 (monsters shouldn't regenerate as much as players)
        addHp(DefaultModifierImpl.calculateInt(this, getPhysicalConstitution() * getRace().getBaseHpRegen() / 20, 
                hpRegenModifiers) / 10);
        addSp(DefaultModifierImpl.calculateInt(this, getMentalConstitution() * getRace().getBaseSpRegen() / 20, 
                spRegenModifiers) / 10);
	}

	public void setExperienceWorth(long worth) {
		experienceWorth = worth;
	}
	
	public long getExperienceWorth() {
		double tuneFactor = 2.5;
		HashMap<String, SkillObject.SkillEntry> availableSkills = getSkillObject().getSkills();
		
		int skillBonus = 0;
		for(String skillName : availableSkills.keySet()) {
			long skillPercent = availableSkills.get(skillName).percent;
			skillBonus += skillPercent;
		}

		long exp = (long) ((( physicalStr * 4 )
				+ ( physicalDex * 4 )
				+ ( physicalCon * 6 )
				+ getMaxHp()
				+ skillBonus*5

				// resistances
				
				+ getResistance(Damage.Type.ELECTRIC, false)
				+ getResistance(Damage.Type.POISON, false)
				+ getResistance(Damage.Type.FIRE, false)
				+ getResistance(Damage.Type.COLD, false)
				+ getResistance(Damage.Type.ASPHYXIATION, false)/2
				+ getResistance(Damage.Type.PSIONIC, false)/2
				+ getResistance(Damage.Type.ACID, false)/2
				+ getResistance(Damage.Type.MAGICAL, false)*4
				+ getResistance(Damage.Type.RADIATION, false)/2
				+ getResistance(Damage.Type.SONIC, false)/2
				+ getResistance(Damage.Type.PHYSICAL, false)*8 
				+ getResistance(Damage.Type.STUN, false))* tuneFactor);
				

				/* automatic skills 
				+ getSkill("fighting") * 2
				+ getSkill("martial arts")
				+ getSkill("controlled melee")
				+ getSkill("multi hand combat")
				+ getSkill("single hand combat")
				+ getSkill("dodge")
				+ getSkill("parry")
				+ getSkill("riposte")
				+ getSkill("find weakness")
				+ getSkill("brawling")
				+ getSkill("swords")
				+ getSkill("daggers")
				+ getSkill("axes")
				+ getSkill("bludgeons")
				+ getSkill("spears")
				+ getSkill("polearms")
				+ getSkill("flails")
				+ getSkill("whips")
				+ getSkill("shields")
				+ getSkill("sticky hands")
				+ getSkill("rolling punches")
				+ getSkill("iron palm training")
				+ getSkill("critical")
				+ getSkill("iron will")) * tuneFactor);	
				*/
		
				if(exp<0) exp = 0;
				return exp;
		//return experienceWorth;
	}
	
	protected void setAggressive(boolean b) {
        if(b) {
            Aggressive aggro = new Aggressive();
            aggro.setOwner(this);
            behaviours.add(aggro); 
        } else {
        	Behaviour aggro = findBehaviour(
        		new Predicate<Behaviour>() { 
        			public boolean call(Behaviour o) {
        				return (o instanceof Aggressive);
        			}
        		}
        	);
        	removeBehaviour(aggro);
        }
	}
	
	public int getSkill(String name) { 
		if(skillModifiers == null) {
			Integer intti = (Integer) skills.get(name);
			return intti == null ? 0 : intti;
		}
		else
			return DefaultModifierImpl.calculateInt(this, (Integer) skills.get(name), (LinkedList) skillModifiers.get(name));
	}
	
	public int getSkill(String name, boolean modifiers) {
		return modifiers ? getSkill(name) : (Integer) skills.get(name);
	}
	
	public void setSkill(String skill, int percent) {
		skills.put(skill, new Integer(percent));
	}
		
	@Override
	public boolean tick(Queue type) {
		synchronized(getRoom()) {
			if(isDead()) return false;
			
			updateAfflictions(type);
			
			if(type == Tick.REGEN) {
				onRegenTick(this);
				regen();
			} else if(type == Tick.BATTLE) {
				
				// Use skill
				boolean skill = false;
				if(skillObject.skillActive()) {
					skill = skillObject.updateSkill(this);
					//World.log("--------------------");
				}

				if(!getBattleStyle().tryUse())
					setBattleStyle(new DefaultBattleStyle(this));
				boolean opponent = getBattleStyle().use();
				return opponent || skill;
				
			}		
			return true;
		}
	}
	

	/*public void add(MObject o) {
		if(o instanceof Item)
			inventory.add(o);
	
	}*/
	/* Dump our corpse to the current room */
	public void dumpCorpse() {

		// Unwear all equipment
		if(worn != null) {
			for(int i=0; i<worn.length; i++) {
				if(worn[i] != null)
					unwear(worn[i]);
			}
		}

		// Unwield all wielded items 
		if(wielded != null) {
			for(int i=0; i<wielded.length; i++) {
				if(wielded[i] != null)
					unwield(wielded[i]);
			}
		}
		
		
// HISTORIC_DATA: Archived on 18.2.2005
//		if(race instanceof UndeadRace) {
//			// Undeads drop all items on the ground 
//			for(int i=0; i<inventory.size(); i++) {
//				Item item = (Item) inventory.get(i);
//				if(item.isVisible())
//					currentRoom.add(item);
//			}
//			return;
//		} 
//
//		// World.log("DUMPATAAN NORMAALI CORPSE");
		
		
		// Move inventory items to the corpse
		// and add the corpse to the room

		ContainerItem corpse = new CorpseItem(this, true); /* xxx (turnundead shouldn't always be true) */
		corpse.setName("corpse of "+name);
		corpse.addAlias("corpse");
		int desc = Dice.random(2);
		switch(desc) {
		  case 1: corpse.setDescription("Decomposing carcass of "+name); break;
		  case 2: corpse.setDescription("Hacked remains of "+name); break;
		}
		
		for(int i=0; i<inventory.size(); i++) {
			Item item = (Item) inventory.get(i);
			if(item.isVisible()) {
				corpse.add(item);
			}
		}	
		inventory.clear();
		
		getMoney().moveAll(corpse.getMoney());
		
		getRoom().add(corpse);
	}

	public int getLifeAlignment() {
		return getRace().getLifeAlignment();
	}

	public int getProgressAlignment() {
		return getRace().getProgressAlignment();
	}
	
	public SkillObject getSkillObject() {
		return skillObject;
	}
	
}
