package org.vermin.mudlib;
import org.vermin.util.Print;

/**
 * Predefined skill types for Vermin mudlib.
 */
public class SkillTypes {

	public static final SkillType ARCANE       =	
		new SkillTypeAdapter() {
			public boolean tryUse(SkillUsageContext suc) {
				Skill s = suc.getSkill();
				int cost = s.getCost(suc);
				if(cost > 0) {
					if(suc.getActor().getSp() < s.getCost(suc)) {
						suc.getActor().notice("Your magical powers are spent.");
						return false;
					}
				}
				return true;
			}
			
			public void onTick(SkillUsageContext suc) {
				if(suc.getActor().checkSkill("quick chant") > 0) {
					suc.setTicksLeft(suc.getTicksLeft()-1);
				}
				if(suc.getActor().checkSkill("essence eye") > 0) {
					StringBuffer rounds = new StringBuffer();
					for(int i=0;i<suc.getTicksLeft();i++)
						rounds.append("#");
					suc.getActor().notice(Print.capitalize(suc.getSkill().getName()) + " " + rounds.toString());
				}
			}
	};

	public static final SkillType DIVINE       =	
		new SkillTypeAdapter() {
			public boolean tryUse(SkillUsageContext suc) {
				Skill s = suc.getSkill();
				int cost = s.getCost(suc);
				if(cost > 0) {
					if(suc.getActor().getSp() < s.getCost(suc)) {
						suc.getActor().notice("Your magical powers are spent.");
						return false;
					}
				}
				return true;
			}
			public void onTick(SkillUsageContext suc) {
				if(suc.getActor().checkSkill("quick chant") > 0) {
					suc.setTicksLeft(suc.getTicksLeft()-1);
				}
				if(suc.getActor().checkSkill("essence eye") > 0) {
					StringBuffer rounds = new StringBuffer();
					for(int i=0;i<suc.getTicksLeft();i++)
						rounds.append("#");
					suc.getActor().notice(Print.capitalize(suc.getSkill().getName()) + " " + rounds.toString());
				}
			}
	};

	public static final SkillType TEMPLAROFFENSIVE       =	
		new SkillTypeAdapter() {
			public boolean tryUse(SkillUsageContext suc) {
				Skill s = suc.getSkill();
				int cost = s.getCost(suc);
				if(((Living) suc.getTarget()).getLifeAlignment() >= 0) {
					suc.getActor().notice("You cannot use this skill on a creature of life.");
					return false;
				}
				if(cost > 0) {
					if(suc.getActor().getSp() < s.getCost(suc)) {
						suc.getActor().notice("Your magical powers are spent.");
						return false;
					}
				}
				return true;
			}
			public void onTick(SkillUsageContext suc) {
				if(suc.getActor().checkSkill("quick chant") > 0) {
					suc.setTicksLeft(suc.getTicksLeft()-1);
				}
				if(suc.getActor().checkSkill("essence eye") > 0) {
					StringBuffer rounds = new StringBuffer();
					for(int i=0;i<suc.getTicksLeft();i++)
						rounds.append("#");
					suc.getActor().notice(Print.capitalize(suc.getSkill().getName()) + " " + rounds.toString());
				}
			}
			
			public void onUse(SkillUsageContext suc) {
				int success = suc.getActor().checkSkill("arcane knowledge");
				if(success > 0)
				{
					String message = new String("Your knowledge in arcane studies successfully increases the effectivity of the skill.\n");
					suc.setSkillEffectModifier(100+success, message);
				}
			}
		
	};
		
	
	public static final SkillType SONG       =	
		new SkillTypeAdapter() {
			
			public boolean tryUse(SkillUsageContext suc) {
				Skill s = suc.getSkill();
				int cost = s.getCost(suc);
				if(cost > 0) {
					if(suc.getActor().getSp() < s.getCost(suc)) {
						suc.getActor().notice("Your magical powers are spent.");
						return false;
					}
				}
				return true;
			}
			
			public void onTick(SkillUsageContext suc) {
				if(suc.getActor().checkSkill("tempo control") > 0) {
					suc.setTicksLeft(suc.getTicksLeft()-1);
				}
				if(suc.getActor().checkSkill("audience awareness") > 0) {
					StringBuffer rounds = new StringBuffer();
					for(int i=0;i<suc.getTicksLeft();i++)
						rounds.append("#");
					suc.getActor().notice(Print.capitalize(suc.getSkill().getName()) + " " + rounds.toString());
				}
			}
	};

