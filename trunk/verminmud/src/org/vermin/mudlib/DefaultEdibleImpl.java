
package org.vermin.mudlib;

public class DefaultEdibleImpl extends DefaultItemImpl implements Edible, Cloneable {
	
	protected int maxSustenance;
	protected int sustenance;
	protected transient Material foodMaterial; // DefaultMaterialImpl cannot be constructed by serialisation, so don't save it
	
	protected static final String vokaalit = "aeyoui";

	public DefaultEdibleImpl() {
		foodMaterial = MaterialFactory.createMaterial("food", 2, 10, 5, MaterialType.NONE);
	}
	
	public DefaultEdibleImpl(int sustenance) {
		foodMaterial = MaterialFactory.createMaterial("food", 2, 10, 5, MaterialType.NONE);
		this.maxSustenance = sustenance;
		this.sustenance = sustenance;
	}

	public void start() {
		super.start();
	}
	
	public String getDescription() {
		int percentageEaten = (sustenance / maxSustenance) * 100;
		String prefix = null;
		if(vokaalit.indexOf(getName().charAt(0)) != -1) {
			prefix = "an ";
		} else {
			prefix = "a ";
		}
		
		if(percentageEaten >= 100) {
			return prefix+getName();
		} else if(percentageEaten > 70) {
			return prefix+" partially eaten "+getName();
		} else if(percentageEaten > 30) {
			return prefix+" half-eaten "+getName();
		} else {
			return "the remains of "+prefix+getName();
		}
	}
	
	public int getNutritionValue() {
		return sustenance;
	}
	
	public void setNutritionValue(int sustenance) {
		this.sustenance = sustenance;
	}

	/* Check that the given living can consume this */
	public boolean tryConsume(Living l) {
		if((10000 - l.getSustenance()) < 500 ) {
			l.notice("You are too full to eat anything.");
			return false;
		} else {
			return true;
		}
	}

	/* Hook for consuming */
	public void consume(Living l) {
		int maxEat = 10000 - l.getSustenance();
		
		if(sustenance <= maxEat) {
			l.modifySustenance(sustenance);
			l.notice("You eat the whole "+getName()+".");
			l.remove(this);
		} else {
			l.modifySustenance(maxEat);
			sustenance -= maxEat;
			int percentageEaten = (maxSustenance / maxEat) * 100;
			if(percentageEaten > 80) {
				l.notice("You eat most of your "+getName()+".");
			} else if(percentageEaten > 50) {
				l.notice("You eat some of your "+getName()+".");
			} else {
				l.notice("You nibble on your "+getName()+".");
			}
			l.notice("You are full!");
		}
	}
	
	public Material getMaterial() {
		return foodMaterial;
	}
	
	public int getWeight() {
		if(maxSustenance > 0) {
			return (super.getWeight()/maxSustenance) * sustenance;
		} else {
			return super.getWeight();
		}
	}
	
	public MObject getClone() {
		try {
			return (MObject) this.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
			return null;
		}
	}

}
