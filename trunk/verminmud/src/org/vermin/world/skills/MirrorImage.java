/* MirrorImage.java
 * 
 * This is a mirror image of a living creature.
 */
package org.vermin.world.skills;

import org.vermin.driver.Queue;
import org.vermin.mudlib.BattleGroup;
import org.vermin.mudlib.Damage;
import org.vermin.mudlib.DefaultMonster;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Party;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Room;
import org.vermin.mudlib.Stat;

class MirrorImage extends DefaultMonster
{

	private boolean isDead = false;
	private Party currentParty;
	private MirrorImageBattleGroup miBattleGroup;
	private Living owner;

	private void die() { 
		if(miBattleGroup != null) {
			((Room) getParent()).notice(this, "a mirror image vanishes.");
			miBattleGroup.removeImage(this);
			miBattleGroup = null;
		}
		isDead = true;
	}

	public void leaves(Living who) {
		if(who == owner)
			die();
	}

	public void leaves(Living who, Exit to) {
		leaves(who);
	}

	public void setMirrorImageBattleGroup(MirrorImageBattleGroup bg) { miBattleGroup = bg; }

	public void setOwner(Living who) { owner = who; }

	
	

	@Override
	public boolean tick(Queue type) {
		// don't do anything (prevents mirror images from doing battle)
		return false;
	}

	@Override
	public BattleGroup getBattleGroup() {
		return owner.getBattleGroup();
	}

	public int getHp() { return 1; }
	public int getMaxHp() { return 1; }
	public void setHp(int p) {}
	public void addHp(int p) {}
	public boolean subHp(Damage amount) { die(); return true; }
	public boolean subHp(Damage amount, Living attacker) { die(); return true; }
	public boolean subHp(Damage amount, int hitlocation, Living attacker) { die(); return true; }
	public boolean subHp(Damage amount, int hitlocation) { die(); return true; }
	public int getSp() { return 0; }
	public int getMaxSp() { return 0; }
	public void setSp(int p) {}
	public void addSp(int p) {}
	public void subSp(int p) {}
	public int getAc(int location) { return 0; }
	public boolean isDead() { return isDead; }
	public int getPhysicalStrength() { return 0; }
	public int getMentalStrength() { return 0; }
	public int getPhysicalStrength(boolean modifiers) { return 0; }
	public int getMentalStrength(boolean modifiers) { return 0; }
	public void setPhysicalStrength(int p) {}
	public void setMentalStrength(int p) {}
	public int getPhysicalConstitution() { return 0; }
	public int getMentalConstitution() { return 0; }
	public int getPhysicalConstitution(boolean modifiers) { return 0; }
	public int getMentalConstitution(boolean modifiers) { return 0; }
	public void setPhysicalConstitution(int p) {}
	public void setMentalConstitution(int p) {}
	public int getPhysicalCharisma() { return 0; }
	public int getMentalCharisma() { return 0; }
	public int getPhysicalCharisma(boolean modifiers) { return 0; }
	public int getMentalCharisma(boolean modifiers) { return 0; }
	public void setPhysicalCharisma(int p) {}
	public void setMentalCharisma(int p) {}
	public int getPhysicalDexterity() { return 0; }
	public int getMentalDexterity() { return 0; }
	public int getPhysicalDexterity(boolean modifiers) { return 0; }
	public int getMentalDexterity(boolean modifiers) { return 0; }
	public void setPhysicalDexterity(int p) {}
	public void setMentalDexterity(int p) {}
	public Race getRace() { return race; }
	public void setRace(Race r) { race = r; }
	public int getSkill(String name) { return 0; }
	public int getSkill(String name, boolean modifiers) { return 0; }
	public void addAttacker(Living who) {}
	public int getResistance(int type) { return 0; }
	public long getExperienceWorth() { return 0; }
	public void dumpCorpse() {}
	public int getOffensiveness() { return 0; }
	public void battleReact() {}
	public int getStat(Stat stat) { return 0; }
	public int getStat(Stat stat, boolean modifiers) { return 0; }
	public Party getParty() { return currentParty; }
	public void setParty(Party pi) { currentParty = pi; }
	public boolean isBlocking(String dir) { return false; }
	public boolean canFly() { return true; }
	public boolean canBreatheWater() { return true; }
	public boolean hasToBreathe() { return false; }
}
