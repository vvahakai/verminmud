/* WeaponType.java
	19.1.2002	TT & VV & JP
	
	
	Weapon types.
*/

/*
package org.vermin.mudlib;
import org.vermin.driver.*;

public class WeaponType
{

	public static final int NONE 	 = 0; // any household item or bodypart
	public static final int SWORD	 = 1;
	public static final int DAGGER	 = 2;
	public static final int AXE      = 3;
	public static final int BLUDGEON = 4; // maces, clubs 
	public static final int SPEAR	 = 5; // britney, lance
	public static final int POLEARM	 = 6; //  halberd, naginata, poleaxe
	public static final int FLAIL	 = 7; // morningstar
	public static final int WHIP     = 8;
        public static final int SHIELD   = 9;
	
	public static final String[] skillNames =
		 { "brawling", "swords", "daggers", "axes", "bludgeons", "spears", "polearms", "flails", "whips", "shields" };

	
	public static int getPhysicalDamageType(int weaponType)
	{
	
		switch(weaponType)
		{
		
			case SWORD: return Damage.SLASHING;
			case DAGGER: return Damage.PIERCING;
			case AXE: return Damage.CHOPPING;
			case BLUDGEON: return Damage.CRUSHING;
			case SPEAR: return Damage.PIERCING;
			case POLEARM: return Damage.PIERCING;
			case FLAIL: return Damage.CRUSHING;
			case WHIP: return Damage.SLASHING;
                        case SHIELD: return Damage.CRUSHING;
	
			default: return Damage.CRUSHING;
		}
	}
}
*/

package org.vermin.mudlib;

public enum WeaponType {
	
	NONE     ("brawling",  "weapon",   Damage.Type.CRUSHING),
	SWORD    ("swords",    "sword",    Damage.Type.SLASHING),
	DAGGER   ("daggers",   "dagger",   Damage.Type.PIERCING),
	AXE      ("axes",      "axe",      Damage.Type.CHOPPING),
	BLUDGEON ("bludgeons", "bludgeon", Damage.Type.CRUSHING),
	SPEAR    ("spears",    "spear",    Damage.Type.PIERCING),
	POLEARM  ("polearms",  "polearm",  Damage.Type.PIERCING),
	FLAIL    ("flails",    "flail",    Damage.Type.CRUSHING),
	WHIP     ("whips",     "whip",     Damage.Type.SLASHING),
	SHIELD   ("shields",   "shield",   Damage.Type.CRUSHING);
	
	private final String skillName;
	private final String weaponTypeName;
	private final Damage.Type physicalDamageType;
	
	WeaponType(String skillName, String weaponTypeName, Damage.Type physicalDamageType) {
		this.skillName = skillName;
		this.weaponTypeName = weaponTypeName;
		this.physicalDamageType = physicalDamageType;
	}
	
	public String getSkillName() {
		return skillName;
	}
	
	public String getWeaponTypeName() {
		return weaponTypeName;
	}
	
	public Damage.Type getPhysicalDamageType() {
		return physicalDamageType;
	}
}
