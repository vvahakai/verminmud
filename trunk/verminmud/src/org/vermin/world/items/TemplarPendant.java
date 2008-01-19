package org.vermin.world.items;

import java.util.*;

import org.vermin.mudlib.*;

/**
 * The templar guild item.
 */
public class TemplarPendant extends GuildItem implements Wearable {

	public Slot[] getSlots() {
		return new Slot[] { new Slot(Slot.NECK) };
	}

	public UtilitySlot[] getUtilitySlots() {
		return null;
	}
	
	public int getArmourValue(Damage.Type dt) {
		return 0;
	}
	public int getArmourValue() {
		return 0;
	}

    public TemplarPendant() {
        addAlias("pendant");
        addAlias("sacred pendant");
        addAlias("a sacred pendant");
        addAlias("a pendant");
    }
    
	public void onUnwear(Living who) {}
	public void onWear(Living who) {} 
	public boolean tryWear(Living who) { return true; }
	public boolean tryUnwear(Living who) { return false; }

	public static class Mod implements Modifier {
		int amount;
		boolean active = true;
		ModifierTypes type;
		Object[] args;

		public Mod() {}
		
		Mod(ModifierTypes type, Object ... args) {
			this.type = type;
			this.args = args;
		}

		public boolean isActive() { return active; }
		public int modify(MObject target) {
			return amount;
		}
		public String getDescription() { return null; }
		public void deActivate() {
			active = false;
		}
		public ModifierTypes getType() {
			return type;
		}
		public Object[] getArguments() {
			return args;
		}
	}

	private EnumMap<Damage.Type, Mod> resistanceModifiers = new EnumMap(Damage.Type.class);
	private Mod quickChantModifier = new Mod(ModifierTypes.SKILL, "quick chant");
	private Mod parryModifier = new Mod(ModifierTypes.SKILL, "parry" );
	private Mod riposteModifier = new Mod(ModifierTypes.SKILL, "riposte");
	private Mod criticalModifier = new Mod(ModifierTypes.SKILL, "critical");
	private Mod essenceEyeModifier = new Mod(ModifierTypes.SKILL, "essence eye");
	private Mod spRegenModifier = new Mod(ModifierTypes.SPREGEN);

	// a counter for accumulated "soul stuff" from last rites
	private int virtue = 0;

	private int morale = 0;
	private int deliverance = 0;
	private int communion = 0;
	private int smiting = 0;

	public int getSmiting() {
		return smiting;
	}
	public void increaseSmiting() {
		smiting++;
		if(smiting == 1) {
			Living l = (Living) getParent();
			l.addModifier(criticalModifier);
		}
		criticalModifier.amount++;
	}

	public int getCommunion() {
		return communion;
	}
	public void increaseCommunion() {
		communion++;
		if(communion == 1) {
			// add modifiers
			Living l = (Living) getParent();
			l.addModifier(spRegenModifier);
			l.addModifier(quickChantModifier);
			l.addModifier(essenceEyeModifier);
		}
		spRegenModifier.amount++;
		essenceEyeModifier.amount += 2;
		quickChantModifier.amount++;
	}
	public int getDeliverance() {
		return deliverance;
	}
	public void increaseDeliverance() {
		deliverance++;
		if(deliverance == 1) {
			Living l = (Living) getParent();
			l.addModifier(parryModifier);
			l.addModifier(riposteModifier);
		}
		parryModifier.amount++;
		riposteModifier.amount++;
	}
	public int getMorale() {
		return morale;
	}
	public void increaseMorale() {
		morale++;
		Living l = (Living) getParent();
		
		Mod res = resistanceModifiers.get(Damage.Type.PHYSICAL);
		if(res == null) {
			res = new Mod(ModifierTypes.RESISTANCE, Damage.Type.PHYSICAL.ordinal());
			l.addModifier(res);
			resistanceModifiers.put(Damage.Type.PHYSICAL, res);
		}

		res.amount++;
		//FIXME: Lisää randomilla eri resistanceja myös
	}


	public boolean isVisible() {
		return true;
	}

	public int getVirtue() {
		return virtue;
	}

	public void setVirtue(int lr) {
		virtue = lr;
	}

	public String getDescription() {
		return "a sacred pendant";
	}

	public String getLongDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("This sacred pendant is a symbol of the templar's honor.");
		sb.append(" It seems to have "+virtue+" amount of virtue accumulated.");
		return sb.toString();
	}
	
	public String getGuildName() {
		return "Templars";
	}
}
