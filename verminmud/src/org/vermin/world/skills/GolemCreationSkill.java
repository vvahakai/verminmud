/*
 * Created on 29.3.2005  MV
 */
package org.vermin.world.skills;

import java.util.HashMap;
import java.util.Iterator;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Material;
import org.vermin.mudlib.MaterialFactory;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.World;
import org.vermin.mudlib.minion.DefaultMinionImpl;
import org.vermin.mudlib.minion.Leash;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.items.GolemWorkbench;
import org.vermin.world.items.MaterialChunk;
import org.vermin.world.items.Script;
import org.vermin.world.races.GolemRace;
import org.vermin.world.races.HumanRace;


/**
 * @author Matti V�h�kainu
 *
 */
public class GolemCreationSkill extends BaseSkill {
	
	int maxhp = 0;
	int str = 0;
	int dex = 0;
	int physres = 0;
	int magres = 0;
	int acidres = 0;
	int coldres = 0;
	int elecres = 0;
	int fireres = 0;
	
	String [] effectNames = new String [] { "maxhp", "str", "dex", "physres", "magres", "acidres", "coldres", "elecres", "fireres" }; 
		
	public GolemCreationSkill() {
		
		Iterator it = MaterialFactory.materialList();
		
		while (it.hasNext()) {
			Material mat  = (Material) it.next();
			materialEffects.put(mat.getName(), new GolemEffect(effectNames[mat.getDurability()%9], ((int) mat.getValue() % 10)+1));
		}
				
		effects1.put("kil", new GolemEffect("str", 50));
		effects1.put("snap", new GolemEffect("maxhp", 1200));
		effects1.put("dakka", new GolemEffect("dex", 50));
		effects1.put("bada", new GolemEffect("physres", 10));
		effects1.put("knick", new GolemEffect("magres", 10));
		effects1.put("plim", new GolemEffect("fireres", 20));
		
		effects2.put("zap", new GolemEffect("magres", 10));
		effects2.put("crackle", new GolemEffect("maxhp", 1200));
		effects2.put("lakka", new GolemEffect("physres", 10));
		effects2.put("dum", new GolemEffect("str", 50));
		effects2.put("grip", new GolemEffect("dex", 50));
		effects2.put("bang", new GolemEffect("acidres", 20));
		effects2.put("plam", new GolemEffect("coldres", 20));
		
		effects3.put("bang", new GolemEffect("maxhp", 1200));
		effects3.put("pop", new GolemEffect("dex", 50));
		effects3.put("boom", new GolemEffect("physres", 10));
		effects3.put("rak", new GolemEffect("str", 50));
		effects3.put("zop", new GolemEffect("magres", 10));
		effects3.put("zap", new GolemEffect("acidres", 20));
		effects3.put("plom", new GolemEffect("elecres", 20));
	}
	
	protected SkillType[] skillTypes = new SkillType[] { SkillTypes.SUMMONING };
	
	private class GolemEffect {
		String type = null;
		int value = 0;
		
		public GolemEffect(String type, int value) {
			this.type = type;
			this.value = value;
		}
		
		public void apply() {
			if(type.equals("str")) str += value;
			if(type.equals("maxhp")) maxhp += value;
			if(type.equals("dex")) dex += value;
			if(type.equals("physres")) physres += value;
			if(type.equals("magres")) magres += value;	
			if(type.equals("acidres")) acidres += value;
		}
		
		public void apply(int size, int purity) {
			size = Math.min(size, 27000);
			size = size * purity / 100;
			if(type.equals("str")) str += value * size/2000;
			if(type.equals("maxhp")) maxhp += value * size/500;
			if(type.equals("dex")) dex += value * size/2000;
			if(type.equals("physres")) physres += value * size/10000;
			if(type.equals("magres")) magres += value * size/7000;	
			if(type.equals("acidres")) acidres += value * size/7000;	
		}
	}
	
	HashMap<String,GolemEffect> effects1 = new HashMap<String,GolemEffect>();
	HashMap<String,GolemEffect> effects2 = new HashMap<String,GolemEffect>();
	HashMap<String,GolemEffect> effects3 = new HashMap<String,GolemEffect>();	
	HashMap<String,GolemEffect> materialEffects = new HashMap<String,GolemEffect>();	
	
	public SkillType[] getTypes() { return skillTypes; }
	
	public String getName() {
		return "golem creation";
	}
	public int getTickCount() {
		return 5+Dice.random(4);
	}
	
	public int getCost(SkillUsageContext suc) {
		return 250;
	}
	
