package org.vermin.mudlib;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vermin.driver.Factory;

public class WeaponFactory implements Factory
{
	
    /* (non-Javadoc)
     * @see org.vermin.driver.Factory#create(java.lang.Object[])
     */
    public Object create(Object ... args) throws IllegalArgumentException {
    	String material = args[0].toString();
    	String type = args[1].toString();
    	String craftmanship = args[2].toString();
    	
    	String [] typeNames = type.split(",");
    	WeaponType [] types = new WeaponType[typeNames.length];
    	for(int i=0;i<typeNames.length;i++) {
    		types[i] = getWeaponType(typeNames[i]);
    	}
    	
    	try {
    		return create(material.equals("") ? null : material.split(","),
    						types,
    						Integer.parseInt(craftmanship));
    	} catch(NumberFormatException nfe) {
    		throw new IllegalArgumentException(nfe);
    	}
    }

    public WeaponType getWeaponType(String name) {
    	for(WeaponType type : allWeaponTypes) {
    		if(type.getWeaponTypeName().equals(name)) {
    			return type;
    		}
    	}
    	return WeaponType.NONE;
    }
    
	private static final Random dice = new Random();

	public static class Description
	{
		public String[] materials = null;
		public WeaponType[] weapontypes = null;
		public int craftmanship = 100;
	}

	private static class WeaponSubType
	{
		public String name = "";
		public int damage = 0;
		// size in cubic cm
		public int size = 0;
		public float speed = 1.0f;
		public float aerodynamicity;
		public String longDesc = "";
		public Damage.Type[] damageType = new Damage.Type []{ Damage.Type.PHYSICAL };
		// default available materials for this weaponsubtype (used when generating random material)
		public String[] materials = { "iron" , "steel", "iron", "steel" , "gold" , "silver" , "bronze" , "copper" , "aluminium" , 
	"brass" , "lead" , "nickel" , "platinum" , "tin" , "titanium" , "tungsten" , "zinc" , "agate" , "diamond" , 
	"feldspar" , "garnet" , "granite" , "opal" , "quartz" , "topaz", "balsa", "glass", "ice", "rubber" };

		public WeaponSubType(String name, int damage, int size, float speed, float aerodynamicity, String longDesc, Damage.Type[] damageType, String[] materials)
		{
			this.name = name;
			this.damage = damage;
			this.size = size;
			this.speed = speed;
			this.aerodynamicity = aerodynamicity;	//the bigger the better
			this.longDesc = longDesc;
			this.damageType = damageType;
			if(materials != null)
				this.materials = materials;
		}	
	}
	
	private static String [] axeMaterials = new String [] { "headed+0.2:steel+0.8:cedar" };
	private static String [] maceMaterial = axeMaterials;
	private static String [] poleMaterials = axeMaterials;
	private static String [] spearMaterials = new String [] { "tipped+0.1:steel+0.9:cedar" };
	
	private static Damage.Type[] crush = new Damage.Type[]  { Damage.Type.CRUSHING };
	private static Damage.Type[] slash = new Damage.Type[]  { Damage.Type.SLASHING };
	private static Damage.Type[] pierce = new Damage.Type[]  { Damage.Type.PIERCING };
	private static Damage.Type[] chop = new Damage.Type[]  { Damage.Type.CHOPPING };
	
