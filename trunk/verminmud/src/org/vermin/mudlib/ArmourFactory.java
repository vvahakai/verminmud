package org.vermin.mudlib;

import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import org.vermin.driver.Factory;
import org.vermin.mudlib.Damage.Type;

public class ArmourFactory implements Factory
{
    public static final int SIZE_VERY_SMALL = 0;
	public static final int SIZE_SMALL = 1;
	public static final int SIZE_MEDIUM = 2;
	public static final int SIZE_LARGE = 3;
	public static final int SIZE_VERY_LARGE = 4;

	private static final int PRE_POSITION = 0;
	private static final int POST_POSITION = 1;

    /* (non-Javadoc)
     * @see org.vermin.driver.Factory#create(java.lang.Object[])
     */
    public Object create(Object ... args) throws IllegalArgumentException {
    	String material = args[0].toString();
    	String slot = args[1].toString();
    	String craftmanship = args[2].toString();
    	String sizescale = args[3].toString();
    	
    	try {
    		return create(material.equals("") ? null : material.split(","),
    				slot.equals("") ? null : slot.split(","),
    						Integer.parseInt(craftmanship),
    						Integer.parseInt(sizescale));
    	} catch(NumberFormatException nfe) {
    		throw new IllegalArgumentException(nfe);
    	}
    }
    
	public static class Description
	{
		public String[] materials = null;		// preferred materials for this armour. 
												// note, you may not get an armour of preferred material if there is no armour subtype with that material for desired slot
		public String[] slot = null;			// slot(s) for this armour (you will always get a armour for this slot if such exists)
		public int craftmanship = 100;			// craftmanship of this armour. might affect something.
		public int sizeScale = SIZE_MEDIUM;
	}

	private static class ArmourSubType
	{
		public String name = "";
		public int armourclass = 0;
		// size in cubic cm
		public int size = 0;
		public String longDesc = "";
		public String[] slots = { Slot.TORSO };
		// default available construction methods for this armoursubtype (used when generating random methods)
		public ArmourMethod[] method = new ArmourMethod[] { new ArmourMethod("crude", null, -50, PRE_POSITION, null) };

		public ArmourSubType(String name, int armourclass, int size, String longDesc, String[] slots, ArmourFactory.ArmourMethod[] method)
		{
			this.name = name;
			this.armourclass = armourclass;
			this.size = size;
			this.longDesc = longDesc;
			this.slots = slots;
			if(method != null)
				this.method = method;
		}	
	}

	private static class ArmourMethod 
	{
		public String name = "";
		// affects protection (-100 - 100) percent
		public int protection = 0;
		public int methodPosition = PRE_POSITION;
		// public int[] damageTypeModifiers = new int[] { 0, 0, 0, 0 };
		public EnumMap<Damage.Type, Integer> damageTypeModifiers = new EnumMap<Type, Integer>(Damage.Type.class);
		public String[] materials = { "iron" , "steel" , "silver" , "bronze" , "copper" , "aluminium" , 
	"brass" , "lead" , "nickel" , "platinum" , "tin" , "titanium" , "tungsten" , "zinc" , "agate" , 
	"feldspar" , "opal" , "quartz" , "topaz", "leather", "wax", "cork", "rubber", "glass", "ice", "paper", "cloth" };

		public ArmourMethod(String name, String[] materials, int protection, int methodPosition, int[] damageTypeModifiers)
		{
			this.name = name;
			this.protection = protection;
			this.methodPosition = methodPosition;
			if(materials != null)
				this.materials = materials;
			if(damageTypeModifiers != null) { 
				this.damageTypeModifiers.put(Damage.Type.CRUSHING, damageTypeModifiers[0]);
				this.damageTypeModifiers.put(Damage.Type.PIERCING, damageTypeModifiers[1]);
				this.damageTypeModifiers.put(Damage.Type.CHOPPING, damageTypeModifiers[2]);
				this.damageTypeModifiers.put(Damage.Type.SLASHING, damageTypeModifiers[3]);				
			}
		}
	}
	
