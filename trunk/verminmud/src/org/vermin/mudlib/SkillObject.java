/* SkillObject.java
 19.1.2002	Tatu Tarvainen / Council 4
 
 An invisible Item that handles skill/spell usage.
 */

package org.vermin.mudlib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import static org.vermin.mudlib.LivingProperty.NO_SKILLS;
import org.vermin.util.Print;

public class SkillObject implements ActionHandler<MObject> {
    
	public static Skills skillsProvider;
	
    private static HashMap<String, String> skillMapping;
    
    
    public static class SkillEntry {
    	// This inner entry class is a legacy relic
        // public String skillId = "";
    	
        public int percent = 0;
        public SkillEntry(int percent) {
            this.percent = percent;
        }
        public SkillEntry() {}
    }
    
    /* Player skills and spells.
     * Mapped as (String name, SkillEntry entry).
     */
    protected HashMap<String,SkillEntry> skills;
    
    /* The skill currently being used, the target and the parameters. */
    protected transient Skill skill;
    protected transient SkillUsageContext suc;
    protected transient MObject target;
    protected transient Vector params;
    
    /* Number of ticks left until skill activates */
    protected int ticksLeft;
    
    public SkillObject() {
        skills = new HashMap();
    }
    
    public void clearSkills() {
        skills.clear();
    }
    
    
    /* And now the interesting stuff.
     * The implementation of the commands.
     */
    public boolean action(MObject caller, Vector params) {
    	Living p = (Living) caller; /* I really should catch the ClassCastExcp */
        if(p.isDead()) {
        	p.notice("You are dead.");
        	return true;
        }
        String cmd = (String) params.get(0);
        
        if(cmd.equals("attack") || cmd.equals("kill")) {
            params.removeElementAt(0);
            attack(p, params);
        } else if(cmd.equals("cast") || cmd.equals("use")) {
            params.removeElementAt(0);
            useSkill(p, params);
        } else {
            return false;
        }
        return true;
    }
    
    /* Get a skill percentage */
    public int getSkill(String name) {
        int per = 0;
        try {
            per = ((SkillEntry) skills.get(name)).percent;
        } catch(Exception e) {}
        return per;
    }
    
    /* Set a skill percentage (or add if not in the list) */
    public void setSkill(String name, int percent) {
        SkillEntry se = (SkillEntry) skills.get(name);
        
        if(se != null) {
            se.percent = percent;
        } else {
            skills.put(name, new SkillEntry(percent));
        }
    }
    
