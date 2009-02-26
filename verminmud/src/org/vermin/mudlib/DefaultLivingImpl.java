/* DefaultLivingImpl.java
 5.1.2002	Tatu Tarvainen / Council 4
 
 Default Living implementation.
 */
package org.vermin.mudlib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.regex.Matcher;

import org.vermin.driver.*;

import static org.vermin.mudlib.Stat.*;
import static org.vermin.mudlib.LivingProperty.*;

import org.vermin.mudlib.Affliction.Type;
import org.vermin.mudlib.minion.Leash;
import org.vermin.mudlib.minion.Minion;
import org.vermin.util.Arrays;
import org.vermin.util.Functional;

public class DefaultLivingImpl extends DefaultObjectImpl implements Living {
    
    /* Current room */
    //protected transient Room currentRoom;
    
    /* Current battle group (might be a party etc.) */
    protected transient BattleGroup rootBattleGroup;
    
    /* Current personal battle group.
     * May contain minions and effects (child battle groups).
     */
    protected transient BattleGroup personalBattleGroup;
    
    /* Current leaf battle group.
     */
    protected transient BattleGroup leafBattleGroup;
    
    /* The current party */
    protected transient Party party;
    
    /* Props */
    protected int hp;
    protected int maxHp;
    protected int sp;
    protected int maxSp;
    protected int size;
    
    /* Current untrained experience and total experience gained */
    protected long exp;
    protected long totalExp;
    
    /* Stats */
    protected int physicalStr;
    protected int mentalStr;
    protected int physicalCon;
    protected int mentalCon;
    protected int physicalDex;
    protected int mentalDex;
    protected int physicalCha;
    protected int mentalCha;
    
    /* The race  */
    protected Race race;
    
    /* inventory */
    protected Vector<MObject> inventory;
    
    /* nouns */
    protected String possessive;
    protected String objective;
    protected String pronoun;
    
    protected int[] resistance;
    
    /* Wielded items */
    protected Wieldable[] wielded;
    
    /* Worn items (same size as Race's getSlots array) */
    protected Wearable[] worn;
    
    /* current BattleStyle */
    protected BattleStyle style;
    
    protected int offensiveness;
    
    /* Modifier lists */
    protected EnumMap<Stat,LinkedList<Modifier>> statModifiers;
    protected LinkedList<Modifier> hpRegenModifiers;
    protected LinkedList<Modifier> spRegenModifiers;
    protected LinkedList<Modifier> maxHpModifiers;
    protected LinkedList<Modifier> maxSpModifiers;
    protected LinkedList<Modifier> offensivenessModifiers;
    protected LinkedList[] resistanceModifiers;
    protected Hashtable<String,LinkedList<Modifier>> skillModifiers;
    protected LinkedList<Modifier> sizeModifiers;
    
    protected LinkedList<DamageListener> damageListeners;
    
    protected int alignmentLife;
    protected int alignmentProgress;
    
    protected int lawfulness;
    
    protected int sustenance;
    
    protected EnumMap<Affliction.Type, Affliction> afflictions;
    protected DefaultCompositePropertyProvider<LivingProperty> propertyProvider;
    protected EnumSet<LivingProperty> livingProperties;
    
    private Money money = new DefaultMoneyImpl();
    
    protected List<Behaviour> behaviours = new LinkedList<Behaviour>();

	/* The gender. This is race specific (most should have 'male' and 'female') */
	protected String gender;
    
    public DefaultLivingImpl() {
  // 	currentRoom = null;
        inventory = new Vector();
        hp = 0;
        maxHp = 0;
        sp = 0;
        maxSp = 0;
        size = 0;
        alignmentLife = 0;
        alignmentProgress = 0;
        lawfulness = 0;
        
        possessive = null;
        objective = null;
        pronoun = null;
        gender = null;
        //attackers = new Vector();
        resistance = new int[Damage.NUM_TYPES];
        wielded = null;
        worn = null;
        
        style = new DefaultBattleStyle(this);
        offensiveness = 50;
        
        statModifiers = null;
        hpRegenModifiers = null;
        spRegenModifiers = null;
        maxHpModifiers = null;
        maxSpModifiers = null;
        offensivenessModifiers = null;
        resistanceModifiers = null;
        
        // ordering of these is important
        rootBattleGroup = new RootBattleGroup(this);
        leafBattleGroup = new LeafBattleGroup(this);
        personalBattleGroup = new BranchBattleGroup();
        rootBattleGroup.addChild(personalBattleGroup);
        personalBattleGroup.addChild(leafBattleGroup);
        
        sustenance = 10000;
    }
    
    public Money getMoney() {
        return money;
    }
    
    public boolean contains(MObject obj) {
        return 
        inventory.contains(obj) ||
        Arrays.contains(wielded, obj) ||
        Arrays.contains(worn, obj);
    }
    
