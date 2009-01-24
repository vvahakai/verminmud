package org.vermin.mudlib.behaviour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Skill;
import org.vermin.mudlib.SkillObject;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.World;
import org.vermin.util.Print;

public class BattleSkillBehaviour extends BehaviourAdapter {

	private ArrayList<String> skillList = null;
	
	public BattleSkillBehaviour() {
		super();
	}

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living, org.vermin.mudlib.Exit)
     */
    public void arrives(Living who, Exit from) {
    	if(owner.getBattleGroup().isOpponent(who) && !owner.getSkillObject().skillActive()) {
    		triggerSkill(who);
    	}
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living)
     */
    public void arrives(Living who) {
    	if(owner.getBattleGroup().isOpponent(who) && !owner.getSkillObject().skillActive()) {
    		triggerSkill(who);
    	}
    }
    
    public void onBattleTick(Living who) {
    	// World.log("BattleSkillBehaviour.onBattleTick() called.");
    	if(owner.inBattle() && !owner.getSkillObject().skillActive()) {
    		triggerSkill(owner.getBattleGroup().getOpponent(owner.getRoom()));
    	}
    }
	
    protected void triggerSkill(Living target) {
    	// World.log("BattleSkillBehaviour.triggerSkill() called.");
    	if(skillList == null || skillList.isEmpty()) {
    		World.log("BattleSkillBehaviour.triggerSkill() behaviour has no defined skills to use");
    		findSkills();
    		if(skillList == null) {
    			World.log("BattleSkillBehaviour.triggerSkill() no skills found.");
    			return;	
    		}
    	}
    	String skill = skillList.get(Dice.random(skillList.size())-1);
    	Vector params = new Vector();
    	params.add(skill);
    	params.add("at");
    	params.add(target.getName());
    	World.log("BattleSkillBehaviour.triggerSkill() Using '"+Print.vectorToString(params, " ")+"'");
    	owner.getSkillObject().useSkill(owner, params);
    }
    
    protected void findSkills() {
    	// World.log("BattleSkillBehaviour.findSkills() called.");
    	HashSet<String> availableSkills = new HashSet<String>(owner.getSkillObject().getSkills().keySet());
    	for(String skill : availableSkills) {
    		Skill sk = SkillObject.skillsProvider.get(skill);
        	World.log("BattleSkillBehaviour.findSkills() examining skill "+sk.getName()+".");
    		if(sk != null) {
    			SkillType[] st = sk.getTypes();
    			for(int i=0;i<st.length;i++) {
    				if(st[i] == SkillTypes.OFFENSIVE) {
    			    	World.log("BattleSkillBehaviour.findSkills() skill is valid.");
    			    	if(skillList == null) 
    			    		skillList = new ArrayList<String>();
    					skillList.add(sk.getName());
    					break;
    				}
    			}
    		}
    	}
    	return;
    }
}
