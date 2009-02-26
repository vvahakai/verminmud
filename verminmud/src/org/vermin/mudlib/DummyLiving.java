package org.vermin.mudlib;



import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.vermin.driver.PropertyProvider;
import org.vermin.driver.Queue;
import org.vermin.mudlib.Damage.Type;
import org.vermin.util.Functional.Predicate;
import org.vermin.wicca.ClientOutput;

public class DummyLiving implements Player {

	public static final Player it = new DummyLiving();
	
	public Object getPreference(String key) { return null; }
	public void setPrompt(Prompt pmt) {
		

	}

	public void setPlan(String plan) {
		

	}

	public String getPlan() {
		
		return null;
	}

	public void addAvailableTitle(String title) {
		

	}
	
	public void addAvailableTitle(String title, String msg) {
	
	}

	public boolean isVerbose() {
		
		return false;
	}

	public void addHandler(String cmd, ActionHandler<MObject> handler) {
		

	}

	public void removeHandler(ActionHandler<MObject> handler) {
		

	}

	public ActionHandler<MObject> getHandler(String cmd) {
		
		return null;
	}

	public void handleAll(ActionHandler<MObject> handler) {
		

	}

	public void unhandleAll() {
		

	}

	public int getLevel() {
		
		return 0;
	}

	public void setLevel(int l) {
		

	}

	public String getSurname() {
		
		return null;
	}

	public String getTitle() {
		
		return null;
	}

	public void setStartingRoom(String id) {
		

	}

	public String getStartingRoom() {
		
		return null;
	}

	public void setExperience(long exp) {
		

	}

	public void setTotalExperience(long exp) {
		

	}

	public long getExperience() {
		
		return 0;
	}

	public long getTotalExperience() {
		
		return 0;
	}

	public SkillObject getSkillObject() {
		
		return null;
	}

	public ClientOutput getClientOutput() {
		
		return null;
	}

	public void addAvailableBattleStyle(BattleStyle style) {
		

	}

	public Vector<BattleStyle> listAvailableBattleStyles() {
		
		return null;
	}

	public void clearSkills() {
		

	}

	public void clearAvailableTitles() {
		

	}

	public void clearAvailableBattleStyles() {
		

	}

	public void addFriend(String name, String list) {
		

	}

	public HashSet getFriends(String list) {
		
		return null;
	}

	public boolean removeFriend(String name, String list) {
		
		return false;
	}

	public Date getCreated() {
		
		return null;
	}

	public String getBestSoloKillDescription() {
		
		return null;
	}

	public long getBestSoloKillExperience() {
		
		return 0;
	}

	public String getBestPartyKillDescription() {
		
		return null;
	}

	public long getBestPartyKillExperience() {
		
		return 0;
	}

	public void setBestSoloKill(Living kill) {
		

	}

	public void setBestPartyKill(Living kill) {
		

	}

	public int getExploreCount() {
		
		return 0;
	}

	public void increaseExploreCount() {
		

	}

	public void startSession() {
		

	}

	public void addDamageListener(DamageListener listener) {
		

	}

	public int getHp() {
		
		return 0;
	}

	public int getMaxHp() {
		
		return 0;
	}

	public void setHp(int p) {
		

	}

	public void addHp(int p) {
		

	}

	public boolean subHp(Damage amount) {
		
		return false;
	}

	public boolean subHp(Damage amount, Living attacker) {
		
		return false;
	}

	public boolean subHp(Damage amount, int hitlocation, Living attacker) {
		
		return false;
	}

	public boolean subHp(Damage amount, int hitlocation) {
		
		return false;
	}

	public int getSp() {
		
		return 0;
	}

	public int getMaxSp() {
		
		return 0;
	}

	public void setSp(int p) {
		

	}

	public void addSp(int p) {
		

	}

	public void subSp(int p) {
		

	}

	public boolean isDead() {
		
		return false;
	}

	public int getPhysicalStrength() {
		
		return 0;
	}

	public int getMentalStrength() {
		
		return 0;
	}

	public int getPhysicalStrength(boolean modifiers) {
		
		return 0;
	}

	public int getMentalStrength(boolean modifiers) {
		
		return 0;
	}

	public void setPhysicalStrength(int p) {
		

	}

