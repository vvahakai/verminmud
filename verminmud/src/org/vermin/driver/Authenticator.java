/* Authenticator.java
	13.1.2002	Tatu Tarvainen / Council 4
	
	Authenticates players and returns their
	id number upon successful authentication.
*/
package org.vermin.driver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.vermin.util.Print;

public class Authenticator implements AuthenticationProvider {

	/* inner class for password records */
	public class PasswordRecord {
		private String password;
		private String id;
		
		public PasswordRecord(String password, String id) {
			this.password = password;
			this.id = id;
		}
		
		public boolean matches(String password) {
			if(this.password.equals(password))
				return true;
			else
				return false;
		}
		
		public String getId() { return id;	}
		public String getPassword() { return password; }
		public void setId(String id) { this.id = id; }
		public void setPassword(String password) { this.password = password; }
	}
	
	/* The password file name */
	private String passwd;
	
	/* hashtable of (String name, PasswordRecord pw) as (key, value) */
	private Hashtable record;
	
	public Authenticator(String passwd) {
		record = new Hashtable();
		this.passwd = passwd;
		try {
			readPasswordList();
	 	} catch(IOException e) {
			System.err.println("[Authenticator] Unable to initialize service. Can't read password file. "+e.getMessage());
		}
	}
	
	private void readPasswordList() throws IOException {
		BufferedReader in = 
			new BufferedReader(new FileReader(passwd));
		
		String line=null;		
		line = in.readLine();
		
		while(line != null) {
			StringTokenizer st = new StringTokenizer(line, ":");
			
			if(st.countTokens() != 3) {
				System.out.println("[Authenticator] WARNING: Incorrect number of tokens in password entry, skipped..");
			} else {
				String name, password;
				String id;
				
				name = st.nextToken();
				password = st.nextToken();
				id = st.nextToken();
				
				record.put(name, new PasswordRecord(password, id));
			}
			
			line = in.readLine();
		}
	}
	
	public void savePasswordList() throws IOException {
		PrintWriter out =
			new PrintWriter(new FileWriter(passwd));
		
		Enumeration keys = record.keys();
		while(keys.hasMoreElements()) {
			String name = (String)keys.nextElement();
			PasswordRecord pw = (PasswordRecord)record.get(name);
			out.println(name + ":" + pw.getPassword() + ":" + pw.getId());
		}
		
		out.close();
	}
	
	public void remove(String name) {
		record.remove(Print.capitalize(name));
	}
	
	public void add(String name, String clearText, String id) {
		record.put(Print.capitalize(name), new PasswordRecord(crypt(clearText), id));
        try {
            savePasswordList();
        } catch(IOException e) {}
	}
	
	/* Authenticates player and returns id on success,
		0 on failure. */
	public String authenticate(String name, String password) {
		name = Print.capitalize(name);
		
		System.out.println("authenticate: name=\""+name+"\"");
		PasswordRecord pw = (PasswordRecord) record.get(name);
		
		if(pw == null) System.out.println("authenticate: password record null.");
		if(pw == null) return null;
		
		if(pw.matches( crypt(password) )) {
			return pw.getId();
		}
		System.out.println("authenticate: wrong password...");
		return null;
	}

	public boolean contains(String name) {
		if(record.get(name) != null)
			return true;
		else
			return false;
	}
	
	public String crypt(String cleartext) {
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException nsa) {
			System.err.println("[Authenticator] NO HASH ALGORITHM. USING CLEAR TEXT!!!.");
			return cleartext;
		}
		
		md.update(cleartext.getBytes());
		StringBuffer crypted = new StringBuffer();
		
		byte[] c = md.digest();
		for(int i=0; i<c.length; i++) {
			String hex = Integer.toHexString( (new Integer(c[i])).intValue() );
			
			if(hex.length() == 1)
				hex = "0"+hex;
			crypted.append(hex);	
		}
		
		return crypted.toString();
		
	}
	

    /* (non-Javadoc)
     * @see org.vermin.driver.AuthenticationProvider#getIdForRecord(java.lang.String)
     */
    public String getIdForRecord(String name) {
        return ((PasswordRecord) record.get(name)).getId();
    }
    
	public PasswordRecord findByName(String name) {
		return (PasswordRecord) record.get(Print.capitalize(name));
	}
    
    public void changePassword(String name, String password) {
        PasswordRecord pr = findByName(name);
        pr.password = crypt(password);
        try { savePasswordList(); }
        catch(IOException ioe) {
            throw new RuntimeException("Unable to save password list.");
        }
    }

	public Iterable<String> getPlayerNames() {
		return record.keySet();
	}
    
}
