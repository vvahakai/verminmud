package org.vermin.world.skills;

import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.driver.*;

import java.util.Iterator;

public class ShelterSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.PROTECTIVE, SkillTypes.LOCAL };

	private class Forcefield extends DefaultItemImpl implements Tickable {

		private int ticks;
		private Player caster;

		public Forcefield(Player caster, int ticks) {
			this.caster = caster;
			this.ticks = ticks;
			Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
			caster.addHandler("touch", this);
		}

		public boolean isActive() {
			if(ticks > 0)
				return true;
			return false;
		}

		public boolean tick(short queue) {
			ticks--;
			if(ticks <= 0)
			{
				if(this.getParent() != null) {
					caster.removeHandler(this);
					this.getParent().remove(this);
					this.setParent(null);
				}
				return false;
			}
			return true;
		}

		public boolean tryTake(Living who) {
			return false;
		}

		public boolean action(Player who, String command) {
			if(who == caster && (command.equals("touch forcefield") || command.equals("touch field"))) {
				ticks = 0;
				this.tick((short) 1);
				return true;
			}
			return false;
		}

		public String getDescription() {
			return "a flashing forcefield";
		}

		public String getLongDescription() {
			return "a flashing forcefield created by "+caster.getName();
		}
	}

	private class ExitBlockingModifier implements Modifier {
		private Forcefield ff;
		private int strength;
		private String roomId;
		private boolean active = true;

		public Object[] getArguments() {
			return new String[] { roomId };
		}

		public ModifierTypes getType() {
			return ModifierTypes.BARRIER;
		}

		public void deActivate() {
			active = false;
		}

		
		public boolean isActive() {
			active = ff.isActive();
			return active;
		}
		
		public int modify(MObject target) {
			return strength;
		}

		public String getDescription() {
			return "The forcefield prevents you from moving.";
		}

		public ExitBlockingModifier(String roomId, Forcefield ff, int str) {
			this.roomId=roomId;
			this.ff = ff;
			this.strength = str;
		}
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Ananasa kaama!";

	public String getName() {
		return "shelter";
	}

	public boolean tryUse(Living who, MObject target) {
		if(!(who instanceof Player))
			return false;
		return true;
	}

	public int getCost(SkillUsageContext suc) { return 128; }

   public int getTickCount() {
      return 5 + Dice.random(2);
   }

	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();

		if(!(who instanceof Player))
			return;
	
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0)
		{
			who.notice("You chant '"+spellWords+"' and create a flashing forcefield.");
			room.notice(who, who.getName() + "'s spell creates a flashing forcefield.");
			
			int length = Dice.random(who.getMentalConstitution());
			int str = who.getMentalStrength();
			
			Forcefield ff = new Forcefield((Player) who, length);
			room.add(ff);

			Exit e;
			Iterator it = room.findByType(Types.EXIT);
			while(it.hasNext()) {
				e = (Exit) it.next();				
				ExitBlockingModifier ebm = new ExitBlockingModifier(room.getId(), ff, str);
				e.addModifier(ebm);	
			}
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
