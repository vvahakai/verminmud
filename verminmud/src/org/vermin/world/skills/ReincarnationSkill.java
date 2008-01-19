package org.vermin.world.skills;

import org.vermin.mudlib.Commander;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.RegexChoice;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.commands.Look;
import org.vermin.mudlib.skills.BaseSkill;
public class ReincarnationSkill extends BaseSkill {
   public ReincarnationSkill() {
   }

   public static class ReincarnationChoice extends RegexChoice {
	   private Player chooser;
	   private Player reincarnator;
	   
	   public ReincarnationChoice(Player chooser, Player reincarnator) {
		   super(chooser, 3);
		   this.chooser = chooser;
		   this.reincarnator = reincarnator;
	   }
	   
	   public String[] getDispatchConfiguration() {
		   return new String[] {"choose accept => accept(actor)",
				   				"choose decline => decline(actor)"};
	   }
	   public void accept(Living actor) {
		   chooser.notice("You accept reincarnation from "+reincarnator.getName()+".");
		   chooser.notice("You have been reincarnated by "+reincarnator.getName()+".");
		   //TODO: add reinc exp cost and reward to reincarnator
		   
		   chooser.getRoom().remove(chooser);
		   Room r = new org.vermin.mudlib.rooms.Reincarnation(chooser);
		   chooser.setParent(r);
		   
		   Look l = (Look) Commander.getInstance().get("look");
		   l.look(chooser, true);

		   reincarnator.notice("You have reincarnated "+chooser.getName()+".");
	   }
	   
	   public void decline(Living actor) {
		   actor.notice("You decline reincarnation from "+reincarnator.getName()+".");
		   reincarnator.notice(chooser.getName()+" declines your offer for reincarnation.");
	   }
	   
	   public void usage(Living who) {
		   who.notice(reincarnator.getName()+" has offered to reincarnate you. To accept, type 'choose accept'. To decline, type 'choose decline'.");
	   }
	   
	   public void timeout(Living who) {
		   who.notice("The reincarnation offer from "+reincarnator.getName()+" has expired.");
		   reincarnator.notice("Your offer to reincarnate "+who.getName()+" expires unanswered.");
	   }
   }   
   
   
   
   public boolean tryUse(Living who, MObject target) {
	   if (hasLivingTarget(who, target) && target instanceof Player)
	   {
		if (((Player)target).isDead())
			return true;
		who.notice(target.getName()+" is still alive.");
	   }
	   return false;
   }		

   protected SkillType[] skillTypes = new SkillType[] { SkillTypes.REMOTE, SkillTypes.RESURRECT };

   public SkillType[] getTypes() {
	   return skillTypes;
   }

   public void use(SkillUsageContext suc) {
	   Living who = suc.getActor();
	   MObject target = suc.getTarget();
	   int s = suc.getSkillSuccess();
	   if(s < 0) {
		   who.notice("You chant 'incarnis ratrum' but your spell just fizzles.");
		   who.getRoom().notice(who, who.getName()+" chants 'incarnis ratrum'.");
		   return;
	   }
	   else {
		   who.notice("You chant 'incarnis ratrum'.");
		   who.getRoom().notice(who, who.getName()+" chants 'incarnis ratrum'."); 
	   }
	   ((Living) target).notice(who.getName()+" offers to reincarnate you. To accept, type 'choose accept'. To decline, type 'choose decline'.");
	   who.notice("You offer to reincarnate "+target.getName()+".");
	   new ReincarnationChoice((Player) target, (Player) who);
   }

   public int getCost() {
	   return 300;
   }

   public int getTickCount() {
	   return 10;
   }

   public String getName() {
	   return "reincarnation";
   }

}
