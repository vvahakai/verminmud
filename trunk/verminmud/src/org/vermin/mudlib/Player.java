/**
 * Interface for Players
 *
 */
package org.vermin.mudlib;

import org.vermin.wicca.ClientInputHandler;
import org.vermin.wicca.ClientOutput;

import java.util.Vector;
import java.util.HashSet;
import java.util.Date;

public interface Player extends Living, ClientInputHandler {
	
    /**
     * Set the player's prompt.
     * This is meant to be used in special
     * command handling modes (other than normal game).
     * If the prompt is <code>null</code> the player's
     * configured default will be shown.
     * 
     * @param pmt the prompt to use or <code>null</code> for default
     */
    public void setPrompt(Prompt pmt);
    
    /**
     * Set the player's plan or <code>null</code> to clear it.
     * 
     * @param plan the plan text or <code>null</code>
     */
    public void setPlan(String plan);
    
    /**
     * Get the player's plan.
     * 
     * @return the plan text
     */
    public String getPlan();
    
    /**
     * Returns the player's preference value for given key.
     * 
     * @param key  preference key
     * @return  preference value
     */
    public Object getPreference(String key);
    
    /**
     * Add an available title.
     * The player can select his title from the
     * available titles.
     * 
     * A default message is displayed informing the
     * player of the availablility of the title.
     * 
     *  @param title the title to add
     */
	public void addAvailableTitle(String title);
	
	/**
	 * Add an available title with a custom notification message.
	 * 
	 * The custom message overrides the default info message. If
	 * the custom message is null, no message is given to the player.
	 * 
	 * @param title  the title to add
	 * @param msg  the info message to display
	 */
	public void addAvailableTitle(String title, String msg);

    /**
     * Check if the player wants verbose messages. 
     * @return true if this option is selected, false otherwise
     */
	public boolean isVerbose();
       
    /**
     * Add an <code>ActionHandler</code> for the given
     * command prefix.
     * 
     * @param cmd the command prefix
     * @param handler the handler
     */
	public void addHandler(String cmd, ActionHandler<MObject> handler);
    
    /**
     * Remove the given <code>ActionHandler</code>.
     * 
     * @param handler the handler to remove
     */
	public void removeHandler(ActionHandler<MObject> handler);
    
	/**
     * Returns the handler associated with the given command prefix. 
     * @param cmd the command prefix
     * @return the <code>ActionHandler</code> or null
	 */
	public ActionHandler<MObject> getHandler(String cmd);
    
    /**
     * Add a catch all handler for commands.
     * The handler will be called for all command prefixes
     * regardless of other added handlers.
     * 
     * @param handler the handler
     */
	public void handleAll(ActionHandler<MObject> handler);
    
    /**
     * Remove the catch all handler.
     */
	public void unhandleAll();
    
    /**
     * Returns the player's level.
     * @return the level
     */
	public int getLevel();
    
    /**
     * Sets the player's level.
     * @param l the new level
     */
	public void setLevel(int l);
    
    /**
     * Returns the player's surname.
     * @return the surname
     */
   public String getSurname();
   
   /**
    * Returns the player's selected title.
    * @return the selected title
    */
   public String getTitle();
   
   /**
    * Set the id of the starting room.
    * 
    * @param id the id
    */
	public void setStartingRoom(String id);
    
    /**
     * Returns the id of the starting room.
     * @return the id
     */
	public String getStartingRoom();
	
    /**
     * Set the player's experience.
     * @param exp experience
     */
	public void setExperience(long exp);
    
    /**
     * Set the player's total experience.
     * @param exp total experience
     */
	public void setTotalExperience(long exp);
    
    /**
     * Returns the player's experience.
     * @return experience
     */
	public long getExperience();
    
    /**
     * Returns the player's total experience.
     * @return total experience
     */
	public long getTotalExperience();

    /**
     * 
     * @return
     */
	public ClientOutput getClientOutput();

    /**
     * 
     * @param style
     */
	public void addAvailableBattleStyle(BattleStyle style);
    
    /**
     * 
     * @return
     */
	public Vector<BattleStyle> listAvailableBattleStyles();

	// reincarnation support
    /**
     * 
     */
	public void clearSkills();
    
    /**
     * 
     *
     */
	public void clearAvailableTitles();
    
    /**
     * 
     *
     */
	public void clearAvailableBattleStyles();

    /**
     * 
     * @param name
     * @param list
     */
	public void addFriend(String name, String list);
    
    /**
     * 
     * @param list
     * @return
     */
	public HashSet getFriends(String list);
    
    /**
     * 
     * @param name
     * @param list
     * @return
     */
	public boolean removeFriend(String name, String list);
    
    /**
     * 
     * @return
     */
	public Date getCreated();

    /**
     * 
     * @return
     */
	public String getBestSoloKillDescription();
    
    /**
     * 
     * @return
     */
	public long getBestSoloKillExperience();
    
    /**
     * 
     * @return
     */
    public String getBestPartyKillDescription();
    
    /**
     * 
     * @return
     */
	public long getBestPartyKillExperience();
    
    /**
     * 
     * @param kill
     */
	public void setBestSoloKill(Living kill);
    
    /**
     * 
     * @param kill
     */
	public void setBestPartyKill(Living kill);

    /**
     * 
     * @return
     */
	public int getExploreCount();
    
    /**
     * 
     *
     */
	public void increaseExploreCount();
    
    /**
     * 
     *
     */
	public void startSession();

	public String getPromptString();
	
	public int getFreeStatPoints();
	
	public int getUsedStatPoints(Stat s);
	
	public int getStatPointCost(Stat s);
	
	public void setFreeStatPoints(int points);
	
	/**
	 * Clears used stat points.
	 * 
	 */
	public void clearUsedStatPoints();
	
	public void useStatPoint(Stat s);
	
	public int getMaxStat(Stat s);
    
}
