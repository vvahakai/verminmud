/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib.quest;

import org.vermin.mudlib.Player;

/**
 * A common interface for Quests.
 * @author tadex
 *
 */
public interface Quest {
    
	/**
	 * Gets a human readable title which is used to
	 * identify this quest.
	 * 
	 * @return  a title used by players to idenfify this quest
	 */
	public String getTitle();
	
	/**
	 * Gets a short briefing which explains the background
	 * and general objectives of this quest.
	 * 
	 * @return  the briefing of this quest
	 */
	public String getDescription();
	
    /**
     * Start the given quest.
     * This should propably add a Journal to the player and
     * configure it.
     * 
     * @param who the player who is starting the quest
     */
    public void startQuest(Player who);
	
	/**
	 * Allows a player to abandon the quest.
	 * This must remove any quest related
	 * items from the player.
	 * This method may notify player of the abandoned quest.
	 * 
	 * @param who  the player who decided to abandon the quest
	 */
	public void abandonQuest(Player who);
    
    /***
     * Complete the quest. This removes quest related
     * objects from the player.
     * 
     * @param who the player who completed the quest
     */
	public void completeQuest(Player who);
	
	/**
	 * Return a human readable description of current
	 * quest objectives. The description may change
	 * over time as the quest progresses.
	 * This method is optional, and may return null if
	 * the player should not be able to see objectives.
	 * 
	 * @param player the player who is doing the quest
	 * @param journal the player's journal object
	 */
	public String describeObjectives(Player player, Journal journal);
}