    public void useSkill(Living p, Vector params) {
        
    	if(params.size() < 1) {
    		if(suc != null)
    			p.notice("You are currently using: "+suc.getSkill().getName());
    		else
    			p.notice("Use what?");
            return;
        }
    	
        if(suc != null) {
            p.notice("You decide to try something different.");
            suc = null;
            
        }
        
        target = null; 
        
        StringBuffer skillName = new StringBuffer();
        while(params.size() > 0 && !params.get(0).toString().equalsIgnoreCase("at")) {
            skillName.append(params.get(0).toString());
            skillName.append(" ");
            params.removeElementAt(0);
        }
        String sk = skillName.toString().trim();
        SkillEntry se = (SkillEntry) skills.get(sk);
        if(se == null) {
            p.notice("You don't know a skill named '"+sk+"'.");
            return;
        }
        
        // Load the skill
        skill = skillsProvider.get(sk);
        if(skill == null)
            throw new RuntimeException("Skill '"+sk+"' was not found.");
        suc = new SkillUsageContext(p, target, skill);
        
        
        SkillType[] types = skill.getTypes();
        
        if(params.size() > 1) {
            // World.log("Starting skill against specified target");
            // if target is specified, don't use default
            suc.setTarget(null);
            
            params.removeElementAt(0); // remove the 'at' word
            StringBuilder targetName = new StringBuilder();
            while(params.size() > 0) {
                targetName.append(params.get(0).toString());
                targetName.append(" ");
                params.removeElementAt(0);
            }
            String tn = targetName.toString().trim();
            target = null;
            int typei=0;
            
            suc.setTargetName(tn);
            /* 1. Resolve the skill target */
            while(target == null && typei < types.length)
                target = types[typei++].findTarget(p, tn);
            suc.setTarget(target);
            // World.log("Target found for skill.");
        }
        
        /* 2. Check if skill can be used */
        for(SkillType type : types) {
            boolean canUse = type.tryUse(suc);
            if(!canUse) {
            	// World.log("Skill cannot be used because of skilltype");
                suc = null;
                target = null;
                return;
            }
        }
        // also check the skill itself
        if(!skill.tryUse(suc)) {
        	// World.log("Skill try use failed");
            suc = null;
            target = null;
            return;
        }

        // World.log("Calling type and skill onStarts");
        
        for(SkillType type : types)
			type.onStart(suc);
		skill.onStart(suc);		
        
        p.doBattle();
        
        /* Notify others in the room that we start using a skill */
        // PENDING: How to implement concealed usage?
        Iterator en = p.getRoom().findByType(Types.TYPE_LIVING);
        while(en.hasNext()) {
            ((Living) en.next()).startsUsing(p, null);
        }
    }
    
    
    public boolean attack(Living p, Vector params) {
        
        if(params.size() < 1) {
            // World.log("No parameters...");
            return false;
        }
        
        BattleGroup grp = p.getBattleGroup();
        
        String tgtName = Print.vectorToString(params, " ", 0);
        target = p.getRoom().findByNameAndType(tgtName, Types.TYPE_LIVING);
        
        if(target == null) {
            p.notice(tgtName+" is not present.");
            // World.log("Target is null");
        } else {
            grp.addHostileGroup(((Living) target).getBattleGroup());
            p.getBattleStyle().setTarget((Living) target);
        }
        
        return true;
    }
    
    public boolean updateSkill(Living p) {
        
        if(suc == null) return false;
        
        if(suc.getActor().provides(NO_SKILLS))
            return false;
        
        // World.log("UPDATE SKILL: "+suc.getSkill().getName());
        suc.setTicksLeft(suc.getTicksLeft()-1);
        
        SkillType[] types = suc.getSkill().getTypes();
        for(SkillType type : types) {
            type.onTick(suc);
        }
        
		suc.getSkill().onTick(suc);        
        
        if(suc.isAborted()) {
            // World.log("aborted skill!");
            suc = null;
            skill = null;
            return false;
        }
        
        if(suc.getTicksLeft() < 1) {
            
            // World.log("FIRE SKILL: "+suc.getSkill().getName());
            
            suc.setSkillSuccess(skill.checkSuccess(suc.getActor()));
            
            SkillType[] subTypes = suc.getSkill().getTypes();
            for(int i=0; i<subTypes.length; i++)
                subTypes[i].calculateSkillCostModifier(suc, suc.getSkill().getCost(suc));			
            
            int cost = skill.getCost(suc);
            
            if(Math.abs(suc.getSkillCostModifier()) > 0) {
                cost = cost + suc.getSkillCostModifier();
                if(suc.getSkillCostModifierMessage() != null && suc.getSkillCostModifierMessage().length() > 0)
                    suc.getActor().notice(suc.getSkillCostModifierMessage());
            }
            
            suc.getActor().subSp(cost);
            
            for(SkillType type : types) 
                type.onUse(suc);
            
            if(!suc.isAborted()) {
                suc.getActor().notice("&B;You are done concentrating.&;");
                skill.use(suc);
            }
            
            suc = null;
            skill = null;
            return false;
        } else {
            return true;
        }
    }
    
    public HashMap<String, SkillEntry> getSkills() {
        return skills;
    }
    
  
    
    /* Returns true if the player is currently using a skill. */
    public boolean skillActive() {
        return suc==null ? false : true;
    }
    
    public void stopSkill() {
        suc = null;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.ActionHandler#action(java.lang.Object, java.lang.String)
     */
    public boolean action(MObject caller, String action) {
        Vector v = new Vector();
        String[] args = action.split(" +");
        for(String a : args) v.add(a);
        System.out.println("SkillObject action: "+action);
        System.out.println("   # args: "+v.size());
        return action(caller, v);
    }
}
