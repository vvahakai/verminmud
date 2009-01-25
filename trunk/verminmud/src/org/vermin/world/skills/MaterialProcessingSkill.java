package org.vermin.world.skills;

import java.util.Iterator;
import java.util.Vector;

import org.vermin.mudlib.DefaultItemImpl;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.DummyLiving;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.MagicItem;
import org.vermin.mudlib.SkillType;
import org.vermin.mudlib.SkillTypes;
import org.vermin.mudlib.SkillUsageContext;
import org.vermin.mudlib.Types;
import org.vermin.mudlib.skills.BaseSkill;
import org.vermin.world.items.MaterialChunk;

public class MaterialProcessingSkill extends BaseSkill {

	public String getName() {
		return "material processing";
	}

	public int getTickCount() {
		return 3+Dice.random(3);
	}

	public SkillType[] getTypes() {
		return new SkillType[] {SkillTypes.AREA};
	}
	
	public void use(SkillUsageContext suc) {
		
		int success = suc.getSkillSuccess();
		
		if(success < 0) {
			suc.getActor().notice("You are unable to process the materials.");
		}
		else {
			
			suc.getActor().notice("You process the materials successfully.");
			Vector chunks = new Vector();
			Vector removables = new Vector();
			Iterator it = suc.getActor().getParent().findByType(Types.ITEM);
			
			while(it.hasNext()) {
				boolean found = false;
				Item item = (Item) it.next();
				int size = (item.getSize() * suc.getActor().getSkill(item.getMaterial().getType().getMasterySkill())) / 100;				
				if (!item.tryTake(DummyLiving.it) || item instanceof MagicItem)
					continue;
				removables.add(item);
				for(Object o : chunks) {
					MaterialChunk existingChunk = (MaterialChunk) o;
					if(existingChunk.getMaterial() == item.getMaterial()) {
						existingChunk.setSize(existingChunk.getSize()+size);
						if(item instanceof MaterialChunk) {
							MaterialChunk tempitem = (MaterialChunk) item;
							if (tempitem.getPurity() < existingChunk.getPurity()) {
								existingChunk.setPurity(tempitem.getPurity());
							}
						}
						else existingChunk.setPurity((int) (existingChunk.getPurity()*0.9));

						found = true;
					}
				}
				if (!found) {
					MaterialChunk chunk = new MaterialChunk();
					chunk.setMaterial(item.getMaterial().getName());
					chunk.setSize(size);
					chunk.setDescription("a chunk of "+item.getMaterial().getName());
					chunk.setLongDescription("a chunk of "+item.getMaterial().getName()+".");
					chunk.setName("chunk");
					chunk.addAlias(item.getMaterial().getName());
					if(item instanceof MaterialChunk) {
						MaterialChunk tempitem = (MaterialChunk) item;
						chunk.setPurity(tempitem.getPurity());
					}
					else chunk.setPurity(suc.getSkillSuccess());
					chunks.add(chunk);
				}
			}
			for(Object o : removables) {
				DefaultItemImpl item = (DefaultItemImpl) o;
				suc.getActor().getParent().remove(item);
			}
			for(Object o : chunks) {
				DefaultItemImpl item = (DefaultItemImpl) o;
				suc.getActor().getParent().add(item);
			}
		}
	}

}
