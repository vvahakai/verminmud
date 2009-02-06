/* FactoryWeapon.java
	10.3.2002	Ville V�h�kainu
	
	a configurable melee weapon.
*/

package org.vermin.mudlib;

import java.util.EnumSet;

public class FactoryWeapon extends DefaultWieldableImpl {	
	
	private Damage.Type[] damageTypes = { Damage.Type.PHYSICAL };

	private int weaponClass;	
	private int projectileClass;
	
	private int defensiveValue;

	private transient int weaponType;  // old save field for weapon type,
												  // marked transient to remove the old style
												  // data from savefiles.
	private WeaponType type; // new enum weapon type.
	
	public Damage[] getHitDamage(Living target) {
		Damage[] dmg = new Damage[damageTypes.length];
		for(int i = 0; i < damageTypes.length; i++) {
			dmg[i] = new Damage();
			dmg[i].damage = this.weaponClass/damageTypes.length;
			dmg[i].type = damageTypes[i];
		}	
		return dmg;	
	}
	
	public Damage[] getProjectileDamage() {
		Damage.Type damType = Damage.Type.PIERCING ;
		for(int i = 0; i < damageTypes.length; i++) {
			if(damageTypes[i].equals(Damage.Type.CRUSHING)) {
				damType = Damage.Type.CRUSHING;
			}
			if(damageTypes[i].equals(Damage.Type.CHOPPING)) {
				damType = Damage.Type.CHOPPING;
			}				
		}
			
		Damage[] projectileDmg = new Damage[1];
		projectileDmg[1] = new Damage();
		projectileDmg[1].damage = this.projectileClass;
		projectileDmg[1].type = damType;	
		return projectileDmg;		
	}
	
	// start method added to mirgate existing save files
	// to enum weapon types.
	public void start() {
		if(weaponType > 0) {
			int index = 0;
			for(WeaponType type : EnumSet.allOf(WeaponType.class)) {
				if(index == weaponType) {
					this.type = type;
					this.weaponType = 0;
					break;
				}
				index++;
			}
		}
		
		if(this.type == null) {
			this.type = WeaponType.NONE;
		}
	}
	
	public void setDamageType( Damage.Type[] damageTypes ) 
	{
		this.damageTypes = damageTypes;
	}
	
	public WeaponType getWeaponType() { return type; }
	
	public void setHitDamage(int weaponClass)
	{
		this.weaponClass = weaponClass;
	}
	
	public void setProjectileClass(int projectileDamage)
	{
		this.projectileClass = projectileDamage;
	}	

	public void setWeaponType(WeaponType weaponType)
	{
		this.type = weaponType;
	}

	public void setMaterial(String material)
	{
		this.material = material;
	}

	public int getDefensiveValue() {
		return defensiveValue;
	}

	public void setDefensiveValue(int defensiveValue)
	{
		this.defensiveValue = defensiveValue;
	}

	public void setSpeed(float speed) {
		this.hitSpeed = speed;
	}
}
