package org.vermin.mudlib;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaterialFactory {

	private static Object lock = new Object();
	private static Map defaultMaterials = Collections.synchronizedMap(new TreeMap());
	private static Map cachedMaterials = Collections.synchronizedMap(new TreeMap());
	
	protected static final Pattern MATERIALDESC_REGEX = Pattern.compile("^(\\S+)\\+(\\S+)\\:(\\S+)\\+(\\S+)\\:(\\S+)$");
	
	public static Material createMaterial(String name, int weight, int durability, long value, MaterialType type) {
		if(defaultMaterials.isEmpty()) {
			synchronized(lock) {
				initializeDefaultMaterials();
			}
		}
			
		if(defaultMaterials.containsKey(name))	{
			return (Material) defaultMaterials.get(name);
		} else if(cachedMaterials.containsKey(name))	{
			return (Material) cachedMaterials.get(name);
		} else {
			Material m = new DefaultMaterialImpl(name, weight, durability, value, type);
			cachedMaterials.put(name, m);
			return m;
		}
	}
	public static Material createMaterial(String name) {
	  if(defaultMaterials.isEmpty()) {
	     synchronized(lock) {
	        initializeDefaultMaterials();
	     }
	  }
		Matcher m = MATERIALDESC_REGEX.matcher(name);
		if(defaultMaterials.containsKey(name))	{
			return (Material) defaultMaterials.get(name);
		} else if(cachedMaterials.containsKey(name)) {
			return (Material) cachedMaterials.get(name);
		} else if(m.matches()) {
			return buildComposite(name);
		} else {
			return (Material) defaultMaterials.get("antimatter");
	  }
	}
	public static void addDefaultMaterial(Material m) {
		defaultMaterials.put(m.getName(), m);
	}
	public static Iterator materialList() {
		if(defaultMaterials.isEmpty()) {
			synchronized(lock) {
				initializeDefaultMaterials();
			}
		}
		return defaultMaterials.values().iterator();
	}
	private static void initializeDefaultMaterials() {
		defaultMaterials.put("antimatter",	new DefaultMaterialImpl("antimatter",  0,	0,  100, MaterialType.NONE));
		defaultMaterials.put("iron", 		new DefaultMaterialImpl("iron", 	  	7, 	67, 35, MaterialType.METAL));		
		defaultMaterials.put("steel", 		new DefaultMaterialImpl("steel", 	  	8, 	72, 45, MaterialType.METAL));		
		defaultMaterials.put("gold", 		new DefaultMaterialImpl("gold", 	  	20, 45, 75, MaterialType.METAL));	
		defaultMaterials.put("silver", 		new DefaultMaterialImpl("silver",	  	10, 55, 65, MaterialType.METAL));	
		defaultMaterials.put("bronze", 		new DefaultMaterialImpl("bronze", 	  	9, 	54, 45, MaterialType.METAL));
		defaultMaterials.put("copper", 		new DefaultMaterialImpl("copper", 	  	9, 	50, 32, MaterialType.METAL));
		defaultMaterials.put("aluminium", 	new DefaultMaterialImpl("aluminium", 	3, 	60, 43, MaterialType.METAL));
		defaultMaterials.put("brass", 		new DefaultMaterialImpl("brass", 		8, 	60, 34, MaterialType.METAL));								
		defaultMaterials.put("lead", 		new DefaultMaterialImpl("lead", 		11, 50, 22, MaterialType.METAL));								
		defaultMaterials.put("magnesium", 	new DefaultMaterialImpl("magnesium", 	2, 	25, 32, MaterialType.METAL));								
		defaultMaterials.put("mercury", 	new DefaultMaterialImpl("mercury", 	14, 1,  5,  MaterialType.METAL));								
		defaultMaterials.put("nickel", 		new DefaultMaterialImpl("nickel", 		9, 	50, 31, MaterialType.METAL));								
		defaultMaterials.put("platinum", 	new DefaultMaterialImpl("platinum", 	21, 67, 66, MaterialType.METAL));								
		defaultMaterials.put("tin", 		new DefaultMaterialImpl("tin", 		7, 	30, 23, MaterialType.METAL));								
		defaultMaterials.put("titanium", 	new DefaultMaterialImpl("titanium", 	4, 	72, 87, MaterialType.METAL));								
		defaultMaterials.put("tungsten", 	new DefaultMaterialImpl("tungsten", 	20, 65, 13, MaterialType.METAL));								
		defaultMaterials.put("zinc", 		new DefaultMaterialImpl("zinc", 		7, 	59, 31, MaterialType.METAL));								
		defaultMaterials.put("agate", 		new DefaultMaterialImpl("agate", 		3, 	67, 68, MaterialType.MINERAL));								
		defaultMaterials.put("alabaster",	new DefaultMaterialImpl("alabaster", 	3, 	34, 43, MaterialType.MINERAL));								
		defaultMaterials.put("amber", 		new DefaultMaterialImpl("amber", 		1, 	55, 77, MaterialType.ORGANIC));								
		defaultMaterials.put("beryl", 		new DefaultMaterialImpl("beryl", 		3, 	65, 64, MaterialType.MINERAL));								
		defaultMaterials.put("brick", 		new DefaultMaterialImpl("brick", 		2, 	60, 68, MaterialType.MINERAL));								
		defaultMaterials.put("cement", 		new DefaultMaterialImpl("cement", 		3, 	60, 45, MaterialType.MINERAL));								
		defaultMaterials.put("chalk", 		new DefaultMaterialImpl("chalk", 		2, 	10, 11, MaterialType.MINERAL));								
		defaultMaterials.put("coal", 		new DefaultMaterialImpl("coal", 		1, 	7,  7,  MaterialType.MINERAL));								
		defaultMaterials.put("coke", 		new DefaultMaterialImpl("coke", 		1, 	8,  8,  MaterialType.MINERAL));								
		defaultMaterials.put("diamond", 	new DefaultMaterialImpl("diamond", 	3, 	81, 99, MaterialType.GEM));								
		defaultMaterials.put("dolomite", 	new DefaultMaterialImpl("dolomite", 	3, 	44, 51, MaterialType.MINERAL));								
		defaultMaterials.put("feldspar", 	new DefaultMaterialImpl("feldspar", 	3, 	50, 55, MaterialType.MINERAL));								
		defaultMaterials.put("flint", 		new DefaultMaterialImpl("flint", 		3, 	35, 37, MaterialType.MINERAL));								
		defaultMaterials.put("garnet", 		new DefaultMaterialImpl("garnet", 		4, 	60, 74, MaterialType.MINERAL));								
		defaultMaterials.put("granite", 	new DefaultMaterialImpl("granite", 	3, 	67, 50, MaterialType.MINERAL));								
		defaultMaterials.put("haematite",	new DefaultMaterialImpl("haematite", 	5, 	20, 19, MaterialType.MINERAL));								
		defaultMaterials.put("magnetite",	new DefaultMaterialImpl("magnetite", 	5, 	25, 22, MaterialType.MINERAL));								
		defaultMaterials.put("malachite", 	new DefaultMaterialImpl("malachite", 	4, 	60, 78, MaterialType.MINERAL));								
		defaultMaterials.put("marble", 		new DefaultMaterialImpl("marble", 		3, 	55, 56, MaterialType.MINERAL));								
		defaultMaterials.put("opal", 		new DefaultMaterialImpl("opal", 		2, 	70, 93, MaterialType.GEM));								
		defaultMaterials.put("quartz", 		new DefaultMaterialImpl("quartz", 		3, 	30, 33, MaterialType.MINERAL));								
		defaultMaterials.put("sandstone", 	new DefaultMaterialImpl("sandstone", 	2, 	5,  3,  MaterialType.MINERAL));								
		defaultMaterials.put("topaz", 		new DefaultMaterialImpl("topaz", 		4, 	75, 87, MaterialType.GEM));								
		defaultMaterials.put("skin", 		new DefaultMaterialImpl("skin", 	  	1, 	15, 1,  MaterialType.ORGANIC));
	
		defaultMaterials.put("apple", 		new DefaultMaterialImpl("apple", 		1, 	30, 40, MaterialType.WOOD));								
		defaultMaterials.put("ash", 		new DefaultMaterialImpl("ash", 		1, 	35, 46, MaterialType.WOOD));								
		defaultMaterials.put("balsa", 		new DefaultMaterialImpl("balsa", 		1, 	2,  2,  MaterialType.WOOD));								
		defaultMaterials.put("bamboo", 		new DefaultMaterialImpl("bamboo", 		1, 	14, 24, MaterialType.WOOD));								
		defaultMaterials.put("cedar", 		new DefaultMaterialImpl("cedar", 		1, 	35, 45, MaterialType.WOOD));								
		defaultMaterials.put("ebony", 		new DefaultMaterialImpl("ebony", 		1, 	40, 46, MaterialType.WOOD));								
		defaultMaterials.put("mahogany", 	new DefaultMaterialImpl("mahogany", 	1, 	35, 46, MaterialType.WOOD));								
		defaultMaterials.put("oak", 		new DefaultMaterialImpl("oak", 		1, 	30, 43, MaterialType.WOOD));								
		defaultMaterials.put("pine", 		new DefaultMaterialImpl("pine", 		1, 	33, 47, MaterialType.WOOD));								
		defaultMaterials.put("teak", 		new DefaultMaterialImpl("teak", 		1, 	34, 48, MaterialType.WOOD));								
		defaultMaterials.put("willow", 		new DefaultMaterialImpl("willow", 	  	1, 	31, 44, MaterialType.WOOD));
		defaultMaterials.put("asbestos", 	new DefaultMaterialImpl("asbestos", 	3, 	34, 49, MaterialType.MINERAL));
		defaultMaterials.put("beeswax", 	new DefaultMaterialImpl("beeswax",  	1, 	3,  4,  MaterialType.NONE));
		defaultMaterials.put("bone", 		new DefaultMaterialImpl("bone", 	 	2, 	40, 14, MaterialType.ORGANIC));
		defaultMaterials.put("charcoal", 	new DefaultMaterialImpl("charcoal", 	1, 	5,  6,  MaterialType.ORGANIC));
		defaultMaterials.put("clay", 		new DefaultMaterialImpl("clay", 	 	3, 	20, 8,  MaterialType.ORGANIC));
		defaultMaterials.put("cork", 		new DefaultMaterialImpl("cork", 	 	1, 	10, 9,  MaterialType.WOOD));
		defaultMaterials.put("glass", 		new DefaultMaterialImpl("glass", 		3, 	17, 24, MaterialType.MINERAL));
		defaultMaterials.put("ice", 		new DefaultMaterialImpl("ice", 	  	1, 	10, 20, MaterialType.NONE));
		defaultMaterials.put("ivory", 		new DefaultMaterialImpl("ivory", 	  	2, 	45, 40, MaterialType.WOOD));
		defaultMaterials.put("leather",		new DefaultMaterialImpl("leather", 	1, 	30, 22, MaterialType.ORGANIC));
		defaultMaterials.put("ochre", 		new DefaultMaterialImpl("ochre", 		4, 	35, 29, MaterialType.MINERAL));
		defaultMaterials.put("paper", 		new DefaultMaterialImpl("paper", 		1, 	11, 6,  MaterialType.ORGANIC));
		defaultMaterials.put("wax", 		new DefaultMaterialImpl("wax", 	  	2, 	4,  3,  MaterialType.ORGANIC));
		defaultMaterials.put("pitch", 		new DefaultMaterialImpl("pitch", 	  	1, 	2,  1,  MaterialType.NONE));
		defaultMaterials.put("porcelain", 	new DefaultMaterialImpl("porcelain", 	2, 	20, 24, MaterialType.MINERAL));
		defaultMaterials.put("rubber", 		new DefaultMaterialImpl("rubber", 	  	1, 	25, 32, MaterialType.ORGANIC));
		defaultMaterials.put("cloth",		new DefaultMaterialImpl("cloth",		1,  18, 24, MaterialType.ORGANIC));
	
		//This is only here to test composite materials
		/*CompositeMaterial cedariron = new CompositeMaterial();
		cedariron.addMaterial(0.9f, (Material) defaultMaterials.get("cedar"));
		cedariron.addMaterial(0.1f, (Material) defaultMaterials.get("iron"));
		cedariron.setName("iron tipped cedar");
		defaultMaterials.put("0.9:cedar+0.1:iron", cedariron);
		
		CompositeMaterial cedarsteel = new CompositeMaterial();
		cedarsteel.addMaterial(0.9f, (Material) defaultMaterials.get("cedar"));
		cedarsteel.addMaterial(0.1f, (Material) defaultMaterials.get("steel"));
		cedarsteel.setName("steel tipped cedar");
		defaultMaterials.put("0.9:cedar+0.1:steel", cedarsteel);
		
		CompositeMaterial ashiron = new CompositeMaterial();
		ashiron.addMaterial(0.9f, (Material) defaultMaterials.get("ash"));
		ashiron.addMaterial(0.1f, (Material) defaultMaterials.get("iron"));
		ashiron.setName("iron tipped ash");
		defaultMaterials.put("0.9:ash+0.1:iron", ashiron);
		
		CompositeMaterial ashsteel = new CompositeMaterial();
		ashsteel.addMaterial(0.9f, (Material) defaultMaterials.get("ash"));
		ashsteel.addMaterial(0.1f, (Material) defaultMaterials.get("steel"));
		ashsteel.setName("steel tipped ash");
		defaultMaterials.put("0.9:ash+0.1:steel", ashsteel);
		
		CompositeMaterial cedariron2 = new CompositeMaterial();
		cedariron2.addMaterial(0.9f, (Material) defaultMaterials.get("cedar"));
		cedariron2.addMaterial(0.1f, (Material) defaultMaterials.get("iron"));
		cedariron2.setName("iron headed cedar");
		defaultMaterials.put("0.8:cedar+0.2:iron", cedariron2);
		
		CompositeMaterial cedarsteel2 = new CompositeMaterial();
		cedarsteel2.addMaterial(0.9f, (Material) defaultMaterials.get("cedar"));
		cedarsteel2.addMaterial(0.1f, (Material) defaultMaterials.get("steel"));
		cedarsteel2.setName("steel headed cedar");
		defaultMaterials.put("0.8:cedar+0.2:steel", cedarsteel2);
		
		CompositeMaterial ashiron2 = new CompositeMaterial();
		ashiron2.addMaterial(0.9f, (Material) defaultMaterials.get("ash"));
		ashiron2.addMaterial(0.1f, (Material) defaultMaterials.get("iron"));
		ashiron2.setName("iron headed ash");
		defaultMaterials.put("0.8:ash+0.2:iron", ashiron2);
		
		CompositeMaterial ashsteel2 = new CompositeMaterial();
		ashsteel2.addMaterial(0.9f, (Material) defaultMaterials.get("ash"));
		ashsteel2.addMaterial(0.1f, (Material) defaultMaterials.get("steel"));
		ashsteel2.setName("steel headed ash");
		defaultMaterials.put("0.8:ash+0.2:steel", ashsteel2);*/
		
	}
	
	private static Material buildComposite(String description) {
		Matcher m = MATERIALDESC_REGEX.matcher(description);
		if(!m.matches())
			return null;
		
		CompositeMaterial cm = new CompositeMaterial();
		cm.addMaterial(Float.parseFloat(m.group(2)), (Material) defaultMaterials.get(m.group(3)));
		cm.addMaterial(Float.parseFloat(m.group(4)), (Material) defaultMaterials.get(m.group(5)));
		cm.setName(m.group(3)+" "+m.group(1)+" "+m.group(5));
		return cm;
	}
	
	
	
	
	/*
	public static void main(String [] arg) {	
		initializeDefaultMaterials();
		Material mat = buildComposite("headed+0.2:platinum+0.8:ash");
		
		System.out.println(mat.getName());
		System.out.println(mat.getWeight());
		System.out.println(mat.getDurability());
		System.out.println(mat.getValue());
	}
		
		Iterator it = defaultMaterials.values().iterator();
		int [] effects = new int [10];
		String [] mats = new String [] {"", "", "", "", "", "", "", "", "", ""}; 
		while(it.hasNext()) {
			DefaultMaterialImpl mat = (DefaultMaterialImpl) it.next();
			effects[(int)(mat.value)%10]++;
			mats[(int)mat.value%10] = mats[(int)mat.value%10] + mat.name + ",";
		}
		for(int i=0;i<effects.length;i++) {
			System.out.println(i+":"+effects[i]+"..."+mats[i]);
		}
	}*/

}
