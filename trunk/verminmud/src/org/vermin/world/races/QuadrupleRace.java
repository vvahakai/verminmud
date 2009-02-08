/*
 *  QuadrupleRace.java
 *  Four-legged animal race
 *  intended for monster use.
 */
package org.vermin.world.races;

import java.util.EnumSet;
import java.util.Vector;

import org.vermin.driver.Singleton;
import org.vermin.mudlib.DefaultRaceImpl;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Race;
import org.vermin.mudlib.Slot;
import org.vermin.mudlib.Wieldable;
import org.vermin.mudlib.battle.GoreMessageProvider;
import org.vermin.mudlib.battle.GoreMessageProviderFactory;
import org.vermin.mudlib.battle.GoreProperty;
import org.vermin.world.items.QuadrupleHoof;
import org.vermin.world.items.QuadrupleHorns;
import org.vermin.world.items.QuadrupleJaws;
import org.vermin.world.items.QuadruplePaw;
import org.vermin.world.items.QuadrupleTail;
import org.vermin.world.races.QuadrupleFactory;

import static org.vermin.mudlib.battle.GoreProperty.HAS_BLOOD;
import static org.vermin.mudlib.battle.GoreProperty.HAS_BONES;
import static org.vermin.mudlib.battle.GoreProperty.HAS_INTERNAL_ORGANS;
import static org.vermin.mudlib.battle.GoreProperty.IS_ANIMAL;

public class QuadrupleRace extends DefaultRaceImpl implements Singleton {

	public static final GoreMessageProvider msg;

	private static final Wieldable leftHoof = new QuadrupleHoof();

	private static final Wieldable rightHoof = new QuadrupleHoof();

	private static final Wieldable leftPaw = new QuadruplePaw();

	private static final Wieldable rightPaw = new QuadruplePaw();

	private static final Wieldable horns = new QuadrupleHorns();

	private static final Wieldable jaws = new QuadrupleJaws();

	private static final Wieldable tail = new QuadrupleTail();

	protected static final QuadrupleRace _instance = new QuadrupleRace();

	private static String[] genders = new String[0];

	private static final String name = new String("animal");

	private static final String[] hitlocations = new String[] { "left eye",
			"right eye", "forehead", "forehead", "muzzle", "muzzle", "jaw",
			"poll", "throat", "throat", "throat", "abdomen", "abdomen",
			"abdomen", "abdomen", "abdomen", "abdomen", "abdomen", "spine",
			"chest", "chest", "chest", "chest", "chest", "chest", "chest",
			"chest", "chest", "chest", "chest", "chest", "neck", "neck",
			"neck", "neck", "neck", "neck", "neck", "neck", "back", "back",
			"back", "back", "back", "back", "back", "back", "loin", "loin",
			"loin", "loin", "loin", "loin", "loin", "croup", "croup", "croup",
			"croup", "croup", "left shoulder", "left shoulder",
			"right shoulder", "right shoulder", "left flank", "left flank",
			"left flank", "left flank", "right flank", "right flank",
			"right flank", "right flank", "left thigh", "left thigh",
			"left thigh", "left thigh", "left thigh", "left thigh",
			"right thigh", "right thigh", "right thigh", "right thigh",
			"right thigh", "right thigh", "buttocks", "buttocks", "buttocks",
			"buttocks", "buttocks", "buttocks", "left forefoot",
			"left forefoot", "right forefoot", "right forefoot", "left knee",
			"right knee", "left rear foot", "left rear foot",
			"right rear foot", "right rear foot", "left hock", "right hock" };

	static {
		_instance.start();
		// System.out.println("GORE MESSAGE PrOvIdEr !!!!
		// --->"+_instance.goreMessageProvider);
		for (int i = 0; i < 100; i++) {
			hitlocations[i] = hitlocations[i].intern();
		}

		msg = GoreMessageProviderFactory.getInstance().findProvider(
				EnumSet
						.of(HAS_BLOOD, HAS_BONES, HAS_INTERNAL_ORGANS,
								IS_ANIMAL));
	}

	public int getMinimumVisibleIllumination() {
		return 35;
	}

	public EnumSet<GoreProperty> getGoreFlags() {
		return EnumSet.of(HAS_BLOOD, HAS_BONES, HAS_INTERNAL_ORGANS, IS_ANIMAL);
	}

	public int getLifeAlignment() {
		return 0;
	}

	public int getProgressAlignment() {
		return 0;
	}

