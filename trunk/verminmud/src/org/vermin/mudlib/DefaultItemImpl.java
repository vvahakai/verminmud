/* DefaultItemImpl.java
	13.1.2002	Tatu Tarvainen / Council 4
	
	Default item implementation.
*/

package org.vermin.mudlib;

import java.util.LinkedList;

public class DefaultItemImpl extends DefaultObjectImpl implements Item {

	protected int size;
	protected String material;
	protected int dp;
   protected int maxDp;
   protected boolean mayTake;

   protected String pluralForm;

	protected LinkedList sizeModifiers;
	protected LinkedList maxDpModifiers; 
	
	public DefaultItemImpl() {
		size = 1;
		material = "antimatter";
		dp = 1;
      maxDp = 1;
      mayTake = true;
	}

	public boolean tryTake(Living who) {
		if(!mayTake)
			who.notice("You can't take "+name);
		return mayTake;
	}

	public boolean tryDrop(Living who, MObject where) {
		return true;
	}
	
	public boolean isVisible() {
		return true;
	}
	
	public void setMaterial(String material) {
		this.material = material;
	}
	
	public void take(Living who) {}
	
	public void drop(Living who) {}

	public boolean isWeapon() {
		return true;
	}

	public String getLongDescription() { 
		return longDescription+
			"\nIt weights about: "+(this.getWeight()+500)/1000+" kg."; }	
	
	public Damage[] getHitDamage(Living target) {
		Damage[] d = new Damage[1];
		d[0] = new Damage();
		d[0].type = Damage.Type.PHYSICAL;
		d[0].damage = 1;
		
		return d;
	}
	
	// xxx: why is getWeaponType implemented here?
	// 	  it's defined in the Wieldable interface,
	//      not in Item
	public WeaponType getWeaponType() {
		return WeaponType.NONE;
	}

	public boolean tryWield(Living who) {
		if(!isWeapon()) {
			who.notice("You can't wield that.");
			return false;
		}
		
		return true;
	}
	
	public boolean tryUnwield(Living who) {
		return true;
	}
	
	public void wield(Living who) {}
	
	public void unwield(Living who) {}
	
	public String getObjectHitMessage(int damageType) { return null; }
	public String getSubjectHitMessage(int damageType) { return null; }
	public String getSpectatorHitMessage(int damageType) { return null; }
	
	public int getSize() {
		return size;
	}

	public int getSize(boolean modifiers) {
		return modifiers ? getSize() : DefaultModifierImpl.calculateInt(this, getSize(), sizeModifiers);
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public Material getMaterial() {
		return MaterialFactory.createMaterial(material);
	}
	
	public int getDp() {
		return dp;
	}
	
   public void setMaxDp(int maxDp) {
      this.maxDp = maxDp;
   }
   public int getMaxDp() {
      return maxDp;
   }

	public void addDp(int amount) {
		dp += amount;
	}
	
	public void subDp(int amount) {
		dp -= amount;
		
		if(dp <= 0) {
			if(this.getParent() instanceof Player)
				((Player) this.getParent()).notice("Your "+description+" shatters to pieces.");
			this.getParent().remove(this);
		}
	}
	
	public String getObjectHitVerbose(int hit) {
		return "wildly rotate your $weapon$";
	}
	
	public String getSubjectHitVerbose(int hit) {
		return "wildly rotates $his$ $weapon";
	}
	
	public void hitReact() {}
	
	public boolean hitOverride(Living attacker, Living target) {
		return false;
	}
	
   public String getPluralForm() {
      return pluralForm;
   }
   public void setPluralForm(String form) {
      pluralForm = form;
   }

	public void addModifier(Modifier m) {
		switch(m.getType()) {
		  case SIZE:
			  if(sizeModifiers == null) 
				  sizeModifiers = new LinkedList();
			  sizeModifiers.add(m);
			  break;

		  case MAXDP:
			  if(maxDpModifiers == null) 
				  maxDpModifiers = new LinkedList();
			  maxDpModifiers.add(m);
			  break;

		  default: super.addModifier(m);
		}
	}

	public int getWeight() {
		return getSize()*getMaterial().getWeight();
	}

}