	//SUB TYPES
	public static final SkillType HEALING       =	
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int diceRoll = Dice.random();
				int success = suc.getActor().checkSkill("healing efficiency");
				if(success > 0)
				{
					String message = new String("Your extremely effective healing techniques allow you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}
	};

	public static final SkillType OFFENSIVE     =	new SkillTypeAdapter();
	public static final SkillType SUMMONING     =	new SkillTypeAdapter();
	public static final SkillType PROTECTIVE    =	new SkillTypeAdapter();
	public static final SkillType DIVINATION    =	new SkillTypeAdapter();
	public static final SkillType TELEPORTATION =	new SkillTypeAdapter();

	
	//TARGET TYPES: LOCAL, AREA, REMOTE, SELF
	//LORE TYPES: PSIONIC, DRUIDIC, NECROMANTIC, DEMONIC, ELEMENTAL, RUNIC
	//DAMAGE TYPES: all damage types except physical subtypes

	/**
	 * A skilltype that checks that the player is dead before
	 * allowing use. Must be used in conjunction with a type
	 * that resolves the target to a player.
	 */
	public static final SkillType RESURRECT =
		new SkillTypeAdapter() {
			public boolean tryUse(SkillUsageContext suc) {
				if(!((Player)suc.getTarget()).isDead()) {
					suc.getActor().notice(suc.getTarget().getName()+" is not dead.");
					return false;
				}
				return true;
			}};

	/**
	 * Find an object from the current room.
	 */
	public static final SkillType LOCAL =
		new SkillTypeAdapter() {
			public MObject findTarget(Living who, String target) {
				return who.getRoom().findByName(target);
			}
			public boolean tryUse(SkillUsageContext suc) {
				if(suc.getTarget() == null) {
					suc.getActor().notice("Use "+suc.getSkill().getName()+" at what?");
					return false;
				}
				return true;
			}
		};

	public static final SkillType AREA =
		new SkillTypeAdapter() {
			public MObject findTarget(Player who, String target) {
				return who.getRoom();
			}};

	public static final SkillType SELF =
		new SkillTypeAdapter() {
			public MObject findTarget(Player who, String target) {
				return who;
			}};

	/**
	 * Use a remote player as a target. The player must be in the game
	 * to be used as a target.
	 */
	public static final SkillType REMOTE =
		new SkillTypeAdapter() {
			public MObject findTarget(Living who, String target) {
				if(!World.isLoggedIn(target)) { return null;	}
					return (MObject) World.get("players/"+target);
			}
			public boolean tryUse(SkillUsageContext suc) {
				if(suc.getTarget() == null) {
					suc.getActor().notice("Use "+suc.getSkill().getName()+" at who?");
					return false;
				}
				return true;
			}};
	
	public static final SkillType MAGICAL =
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int success = suc.getActor().checkSkill("magic lore");
				if(success > 0)
				{
					String message = new String("Your extremely effective spell casting technique allows you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}
		};

	public static final SkillType ACID =
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int success = suc.getActor().checkSkill("acid lore");
				if(success > 0)
				{
					String message = new String("Your mastery of the acid lore allows you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}
			public void onUse(SkillUsageContext suc) {
				int success = suc.getActor().checkSkill("corrosive force");
				if(success > 0)
				{
					String message = new String("You enhance the spell with your expertise in forces of corrosion.\n");
					suc.setSkillEffectModifier(100+success, message);
				}
			}
		};

	public static final SkillType ELECTRIC =
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int success = suc.getActor().checkSkill("electricity lore");
				if(success > 0)
				{
					String message = new String("Your mastery of the electricity lore allows you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}
			public void onUse(SkillUsageContext suc) {
				int success = suc.getActor().checkSkill("electric force");
				if(success > 0)
				{
					String message = new String("Blue streaks of electricity channel through you as the spell fires.\n");
					suc.setSkillEffectModifier(100+success, message);
				}
			}

		};

	public static final SkillType POISON =
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int success = suc.getActor().checkSkill("poison lore");
				if(success > 0)
				{
					String message = new String("Your mastery of the poison lore allows you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}
			public void onUse(SkillUsageContext suc) {
				int success = suc.getActor().checkSkill("enhance poison");
				if(success > 0)
				{
					String message = new String("Your masteries in poison knowledge allow you to make extreme damage.\n");
					suc.setSkillEffectModifier(100+success, message);
				}
			}

		};

	public static final SkillType ASPHYXIATION =
		new SkillTypeAdapter() {
			public void calculateSkillCostModifier(SkillUsageContext suc, int skillCost) { 
				int success = suc.getActor().checkSkill("vacuum lore");
				if(success > 0)
				{
					String message = new String("Your mastery of the vacuum lore allows you to conserve your magical energies.\n");
					suc.setSkillCostModifier(0 - (skillCost / 350 * success), message);
				}
			}

			public void onUse(SkillUsageContext suc) {
				int success = suc.getActor().checkSkill("force vacuum");
				if( success > 0)
				{
					String message = new String("You can see the enemy run out of oxygen as you masterfully create a vacuum around your enemy.\n");
					suc.setSkillEffectModifier(100+success, message);
				}
			}

		};

	public static final SkillType FIRE =	new SkillTypeAdapter();
}
