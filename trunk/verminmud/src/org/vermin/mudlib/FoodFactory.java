package org.vermin.mudlib;

import java.util.*;
/**
 * This class is capable of producing various kinds
 * of solid consumables.
 * 
 * @author Jaakko Pohjamo
 */
public class FoodFactory {

	protected static final FoodFactory _instance = new FoodFactory();
	
	protected LinkedHashMap<IntegerRange, EdiblePrototype> consumableMap;
	
	// used as a map key to map ranges of sustenance values to
	// appropriate consumables.
	private static class IntegerRange {
		private int min;
		private int max;
		
		public IntegerRange(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		public boolean equals(Object o) {
			if(o instanceof Integer) {
				Integer i = (Integer) o;
				
				if(i >= min && i <= max) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	private static class EdiblePrototype {
		public String name;
		public String plural;
		public int minSize;
		public int maxSize;
		public int sustenance;
		public String[] aliases;
		
		public EdiblePrototype(String name, String plural, String[] aliases, int minSize, int maxSize, int sustenance) {
			this.name = name;
			this.plural = plural;
			this.minSize = minSize;
			this.maxSize = maxSize;
			this.sustenance = sustenance;
			this.aliases = aliases;
		}

		public EdiblePrototype(String name, String plural, String alias, int minSize, int maxSize, int sustenance) {
			this(name, plural, 
				  alias == null ? new String[0] : new String[] { alias }, 
				  minSize, maxSize, sustenance);
		}
	}
	
	protected FoodFactory() {
		consumableMap = new LinkedHashMap<IntegerRange, EdiblePrototype>();
		consumableMap.put(new IntegerRange(1, 20), new EdiblePrototype("berry", "berries", (String)null,  1, 2, 10));
		consumableMap.put(new IntegerRange(21, 50), new EdiblePrototype("cherry", "cherries", (String)null, 10, 20, 30));
		consumableMap.put(new IntegerRange(51, 100), new EdiblePrototype("tomato", "tomatoes",(String) null,  50, 100, 70));
		consumableMap.put(new IntegerRange(101, 200), new EdiblePrototype("apple", "apples", (String)null, 80, 140, 150));
		consumableMap.put(new IntegerRange(201, 300), new EdiblePrototype("cucumber sandwich", "cucumber sandwiches", "sandwich", 80, 140, 250));
		consumableMap.put(new IntegerRange(301, 400), new EdiblePrototype("cheese sandwich", "cheese sandwiches", "sandwich", 80, 140, 350));
		consumableMap.put(new IntegerRange(401, 500), new EdiblePrototype("ham sandwich", "ham sandwiches", "sandwich", 80, 140, 450));
		consumableMap.put(new IntegerRange(501, 800), new EdiblePrototype("piece of cake", "pieces of cake", "cake", 200, 400, 650));
		consumableMap.put(new IntegerRange(801, 1200), new EdiblePrototype("loaf of bread", "loaves of bread", new String[] { "loaf", "bread" }, 300, 500, 1000));
		consumableMap.put(new IntegerRange(1201, 2000), new EdiblePrototype("piece of waybread", "pieces of waybread", "bread", 100, 200, 1600));
		consumableMap.put(new IntegerRange(2001, 3000), new EdiblePrototype("cut of meat", "cuts of meat", "meat", 100, 200, 2500));
		consumableMap.put(new IntegerRange(3001, 4000), new EdiblePrototype("fried fish", "fried fishes", "fish", 200, 500, 3500));
		consumableMap.put(new IntegerRange(4001, 5000), new EdiblePrototype("hunk of meat", "hunks of meat", new String[] { "hunk", "meat" }, 200, 500, 4500));
		consumableMap.put(new IntegerRange(5001, 6000), new EdiblePrototype("iron ration", "iron rations", "ration", 300, 300, 5500));
		consumableMap.put(new IntegerRange(6001, 7000), new EdiblePrototype("roasted pig", "roasted pigs", "pig", 10000, 20000, 6500));
		consumableMap.put(new IntegerRange(7001, 10000), new EdiblePrototype("dead cow", "dead cows", "cow", 50000, 100000, 10000));
	}
	
	public static FoodFactory getInstance() {
		return _instance;
	}
	
	public Edible create(int sustenance, boolean halfEaten) {
		if(sustenance > 10000) {
			sustenance = 10000;
		} else if(sustenance < 1) {
			sustenance = 1;
		}
		
		for(Map.Entry e : consumableMap.entrySet()) {
			if(e.getKey().equals(sustenance)) {
				return create((EdiblePrototype) e.getValue(), halfEaten);
			}
		}
		
		return null; 
	}
	
	public Edible create(int sustenance) {
		return create(sustenance, false);
	}
	
	public Edible create() {
		return create(Dice.random(10000), false);
	}
	
	private Edible create(EdiblePrototype p, boolean halfEaten) {
		int sizeVariance = p.maxSize - p.minSize;
		int sizeBonus = sizeVariance > 0 ? Dice.random(sizeVariance) : 0;
		double relativeSize = sizeVariance / (sizeVariance - sizeBonus+1);
		
		DefaultEdibleImpl food = new DefaultEdibleImpl((int) (p.sustenance + (p.sustenance * relativeSize) * 0.2));
		if(relativeSize < 0.2) {
			food.setName("small "+p.name);
			food.setPluralForm("small "+p.plural);
			food.setLongDescription("This is a small "+p.name+". It looks tasty nonetheless.");
		} else if(relativeSize < 0.7) {
			food.setName(p.name);
			food.setPluralForm(p.plural);
			food.setLongDescription("This is a tasty looking "+p.name+".");
		} else {
			food.setName("large "+p.name);
			food.setPluralForm("large "+p.plural);
			food.setLongDescription("This is an unusually large "+p.name+". It looks very tasty indeed.");
		}
		
		if(halfEaten) {
			food.setNutritionValue(food.getNutritionValue() - Dice.random(food.getNutritionValue()));
		}

		food.addAlias(p.name);
		food.setSize(sizeVariance+sizeBonus);

		for(String alias : p.aliases)
			food.addAlias(alias);
		
		return food;
	}
}
