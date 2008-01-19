/* GuildItem.java
	16.2.2002
	
*/

package org.vermin.world.items;

import org.vermin.mudlib.*;
import org.vermin.util.Print;

public class GuildItem extends DefaultItemImpl 
{
	protected int level;
	/**
	 * Contains a human readable name for the guild.
	 */
	protected String guildName;
	
	public GuildItem() {
		level = 0;
	}
	
	public void start() {
		if(guildName == null) {
			if(getName().indexOf('_') != -1) {
				guildName = Print.capitalize(getName().substring(0, getName().length()-13).replace('_', ' '));
			}
		}
	}
	
	public boolean isVisible() {
		return false;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean tryDrop(Living who, MObject where) {
		return false;
	}
	
	public boolean tryTake(Living who) {
		return false;
	}
	
	public String getGuildName() {
		return guildName;
	}
	
	public void subDp(int amount) {
		// guild items should not preferably be destroyed in combat
	}

	/**
	 * Get the guild master object, or null if no such thing exists.
	 * 
	 * @return the master object or null
	 */
	public Object getMasterObject() {
		return null;
	}
}
