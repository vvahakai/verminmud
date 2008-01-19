/* CentralBank.java
	6.12.2003 VV / Council 4
	
	the Vermin Central Bank.
	
*/
package org.vermin.mudlib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CentralBank { 

	private static PreparedStatement updateAccount = null;
	private static synchronized void updateAccount(String playerID, String amount) {
		try {
			Connection c = World.getDatabaseConnection();
			if(updateAccount == null) {
				updateAccount = c.prepareStatement("UPDATE players SET bank=? WHERE id=?");
			}
			updateAccount.setString(1, amount);
			updateAccount.setString(2, playerID);
			updateAccount.execute();
		} catch(SQLException se) {
			World.log("Unable to update bank account for player: "+playerID+", messaage: "+se.getMessage());
		}
	}
	
	private static PreparedStatement fetchAccountValue = null;
	private static synchronized String fetchAccountValue(String playerID) {
		try {
			Connection c = World.getDatabaseConnection();
			if(fetchAccountValue == null) {
				fetchAccountValue = c.prepareStatement("SELECT bank FROM players WHERE id=?");
			}
			fetchAccountValue.setString(1, playerID);
			ResultSet rs = fetchAccountValue.executeQuery();
			String v = "";
			if(rs.next())
				v = rs.getString(1);
			rs.close();
			return v;
			
		} catch(SQLException se) {
			World.log("Unable to fetch bank account value for player: "+playerID+", message: "+se.getMessage());
			return "";
		}
	}
	
	// private HashMap<String,Account> accounts;

	private static CentralBank _instance;

	public static class Account
	{
		public Vector transactions;
		private String playerID;
		
		Account(String pid) {
			playerID = pid;
			transactions = new Vector();
		}
		
		public void setValue(DefaultMoneyImpl value) {
			updateAccount(playerID, value.getDescription());
			
		}
		public DefaultMoneyImpl getValue() {
			return new DefaultMoneyImpl(fetchAccountValue(playerID)) {
				private void store() {
					updateAccount(playerID, getDescription());
				}
				@Override
				public void substractValue(long value) {
					super.substractValue(value);
					store();
				}
				@Override
				public void substract(Coin type, long amnt) {
					super.substract(type, amnt);
					store();
				}
				@Override
				public Money moveAll(Money target) {
					Money m = super.moveAll(target);
					store();
					return m;
				}
				@Override
				public Money move(String what, Money target) {
					Money m = super.move(what, target);
					store();
					return m;
				}
				@Override
				public void addValueUpToType(long value, Coin maxType) {
					super.addValueUpToType(value, maxType);
					store();
				}
				@Override
				public void addValue(long value) {
					super.addValue(value);
					store();
				}
				@Override
				public void add(Coin type, long amnt) {
					super.add(type, amnt);
					store();
				}
			};
		}
	}

	private CentralBank()	 {}

	public static synchronized CentralBank getInstance() { 
		if (_instance == null) {
			_instance = new CentralBank();
		}
		return _instance; 
	}

	public Account getAccount(Player who) {
		return new Account(who.getId());
	}

	public Account getAccount(String who) {
		return new Account(World.getPlayerIdByName(who));
	}

	public void createAccount(Player who) {
	/*	Account acc = new Account(who.getId());
		acc.setValue(new BankMoneyItem());
		acc.transactions = new Vector();
		accounts.put(who.getName(), acc); */
	}

	public long getValue(Player who) {
		return getAccount(who).getValue().getValue();
	}

	public boolean hasAccount(Player who) {
		return true;
	}

	public boolean hasAccount(String who) {
		return true;
	}

	public void removeAccount(Player who) {
		updateAccount(who.getId(), "");
	}

}