	String[] metallicMaterials = { "iron" , "steel" , "gold" , "silver" , "bronze" , "copper" , "aluminium" , 
	"brass" , "lead" , "nickel" , "platinum" , "tin" , "titanium" , "tungsten" , "zinc" , "agate" , "diamond" , 
	"feldspar" , "opal" , "quartz" , "topaz", "glass"};
																														//crush,	pierce, chop, slash
	ArmourMethod chainmail = new ArmourMethod("chainmail", metallicMaterials, 75, POST_POSITION,				new int[] { -20,	0,		20,		20 });
	ArmourMethod ringmail = new ArmourMethod("ringmail", metallicMaterials, 60, POST_POSITION,					new int[] {   0,	10,		 5,		10 });
	ArmourMethod scalemail = new ArmourMethod("scalemail", metallicMaterials, 80, POST_POSITION,				new int[] {	  0,	10,		 0,		 0 });
	ArmourMethod platemail = new ArmourMethod("platemail", metallicMaterials, 95, POST_POSITION,				new int[] {	  0,	20,		15,		30 });
	ArmourMethod brigandine = new ArmourMethod("brigandine", metallicMaterials, 55, POST_POSITION,				new int[] {	 20,	10,		 0,		 0 });
	ArmourMethod lamellar = new ArmourMethod("lamellar", metallicMaterials, 50, POST_POSITION,					new int[] {	 10,	 0,		15,		20 });
	ArmourMethod finemesh = new ArmourMethod("fine-mesh", metallicMaterials, 65, POST_POSITION,					new int[] { -10,	30,		 0,		10 });
	ArmourMethod cuirbouilli = new ArmourMethod("cuirbouilli", new String[] { "leather" }, 50, PRE_POSITION,	new int[] {   0,	 0,		 5,		10 });
	ArmourMethod studded = new ArmourMethod("studded", new String[] { "leather" }, 40, PRE_POSITION,			new int[] {   0,	10,		 0,		10 });
	ArmourMethod bezainted = new ArmourMethod("bezainted", new String[] { "leather" }, 30, PRE_POSITION,		new int[] {  10,	10,		 0,		 5 });
	ArmourMethod soft = new ArmourMethod("soft", new String[] { "leather" }, 20, PRE_POSITION,					new int[] {   0,   -20,		 0,		 0 });
	ArmourMethod woven = new ArmourMethod("woven", new String[] { "cloth" }, 10, PRE_POSITION,					new int[] {   0,   -10,		 0,		 0 });
	ArmourMethod crude = new ArmourMethod("crude", null, 0, PRE_POSITION,										new int[] {   0,	 0,		 0,		 0 });

	public ArmourMethod[] softMethods = new ArmourMethod[] {cuirbouilli, studded, soft, bezainted, woven, crude};
	public ArmourMethod[] hardMethods = new ArmourMethod[] {chainmail, ringmail, scalemail, platemail, brigandine, lamellar, finemesh, crude};
	public ArmourMethod[] allMethods = new ArmourMethod[] {chainmail, ringmail, scalemail, platemail, brigandine, lamellar, finemesh, cuirbouilli, studded, soft, bezainted, woven, crude};

	Hashtable<String[], ArmourSubType[]> slotTypes = new Hashtable<String[], ArmourSubType[]>();

	public String[][] slotChoices = new String[][] {};

	String vokaalit = "aeyoui";

	protected final static ArmourFactory _instance = new ArmourFactory();	