	public boolean processChunks(SkillUsageContext suc) {
		MObject obj = suc.getActor().getRoom().findByNameAndType("golem workbench", Types.TYPE_ITEM);
		if (obj instanceof GolemWorkbench) {
			GolemWorkbench bench = (GolemWorkbench) obj;
			Iterator it = bench.findByType(Types.TYPE_ITEM);
			while (it.hasNext()) {
				Item item  = (Item) it.next();
				if(!(item instanceof MaterialChunk))
					continue;
				MaterialChunk chunk = (MaterialChunk) item;
				World.log("GolemCreationSkill: applying golem effect for "+chunk.getMaterial().getName());				
				materialEffects.get(chunk.getMaterial().getName()).apply(chunk.getSize(), chunk.getPurity());
			}
			bench.emptyBench();
			return true;
		}
		else {
			suc.getActor().notice("You need a golem workbench to create a golem");
			return false;
		}
	}
	
	public void powerWords(SkillUsageContext suc) {
		Living who = suc.getActor();
		Script s = (Script) who.findByName("_golem_perkele_script");
		who.remove(s);
		
		if(s == null) {
			who.notice("Saatanan paska, käytä scriptiä!");
		}
		else {
			String word1 = s.getPowerword1();
			String word2 = s.getPowerword2();
			String word3 = s.getPowerword3();	
			
			effects1.get(word1).apply();
			effects2.get(word2).apply();
			effects3.get(word3).apply();
		}
	}
	
	public int minionMax(){
		return Leash.getMinionSize();
	}
	
	public void onTick(SkillUsageContext suc) {
		Living who = suc.getActor();
		if (Dice.random() > who.getSkill("golem creation") && suc.getTicksLeft() <= 5) {
			who.notice("You lose your link to the ether and the golem body is left cold and lifeless.");
			suc.abort();
		}
		else if (suc.getTicksLeft() == 5) {
			who.notice("You assemble the golem body on the workbench from the prepared materials.");
		}	
		else if (suc.getTicksLeft() == 4) {
			who.notice("A scent of burning incense fills the room as you begin to chant ancient words.");
		}
		else if (suc.getTicksLeft() == 3) {
			who.notice("The atmosphere starts to crackle with static and shadows seem to dance at the edge of your sight.");
		}
		else if (suc.getTicksLeft() == 2) {
			who.notice("A wisp of smoke from the candles creates strange images around the lifeless body of the golem. The air above the golem body begins to shimmer just noticeably.");
		}
		else if (suc.getTicksLeft() == 1) {
			who.notice("The golem body seems to be engulfed by some mystical aura. Suddenly you notice an inner fire light up in the golem's eye sockets. Then the golem begins to rise slowly from the workbench.");
		}
	}
	
	public void use(SkillUsageContext suc) {
		int golemSuccess = suc.getSkillSuccess();
		if (golemSuccess < 0) {
			suc.getActor().notice("You fail the skill.");
		}
		else if (minionMax() > 2) {
			suc.getActor().notice("You fail to summon more minions.");
		}
		else {
			if(!this.processChunks(suc))
				return;			
			suc.getActor().notice("You have succesfully created a golem servant.");
			this.powerWords(suc);
			DefaultMinionImpl golem = new DefaultMinionImpl(suc.getActor());
			golem.setName("golem");
			golem.setLongDescription("This massive construct has been assembled to serve "+suc.getActor().getName()+" and "+suc.getActor().getName()+" only.");
			golem.setDescription("a massive golem construct");
			golem.setFollowing(true);
			golem.setRace(new GolemRace(HumanRace.getInstance()));
			golem.setPhysicalConstitution(150);
			golem.setPhysicalStrength(50 + str);
			golem.setPhysicalDexterity(50 + dex);
			golem.setResistance(Damage.Type.MAGICAL, magres);
			golem.setResistance(Damage.Type.PHYSICAL, physres);
			golem.setResistance(Damage.Type.ACID, acidres);
			golem.setResistance(Damage.Type.FIRE, fireres);
			golem.setResistance(Damage.Type.COLD, coldres);
			golem.setResistance(Damage.Type.ELECTRIC, elecres);
			golem.setMaxHp(1000 + maxhp);
			golem.setHp(1000 + maxhp);
			golem.addCommand("report");
			golem.addCommand("get");
			golem.addCommand("take");			
			golem.addCommand("drop");
			golem.addCommand("put");
			golem.addCommand("eq");			
			golem.addCommand("look");
			golem.addCommand("loot");
			golem.addCommand("stats");			
			suc.getActor().getParent().add(golem);
			Leash l = (Leash) suc.getActor().findByNameAndType("_minion_leash", Types.TYPE_ITEM);
			if(l == null) {
				l = new Leash();
				suc.getActor().add(l);				
			}	
			golem.start();
			l.addMinion(golem);
		}
		
		
	}

}