	/**
	 * Returns a string describing a hitlocation in the specified percent.
	 * 
	 * @param pos
	 *            a percentage describing the 'goodness' of the hitlocation
	 * @return the hitLocation value
	 */
	public String getHitLocation(int pos) {
		return hitlocations[pos];
	}

	/**
	 * Returns null.
	 * 
	 * @param pos
	 *            hitlocation as percentage
	 * @return null
	 */
	public Slot getSlotForLocation(int pos) {
		return null;
	}

	/**
	 * Returns null.
	 * 
	 * @return null
	 */
	public Slot[] getSlots() {
		return new Slot[0];
	}

	/**
	 * Returns the name of this race ('quadruple').
	 * 
	 * @return "quadruple"
	 */
	public String getName() {
		return QuadrupleRace.name;
	}

	/**
	 * Returns a string describing the name of a limb in the specified index.
	 * (example: "hoof")
	 * 
	 * @param limb
	 *            the index of the limb to return name of
	 * @return the name of the limb
	 */
	public String getLimbName(int limb) {
		return null;
	}

	/**
	 * Returns the limb item in the specified index.
	 * 
	 * @param limb
	 * @return the Limb
	 */
	public Item getLimb(int limb) {
		return null;
	}

	/**
	 * Returns the limb count of this Quadruple.
	 * 
	 * @return number of limbs
	 */
	public int getLimbCount() {
		return 0;
	}

	/**
	 * Returns an instance of Quadruple, with default options set.
	 * 
	 * Equal to calling QuadrupleRace.getInstance(new int[]
	 * {QuadrupleRace.HOOVES})
	 * 
	 * @return an instance of QuadrupleRace
	 */
	public static Race getInstance() {
		return getInstance(new Object[] { QuadrupleFactory.QuadrupleOption.HOOVES });
	}

	/**
	 * Returns an instance of Quadruple, with the specified options enabled.
	 * 
	 * @return a Quadruple
	 */
	public static Race getInstance(Object parameters) {

		QuadrupleFactory.QuadrupleOption[] options = new QuadrupleFactory.QuadrupleOption[(((Object[]) parameters).length)];

		for (int i = 0; i < options.length; i++) {
			options[i] = (QuadrupleFactory.QuadrupleOption) ((Object[]) parameters)[i];
		}

		Vector wieldables = new Vector();

		for (int i = 0; i < options.length; i++) {
			if (options[i] == QuadrupleFactory.QuadrupleOption.HOOVES) {
				wieldables.add(leftHoof);
				wieldables.add(rightHoof);
			} else if (options[i] == QuadrupleFactory.QuadrupleOption.PAWS) {
				wieldables.add(leftPaw);
				wieldables.add(rightPaw);
			} else if (options[i] == QuadrupleFactory.QuadrupleOption.HORNS) {
				wieldables.add(horns);
			} else if (options[i] == QuadrupleFactory.QuadrupleOption.JAWS) {
				wieldables.add(jaws);
			} else if (options[i] == QuadrupleFactory.QuadrupleOption.TAIL) {
				wieldables.add(tail);
			}
		}

		return (Race) new ConcreteQuadrupleRace(_instance,
				(Wieldable[]) wieldables.toArray(new Wieldable[0]));
	}

	public int getBaseHpRegen() {
		return 50;
	}

	public int getBaseSpRegen() {
		return 50;
	}

	public int getSize() {
		return 50;
	}

	public int getExpRate() {
		return 100;
	}

	/*
	 * Max stats for this race
	 */
	public int getMaxPhysicalStrength() {
		return 50;
	}

	public int getMaxMentalStrength() {
		return 50;
	}

	public int getMaxPhysicalConstitution() {
		return 50;
	}

	public int getMaxMentalConstitution() {
		return 50;
	}

	public int getMaxPhysicalCharisma() {
		return 50;
	}

	public int getMaxMentalCharisma() {
		return 50;
	}

	public int getMaxPhysicalDexterity() {
		return 50;
	}

	public int getMaxMentalDexterity() {
		return 50;
	}

	/*
	 * Statcosts for this race (0-10)
	 */
	public int getPhysicalStrengthCost() {
		return 5;
	}

	public int getMentalStrengthCost() {
		return 5;
	}

	public int getPhysicalConstitutionCost() {
		return 5;
	}

	public int getMentalConstitutionCost() {
		return 5;
	}

	public int getPhysicalCharismaCost() {
		return 5;
	}

	public int getMentalCharismaCost() {
		return 5;
	}

	public int getPhysicalDexterityCost() {
		return 5;
	}

	public int getMentalDexterityCost() {
		return 5;
	}

	public String[] getGenders() {
		return genders;
	}
}
