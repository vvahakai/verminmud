/*
 * Created on Jul 17, 2004
 */
package org.vermin.world.items;

import org.vermin.mudlib.DefaultWieldableImpl;
import org.vermin.mudlib.*;

/**
 * @author Jaakko Pohjamo
 */
public class InsectStinger extends DefaultWieldableImpl {
	
	private static Damage[] dmg = null;
	private static String[] OBJECT_HIT_VERBS = new String[] { "impale", "pierce", "sting" };
	private static String[] SUBJECT_HIT_VERBS = new String[] { "impales", "pierces", "stings" };
	private static String[] HIT_NOUNS = new String[] { "impale", "pierce", "sting" };
			
	public boolean isWeapon() { return true; }
	
	static {
	   dmg = new Damage[2];
	   dmg[0] = new Damage();
	   dmg[0].type = Damage.Type.POISON;
	   dmg[0].damage = 15;
	   dmg[1] = new Damage();
	   dmg[1].type = Damage.Type.PIERCING;
	   dmg[1].damage = 5;
	}  
   
	public Damage[] getHitDamage(Living target) {
	   return dmg;
	}
   
	public String getObjectHitVerb(Damage.Type damageType) {
		if(damageType == Damage.Type.POISON) {
			return "poison";
		}
	   return oneOf(OBJECT_HIT_VERBS);
	}
	
	public String getSubjectHitVerb(Damage.Type damageType) {
		if(damageType == Damage.Type.POISON) {
			return "poisons";
		}
	   return oneOf(SUBJECT_HIT_VERBS);
	}
	
	public String getHitNoun(Damage.Type damageType) {
		return oneOf(HIT_NOUNS);
	}
	
	public int getDefensiveValue() {
	   return 0;
	}
	public boolean isVisible() {
		return false;
	} 
	public String getName() {
	   return "stinger";
	}
}
