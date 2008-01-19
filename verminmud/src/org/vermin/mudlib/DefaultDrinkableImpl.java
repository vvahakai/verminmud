package org.vermin.mudlib;

public class DefaultDrinkableImpl extends DefaultEdibleImpl implements Drinkable {

	public DefaultDrinkableImpl() {
		foodMaterial = MaterialFactory.createMaterial("water", 1, 2, 1, MaterialType.NONE);
	}
	
	public DefaultDrinkableImpl(int sustenance) {
		foodMaterial = MaterialFactory.createMaterial("water", 1, 2, 1, MaterialType.NONE);
		this.maxSustenance = sustenance;
		this.sustenance = sustenance;
	}	
	
	public String getDescription() {
		int percentageDrinked = (sustenance / maxSustenance) * 100;
		String prefix = null;
		if(vokaalit.indexOf(getName().charAt(0)) != -1) {
			prefix = "an ";
		} else {
			prefix = "a ";
		}
		
		if(percentageDrinked >= 100) {
			return prefix+getName();
		} else if(percentageDrinked > 70) {
			return "an almost full "+getName();
		} else if(percentageDrinked > 30) {
			return "a half-full "+getName();
		} else {
			return "an almost empty "+prefix+getName();
		}
	}

	/* Check that the given living can consume this */
	public boolean tryConsume(Living l) {
		if((10000 - l.getSustenance()) < 500 ) {
			l.notice("You are too full to drink anything.");
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
			l.notice("You drink the whole "+getName()+".");
			l.remove(this);
		} else {
			l.modifySustenance(maxEat);
			sustenance -= maxEat;
			int percentageEaten = (maxSustenance / maxEat) * 100;
			if(percentageEaten > 80) {
				l.notice("You drink most of your "+getName()+".");
			} else if(percentageEaten > 50) {
				l.notice("You drink some of your "+getName()+".");
			} else {
				l.notice("You sip on your "+getName()+".");
			}
			l.notice("You are full!");
		}
	}
	
	public Material getMaterial() {
		return foodMaterial;
	}
	
}
