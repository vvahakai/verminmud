/*
 * Created on 15.1.2005
 *
 */
package org.vermin.mudlib;

/**
 * An object that handles command prompting.
 * @author tadex
 *
 */
public interface Prompt {

    /**
     * Generate a prompt for the given player.
     * @param who the player
     * @return the prompt message
     */
    public String prompt(Player who);
}