	public void setMentalStrength(int p) {
		

	}

	public int getPhysicalConstitution() {
		
		return 0;
	}

	public int getMentalConstitution() {
		
		return 0;
	}

	public int getPhysicalConstitution(boolean modifiers) {
		
		return 0;
	}

	public int getMentalConstitution(boolean modifiers) {
		
		return 0;
	}

	public void setPhysicalConstitution(int p) {
		

	}

	public void setMentalConstitution(int p) {
		

	}

	public int getPhysicalCharisma() {
		
		return 0;
	}

	public int getMentalCharisma() {
		
		return 0;
	}

	public int getPhysicalCharisma(boolean modifiers) {
		
		return 0;
	}

	public int getMentalCharisma(boolean modifiers) {
		
		return 0;
	}

	public void setPhysicalCharisma(int p) {
		

	}

	public void setMentalCharisma(int p) {
		

	}

	public int getPhysicalDexterity() {
		
		return 0;
	}

	public int getMentalDexterity() {
		
		return 0;
	}

	public int getPhysicalDexterity(boolean modifiers) {
		
		return 0;
	}

	public int getMentalDexterity(boolean modifiers) {
		
		return 0;
	}

	public void setPhysicalDexterity(int p) {
		

	}

	public void setMentalDexterity(int p) {
		

	}

	public Race getRace() {
		
		return null;
	}

	public void setRace(Race race) {
		

	}

	public int getSkill(String name) {
		
		return 0;
	}

	public int getSkill(String name, boolean modifiers) {
		
		return 0;
	}

	public void addAttacker(Living who) {
		

	}

	public int getResistance(Type type) {
		
		return 0;
	}

	public int getResistance(Type type, boolean modifiers) {
		
		return 0;
	}

	public Room getRoom() {
		
		return null;
	}

	public Wieldable[] getWieldedItems(boolean naturals) {
		
		return null;
	}

	public Wearable[] getWornItems() {
		
		return null;
	}

	public long getExperienceWorth() {
		
		return 0;
	}

	public void addExperience(long amount) {
		

	}

	public String getPossessive() {
		
		return null;
	}

	public String getObjective() {
		
		return null;
	}

	public String getPronoun() {
		
		return null;
	}

	public void dumpCorpse() {
		

	}

	public boolean wield(Wieldable what) {
		
		return false;
	}

	public boolean unwield(Wieldable what) {
		
		return false;
	}

	public boolean move(Exit to) {
		
		return false;
	}

	public void setBattleStyle(BattleStyle style) {
		

	}

	public BattleStyle getBattleStyle() {
		
		return null;
	}

	public void setOffensiveness(int amount) {
		

	}

	public int getOffensiveness() {
		
		return 0;
	}

	public void battleReact() {
		

	}

	public int getSize() {
		
		return 0;
	}

	public Slot[] getAvailableSlots() {
		
		return null;
	}

	public BattleGroup getBattleGroup() {
		
		return null;
	}

	public BattleGroup getLeafBattleGroup() {
		
		return null;
	}

	public BattleGroup getPersonalBattleGroup() {
		
		return null;
	}

	public boolean inBattle() {
		
		return false;
	}

	public void doBattle() {
		

	}

	public int getLawfulness() {
		
		return 0;
	}

	public void modifySustenance(int amount) {
		

	}

	public void setSustenance(int sustenance) {
		

	}

	public int getSustenance() {
		
		return 0;
	}

	public void clearInventory() {
		

	}

	public void putItemsToInventory() {
		

	}

	public void updateStats() {
		

	}

	public String getShape() {
		
		return null;
	}

	public int getStat(Stat stat) {
		
		return 0;
	}

	public int getStat(Stat stat, boolean modifiers) {
		
		return 0;
	}

	public Party getParty() {
		
		return null;
	}

	public void setParty(Party pi) {
		

	}

	public boolean isBlocking(String dir) {
		
		return false;
	}

	public boolean canFly() {
		
		return false;
	}

	public boolean canBreatheWater() {
		
		return false;
	}

	public boolean hasToBreathe() {
		
		return false;
	}

	public void addAffliction(Affliction a) {
		

	}

	public void removeAffliction(Affliction a) {
		

	}

	public void notice(String text) {
		

	}

