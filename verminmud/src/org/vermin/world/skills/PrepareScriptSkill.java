/*
 * Created on 30.3.2005
 *
 */
package org.vermin.world.skills;

import java.util.HashSet;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.items.Script;

/**
 * @author Matti V�h�kainu
 *
 */
public class PrepareScriptSkill extends BaseSkill {
	
	public SkillType[] getTypes() {
		return new SkillType[] {SkillTypes.SUMMONING};
	}
	
	public String getName() {
		return "prepare script";
	}
	
	public int getTickCount() {
		return 10;
	}
	
	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		Script script = new Script();
		script.addAlias("_golem_perkele_script");
		int success = suc.getSkillSuccess();
		
		String name = suc.getTargetName();
		if (success > 0) {
			int languages = who.getSkill("ancient languages");
			int rites = who.getSkill("ancient rites");
			
			script.setPower((languages + rites) * 2 + success);
			if (name == null) 	
				who.notice("You must specify power words for the script.");
			/*
			 * Following words represent different statbonuses to golem
			 * They are place sensitive, therefore you zapkilbang words 
			 * do nothing, but kilzapband gives golem +50str(kil),
			 * +20 magical resistance(zap), +1200 maxhp(bang)
			 *
			 * The bonuses are defined in the GolemCreationSkill.java
			 */
			
			HashSet<String> scripts = new HashSet<String>();
			
			scripts.add("kil-zap-bang");
			scripts.add("kil-bang-zap");
			scripts.add("snap-crackle-pop");
			scripts.add("dakka-lakka-boom");
			scripts.add("bada-dum-rak");
			scripts.add("knick-grip-zop");
			scripts.add("plim-plam-plom");
			scripts.add("plim-dum-pop");
			scripts.add("dakka-plam-rak");
			scripts.add("kil-grip-plom");
			
			boolean found = false;
			
			for(String existingScript : scripts) {
				if(existingScript.equals(name)) {
					String [] words = name.split("-"); 
					script.setWords(words[0], words[1], words[2]);
					who.notice("You successfully prepare a script.");
					who.add(script);
					found = true;
					break;
				}
			} 
			if(!found) {
				who.notice("These words don't seem to have any power.");
			}
		} else
			who.notice("You fail to prepare a script.");
		
	}

}
