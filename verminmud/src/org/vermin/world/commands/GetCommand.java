/* GetCommand.java
   23.3.2002	Tatu Tarvainen
	
	Implements a generic get and put command for players.

	Syntax:									
	
    Getting items:
        loot
		(get|take) <items> [from <container>]
        
    Putting items:
        (put|drop) <items> [(to|in) <container>]
        
		
	<items> = <itemName> [, <items>]
	<itemName> = ...name of item...
               | all
               | all ...name of item...
               
	
	<container>: Name of a container item.
	

    Examples:
    
      get all from bag
      drop 6 gold, book 
      take all pint, sword
      put sword 1, sword 4 in chest
      
      
*/
package org.vermin.world.commands;

import org.vermin.mudlib.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class GetCommand extends RegexCommand
{
    
    public String[] getDispatchConfiguration() {
        return new String[] {
                "(get|take) (.*) from (.*) => move(actor, 2, item 3, actor)",
                "(get|take) (.*) => move(actor, 2, room, actor)",
                "(put|drop) (.*) (in|to) (.*) => move(actor, 2, actor, item 4)", 
                "(put|drop) (.*) => move(actor, 2, actor, room)",
                "loot => loot(actor)"
        };
    }
                

    public void loot(Living who) {
        Iterator it = who.getRoom().findByType(Types.TYPE_ITEM);
        while(it.hasNext()) {
            MObject item = (MObject) it.next();
            if(item.isAlias("corpse")) {
                move(who, "all", item, who);
            }
        }
    }
    
    public void move(Living who, String itemNames, MObject from, MObject to) {
    	Vector got = new Vector();
    	
    	String[] items = itemNames.split(" *, *");
    	
    	for(String itemName : items) {
    		
    		itemName = itemName.trim();
    		
    		if(from instanceof Purse && to instanceof Purse) {
    			// Move money
    			
    			Money mi = ((Purse) from).getMoney();
    			if(mi.isMoneyDescription(itemName) || 
    					itemName.equalsIgnoreCase("coins") ||
    					itemName.equalsIgnoreCase("all")) {
    				Money result = mi.move(itemName, ((Purse)to).getMoney());
    				if(!result.isEmpty())
    					got.add(result.getDescription());
    				if(!itemName.equalsIgnoreCase("all"))
    					continue;
    			}
    		} 
    		if(from instanceof Container && to instanceof Container) {
    			// Try to get as normal item
    			for(Item item : getItems((Container) from, itemName)) {
    				
    				if(((Container)to).tryAdd(item) && ((Container)from).tryRemove(item)) {
    					if((from instanceof Living && item.tryDrop(who, to)) ||
    							(!(from instanceof Living) && item.tryTake(who))) {
    						((Container)from).remove(item);
    						got.add(item.getDescription());
    						((Container)to).add(item);
    					}
    				}
    			}
    		} 
    	}
    	
    	boolean take = who==to;
    	
    	if(got.size() < 1 ) {
    		who.notice((take?"Take":"Put") + " what?");
    	}
    	else {
    		who.notice("You "+(take?"take":"put")+" "+vectorToString(got, 0, ", ")+" "+(take?"from ":"in ")+
    				(take? (from instanceof Room ? "the ground" : from.getDescription()) :
    					(to instanceof Room ? "the ground" : to.getDescription())) + ".");
	    	Iterator it = who.getRoom().findByType(Types.TYPE_LIVING);
	    	while(it.hasNext()) {
	    		Living spectator = (Living) it.next();
	    		if(spectator != who) {
	    			spectator.notice(who.getName()+" "+(take?"take":"put")+"s "+vectorToString(got, 0, ", ")+" "+(take?"from ":"in ")+
	    					(take? (from instanceof Room ? "the ground" : from.getDescription()) :
	    						(to instanceof Room ? "the ground" : to.getDescription())) + ".");
	    		}
	    	}
    	}
    	
    }
    

   protected Iterable<Item> getItems(final Container container, String item) {
       
       if(item.equalsIgnoreCase("all")) {
                      
           Iterator it = container.findByType(Types.TYPE_ITEM);
           // Copy items to an arraylist to avoid concurrent modification exception
           ArrayList al = new ArrayList();
           while(it.hasNext()) al.add(it.next());
           
           return al;
      } else {
          HashSet<Item> items = new HashSet();
          
          if(item.startsWith("all ")) {
              String alias = item.substring(4).trim();
              Iterator it = container.findByType(Types.TYPE_ITEM);
              while(it.hasNext()) {
                  Item i = (Item) it.next();
                  if(i.isAlias(alias))
                      items.add(i);
              }
          } else {
              Item i = (Item) container.findByNameAndType(item, Types.TYPE_ITEM);
              if(i != null)
                  items.add(i);
          }
          return items;
      }
   }
	
	protected String vectorToString(Vector v, int start, String combiner) {
		StringBuffer sb = new StringBuffer();
		for(int i=start; i<v.size(); i++) {
			sb.append(v.get(i).toString().toLowerCase() + (i==v.size()-1 ? "" : combiner));
		}
		return sb.toString();
	}
}