	public void addBehaviour(Behaviour b) {
		

	}

	public void removeBehaviour(Behaviour b) {
		

	}

	public int checkSkill(String skill) {
		
		return 0;
	}

	public Types getType() {
		
		return null;
	}

	public void addAlias(String alias) {
		

	}

	public void setDescription(String name) {
		

	}

	public void setLongDescription(String name) {
		

	}

	public void start() {
		

	}

	public MObject getClone() {
		
		return null;
	}

	public Container getParent() {
		
		return null;
	}

	public void setParent(Container parent) {
		

	}

	public int getIllumination() {
		
		return 0;
	}

	public void addModifier(Modifier m) {
		

	}

	public boolean action(MObject caller, String action) {
		
		return false;
	}

	public boolean tick(Queue queue) {
		
		return false;
	}

	public void setAnonymous(boolean anonymous) {
		

	}

	public boolean isAnonymous() {
		
		return false;
	}

	public String getId() {
		
		return null;
	}

	public void setId(String id) {
		

	}

	public void save() {
		

	}

	public String getName() {
		
		return null;
	}

	public void setName(String name) {
		

	}

	public String getDescription() {
		
		return null;
	}

	public String getLongDescription() {
		
		return null;
	}

	public boolean isAlias(String alias) {
		
		return false;
	}

	// behaviour interface:
	public void leaves(Living who) { }
	public void arrives(Living who) { }
	public void arrives(Living who, Exit from) { }
	public void afterArrives(Living who) { }
	public void leaves(Living who, Exit to) { }
	public void startsUsing(Living who, Skill skill) { }
	public void takes(Living who, Item what) { }
	public void drops(Living who, Item what) { }
	public void wields(Living who, Item what) {	}
	public void unwields(Living who, Item what) { }
	public void says(Living who, String what) {	}
	public void dies(Living victim, Living killer) { }
	public void asks(Living asker, Living target, String subject) {	}
	public void onBattleTick(Living who) { }
	public void onRegenTick(Living who) { }

	public Iterator findByType(Types type) {
		
		return null;
	}

	public MObject findByName(String name) {
		
		return null;
	}

	public MObject findByName(String name, int index) {
		
		return null;
	}

	public MObject findByNameAndType(String name, Types type) {
		
		return null;
	}

	public MObject findByNameAndType(String name, int index, Types type) {
		
		return null;
	}

	public boolean tryAdd(MObject obj) {
		
		return false;
	}

	public boolean tryRemove(MObject obj) {
		
		return false;
	}

	public void add(MObject child) {
		

	}

	public void remove(MObject child) {
		

	}

	public boolean contains(MObject child) {
		
		return false;
	}

	public Money getMoney() {
		
		return null;
	}

	public int getLifeAlignment() {
		
		return 0;
	}

	public int getProgressAlignment() {
		
		return 0;
	}

	public void addProvider(PropertyProvider<LivingProperty> provider) {
		

	}

	public void removeProvider(PropertyProvider<LivingProperty> provider) {
		

	}

	public boolean provides(LivingProperty first, LivingProperty... rest) {
		
		return false;
	}

	public boolean provides(LivingProperty property) {
		
		return false;
	}

	public boolean providesAny(LivingProperty first, LivingProperty... rest) {
		
		return false;
	}

	public boolean clientCommand(String command) {
		
		return false;
	}

	public void setClientOutput(ClientOutput output) {
		

	}

	public void prompt() {
		

	}

	public String getPromptString() {
		
		return null;
	}

	public int getFreeStatPoints() {
		return 0;
	}

	public void setFreeStatPoints(int points) {
	}

	public int getUsedStatPoints(Stat s) {
		return 0;
	}
	
	public void clearUsedStatPoints() {}

	public int getStatPointCost(Stat s) {
		return 0;
	}

	public void useStatPoint(Stat s) {
		
	}

	public int getMaxStat(Stat s) {
		return 0;
	}

	public boolean isConcentrating() { return false; }
	public void endSession() {
		
	}
	public void removeBehaviour(Predicate<Behaviour> p) {
		
	}
	public Behaviour findBehaviour(Predicate<Behaviour> p) {
		return null;
	}
	public void command(Object... args) {
		
	}
	public String getGender() {
		return "male";
	}
}
