package org.vermin.world.skills;
import org.vermin.mudlib.*;
import org.vermin.mudlib.skills.*;
import org.vermin.mudlib.commands.*;
public class ResurrectionSkill extends BaseSkill {
   public ResurrectionSkill() {
   }
   
   public static class ResurrectionChoice extends RegexChoice {
	   private Player chooser;
	   private Player resurrector;
	   
	   public ResurrectionChoice(Player chooser, Player resurrector) {
		   super(chooser, 3);
		   this.chooser = chooser;
		   this.resurrector = resurrector;
	   }
	   
	   public String[] getDispatchConfiguration() {
		   return new String[] {"choose accept => accept(actor)",
				   				"choose decline => decline(actor)"};
	   }
	   public void accept(Living actor) {
		   actor.notice("You accept resurrection from "+resurrector.getName()+".");

		   chooser.getRoom().remove(chooser);
		   resurrector.getRoom().add(chooser);
		   chooser.setExperience((long) (chooser.getExperience()*0.85));
		   chooser.notice("You have been resurrected by "+resurrector.getName()+".");
		   Look l = (Look) Commander.getInstance().get("look");
		   l.look(chooser, true);
		   resurrector.notice("You have resurrected "+chooser.getName()+".");
		   chooser.setHp(Math.min(chooser.getMaxHp(), 50));
	   

		   long prevExp = ((Player) resurrector).getExperience();
		   resurrector.addExperience((long) (chooser.getExperience()*0.075));
		   if(((Player) resurrector).getPreference("showexp").equals("on")) {
				long expDelta = resurrector.getExperience() - prevExp;
				resurrector.notice("Resurrecting "+chooser.getName()+" gains you "+expDelta+" experience.");
		   }   
	   }
	   
	   public void decline(Living actor) {
		   actor.notice("You decline resurrection from "+resurrector.getName()+".");
		   resurrector.notice(chooser.getName()+" declines your offer for resurrection.");
	   }
	   
	   public void usage(Living who) {
		   who.notice(resurrector.getName()+" has offered to resurrect you. To accept, type 'choose accept'. To decline, type 'choose decline'.");
	   }
	   
	   public void timeout(Living who) {
		   who.notice("The resurrection offer from "+resurrector.getName()+" has expired.");
		   resurrector.notice("Your offer to resurrect "+who.getName()+" expires unanswered.");
	   }
   }
   public boolean tryUse(Living who, MObject target) {
	   if (hasLivingTarget(who, target) && target instanceof Player)
	   {
		if (((Player)target).isDead())
			return true;
		who.notice(target.getName()+" is still alive.");
	   } //add accept thingie
   			return false;
   }

   protected SkillType[] skillTypes = new SkillType[] { SkillTypes.REMOTE, SkillTypes.RESURRECT };
   public SkillType[] getTypes() {
	   return skillTypes;
   }

   public void use(SkillUsageContext suc) {
	   final Living who = suc.getActor();
	   MObject target = suc.getTarget();
	   int s = suc.getSkillSuccess();

	   if(s < 0) {
			   who.notice("You chant 'resur-rectum absurdius' but your spell just fizzles.");
		   who.getRoom().notice(who, who.getName()+" chants 'resur-rectum absurdius' but "+who.getPossessive()+" spell just fizzles.");
		   return;
	   }
	   else {
		   who.notice("You chant 'resur-rectum absurdius'.");
		   who.getRoom().notice(who, who.getName()+" chants 'resur-rectum absurdius'."); 
	   }
	   final Player p = (Player)target;
	   
	   p.notice(who.getName()+" offers to resurrect you. To accept, type 'choose accept'. To decline, type 'choose decline'.");
	   who.notice("You offer to resurrect "+p.getName()+".");
	   
	   new ResurrectionChoice(p, (Player) who);
	   /*
	   new RegexChoice(p) {
		   public String[] getDispatchConfiguration() {
			   return new String[] {"choose accept => accept(actor)",
					   				"choose decline => decline(actor)"};
		   }
		   
		   public void accept(Living actor) {
			   actor.notice("You accept resurrection from "+who.getName()+".");

			   p.getRoom().remove(p);
			   who.getRoom().add(p);
			   p.setExperience((long) (p.getExperience()*0.85));
			   p.notice("You have been resurrected by "+who.getName());
			   Look l = (Look) Commander.getInstance().get("look");
			   l.look(p, true);
			   who.notice("You have resurrected "+p.getName()+".");
			   p.setHp(Math.min(p.getMaxHp(), 50));
		   

			   long prevExp = ((Player) who).getExperience();
			   who.addExperience((long) (p.getExperience()*0.075));
			   if(((Player) who).getPreference("showexp").equals("on")) {
					long expDelta = p.getExperience() - prevExp;
					who.notice("Resurrecting "+p.getName()+" gains you "+expDelta+" experience.");
			   }   
		   }
		   
		   public void decline(Living actor) {
			   actor.notice("You decline resurrection from "+who.getName()+".");
			   who.notice(p.getName()+" declines your offer for resurrection.");
		   }
		   
		   public void usage(Living who) {
			   who.notice(who.getName()+" has offered to resurrect you. To accept, type 'choose accept'. To decline, type 'choose decline'.");
		   }
	   };
	   */
   }

   public int getCost() {
	   return 250;
   }
   public int getTickCount() {
	   return 10;
   }
   public String getName() {
	   return "resurrection";
   }

}