	protected ArmourFactory() {											
						// Slots								// Types				   //name		  //ac //size //desc, //slots					//materials
		slotTypes.put(new String[] { Slot.TORSO },	new ArmourSubType[] { new ArmourSubType("vest",			50, 400, "", new String[] { Slot.TORSO }, softMethods),
  																		  new ArmourSubType("breastplate",	70, 300, "", new String[] { Slot.TORSO }, hardMethods),
																		  new ArmourSubType("bra",			25, 100, "", new String[] { Slot.TORSO }, allMethods),
																		  new ArmourSubType("brigandine",	75, 500, "", new String[] { Slot.TORSO }, hardMethods) });

		slotTypes.put(new String[] { Slot.HEAD },	new ArmourSubType[] { new ArmourSubType("cap",			35,  70, "", new String[] { Slot.HEAD }, allMethods),
  																		  new ArmourSubType("full helm",	75, 100, "", new String[] { Slot.HEAD }, hardMethods),
																		  new ArmourSubType("helmet",		60,  80, "", new String[] { Slot.HEAD }, hardMethods),
																		  new ArmourSubType("mask",			40,  50, "", new String[] { Slot.HEAD }, hardMethods),
  																		  new ArmourSubType("bascinet",		50,  90, "", new String[] { Slot.HEAD }, hardMethods),
																		  new ArmourSubType("great helm",	80, 120, "", new String[] { Slot.HEAD }, hardMethods),
																		  new ArmourSubType("hat",			20,  60, "", new String[] { Slot.HEAD }, softMethods) });
	

		slotTypes.put(new String[] { Slot.NECK },	new ArmourSubType[] { new ArmourSubType("neck guard",	65,  25, "", new String[] { Slot.NECK }, hardMethods),
  																		  new ArmourSubType("collar",		30,  15, "", new String[] { Slot.NECK }, allMethods),
																		  new ArmourSubType("gorget",		50,  20, "", new String[] { Slot.NECK }, hardMethods) });

		slotTypes.put(new String[] { Slot.CLOAK },	new ArmourSubType[] { new ArmourSubType("cape",			65,  30, "", new String[] { Slot.CLOAK }, softMethods),
																		  new ArmourSubType("cloak",		50,  20, "", new String[] { Slot.CLOAK }, softMethods) });

		slotTypes.put(new String[] { Slot.ARM },	new ArmourSubType[] { new ArmourSubType("bracer",		25,  30, "", new String[] { Slot.ARM }, hardMethods),
																		  new ArmourSubType("gardebras",	50,  50, "", new String[] { Slot.ARM }, hardMethods),
																		  new ArmourSubType("pauldron",		75, 100, "", new String[] { Slot.ARM }, hardMethods),
  																		  new ArmourSubType("rerebrace",	60,  80, "", new String[] { Slot.ARM }, hardMethods),
																		  new ArmourSubType("vambrace",		55,  60, "", new String[] { Slot.ARM }, hardMethods),
																		  new ArmourSubType("sleeve",		40, 100, "", new String[] { Slot.ARM }, softMethods) });

		slotTypes.put(new String[] { Slot.WRIST },	new ArmourSubType[] { new ArmourSubType("bracelet",		15,  10, "", new String[] { Slot.WRIST }, hardMethods) });		
		
		slotTypes.put(new String[] { Slot.HAND },	new ArmourSubType[] { new ArmourSubType("gauntlet",		65,  20, "", new String[] { Slot.CLOAK }, hardMethods),
																		  new ArmourSubType("glove",		40,  17, "", new String[] { Slot.CLOAK }, softMethods) });

		slotTypes.put(new String[] { Slot.BELT },	new ArmourSubType[] { new ArmourSubType("belt",			40,  10, "", new String[] { Slot.BELT }, softMethods),
																		  new ArmourSubType("sash",			50,  15, "", new String[] { Slot.BELT }, softMethods) });

		slotTypes.put(new String[] { Slot.FOOT },	new ArmourSubType[] { new ArmourSubType("boot",			50, 30, "", new String[] { Slot.FOOT }, allMethods),
  																		  new ArmourSubType("shoe",			35, 20, "", new String[] { Slot.FOOT }, softMethods),
																		  new ArmourSubType("sabaton",		65, 20, "", new String[] { Slot.FOOT }, hardMethods),
																		  new ArmourSubType("sandal",		20, 10, "", new String[] { Slot.FOOT }, softMethods) });		

		slotTypes.put(new String[] { Slot.LEG },	new ArmourSubType[] { new ArmourSubType("shin guard",	45, 150, "", new String[] { Slot.LEG }, hardMethods),
  																		  new ArmourSubType("cuisse",		55, 150, "", new String[] { Slot.LEG }, hardMethods),
																		  new ArmourSubType("greave",		65, 150, "", new String[] { Slot.LEG }, softMethods),
																		  new ArmourSubType("poleyn",		70, 150, "", new String[] { Slot.LEG }, hardMethods) });

		slotTypes.put(new String[] { Slot.TAIL },	new ArmourSubType[] { new ArmourSubType("tail guard",	40,  10, "", new String[] { Slot.TAIL }, allMethods) });

		slotTypes.put(new String[] { Slot.FINGER },	new ArmourSubType[] { new ArmourSubType("ring",		    1,  1, "", new String[] { Slot.FINGER }, hardMethods) });		
		
		slotTypes.put(new String[] { Slot.FOOT, Slot.FOOT }, new ArmourSubType[] { new ArmourSubType("boots",	50,  60, "", new String[] { Slot.FOOT, Slot.FOOT }, allMethods),
  																		  new ArmourSubType("shoes",			35,  40, "", new String[] { Slot.FOOT, Slot.FOOT }, softMethods),
																		  new ArmourSubType("sabatons",			65,  40, "", new String[] { Slot.FOOT, Slot.FOOT }, hardMethods),
																		  new ArmourSubType("sandals",			20,  20, "", new String[] { Slot.FOOT, Slot.FOOT }, softMethods) });

		slotTypes.put(new String[] { Slot.LEG, Slot.LEG }, new ArmourSubType[] { new ArmourSubType("greaves",	65, 300, "", new String[] { Slot.LEG, Slot.LEG }, softMethods),
																		  new ArmourSubType("pants",			50, 400, "", new String[] { Slot.LEG, Slot.LEG }, softMethods),
																		  new ArmourSubType("leggings",			40, 250, "", new String[] { Slot.LEG, Slot.LEG }, allMethods),
																		  new ArmourSubType("skirt",			35, 300, "", new String[] { Slot.LEG, Slot.LEG }, softMethods) });

		slotTypes.put(new String[] { Slot.TORSO, Slot.LEG, Slot.LEG }, new ArmourSubType[] { new ArmourSubType("cuirass",	55, 800, "", new String[] { Slot.TORSO, Slot.LEG, Slot.LEG }, hardMethods) });

		slotTypes.put(new String[] { Slot.HEAD, Slot.NECK }, new ArmourSubType[] { new ArmourSubType("coif",	50, 100, "", new String[] { Slot.HEAD, Slot.NECK }, hardMethods) });

		slotTypes.put(new String[] { Slot.ARM, Slot.ARM }, new ArmourSubType[] { new ArmourSubType("bracers",			50,  60, "", new String[] { Slot.ARM, Slot.ARM }, hardMethods),
																		  new ArmourSubType("sleeves",			40, 200, "", new String[] { Slot.ARM, Slot.ARM }, softMethods) });

		slotTypes.put(new String[] { Slot.WRIST, Slot.WRIST }, new ArmourSubType[] { new ArmourSubType("bracelets", 65,  20, "", new String[] { Slot.WRIST, Slot.WRIST }, hardMethods) });

		slotTypes.put(new String[] { Slot.HAND, Slot.HAND }, new ArmourSubType[] { new ArmourSubType("gauntlets", 65,  40, "", new String[] { Slot.HAND, Slot.HAND }, hardMethods),
																		  new ArmourSubType("gloves",			  40,  34, "", new String[] { Slot.HAND, Slot.HAND }, softMethods) });	

		slotTypes.put(new String[] { Slot.TORSO, Slot.ARM , Slot.ARM }, new ArmourSubType[] { 
																new ArmourSubType("haubergeon",	60,  700, "", new String[] { Slot.TORSO, Slot.ARM , Slot.ARM }, hardMethods),
																new ArmourSubType("shirt",		40,  650, "", new String[] { Slot.TORSO, Slot.ARM , Slot.ARM }, softMethods),
																new ArmourSubType("coat",		50,  700, "", new String[] { Slot.TORSO, Slot.ARM , Slot.ARM }, softMethods) });

		slotTypes.put(new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM  }, new ArmourSubType[] { 
																new ArmourSubType("hauberk",	60,  1000, "", new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM  }, hardMethods),
																new ArmourSubType("tunic",		50,  1000, "", new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM  }, softMethods) });

		slotTypes.put(new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM, Slot.NECK }, new ArmourSubType[] { 
																new ArmourSubType("gown",		45,  1100, "", new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM, Slot.HEAD  }, softMethods) });

		slotTypes.put(new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM, Slot.NECK, Slot.HEAD }, new ArmourSubType[] { 
																new ArmourSubType("robe",		60,  1100, "", new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM, Slot.HEAD  }, softMethods) });


		slotChoices = new String[][] { new String[] { Slot.TORSO }, new String[] { Slot.HEAD }, new String[] { Slot.NECK }, 
		new String[] { Slot.CLOAK }, new String[] { Slot.ARM }, new String[] { Slot.WRIST }, new String[] { Slot.HAND }, new String[] { Slot.BELT }, 
		new String[] { Slot.FOOT }, new String[] { Slot.LEG }, new String[] { Slot.TAIL }, new String[] { Slot.FINGER }, new String[] { Slot.FOOT, Slot.FOOT },
		new String[] { Slot.LEG, Slot.LEG }, new String[] { Slot.TORSO, Slot.LEG, Slot.LEG }, new String[] { Slot.HEAD, Slot.NECK },
		new String[] { Slot.ARM, Slot.ARM  }, new String[] { Slot.WRIST, Slot.WRIST  }, new String[] { Slot.HAND, Slot.HAND }, new String[] { Slot.TORSO, Slot.ARM, Slot.ARM  },
		new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM , Slot.ARM  }, new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM, Slot.ARM, Slot.NECK }, 
		new String[] { Slot.TORSO, Slot.LEG, Slot.LEG, Slot.ARM, Slot.ARM, Slot.NECK, Slot.HEAD } };

		
	}

	public static ArmourFactory getInstance() { return _instance; }

	public Item create(Description desc)
	{
		return create(desc.materials, desc.slot, desc.craftmanship, desc.sizeScale);
	}

	public static void main(String args[]) {
		ArmourFactory af = ArmourFactory.getInstance();
		af.create(null,null,100,2);
	}

	public Item create(String[] materials, String[] slot, int craftmanship, int sizeScale) {
		Random dice = new Random();
		int dicenum;
		ArmourSubType type;
		ArmourSubType[] types = null;
		String[] slots = null;
		Vector<String> possibleMaterials = new Vector<String>();
		String shortDesc;
		String sizeDesc = "";
		String shortDescAlias;
		String shortDescPrefix;
		StringBuffer longDesc = new StringBuffer();
		int armourValue;
		Material mat;
		int size = 0;

		// randomize slot
		if(slot == null) {
			dicenum = dice.nextInt(slotChoices.length);
			slot = new String[slotChoices[dicenum].length];
			for(int i=0; i<slot.length; i++)
				slot[i] = slotChoices[dicenum][i];
		}
			
		// find subtypes for slot
		Enumeration<String[]> e = slotTypes.keys();
		while(e.hasMoreElements()) {
			int counter = 0;
			String[] temptype = e.nextElement();
			boolean[] usedTypes = new boolean[temptype.length];
			if(slot.length != temptype.length) {
				continue;
			}
			for(int i=0;i<slot.length;i++) {
				for(int j=0;j<temptype.length;j++) {
					if(temptype[j].equals(slot[i]) && !usedTypes[j]) {
						usedTypes[j] = true;
						counter++;
					}
				}
			}
			if(counter == slot.length) {
				slots = temptype;
				types = slotTypes.get(temptype);
				break;
			}
		}
		
		if(types == null || types.length  == 0 || slots == null)
		{
			return null;
		}

		//randomize subtype
		dicenum = dice.nextInt(types.length);
		type = types[dicenum];	

		//randomize construction method
		dicenum = dice.nextInt(type.method.length);
		ArmourMethod method = type.method[dicenum];	


		//find possible materials from intersection of preferred materials and materials for this type
		if(materials != null) {
			for(int i=0;i < materials.length;i++) {
				for(int j=0;j < method.materials.length; j++) {
					if(materials[i].equals(method.materials[j])) {
						possibleMaterials.add(materials[i]);
					}
				}
			}
		}
		if(materials == null || possibleMaterials.size() == 0) {
			for(int i=0;i < method.materials.length; i++) {
				possibleMaterials.add(method.materials[i]);
			}
		}

		if(possibleMaterials.size() == 0) {
			World.log("possibleMaterials was empty, using iron.");
			possibleMaterials.add("iron");
		}
			
		//randomize material from possible materials
		dicenum = dice.nextInt(possibleMaterials.size());
		mat = MaterialFactory.createMaterial((possibleMaterials.get(dicenum)));	
	
		// luodaan descriptionit
		if(vokaalit.indexOf(mat.getName().charAt(0)) != -1) {
			shortDescPrefix = "an ";
		} else {
			shortDescPrefix = "a ";
		}
		
		size = type.size;
		
		if(sizeScale == SIZE_VERY_SMALL) {
			sizeDesc = "very small ";
			size = (int) (size * 0.33);
		}
		else if(sizeScale == SIZE_SMALL) {
			sizeDesc = "small ";
			size = (int) (size * 0.66);
		}
		else if(sizeScale == SIZE_LARGE) {
			sizeDesc = "large ";
			size = (int) (size * 1.5);
		}
		else if(sizeScale == SIZE_VERY_LARGE) {
			sizeDesc = "very large ";
			size = size * 3;
		}

		if(method.methodPosition == POST_POSITION)
		{
			shortDesc = shortDescPrefix + sizeDesc + mat.getName() +" "+ method.name +" "+ type.name;
			shortDescAlias = mat.getName()+" "+ method.name +" "+type.name;
		}
		else
		{
			shortDesc = shortDescPrefix + sizeDesc + method.name +" "+ mat.getName() +" "+ type.name;
			shortDescAlias = method.name+" "+ mat.getName() +" "+type.name;
		}


		longDesc.append("This "+method.name+" "+type.name+" is made of "+mat.getName()+". It will take up slots: ");
		for(int i=0;i<slots.length;i++)
		{
			if(i != 0)
				longDesc.append(", ");
			longDesc.append(slots[i]);
		}
		longDesc.append(".");

		//longDesc.append(" It weights about "+ (size*mat.weight)/1000 +" kg.");
		
		armourValue = (craftmanship + mat.getDurability() + type.armourclass + method.protection) / 4;

		World.log(shortDesc);

		FactoryArmour armour = new FactoryArmour();
		armour.setDescription(shortDesc);
		armour.setLongDescription(longDesc.toString());
		armour.setName(type.name);
		armour.addAlias(shortDesc);
		armour.addAlias(shortDescAlias);
		armour.addAlias(method.name);
		armour.setMaterial(mat.getName());
		armour.setSlots(slots);
		armour.setSizeScale(sizeScale);
		armour.setArmourValue(armourValue);
		armour.setSize(size);
		armour.addDp((mat.getDurability()*size/10)-1);
		armour.setMaxDp(mat.getDurability()*size/10);
		armour.setDamageTypeModifiers(method.damageTypeModifiers);

		return armour;
	}	
}
