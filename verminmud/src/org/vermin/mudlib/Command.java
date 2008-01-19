/* Command.java
	16.2.2002
	
	A player command.
*/

package org.vermin.mudlib;


public interface Command extends ActionHandler<Living> {
    
    /**
     * Execute this command. The return value is 
     * currently ignored.
     * 
     * @param who the player who is executing the command
     * @param params the command string
     * @return
     */
	public boolean action(Living who, String params);
}
