package org.vermin.driver;

import java.sql.Connection;
import java.sql.DriverManager;

public class BootstrapDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		try {
			String driver = args[0];
			String url = args[1];
		
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url);
		
			con.createStatement().executeUpdate("CREATE TABLE players (name varchar(32), password varchar(32), id varchar(255))");
			con.createStatement().executeUpdate("CREATE TABLE explore (player varchar(255), room varchar(255))");
			con.createStatement().executeUpdate("CREATE TABLE plaque (type varchar(32), player varchar(32), value bigint)");
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
