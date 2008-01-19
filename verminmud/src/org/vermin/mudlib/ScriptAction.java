/**
 * ScriptAction.java
 * Interface for script actions.
 *
 * 20.7.2003 Tatu Tarvainen / Council 4
 */
package org.vermin.mudlib;

public interface ScriptAction {

    /**
     * Run this action.
     * 
     * @param self the object in whose context the procedure is applied
     * @param args the rest of the arguments
     * @return the value returned by the script
     */
	public Object run(Object self, Object ... args);

}
