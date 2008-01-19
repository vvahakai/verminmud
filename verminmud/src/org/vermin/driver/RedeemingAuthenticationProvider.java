/*
 * Created on 19.2.2005
 *
 */
package org.vermin.driver;

/**
 * A hack that allows to use a new and an old authentication
 * provider in parallel.
 * All authentication data matched in the old provider is automatically
 * added to the new provider and removed from the old.
 * 
 * @author tadex
 *
 */
public class RedeemingAuthenticationProvider implements AuthenticationProvider {

    private AuthenticationProvider provider1;
    private AuthenticationProvider provider2;
    
    public RedeemingAuthenticationProvider(AuthenticationProvider newProvider, 
            AuthenticationProvider oldProvider) {
        provider1 = newProvider;
        provider2 = oldProvider;
    }
    
    /**
     * @param name
     * @param clearText
     * @param id
     */
    public void add(String name, String clearText, String id) {
        provider1.add(name, clearText, id);
    }
    /**
     * @param name
     * @param password
     * @return
     */
    public String authenticate(String name, String password) {
        String res = provider1.authenticate(name, password);
        if(res == null) {
            res = provider2.authenticate(name, password);
            if(res != null) {
                provider1.add(name, password, res);
                provider2.remove(name);
            }
        }
        return res;
    }
    /**
     * @param name
     * @return true if name is contained
     */
    public boolean contains(String name) {
        return provider1.contains(name) || provider2.contains(name);
    }
    /**
     * @param name
     * @return the id
     */
    public String getIdForRecord(String name) {
        String res = provider1.getIdForRecord(name);
        if(res == null)
            res = provider2.getIdForRecord(name);
        return res;
    }
    /**
     * @param name
     */
    public void remove(String name) {
        provider1.remove(name);
    }
    
    public void changePassword(String name, String password) {
        if(provider1.contains(name))
            provider1.changePassword(name, password);
        else if(provider2.contains(name))
            provider2.changePassword(name, password);
    }

	public Iterable<String> getPlayerNames() {
		throw new UnsupportedOperationException("not supported");
	}
    
    
}
