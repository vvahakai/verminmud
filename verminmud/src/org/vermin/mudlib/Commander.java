/* Commander.java
	16.2.2002
	
	A singleton pool for player command objects.
*/

package org.vermin.mudlib;

import java.util.HashMap;
import java.util.Map;

public class Commander {

	protected final static Commander _playerInstance = new Commander("common/commands/player-commands", null); 
	protected final static Commander _wizardInstance = new Commander("common/commands/wizard-commands", _playerInstance);
	
	private Map<String, Command> commands;
	private Commander delegateTo;
	
	
	protected Commander(String commandSpec, Commander delegateTo) {
		this.delegateTo = delegateTo;
		try {
			commands = (Map<String, Command>) World.get(commandSpec);
			World.log("[Commander] Loaded with "+commands.size()+" commands");
		} catch(Exception e) {
			World.log("[Commander] No player commands ("+commandSpec+")... sucks to be them.");
			e.printStackTrace();
			commands = new HashMap<String, Command>();
		}
	}
	
	public static Commander getInstance() {
		return _playerInstance;
	}
	
	public static Commander getWizardInstance() {
		return _wizardInstance;
	}
	
	public Command get(String name) {
		Command c = commands.get(name);
		return c != null 
			? c 
			: (delegateTo != null ? delegateTo.get(name) : null);
	}
	
	public void add(String name, Command cmd) {
		commands.put(name, cmd);
	}
	
	public void update(String name, Command newCmd) {
		commands.remove(name);
		commands.put(name, newCmd);
	}
	
	public static Command getCommand(String name) {
        return getInstance().get(name);
    }
    public static void loadCommandDefinitions() {
        World.unload("common/commands/player-commands");
        getInstance().commands = (Map<String, Command>) World.get("common/commands/player-commands");
    }
}