	private static Damage.Type[] crushstun = new Damage.Type[]  { Damage.Type.CRUSHING, Damage.Type.STUN };
	private static Damage.Type[] slashpierce = new Damage.Type[]  { Damage.Type.SLASHING, Damage.Type.PIERCING };
	private static Damage.Type[] slashchop = new Damage.Type[]  { Damage.Type.SLASHING, Damage.Type.CHOPPING };
	
	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] noTypes = { 
		new WeaponSubType("shovel",			40,3500,1.0f, 0.1f, "", crushstun, null),
		new WeaponSubType("table",			17,10000,0.5f, 0.001f, "", crushstun, new String[] { "ash", "balsa", "cedar", "mahogany" }) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] swords = { 
		new WeaponSubType("dadao", 			25, 960, 1.6f, 0.2f, "", slashpierce, null),
		new WeaponSubType("short sword", 	28, 1000, 1.6f, 0.3f, "", slashpierce, null),
		new WeaponSubType("gladius", 		35, 850, 1.9f, 0.28f, "", pierce, null),
		new WeaponSubType("cutlass", 		37, 1100, 1.5f, 0.27f, "", slash, null),
		new WeaponSubType("scimitar", 		41, 1050, 1.6f, 0.32f, "", slash, null),
		new WeaponSubType("backsword", 		45, 1090, 1.4f, 0.25f, "", slash, null),
		new WeaponSubType("rapier", 		50, 820, 2.0f, 0.15f, "", pierce, null),
		new WeaponSubType("foil", 			52, 900, 2.0f, 0.15f, "", slashpierce, null),
		new WeaponSubType("falchion", 		58, 1120, 1.4f, 0.2f, "", slash, null),
		new WeaponSubType("sabre", 			63, 1110, 1.9f, 0.32f, "", slash, null),
		new WeaponSubType("bastard sword", 	69, 1180, 1.3f, 0.13f, "", slashpierce, null),
		new WeaponSubType("katana", 		75, 1030, 1.6f, 0.15f, "", slash, null),
		new WeaponSubType("long sword", 	76, 1170, 1.2f, 0.11f, "", slashpierce, null),
		new WeaponSubType("claymore", 		85, 1230, 1.1f, 0.07f, "", slashpierce, null),
		new WeaponSubType("no-dachi", 		88, 1350, 1.0f, 0.08f, "", slash, null),
		new WeaponSubType("flamberge", 		93, 1410, 1.0f, 0.09f, "", slashpierce, null) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] daggers = { 
		new WeaponSubType("knife", 			20, 200, 2.0f, 0.4f, "This is a normal knife that can also be used for eating.", slash, null),
		new WeaponSubType("throwing knife", 20, 200, 2.0f, 0.8f, "This is a throwing knife that is specially balanced for throwing. It would not serve well as a eating utensil.", pierce, null),
		new WeaponSubType("main-gauche", 	25, 350, 2.0f, 0.42f, "Main-gauche is dagger that is mainly used for parrying but can also be used to attack.", pierce, null),
		new WeaponSubType("sai", 			30, 400, 1.8f, 0.42f, "", pierce, null),
		new WeaponSubType("dagger", 		45, 250, 2.0f, 0.50f, "", slashpierce, null),
		new WeaponSubType("dirk", 			50, 350, 1.9f, 0.43f, "", slashpierce, null),
		new WeaponSubType("poignard", 		55, 352, 1.8f, 0.39f, "", slashpierce, null),
		new WeaponSubType("basilard", 		65, 344, 1.9f, 0.38f, "", slashpierce, null),
		new WeaponSubType("sax", 			75, 390, 1.7f, 0.43f, "", slashpierce, null),
		new WeaponSubType("wakizashi", 		80, 410, 1.7f, 0.34f, "", slash, null) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] axes = { 
		new WeaponSubType("hand axe", 		32, 1150, 1.7f, 0.33f, "This axe is of the variety that is used for cutting wood. It can also serve as a weapon.", chop, axeMaterials),
		new WeaponSubType("pick", 			36, 1340, 1.2f, 0.23f, "", pierce, axeMaterials),
		new WeaponSubType("axe", 			38, 1290, 1.4f, 0.27f, "", chop, axeMaterials),
		new WeaponSubType("tomahawk", 		45, 1200, 1.5f, 0.68f, "", chop, axeMaterials),
		new WeaponSubType("horseman's axe", 54, 1340, 1.5f, 0.30f, "", chop, axeMaterials),
		new WeaponSubType("broad axe", 		65, 1540, 1.4f, 0.27f, "", chop, axeMaterials),
		new WeaponSubType("battle axe", 	71, 1620, 1.2f, 0.34f, "", chop, axeMaterials),
		new WeaponSubType("bearded axe", 	81, 1640, 1.2f, 0.26f, "", chop, axeMaterials),
		new WeaponSubType("great axe", 		88, 1732, 1.0f, 0.28f, "Great axes are long shafted axes with huge blades that can cut through armour. It is usually wielded in two hands but stronger invidual could use it one handed.", chop, axeMaterials), };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] bludgeons = { 
		new WeaponSubType("club", 			40, 1200, 1.0f, 0.34f, "Club is one of the simplest weapon one can imagine and it's efficiency relies heavily on the physical prowess of it's wielder.",new Damage.Type[] { Damage.Type.CRUSHING, Damage.Type.STUN }, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony" }),
		new WeaponSubType("hammer", 		55, 1230, 1.0f, 0.42f, "Hammer is an everyday tool that can also be used efficiently in combat.",crushstun, null),
		new WeaponSubType("staff", 			66, 1540, 1.0f, 0.21f, "Staffs can be used as weapons in addition to their everyday use as walking sticks.",crushstun, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony", "iron", "steel", "titanium", "diamond" }),
		new WeaponSubType("spiked club", 	70, 1230, 1.0f, 0.27f, "", new Damage.Type[] { Damage.Type.CRUSHING, Damage.Type.PIERCING, Damage.Type.STUN }, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony" }),
		new WeaponSubType("mace", 			77, 1320, 1.0f, 0.36f, "Mace is a refined version of a standard club.", crushstun, maceMaterial),
		new WeaponSubType("maul", 			85, 3000, 1.0f, 0.12f, "", crush, maceMaterial),
		new WeaponSubType("flanged mace", 	96, 1240, 1.0f, 0.37f, "Flanged mace is a improved version of a normal mace. It features a metal head designed to penetrate armour.",crushstun, maceMaterial),
		new WeaponSubType("quarterstaff", 	120, 2200, 1.0f, 0.18f, "Quarterstaff is a staff weapon that consists of a normally wooden shaft that is about 2 to 3 metres long.",crushstun, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony", "iron", "steel", "titanium", "diamond" }),
		new WeaponSubType("warhammer", 		134, 1700, 1.0f, 0.38f, "Warhammer is an hammer like weapon that is designed for warfare. It has a metal head with a hammer head and a spike.",crushstun, maceMaterial) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] spears = { 
		new WeaponSubType("javelin", 		33, 1750, 1.0f, 0.89f, "Javelin is light spear usually used as a missile weapon but it can also serve as a close combat weapon when needed.", pierce, spearMaterials),
		new WeaponSubType("pilum", 			42, 1840, 1.0f, 0.72f, "Pilum is designed to be an efficient throwing spear. When thrown it will stick to whatever it hits.", pierce, spearMaterials),
		new WeaponSubType("spear", 			45, 1900, 1.0f, 0.45f, "Spears are basically long sticks with a sharp pointy end.", pierce, spearMaterials),
		new WeaponSubType("lance", 			84, 2200, 1.0f, 0.23f, "Lance is a spear refined for use by cavalry.", pierce, spearMaterials),
		new WeaponSubType("pike", 			92, 2140, 1.0f, 0.30f, "Pike is a long spear designed primarily for use against cavalry in large infantry formations.", pierce, spearMaterials) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] polearms = { 
		new WeaponSubType("pitch fork", 	28, 1800, 1.0f, 0.43f, "Pitchfork is the weapon of choice for all the peasant mobs out to stake the local vampire count.", crush, spearMaterials),
		new WeaponSubType("scythe", 		43, 1950, 1.0f, 0.23f, "Scythe is an aggricultural tool used for cutting hay but it is often seen used as a weapon, particulary in the hands of a peasant mob.", slash, spearMaterials),
		new WeaponSubType("gisarme", 		53, 1960, 1.0f, 0.34f, "", slashchop, poleMaterials),
		new WeaponSubType("glaive", 		66, 1945, 1.0f, 0.32f, "", slash, poleMaterials),
		new WeaponSubType("pollaxe", 		71, 2012, 1.0f, 0.25f, "",crush, poleMaterials),
		new WeaponSubType("halberd", 		75, 2043, 1.0f, 0.18f, "Halberd is the defacto standard infantry man's weapon. It is efficient against all enemies.", chop, poleMaterials),
		new WeaponSubType("partisan", 		79, 2103, 1.0f, 0.19f, "", slashchop, poleMaterials) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] flails = { 
		new WeaponSubType("flail", 			75, 1320, 1.0f, 0.11f, "Flail is a weapon developed from an agricultural tool. It consists of a heavy object attached to a shaft by a chain.",crushstun, null),
		new WeaponSubType("morning star", 	122, 1200, 1.0f, 0.21f, "Morning star is a flail like weapon with a three chains connected to a shaft.",crushstun, null),
		new WeaponSubType("spiked flail", 	136, 1250, 1.0f, 0.10f, "Spiked flail is flail with multiple spikes at the business end.",crushstun, null) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] whips = { 
		new WeaponSubType("whip", 			33, 680, 1.0f, 0.1f, "", slash, new String[] { "leather", "bone", "skin", "rubber" }),
		new WeaponSubType("chain whip", 	43, 800, 1.0f, 0.12f, "Chain whip is is whip that has chain links instead of the usual leather tail.", slash, null) };

	//						name		   dam,size,speed,aerod.,desc,types,mat
	private static WeaponSubType[] shields = { 
		new WeaponSubType("target shield", 	20, 1500, 1.0f, 0.2f, "",crush, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony" }),
		new WeaponSubType("buckler", 		34, 1630, 1.0f, 0.21f, "",crush, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony" }),
		new WeaponSubType("round shield", 	46, 1670, 1.0f, 0.19f, "",crush, new String[] { "ash", "balsa", "cedar", "mahogany", "ebony" }),
		new WeaponSubType("kite shield", 	60, 1740, 1.0f, 0.13f, "",crush, null),
		new WeaponSubType("full shield", 	72, 1920, 1.0f, 0.08f, "",crush, null) };
	//						name		   dam,size,speed,aerod.,desc,types,mat
	
	//WeaponSubType[][] weaponSubTypes = { noTypes, swords, daggers, axes, bludgeons, spears, polearms, flails, whips, shields };
	private static Map<WeaponType, WeaponSubType[]> weaponSubTypes = new HashMap();
	private static WeaponType[] allWeaponTypes;
	
	static {
		weaponSubTypes.put(WeaponType.NONE, noTypes);
		weaponSubTypes.put(WeaponType.SWORD, swords);
		weaponSubTypes.put(WeaponType.DAGGER, daggers);
		weaponSubTypes.put(WeaponType.AXE, axes);
		weaponSubTypes.put(WeaponType.BLUDGEON, bludgeons);
		weaponSubTypes.put(WeaponType.SPEAR, spears);
		weaponSubTypes.put(WeaponType.POLEARM, polearms);
		weaponSubTypes.put(WeaponType.FLAIL, flails);
		weaponSubTypes.put(WeaponType.WHIP, whips);
		weaponSubTypes.put(WeaponType.SHIELD, shields);
		
		EnumSet<WeaponType> allTypes = EnumSet.allOf(WeaponType.class);
		allWeaponTypes = new WeaponType[allTypes.size()];
		
		int i = 0;
		for(WeaponType type : allTypes) {
			allWeaponTypes[i++] = type;
		}
	}
	
	String vokaalit = "aeyoui";

	protected static WeaponFactory _instance = new WeaponFactory();	

	protected WeaponFactory() {}

	public static WeaponFactory getInstance() { return _instance; }

	public Item create(Description desc)
	{
		return create(desc.materials, desc.weapontypes, desc.craftmanship);
	}

	public Item create(String[] materials, WeaponType[] weapontypes, int craftmanship)
	{

		int dicenum;
		WeaponType type;
		Material mat;
		String matId = "antimatter";
		String weaponName;
		String shortDesc;
		String shortDescAlias;
		String longDesc;

		// randomoidaan materiaali, tyyppi ja subtype
		if(weapontypes != null)
		{
			dicenum = ( Math.abs( dice.nextInt() ) % weapontypes.length );
			type = weapontypes[dicenum];
		}
		else
		{
			dicenum = ( Math.abs( dice.nextInt() ) % (weaponSubTypes.size()-1) );
			type = allWeaponTypes[dicenum + 1];
		}

		dicenum = ( Math.abs( dice.nextInt() ) % weaponSubTypes.get(type).length );
		WeaponSubType subType = weaponSubTypes.get(type)[dicenum];
		weaponName = subType.name;

		if(materials != null)
		{
			dicenum = Dice.random(materials.length)-1;
			matId = materials[dicenum];
			mat = MaterialFactory.createMaterial(materials[dicenum]);		
		}
		else
		{
			dicenum = Dice.random(subType.materials.length)-1;
			matId = subType.materials[dicenum];
			mat = MaterialFactory.createMaterial(subType.materials[dicenum]);	
		}

		// luodaan descriptionit
		if(vokaalit.indexOf(mat.getName().charAt(0)) != -1)
		{
			shortDesc = "an "+mat.getName()+" "+weaponName;		
		}
		else
		{
			shortDesc = "a "+mat.getName()+" "+weaponName;
		}

		shortDescAlias = mat.getName()+" "+weaponName;

		if(subType.longDesc.length() == 0)
			longDesc = ""; 
		else
			longDesc = subType.longDesc + " ";
		
		longDesc = longDesc + "This "+weaponName+" is made of "+mat.getName()+". It is a "+type.getWeaponTypeName()+"."; 
		
		int hitDamage = (subType.damage + mat.getDurability()) * craftmanship / 100 / 10;

		World.log("WeaponFactory: "+shortDesc);

		// Tehd��n se ase sitten
		FactoryWeapon weapon = new FactoryWeapon();
		weapon.setDescription(shortDesc);
		weapon.setLongDescription(longDesc);
		weapon.setName(type.getWeaponTypeName());
		weapon.addAlias(shortDesc);
		weapon.addAlias(shortDescAlias);
		weapon.addAlias(weaponName);
		weapon.setMaterial(matId);
		weapon.setWeaponType(type);
		weapon.setSize(subType.size);
		weapon.setSpeed(subType.speed);
		weapon.setDamageType(subType.damageType);
		weapon.addDp(mat.getDurability()*subType.size);
		if(type == WeaponType.SHIELD)
		{
			weapon.setDefensiveValue(hitDamage);
			weapon.setHitDamage((int) ((hitDamage/4) * ((1.0f - subType.aerodynamicity)/2 + 0.5f)));
			weapon.setProjectileClass((int) ((hitDamage/4) * subType.aerodynamicity));
		}
		else
		{
			weapon.setDefensiveValue((hitDamage/4));
			weapon.setHitDamage((int) ((hitDamage) * ((1.0f - subType.aerodynamicity)/2 + 0.5f)));
			weapon.setProjectileClass((int) (hitDamage * subType.aerodynamicity));			
		}
		return weapon;
	}	
	
}
