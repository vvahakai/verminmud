/* ForceGlobeSkill.java
	18.10.2003	VV
	
	skill for creating a force globe that damages everyone in the room
*/
package org.vermin.world.skills;

import java.util.Iterator;

import org.vermin.driver.Driver;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultItemImpl;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.util.Print;
import org.vermin.mudlib.Tick;


public class ForceGlobeSkill extends BaseSkill {

	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.OFFENSIVE, SkillTypes.MAGICAL, SkillTypes.AREA };
	
	private class ForceGlobe extends DefaultItemImpl { 
		   private int ticks;
		   private Living caster;
		   private Room room;
		   private int damage;

		   public int modify(MObject target) {
			   if(caster.getRoom() != room)
			   {
				  ticks = 0;
				  room.remove(this);
				  return 0;
			   }
			   return 10;
		   }

		   public String getDescription() {
			   return "a force globe <glowing>.";
		   }

		   public String getLongDescription() {
			   return "This force globe radiates malevolence.\nIt was created by " + Print.capitalize(caster.getName()) + ".";
		   }

		   public boolean tryTake(Living who) { return false; }

		   public boolean tick(short q) {
			   if(caster.getRoom() != room)
			   {
					room.remove(this);
					return false;
			   }
			   Iterator en = room.findByType(Types.TYPE_LIVING);
			   while(en.hasNext()) {
					Living l = (Living) en.next();
					Damage dam = new Damage();
					dam.damage = Dice.random(damage);
					dam.type = Damage.Type.MAGICAL;
					l.subHp(dam);
					l.notice("The force globe pulses with magical force. You feel weaker.");
			   }		
			   ticks--;
			   if(ticks > 0)
					return true;
			   
			   room.remove(this);
			   return false;
		   }

		   public ForceGlobe(int t, Living who, Room r, int dumass) {
			   ticks = t;
			   caster = who;
			   room = r;
			   damage = dumass;
			   Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
		   }
	}

	public SkillType[] getTypes() {
		return skillTypes;
	}

	private String spellWords = "Kgah eufa a,nas";

	public String getName() {
		return "force globe";
	}

	public int getCost(SkillUsageContext suc) { return 722; }

   public int getTickCount() {
      return 15 + Dice.random(5);
   }


	public void use(SkillUsageContext suc) {
		Living who = suc.getActor();
		int success = suc.getSkillSuccess();
		Room room = who.getRoom();

		if(success > 0)
		{
				who.notice("You chant '"+spellWords+"' and create a force globe.");
				who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' and creates a globe of force.");
		
				ForceGlobe fg = new ForceGlobe(success*who.getMentalDexterity()/10, who, room, (int) (who.getMentalDexterity()/1.33333));
				
				room.add(fg);			
		}
		else
		{
			who.notice("You chant '"+spellWords+"' but your spell just fizzles.");
			who.getRoom().notice(who, who.getName()+" chants '"+spellWords+"' but "+who.getPossessive()+" spell just fizzles.");
		}
	}
}