    public void start() {
        super.start();
        
        // ordering of these is important
        rootBattleGroup = new RootBattleGroup(this);
        leafBattleGroup = new LeafBattleGroup(this);
        personalBattleGroup = new BranchBattleGroup();
        rootBattleGroup.addChild(personalBattleGroup);
        personalBattleGroup.addChild(leafBattleGroup);

        if(behaviours == null)
            behaviours = new LinkedList<Behaviour>();
        else
        	for(Behaviour b : behaviours) setBehaviourOwner(b);
        
        Driver.getInstance().getTickService().addTick(this, Tick.REGEN);
        try {
            //attackers = new Vector();
            updateStats();
            
            // start inventory, worn and wielded items
            for(MObject i : inventory)
                i.start();
            
            if(worn != null)
                for(MObject i : worn)
                    if(i != null) i.start();
            
            if(wielded != null)
                for(MObject i : wielded) 
                    if(i != null) i.start();
            
        } catch(Exception e) {
            World.log("DefaultLivingImpl.start(): "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    /* are we currently fighting */
    public boolean inBattle() {
        return getLeafBattleGroup().getOpponent(getRoom()) != null;
    }
    
    public int getResistance(Damage.Type type) {
        type = type.getResistanceType();
        if(resistanceModifiers == null) {
            return resistance[type.ordinal()];
        } else {
            return DefaultModifierImpl.calculateInt(this, resistance[type.ordinal()],
                    resistanceModifiers[type.ordinal()]);
        }
    }
    
    public int getResistance(Damage.Type type, boolean modifiers) {
        if(!modifiers || resistanceModifiers == null)
            return resistance[type.ordinal()];
        else 
            return DefaultModifierImpl.calculateInt(this, resistance[type.ordinal()],
                    resistanceModifiers[type.ordinal()]);
    }
    
    public void setResistance(Damage.Type type, int value) {
        resistance[type.ordinal()] = value;
    }
    
    public Wieldable[] getWieldedItems(boolean naturals) {
        if(wielded == null) {
            wielded = new Wieldable[getRace().getLimbCount()];
        }
        Wieldable[] w = new Wieldable[wielded.length];
        for(int i=0;i<w.length;i++) {
            w[i] = wielded[i] == null 
            ? (naturals ? (Wieldable) getRace().getLimb(i) : null)
            		: wielded[i];
        }
        return w;
    }
    
    public Wearable[] getWornItems() {
        if(worn == null) {
            worn = new Wearable[getRace().getSlots().length];
        }
        return worn;
    }
    
    public Slot[] getAvailableSlots() {
        Slot[] raceSlots = getRace().getSlots();
        if(worn == null) {
            worn = new Wearable[raceSlots.length];
        }
        
        int count=0;
        for(int i=0; i<worn.length; i++) {
            if(worn[i] == null) count++;
        }
        Slot[] free = new Slot[count];
        int j=0;
        for(int i=0; i<worn.length; i++) {
            if(worn[i] == null) free[j++] = raceSlots[i];
        }
        return free;
    }
    
    protected Wearable getWearable(int hitloc) {
        if(hitloc >= 0) {
            Slot slot = getRace().getSlotForLocation(hitloc);
            Slot[] raceSlots = getRace().getSlots();
            if(slot != null) {
                int slotNum = -1;
                for(int i=0;i<raceSlots.length;i++) {
                    if(raceSlots[i] == slot) {
                        slotNum = i;
                        break;
                    }
                }
                if(worn != null && raceSlots.length != worn.length) {
                    
                    // When the player's worn slots array size is not
                    // the same as the race's: the safest thing to do is
                    // to unwear everything and reinitialize the array.
                    
                    HashSet<Wearable> eq = new HashSet<Wearable>();
                    for(Wearable w : worn) {
                        if(w != null)
                            eq.add(w);
                    }
                    for(Wearable w : eq)
                        unwear(w);
                    
                    worn = new Wearable[raceSlots.length];
                    
                    return null;
                }
                
                if(slotNum >= 0 && worn != null) {
                    return worn[slotNum];
                }
            }
        }
        return null;
    }
    
    /* Update dependend stats */
    public void updateStats() {
        
        /* updateStats() might be called before start() ...so check that race is valid */
        if(race == null)
            return;
        
        /* Maximum hit points */
        maxHp = (int) ((getSize() + (10 * physicalCon)) * 2); //tuned by Siggy
        
        /* Maximum spell points */
        // maxSp = (int) (((1.7 * (double) mentalStr) * (0.59 * (double) mentalDex)) / 5.0);
        maxSp = (int) (((this.getRace().getMaxMentalStrength()+this.getRace().getMaxMentalConstitution()*2)+(17 * (double) mentalCon) + (5.9 * (double) mentalStr))); //tuned by Siggy
        
        
    }
    
    public int getStat(Stat stat, boolean modifiers) {
        int base;
        switch(stat) {
        case PHYS_STR: base = physicalStr; break;
        case MENT_STR: base = mentalStr; break;
        case PHYS_DEX: base = physicalDex; break;
        case MENT_DEX: base = mentalDex; break;
        case PHYS_CON: base = physicalCon; break;
        case MENT_CON: base = mentalCon; break;
        case PHYS_CHA: base = physicalCha; break;
        case MENT_CHA: base = mentalCha; break;
        default: throw new RuntimeException("This can't happen. Stat enum switch fell through.");
        }
        if(!modifiers || statModifiers == null)
            return base;
        else 
            return DefaultModifierImpl.calculateInt(this, base, statModifiers.get(stat));
    }
    
    public int getStat(Stat stat) {
        return getStat(stat, true);
    }
    
    /* Accessors for stats */
    public int getPhysicalStrength() { 
        if(statModifiers == null)
            return physicalStr;
        
        return DefaultModifierImpl.calculateInt(this, physicalStr,
                statModifiers.get(PHYS_STR)); 
    }
    public int getMentalStrength() {
        if(statModifiers == null)
            return mentalStr;
        
        return DefaultModifierImpl.calculateInt(this, mentalStr,
                statModifiers.get(MENT_STR)); 
    }
    public int getPhysicalConstitution() {
        if(statModifiers == null)
            return physicalCon;
        
        return DefaultModifierImpl.calculateInt(this, physicalCon,
                statModifiers.get(PHYS_CON)); 
    }
    public int getMentalConstitution() {
        if(statModifiers == null)
            return mentalCon;
        
        return DefaultModifierImpl.calculateInt(this, mentalCon,
                statModifiers.get(MENT_CON)); 
    }
    public int getPhysicalDexterity() {
        if(statModifiers == null)
            return physicalDex;
        
        return DefaultModifierImpl.calculateInt(this, physicalDex,
                statModifiers.get(PHYS_DEX)); 
    }
    public int getMentalDexterity() {
        if(statModifiers == null)
            return mentalDex;
        
        return DefaultModifierImpl.calculateInt(this, mentalDex,
                statModifiers.get(MENT_DEX)); 
    }
    public int getPhysicalCharisma() {
        if(statModifiers == null)
            return physicalCha;
        
        return DefaultModifierImpl.calculateInt(this, physicalCha,
                statModifiers.get(PHYS_CHA)); 
    }
    public int getMentalCharisma() {
        if(statModifiers == null)
            return mentalCha;
        
        return DefaultModifierImpl.calculateInt(this, mentalCha,
                statModifiers.get(MENT_CHA)); 
    }
    
    public int getPhysicalStrength(boolean p) {
        return p ? getPhysicalStrength() : physicalStr;
    }
    public int getMentalStrength(boolean p) {
        return p ? getMentalStrength() : mentalStr;
    }
    public int getPhysicalConstitution(boolean p) {
        return p ? getPhysicalConstitution() : physicalCon;
    }
    public int getMentalConstitution(boolean p) {
        return p ? getMentalConstitution() : mentalCon;
    }
    public int getPhysicalDexterity(boolean p) {
        return p ? getPhysicalDexterity() : physicalDex;
    }
    public int getMentalDexterity(boolean p) {
        return p ? getMentalDexterity() : mentalDex;
    }
    public int getPhysicalCharisma(boolean p) {
        return p ? getPhysicalCharisma() : physicalCha;
    }
    public int getMentalCharisma(boolean p) {
        return p ? getMentalCharisma() : mentalCha;
    }
    
    public void setPhysicalStrength(int p) 	    { physicalStr = p; }
    public void setMentalStrength(int p) 		{ mentalStr = p; updateStats();  }
    public void setMentalConstitution(int p)    { mentalCon = p;   }
    public void setPhysicalDexterity(int p) 	{ physicalDex = p; }
    public void setMentalDexterity(int p) 	    { mentalDex = p; updateStats();   }
    public void setPhysicalCharisma(int p) 	    { physicalCha = p; }
    public void setMentalCharisma(int p) 		{ mentalCha = p;   }
    
    public void setStat(Stat s, int p) {
    	switch(s) {
    		case MENT_CHA : setMentalCharisma(p); break;
    		case MENT_CON : setMentalConstitution(p); break;
    		case MENT_DEX : setMentalDexterity(p); break;
    		case MENT_STR : setMentalStrength(p); break;
    		case PHYS_CHA : setPhysicalCharisma(p); break;
    		case PHYS_CON : setPhysicalConstitution(p); break;
    		case PHYS_DEX : setPhysicalDexterity(p); break;
    		case PHYS_STR : setPhysicalStrength(p); break;
    	}
    }
    
    /* Accessors for props */	
    public int getHp() { 
        return hp; 	 
    }
    public int getMaxHp() { 
        return maxHp; 
    }
    public int getSp() {
        return sp; 	 
    }
    public int getMaxSp() {
        return maxSp; 
    }
    
    public void addHp(int amount) { 
        if(amount > 0)
            hp = (hp + amount > maxHp) ? maxHp : hp + amount; 
    }
    
    public void addSp(int amount) {
        if(amount > 0)
            sp = (sp + amount > maxSp) ? maxSp : sp + amount;
    }
    
    private boolean doSubHp(Damage dam, int hitloc) {
        int amount = dam.damage;
        int armourclass = 0;
        Wearable armour = getWearable(hitloc);
        
        if(amount < 0)
            return false;
        
        if(armour != null) {
            armourclass = Math.min(armour.getArmourValue(dam.type), 100);
        }
        
        if(hp >= 0) {
            
            int dice = Dice.random(100);
            
            if(dice < armourclass) {
                amount = amount * (100 - (armourclass - dice)) / 100;
            }
            
            if(armour != null)
                armour.subDp(amount/armour.getDp());
            
            amount = amount * (100 - getResistance(dam.type)) / 100;
            
            hp -= amount;
            
            if(hp < 0) {
                return true;
            }
        }
        return false;
    }
    
    private void die(Living killer) {
        // killer may be null
    	getRoom().dies(this, killer);
        
    	//getRoom().accept(new DeathVisitor(this, killer));
    }
    
    
    public boolean subHp(Damage amount) {
        
        amount = callDamageListeners(amount, null, -1);
        
        if(isDead()) return false;
        boolean death = doSubHp(amount, -1);
        if(death) die(null);
        return death;
    }
    
    public boolean subHp(Damage amount, int hitloc) {
        
        amount = callDamageListeners(amount, null, hitloc);
        
        if(isDead()) return false;
        boolean death = doSubHp(amount, hitloc);
        if(death) die(null);
        return death;
    }
    
    private Damage callDamageListeners(Damage amount, Living attacker, int loc) {

        if(damageListeners != null) {
            Iterator<DamageListener> it = damageListeners.listIterator();
            while(it.hasNext()) {
                DamageListener l = it.next();
                if(l.isActive()) {
                    Damage newD = l.onSubHp(amount, attacker, loc);
                    if(newD != null)
                        amount = newD;
                } else
                    it.remove();
            }
        }
        return amount;
    }
    
    public boolean subHp(Damage amount, Living attacker) {
        
        amount = callDamageListeners(amount, attacker, -1);
        
        if(attacker == null)
            return subHp(amount);
            
        if(isDead()) return false;
        boolean death = doSubHp(amount, -1);
        if(death) die(attacker);
        getBattleGroup().addHostileGroup(attacker.getBattleGroup());
        return death;
    }
    
    public boolean subHp(Damage amount, int hitloc, Living attacker) {
        amount = callDamageListeners(amount, attacker, hitloc);
        
        if(isDead()) return false;
        boolean death = doSubHp(amount, hitloc);
        if(death) die(attacker);
        getBattleGroup().addHostileGroup(attacker.getBattleGroup());
        return death;
    }
    
    public void subSp(int amount) {
        sp -= amount;
    }
    
    /* Tick method should call this every heartbeat */
    public void regen() {
        if(provides(LivingProperty.NO_REGENERATION)) {
            return;
        }
        if(!inBattle()) { // no regeneration during battle
        	doRegen();
        } 
    }
    
    protected void doRegen() {
        //World.log("REGENERATION FOR "+getName());
        addHp(DefaultModifierImpl.calculateInt(this, (getPhysicalConstitution() * 2 + getRace().getBaseHpRegen()) / 3, 
        		/*(getPhysicalConstitution()*getRace().getBaseHpRegen()) /100 + (getRace().getBaseHpRegen() / 2)*/
                hpRegenModifiers));
        addSp(DefaultModifierImpl.calculateInt(this, (getMentalConstitution()*getRace().getBaseSpRegen()) / 100 + (getRace().getBaseSpRegen() / 2), 
                spRegenModifiers));
    }
    
    public void updateAfflictions(Queue queue) {
        if(afflictions == null || afflictions.size() == 0) {
            return;
        }
        
        if(queue == Tick.REGEN) // heartbeat
            for(Affliction a : afflictions.values())
                a.onRegen();
        
        else if(queue == Tick.BATTLE) // battle
            for(Affliction a : afflictions.values())
                a.onBattle();
    }
    
    public void setHp(int p) 	 { hp = p; 	  }
    public void setMaxHp(int p) { maxHp = p; }
    public void setSp(int p) 	 { sp = p; 	  }
    public void setMaxSp(int p) { maxSp = p; }
    
    public long getExperience()		{ return exp; 		 }
    public long getTotalExperience() { return totalExp; }
    
    /**
     * Set the amount of unused experience.
     *
     * Do NOT use this method to add experience or use experience.
     * This method does not take exprate into account and does
     * not affect total experience.
     *
     * You are propably better off using <code>addExperience(long)</code>.
     *
     * @param p the new experience amount
     */
    public void setExperience(long p) 		{ exp = p; 		 }
    
    public void setTotalExperience(long p) { totalExp = p; }
    
    public Race getRace() { return race;	}
    public void setPhysicalConstitution(int p)  { physicalCon = p; updateStats(); }

	public void setRace(Race p) { race = p; }
       
    public long getExperienceWorth()	{ return 0; }
    
    public boolean isDead() { return hp < 0; }
    
    /* Override these in implementations */
    public int getSkill(String name) { return 0; }
    public int getSkill(String name, boolean modifiers) {
        return 0;
    }
    
    public String getPossessive() {
    	if(possessive == null) {
			String g = getGender();
	
			if(g.equalsIgnoreCase("male"))
				return "his";
			else if(g.equalsIgnoreCase("female"))
				return "her";
			else
				return "its";
    	}
    	else {
    		return possessive;
    	}
    }
    
    public String getObjective() {
    	if(objective == null) {
			String g = getGender();
	
			if(g.equalsIgnoreCase("male"))
				return "him";
			else if(g.equalsIgnoreCase("female"))
				return "her";
			else
				return "it";
    	}
    	else {
    		return objective;
    	}
    }
    
    public String getPronoun() {
    	if(pronoun == null) {
			String g = getGender();
	
			if(g.equalsIgnoreCase("male"))
				return "he";
			else if(g.equalsIgnoreCase("female"))
				return "she";
			else
				return "it";
    	}
    	else {
    		return pronoun;
    	}
    }
    
    public synchronized void addAttacker(Living who) {
        getBattleGroup().addHostileGroup(who.getBattleGroup());
    }
    
    public void doBattle() {		
        // Driver.getInstance().getTickService().addTick(this, Tick.BATTLE);
    }
    
    /**
     * Add unused experience.
     * This does not increase the total experience.
     * The added experience is scaled by the race's experience rate.
     *
     * @param amount the amount of experience to add
     */
    public void addExperience(long amount) {
        long adder = ((long) getRace().getExpRate())*amount / 100;
        exp += adder;
    }
    
    /**
     * Use some unused experience.
     * The amount of experience is substracted from the unused experience
     * and added to the amount of total experience.
     *
     * @param amount the amount of experience to use
     */
    public void useExperience(long amount) {
        exp -= amount;
        totalExp += amount;
        if(this instanceof Player) {
        	World.updatePlaque((Player) this);
        }
    }
    
    public void dumpCorpse() {}
    
    public void setPossessive(String p) { possessive = p; }
    public void setObjective(String p) { objective = p; }
    public void setPronoun(String p) { pronoun = p; }
    
    
    
 //   public void setRoom(Room r) { currentRoom = r; }
    public Room getRoom() { 
    	//return currentRoom;
    	return (Room) getParent();
    }
    
    /** 
     * Move through an exit.
     * 
     * @return true if successfull, false otherwise
     */
    public boolean move(Exit exit) {		
        
		if(getRoom().requiresAviation() && !canFly()) {
            notice("You are falling.");
            return false;
        }
        
        if(provides(LivingProperty.IMMOBILIZED)) {
            notice("You can't move!");
            return false;
        }
        
        
        if(exit.tryMove(this, getRoom().getId())) {
            String msg = exit.getPassMessage(getRoom().getId());
            if(msg != null)
                notice(msg);
            
            Room oldRoom = getRoom();
            getRoom().leave(this, exit);
            setParent((Room) World.get( exit.getTarget(getRoom().getId()) ));
            
            if(getRoom() == null) {
            	setParent(oldRoom);
            	notice("Room loading failed, returning to previous room.");
            	notice("Please report this as a bug: "+exit.getTarget(getRoom().getId()));
            	notice(World.getBugTrackerURL());
            }
            
            getRoom().enter(this, exit);
			
            modifySustenance(-1);
            waterDamage(true);
            
            moveFollowers(exit, oldRoom);
            return true;
        }
        
        return false;
    }
    

	protected void moveFollowers(Exit to, Room oldRoom) {
		// move following minions
		
		Leash leash = (Leash) this.findByNameAndType("_minion_leash", Types.ITEM);
		if(leash != null) {
			leash.followMaster(to, oldRoom);
		}		
		
	    // move party followers 
		
	    Party p = getParty();
	    if(p != null && p.getLeader() == this) {
	        for(Living l : p.members()) {
	            if(l.getRoom() == oldRoom && p.isFollowing(l)) {
	                l.notice("You follow the party leader.");
	                l.move(to);            
	            }
	        }
	    }
	}    
    
    /* Possibly take damage from water.
     * Returns true if damage was taken, false otherwise
     */
    protected boolean waterDamage(boolean move) {
        
        int wl = getRoom().getWaterLevel();
        if(wl < 1)
            return false;
        
        // If no neeed to breathe or we can breathe water, no damage from water
        if(!hasToBreathe() || canBreatheWater())
            return false;
        
        // if waterlevel lesser than size or we can fly (or hover), no damage from water
        if(wl < 100 && (wl < getSize() || canFly() || provides(LivingProperty.HOVER) || checkSkill("swimming") > 0))
            return false;
        
        // take damage
        
        notice("Your lungs &B3;BURN&; for air.");
        Damage d = new Damage();
        d.damage = getMaxHp() * (move ? 5 : 10) / 100;
        d.type = Damage.Type.ASPHYXIATION;
        subHp(d);
        return true;
    }
    
    /* Action methods */
    
    public boolean wield(Wieldable what) {
        if(wielded == null)
            wielded = new Wieldable[getRace().getLimbCount()];
        
        if(what.tryWield(this)) {
			
			int requiredSlots = 1;
			
			if(getPhysicalStrength()*2.5f < what.getWeight() / 1024) {
				this.notice("That is too heavy for you to wield.");
				return false;
			}			
			
			//if strength is lower than wieldable weight in kilograms -> will take 2 slots
			if(getPhysicalStrength() < what.getWeight() / 1024)
				requiredSlots = 2;
	        int[] found = new int[requiredSlots]; // indices of found slots
	        for(int i=0; i<found.length; i++) {
	            found[i] = -1;
	        }
	        
	        int foundCount = 0;			
			
            for(int i=0; i<wielded.length; i++) {
                if(wielded[i] == null) {
                    found[foundCount] = i;
					foundCount++;
					if(foundCount == requiredSlots)
						break;
                }
            }
            
            if(foundCount == requiredSlots) {
                inventory.remove(what);
				for(int i=0;i<found.length;i++) {
					wielded[found[i]] = what;
				}
                what.onWield(this);
            } else if(requiredSlots == 1) {
                notice("You don't have any free limbs.");
                return false;
            } else {
				notice("You don't have enough free limbs.");
				return false;
            }
        }
        
        return true;
    }
    
    public boolean unwield(Wieldable what) {
        if(wielded == null)
            wielded = new Wieldable[getRace().getLimbCount()];
        
        boolean unwielded = false;
        if(what.tryUnwield(this)) {
            for(int l=0; l<wielded.length; l++) {
                if(wielded[l] == what) {
                    wielded[l] = null;
                    unwielded=true;
                }
            }
            
            if(unwielded) {
                what.onUnwield(this);
                inventory.add(what);
                return true;
            } 
        }
        return false;
    }
    
    protected void wieldAll() {
        ArrayList<Wieldable> al = new ArrayList<Wieldable>();
        Iterator<MObject> it = findByType(Types.ITEM);
        while(it.hasNext()) {
            Object wieldable = it.next();
            if(wieldable instanceof Wieldable)
                al.add((Wieldable)wieldable);
        }
        
        for(Wieldable w : al)
            if(!wield(w))
                return;
        
    }
    
    protected void wearAll() {
        if(worn == null)
            worn = new Wearable[getRace().getSlots().length];
        
        Vector<Wearable> successfulWear = new Vector<Wearable>();
        Enumeration<MObject> en = inventory.elements();
        
        while(en.hasMoreElements()) {
            Item it = (Item) en.nextElement();
            
            if(!(it instanceof Wearable))
                continue;
            
            Wearable what = (Wearable) it;
            
            if(!what.tryWear(this))
                continue;
            
            // satisfy slot requirements
            Slot[] required = what.getSlots();
            if(required.length > 0) {
                int[] found = new int[required.length]; // indices of found slots
                for(int i=0; i<found.length; i++) {
                    found[i] = -1;
                }
                
                int foundCount = 0;
                
                Slot[] raceSlots = getRace().getSlots();
                for(int i=0; i<required.length; i++) {
                    for(int j=0; j<raceSlots.length; j++) {
                        if(raceSlots[j].type.equals(required[i].type) && worn[j] == null) {
                            // mark required slot as used
                            worn[j] = what;
                            found[i] = j;
                            foundCount++;
                            break;
                        }
                    }
                }
                
                if(foundCount != required.length) {
                    // if we didn't find all required slots, clear already reserved slots
                    for(int i=0; i<found.length; i++) {
                        if(found[i] != -1) {
                            worn[found[i]] = null;
                        }
                    }
                    //notice("You don't have the required free slots.");
                    continue;
                }
                successfulWear.add(what);
                what.onWear(this);
                notice("You wear "+what.getDescription()+".");
            }
        }
        
        for(Wearable what : successfulWear)
            inventory.remove(what);
    }
    
    public boolean wear(Wearable what) {
        if(worn == null)
            worn = new Wearable[getRace().getSlots().length];
        
        if(!what.tryWear(this))
        {
            notice("You cannot wear "+what.getDescription()+".");
            return false;
        }
        
        // satisfy slot requirements
        Slot[] required = what.getSlots();
        int[] found = new int[required.length]; // indices of found slots
        for(int i=0; i<found.length; i++) {
            found[i] = -1;
        }
        
        int foundCount = 0;
        
        Slot[] raceSlots = getRace().getSlots();
        for(int i=0; i<required.length; i++) {
            for(int j=0; j<raceSlots.length; j++) {
                if(raceSlots[j].type.equals(required[i].type) && worn[j] == null) {
                    // mark required slot as used
                    worn[j] = what;
                    found[i] = j;
                    foundCount++;
                    break;
                }
            }
        }
        
        if(foundCount != required.length) {
            // if we didn't find all required slots, clear already reserved slots
            for(int i=0; i<found.length; i++) {
                if(found[i] != -1) {
                    worn[found[i]] = null;
                }
            }
            notice("You don't have the required free slots.");
            return false;
        }
        
        notice("You wear "+what.getDescription()+".");
        what.onWear(this);
        inventory.remove(what);
        return true;
    }
    
    public boolean unwear(Wearable what) {
        if(worn == null)
            worn = new Wearable[getRace().getSlots().length];
        
        boolean unworn = false;
        if(what.tryUnwear(this)) {
            for(int l=0; l<worn.length; l++) {
                if(worn[l] == what) {
                    worn[l] = null;
                    unworn=true;
                }
            }
            
            if(unworn) {
                what.onUnwear(this);
                //System.out.println("UNWEARING: "+what);
                inventory.add(what);
                return true;
            }
        }
        return false;
    }
    
    /* a string notify of something */
    public void notice(String what) {}
    //public void printf(String fmt, Object ... args) {}
    
    
    public void arrives(Living who, Exit from) {
        for(Behaviour b : behaviours) b.arrives(who, from);
        if(getBattleGroup().isOpponent(who)) {
            // joko hemmo on huoneessa t�ss� vaiheessa?
            doBattle();
        }      
    }
    
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#arrives(org.vermin.mudlib.Living)
     */
    public void arrives(Living who) {
        for(Behaviour b : behaviours) b.arrives(who);
    }
    
    public void afterArrives(Living who) {
    	for(Behaviour b : behaviours) b.afterArrives(who);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#drops(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void drops(Living who, Item what) {
        for(Behaviour b : behaviours) b.drops(who, what);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#leaves(org.vermin.mudlib.Living, org.vermin.mudlib.Exit)
     */
    public void leaves(Living who, Exit to) {
        for(Behaviour b : behaviours) b.leaves(who, to);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#leaves(org.vermin.mudlib.Living)
     */
    public void leaves(Living who) {
        for(Behaviour b : behaviours) b.leaves(who);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#says(org.vermin.mudlib.Living, java.lang.String)
     */
    public void says(Living who, String what) {
        for(Behaviour b : behaviours) b.says(who, what);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#startsUsing(org.vermin.mudlib.Living, org.vermin.mudlib.Skill)
     */
    public void startsUsing(Living who, Skill skill) {
        for(Behaviour b : behaviours) b.startsUsing(who, skill);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#takes(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void takes(Living who, Item what) {
        for(Behaviour b : behaviours) b.takes(who, what);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#unwields(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void unwields(Living who, Item what) {
        for(Behaviour b : behaviours) b.unwields(who, what);
    }
    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#wields(org.vermin.mudlib.Living, org.vermin.mudlib.Item)
     */
    public void wields(Living who, Item what) {
        for(Behaviour b : behaviours) b.wields(who, what);
    }
        
    public void dies(Living victim, Living killer) {
		for(Behaviour b : behaviours) b.dies(victim, killer);
		
		if(this == victim) {

			Area a = getRoom().getArea();
			if(a != null) {
				a.removed(victim);
			}

			victim.getBattleGroup().onDeath(victim);
			victim.dumpCorpse();

			if(killer != null) {
				
				if(killer instanceof Minion) {
					Minion minime = (Minion) killer;
					killer = minime.getMaster();
				}
				
				Party pi = killer.getParty();
				
				if(pi != null) {
					// Party kill
					pi.addKill(victim);
					pi.addExperience(victim.getExperienceWorth(), killer.getRoom());
					for(Living l : pi.members()) {
						if(l instanceof DefaultPlayerImpl) {
							DefaultPlayerImpl p = (DefaultPlayerImpl) l;
							p.addSummaryKill();
							if(p.getBestPartyKillExperience() < victim.getExperienceWorth())
								p.setBestPartyKill(victim);
						}
					}
					
				} else {
					// Solo kill
					if(killer instanceof DefaultPlayerImpl) {
						((DefaultPlayerImpl) killer).addSummaryKill();
					}
					
					if(killer instanceof Player) {
						long prevExp = ((Player) killer).getExperience();
						killer.addExperience(victim.getExperienceWorth());
						Player p = (Player) killer;
						long expDelta = p.getExperience() - prevExp;
						if(p.getPreference("showexp").equals("on")) {
							p.notice("The kill gains you "+expDelta+" experience.");
						}
						
						if(p.getBestSoloKillExperience() < victim.getExperienceWorth())
							p.setBestSoloKill(victim);
					}
				}
			}
		} else if(victim.getBattleGroup().getCombatant(victim.getRoom()) == null) {
			// If no more combatants in the battle group of the dying creature,
			// remove the battle group from hostiles
			victim.getBattleGroup().removeHostileGroup(victim.getBattleGroup());
		}
    }
    
	public void asks(Living asker, Living target, String subject) {
        for(Behaviour b : behaviours) b.asks(asker, target, subject);
	}
    
	public void command(Object ... args) {
		for(Behaviour b : behaviours) b.command(args);
	}
	
    public void add(MObject obj) {
        obj.setParent(this);
        if(obj instanceof Item) {
            inventory.add(obj);
            ((Item) obj).take(this);
        }
    }
    
    public void remove(MObject obj) {
        if(obj instanceof Item) {
            
            if(obj instanceof Wieldable)
                this.unwield((Wieldable) obj);
            
            if(obj instanceof Wearable)
                this.unwear((Wearable) obj);	
            
            inventory.remove(obj);
            ((Item) obj).drop(this);
        }
    }
    
    public void clearInventory() {
        inventory.clear();
    }
    
    public void putItemsToInventory() {
        if(wielded != null) {
            for(int i=0; i<wielded.length; i++) {
                if(wielded[i] != null) {
                    inventory.add(wielded[i]);
                    wielded[i] = null;
                }
            }
        }
        if(worn != null) {
            for(int i=0; i<worn.length; i++) {
                if(worn[i] != null) {
                    inventory.add(worn[i]);
                    worn[i] = null;
                }
            }
        }
    }
    
    public Iterator<MObject> findByType(Types type) {
        
        if(type != Types.ITEM)
            return null;
        
        HashSet<MObject> items = new HashSet<MObject>();
        
        items.addAll(inventory);
        items.addAll(java.util.Arrays.asList(getWieldedItems(false)));
        items.addAll(java.util.Arrays.asList(getWornItems()));
        
        items.remove((Item) null);
        
        return items.iterator();
    }
    
    public MObject findByName(String name) {
        return findByNameAndType(name, Types.ITEM);
    }
    
    public MObject findByName(String name, int index) {
        return findByNameAndType(name, index, Types.ITEM);
    }	
    
    public MObject findByNameAndType(String name, Types type) {
        
        Matcher m = Container.INDEXED_CONTAINER_ACCESS.matcher(name);
        
        if(m.matches()) {
            findByNameAndType(m.group(1), Integer.parseInt(m.group(2)), type);
        }
        
        if(type == Types.ITEM) {
            for(int i=0; i<inventory.size(); i++) {
                if(((MObject) inventory.get(i)).isAlias(name))
                    return (MObject) inventory.get(i);
            }
            for(Wieldable weapon : getWieldedItems(false)) {
                if(weapon != null && weapon.isAlias(name)) {
                    return weapon;
                }
            }
            for(Wearable armour : getWornItems()) {
                if(armour != null && armour.isAlias(name)) {
                    return armour;
                }
            }
        }
        else
            return super.findByNameAndType(name, type);
        
        return null;
    }
    
    /* Find the index:th object contained */
    public MObject findByNameAndType(String name, int index, Types type) {
        int originalIndex = index;
        if(type == Types.ITEM) {
            for(int i=0; i<inventory.size(); i++) {
                if(((MObject) inventory.get(i)).isAlias(name)) {
                    if(index == 1) {
                        return (MObject) inventory.get(i);
                    }
                    index--;
                }
            }
            for(Wieldable weapon : getWieldedItems(false)) {
                if(weapon != null && weapon.isAlias(name)) {
                    if(index == 1) {					
                        return weapon;
                    }
                    index--;
                }
            }
            for(Wearable armour : getWornItems()) {
                if(armour != null && armour.isAlias(name)) {
                    if(index == 1) {
                        return armour;
                    }
                    index--;
                }
            }
        }
        else
            return super.findByNameAndType(name, originalIndex, type);
        
        return null;
    }	
    
    public Iterator<MObject> children() {
        return inventory.iterator();
    }
    
    public void setBattleStyle(BattleStyle style) {
        style.setOwner(this);
        this.style = style;
    }
    
    public BattleStyle getBattleStyle() {
        return this.style;
    }
    
    public void setOffensiveness(int amount) {
        this.offensiveness = amount;
    }
    
    public int getOffensiveness() {
        return this.offensiveness;
    }
    
    public void battleReact() {}
    
    
    public int getSize() {
        if(race == null) World.log("RACE is NULL");
        return DefaultModifierImpl.calculateInt(this, getRace().getSize() + size,
                (LinkedList<Modifier>) sizeModifiers);
    }
    
    public Types getType() { return Types.LIVING; }
    
    public BattleGroup getBattleGroup() {
        return rootBattleGroup;
    }
    
    public BattleGroup getLeafBattleGroup() {
        return leafBattleGroup;
    }
    
    public BattleGroup getPersonalBattleGroup() {
        return personalBattleGroup;
    }
    
    public void setParent(MObject obj) {
    	if(obj instanceof Container)
    	parent = (Container) obj;
        /*if(obj instanceof Room)
            currentRoom = (Room) obj;*/
    }
    
    public int getLawfulness() { return lawfulness; }
    
    public void modifySustenance(int amount) {
        sustenance += amount;
        if(sustenance < 0) sustenance = 0;
    }
    
    public void setSustenance(int sustenance) {
        this.sustenance = sustenance;
        if(this.sustenance < 0) {
            this.sustenance = 0;
        }
    }
    
    public int getSustenance() {
        return sustenance;
    }
    
    public String getShape() {
        
        int max = getMaxHp();
        max = max==0 ? 1 : max;
        
        int p = 100 * getHp() / max;
        if(p < 10) return "almost dead";
        if(p < 20) return "in very bad shape";
        if(p < 30) return "in bad shape";
        if(p < 50) return "not in a good shape";
        if(p < 70) return "moderately hurt";
        if(p < 80) return "slighty hurt";
        if(p < 90) return "in good shape";
        else return "in excellent shape";
    }
    
    public void setParty(Party party) {
        this.party = party;
    }
    
    public Party getParty() {
        return party;
    }
    
    public boolean isBlocking(String dir) {
        return false;
    }
    
    public boolean hasToBreathe() {
        return !provides(DOES_NOT_BREATHE);
    }
    
    public boolean canFly() {
        return provides(FLIGHT);
    }
    
    public boolean canBreatheWater() {
        return provides(BREATHES_WATER);
    }
    
    
    public void addModifier(Modifier m) {
        
        switch(m.getType()) {
        
        case OFFENSIVENESS:
            if(offensivenessModifiers == null) {
                offensivenessModifiers = new LinkedList<Modifier>();
            }
            offensivenessModifiers.add(m);
            break;
            
            
        case STAT:
            Stat s = (Stat) m.getArguments()[0];
            if(statModifiers == null) {
                statModifiers = new EnumMap<Stat, LinkedList<Modifier>>(Stat.class);
            }
            LinkedList<Modifier> stat = statModifiers.get(s);
            if(stat == null) {
                stat = new LinkedList<Modifier>();
                stat.add(m);
                statModifiers.put(s, stat);
            } else {
                stat.add(m);
            }
            break;
            
        case SKILL:
            String sk = (String) m.getArguments()[0];
            if(skillModifiers == null) {
                skillModifiers = new Hashtable<String, LinkedList<Modifier>>();
            }
            LinkedList<Modifier> skill = skillModifiers.get(sk);
            if(skill == null) {
                skill = new LinkedList<Modifier>();
                skill.add(m);
                skillModifiers.put(sk, skill);
            } else {
                skill.add(m);
            }
            break;
            
        case RESISTANCE:
            int d = (Integer) m.getArguments()[0];
            if(resistanceModifiers == null) {
                resistanceModifiers = new LinkedList[Damage.NUM_TYPES];
            }
            
            if(resistanceModifiers[d] == null) {
                resistanceModifiers[d] = new LinkedList();
            }
            resistanceModifiers[d].add(m);
            break;
            
        case MAXHP:
            if(maxHpModifiers == null) {
                maxHpModifiers = new LinkedList<Modifier>();
            }
            maxHpModifiers.add(m);
            break;
            
        case MAXSP:
            if(maxSpModifiers == null) {
                maxSpModifiers = new LinkedList<Modifier>();
            }
            maxSpModifiers.add(m);
            break;
            
        case HPREGEN:
            if(hpRegenModifiers == null) {
                hpRegenModifiers = new LinkedList<Modifier>();
            }
            hpRegenModifiers.add(m);
            break;
            
        case SPREGEN:
            if(spRegenModifiers == null) {
                spRegenModifiers = new LinkedList<Modifier>();
            }
            spRegenModifiers.add(m);
            break;
            
        case REGEN:
            if(hpRegenModifiers == null) {
                hpRegenModifiers = new LinkedList<Modifier>();
            }
            hpRegenModifiers.add(m);
            if(spRegenModifiers == null) {
                spRegenModifiers = new LinkedList<Modifier>();
            }
            spRegenModifiers.add(m);
            break;
            
        case SIZE:
            if(sizeModifiers == null) {
                sizeModifiers = new LinkedList<Modifier>();
            }
            sizeModifiers.add(m);
            break;
            
        default: super.addModifier(m);
        }
    }
    
    public void addAffliction(Affliction a) {
        
        if(afflictions == null) {
            afflictions = new EnumMap<Type, Affliction>(Affliction.Type.class);
        }
        
        Affliction.Type type = a.getType();
        Affliction old = afflictions.get(type);
        if(old != null) {
            // don't override a stronger affliction
            if(old.getAmount() > a.getAmount()) {
                return;
            }
            old.end();
        }
        
        afflictions.put(type, a);
        a.start(this);
    }
    
    public void removeAffliction(Affliction a) {
        afflictions.remove(a.getType());
    }
    
    public boolean provides(LivingProperty p) {
        
        EnumSet rp = getRace().getRaceProperties();
        if(rp.contains(p))
            return true;
        
        // check other sources, like afflictions
        if(afflictions != null) {
            for(Affliction a : afflictions.values()) {
                if(a.provides(p)) {
                    return true;
                }
            }
        }
        
        // check property providers
        if(propertyProvider != null && propertyProvider.provides(p)) {
            return true;
        }
        
        if(livingProperties != null) {
        	if(livingProperties.contains(p)) { return true; }
        }
        
        return false;
    }
    
    public boolean provides(LivingProperty first, LivingProperty... rest) {
        
        if(!provides(first))
            return false;
        
        for(LivingProperty p : rest)
            if(!provides(p))
                return false;
        
        return true;
    }
    
    public boolean providesAny(LivingProperty first, LivingProperty ... rest) {
        if(provides(first))
            return true;
        
        for(LivingProperty p : rest)
            if(provides(p))
                return true;
        
        return false;
    }
    
    public void addProvider(PropertyProvider<LivingProperty> provider) {
        if(propertyProvider == null)
            propertyProvider = new DefaultCompositePropertyProvider<LivingProperty>();
        propertyProvider.addProvider(provider);
    }
    
    public void removeProvider(PropertyProvider<LivingProperty> provider) {
        if(propertyProvider != null)
            propertyProvider.removeProvider(provider);
    }
    
    public int getLifeAlignment() {
        return alignmentLife;
    }
    public int getProgressAlignment() {
        return alignmentProgress;
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Living#addDamageListener(org.vermin.mudlib.DamageListener)
     */
    public void addDamageListener(DamageListener listener) {
        if(damageListeners == null)
            damageListeners = new LinkedList<DamageListener>();
        damageListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#onBattleTick(org.vermin.mudlib.Living)
     */
    public void onBattleTick(Living who) {
        for(Behaviour b : behaviours)
            b.onBattleTick(who);
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#onRegenTick(org.vermin.mudlib.Living)
     */
    public void onRegenTick(Living who) {
        for(Behaviour b : behaviours)
            b.onRegenTick(who);
    }
    
    public void addBehaviour(Behaviour b) {
    	setBehaviourOwner(b);
        behaviours.add(b);
    }
    
    private void setBehaviourOwner(Behaviour b) {
		if(b instanceof OwnBehaviour)
			((OwnBehaviour)b).setOwner(this);
	}

	public void removeBehaviour(Behaviour b) {
        behaviours.remove(b);
    }
	public void removeBehaviour(Functional.Predicate<Behaviour> p) {
		Iterator<Behaviour> behaviours = this.behaviours.iterator();
		while(behaviours.hasNext()) {
			if(p.call(behaviours.next()))
				behaviours.remove();
		}
	}
	public Behaviour findBehaviour(Functional.Predicate<Behaviour> p) {
		for(Behaviour b : behaviours)
			if(p.call(b))
				return b;
		return null;
	}
	
	public int checkSkill(String skill) {
		return getSkill(skill) - Dice.random();
	}

	public boolean tryAdd(MObject obj) {
		return true;
	}

	public boolean tryRemove(MObject obj) {
		return true;
	}

	public SkillObject getSkillObject() {
		return new SkillObject();
	}

	public String getGender() {
		if(gender == null)
			return "neuter";
		else
			return gender;
	}
	
}