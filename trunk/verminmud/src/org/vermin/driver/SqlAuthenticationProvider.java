/*
 * Created on 19.2.2005
 *
 */
package org.vermin.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.vermin.util.Print;

/**
 * @author tadex
 *
 */
public class SqlAuthenticationProvider implements AuthenticationProvider {

    private Connection connection;
    
    public SqlAuthenticationProvider(String jdbcURL, String user, String password, String jdbcDriver) throws SQLException {

            try {
                Class.forName(jdbcDriver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(jdbcURL, user, password);

    }
    
    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#remove(java.lang.String)
     */
    public void remove(String name) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM players WHERE name = ?");
            stmt.setString(1, Print.capitalize(name));
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException se) {
            logException(se);
        }

    }

    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#add(java.lang.String, java.lang.String, java.lang.String)
     */
    public void add(String name, String clearText, String id) {

        try {
        	name = Print.capitalize(name);
            PreparedStatement stmt = 
                connection.prepareStatement("INSERT INTO players (name, password, id) "+
                "VALUES(?, ?, ?)");
            
            stmt.setString(1, name);
            stmt.setString(2, clearText);
            stmt.setString(3, id);
            
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException se) {
            logException(se);
        }

    }

    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#authenticate(java.lang.String, java.lang.String)
     */
    public String authenticate(String name, String password) {
        try {
        	name = Print.capitalize(name);
            PreparedStatement stmt =
                connection.prepareStatement("SELECT id FROM players WHERE name = ? AND password = ?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            String id = rs.next() ? rs.getString(1) : null;
            
            rs.close();
            stmt.close();
            return id;
        } catch(SQLException se) {
            logException(se);
            return null;
        }   
    }

    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#contains(java.lang.String)
     */
    public boolean contains(String name) {
        try {
        	name = Print.capitalize(name);
            PreparedStatement stmt =
                connection.prepareStatement("SELECT COUNT(*) FROM players WHERE name = ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            boolean res = rs.next() && rs.getInt(1) > 0;
            
            rs.close();
            stmt.close();
            
            return res;
        } catch(SQLException se) {
            logException(se);
            return false;
        }
        
    }

    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#getIdForRecord(java.lang.String)
     */
    public String getIdForRecord(String name) {
        try {
        	name = Print.capitalize(name);
            PreparedStatement stmt =
                connection.prepareStatement("SELECT id FROM players WHERE name = ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            String id = rs.next() ? rs.getString(1) : null;
            
            rs.close();
            stmt.close();
            return id;
        } catch(SQLException se) {
            logException(se);
            return null;
        }   
    }

    public void changePassword(String name, String clearText) {

        try {
        	name = Print.capitalize(name);
            PreparedStatement stmt = 
                connection.prepareStatement("UPDATE players SET password = ? WHERE name = ?");
            
            stmt.setString(1, clearText);
            stmt.setString(2, name);
            
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException se) {
            logException(se);
        }

    }

    private void logException(SQLException se) {
        System.out.println("SqlAuthenticationProvider, SQL exception: "+se.getMessage());
        se.printStackTrace(System.out);
    }

	public Iterable<String> getPlayerNames() {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT name FROM players");
		
			ResultSet rs = stmt.executeQuery();
			ArrayList<String> al = new ArrayList<String>();
			while(rs.next())
				al.add(rs.getString(1));
		
			return al;
		} catch(SQLException se) {
			logException(se);
			return null;
		}
	}
    
}
