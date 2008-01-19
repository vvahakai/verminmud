/*
 * Created on 19.2.2005
 *
 */
package org.vermin.driver;

/**
 * @author tadex
 *
 */
public interface AuthenticationProvider {
    
    /**
     * Remove the record for the given name from
     * this authentication provider.
     * 
     * @param name the name of the record to remove
     */
    public void remove(String name);

    /**
     * Add a new authentication record for the given name,
     * password and id combination.
     * 
     * @param name the name of the record
     * @param clearText the password in clear text
     * @param id the id associated with the record
     */
    public void add(String name, String clearText, String id);

    /**
     * Authenticates player and returns id on success or null on failure.
     * 
     * @param name the given username
     * @param password the given password
     */
    public String authenticate(String name, String password);

    /**
     * Check if this authentication provider contains a record
     * for the given username. The check is done case insensitively.
     * 
     * @param name the name to check for
     * @return true if a record was found, false otherwise
     */
    public boolean contains(String name);
    
    /**
     * Get the id for the given username.
     * 
     * @param name the username
     * @return the id for the username or null
     */
    public String getIdForRecord(String name);
    
    /**
     * Change password for the given record.
     * 
     * @param name the name of the record
     * @param password the new password in clear text
     */
     public void changePassword(String name, String password);
     
     /**
      * Return names of all players this authentication provider knows about.
      */
     public Iterable<String> getPlayerNames();
      

}