/* Torch.java
	2.8.2003	VV
	
	An torch for use by create torch skill
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;
import org.vermin.driver.*;

import java.util.Vector;

public class Torch extends DefaultWieldableImpl
{

	public Torch() {
		name = "torch";
		description = "a torch";
		longDescription = "a torch";
		pluralForm = "torches";
	}
	
	private int fullFuel;
	private int fuel;	
	private boolean onFire = false;

	private static final String OBJECT_HIT_VERBOSE = "swing your $weapon$";
	private static final String SUBJECT_HIT_VERBOSE = "swings $his$ $weapon$";

	private static Damage[] dmg = null;
	
	public boolean isWeapon() { return true; }

	public void setParent(Container parent) {
		super.setParent(parent);
		parent.addModifier(new LightModifier());
	}

	public void torchDouseMessages()
	{
		MObject parent = getParent();

		setDescription("a torch");

		if(parent instanceof Living)
		{
			Living who = (Living) parent;
			who.notice("Your torch is extinguished.");
			who.getRoom().notice(who, who.getName() + "'s torch is extinguished.");
		}
		else if(parent instanceof Room)
		{
			Room where = (Room) parent;
			where.notice(null, "The torch on the ground is extinguished.");
		}
	}

	public void hitReact() 
	{ 
		if(onFire)
		{
			fuel--;
			if(fuel <= 0)
			{
				torchDouseMessages();
				setDescription("a torch (used)");
				onFire = false;
			}
		}
	}

	private class LightModifier implements Modifier {
		public LightModifier() {}
		public boolean isActive() {
			return onFire;
		}
		public int modify(MObject target) {
			if(target == getParent()) {
				return getLightModifier();
			} else {
				return 0;
			}
		}
		public ModifierTypes getType() {
			return ModifierTypes.LIGHT;
		}
		public Object[] getArguments() {
			return new Object[0];
		}
		public void deActivate() {
			onFire = false;
		}

		public String getDescription() {
			return null;
		}
	}

	public int getLightModifier()
	{
		if(onFire && fullFuel > 0)
			return 50 - (25 * (fullFuel - fuel) / fullFuel);
		else
			return 0;
	}

	public boolean action(MObject who, Vector params) {

		if(!(who instanceof Living))
			return false;

		if(params == null)
			return false;

		Living object = (Living) who;

		if(params.get(0).equals("ignite") || params.get(0).equals("light")) {
			if(onFire) {
				object.notice("It is already on fire.");
			} else if(fuel > 0) {
				object.notice("You ignite the torch.");
				Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
				onFire = true;
				setDescription("a torch (burning)");
			} else {
				object.notice("It is all burnt out.");
			}
			return true;
		} else if(params.get(0).equals("douse") || params.get(0).equals("extinguish")) {
			torchDouseMessages();
			fuel--;
			onFire = false;
			return true;
		}
		return false;
	}

	public boolean tick(short type) {
	
		if(fuel <= 0)
		{
			torchDouseMessages();
			setDescription("a torch (used)");
			onFire = false;
		}

		if(!onFire)
			return false;
		
		fuel--;

		return true;
	}

	public Damage[] getHitDamage(Living target)
	{
		if(dmg == null || (onFire && dmg.length == 1) || (!onFire && dmg.length == 2)) {
			if(onFire)
				dmg = new Damage[2];
			else
				dmg = new Damage[1];
	
			dmg[0] = new Damage();
			dmg[0].type = Damage.Type.CRUSHING;
			dmg[0].damage = 2;
			if(onFire)
			{
				dmg[1] = new Damage();
				dmg[1].type = Damage.Type.FIRE;
				dmg[1].damage = fuel / 10;
			}
		}		
		return dmg;
	}

	public WeaponType getWeaponType() { return WeaponType.BLUDGEON; }

	public String getObjectHitVerbose(int hit)
	{
		return OBJECT_HIT_VERBOSE;
	}
	
	public String getSubjectHitVerbose(int hit)
	{
		return SUBJECT_HIT_VERBOSE;
	}
	
	public void setFuel(int amount)
	{
		// TT: Added setting of full fuel... 
		//     I presume, that it is the correct thing to do (otherwise getLightModifier divides by zero)
		fullFuel = amount;
		fuel = amount;
	}
}
