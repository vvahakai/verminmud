/*
 * Created on 15.1.2005
 *
 */
package org.vermin.mudlib;

/**
 * An interface for handling actions.
 * 
 * @author tadex
 *
 */
public interface ActionHandler<T> {
    
    /**
     * Execute an action.
     * 
     * @param caller the object that initiated this action
     * @param action the action command
     * @return true if the action was understood, false otherwise
     */
    public boolean action(T caller, String action);
}
