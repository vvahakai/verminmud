/* Bank.java
	13.9.2003 VV / Council 4
	
	the VerminBank.
	
*/
package org.vermin.world.rooms;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.vermin.mudlib.CentralBank;
import org.vermin.mudlib.DefaultRoomImpl;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Money;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.World;
import org.vermin.util.Print;

public class Bank extends DefaultRoomImpl
{

	private transient CentralBank cb;

	public Bank()
	{
		cb = (CentralBank) CentralBank.getInstance();
		setDescription("Bank");
		setLongDescription("This is an office of the VerminBank.\nAvailable commands: deposit <num> [type], withdraw <num>, balance, transactions [count]");
	}

	public boolean action(Living who, Vector cmd) {
		if(!(who instanceof org.vermin.mudlib.DefaultPlayerImpl))
			return false;

		Player p;
		if(who instanceof Player) {
			p = (Player) who;
		}
		else {
			return false;
		}

		String command = (String) cmd.get(0);
		
		if(command.equalsIgnoreCase("deposit")) {
			if(cmd.size() >= 2)
			{
				String amount = Print.vectorToString(cmd, " ", 1);
				String type = "gold";
				if(cmd.size() >= 3)
					type = Print.vectorToString(cmd, " ", 2);
				depositCash(p, amount, type);
				return true;
			}
			who.notice("Deposit what?");
			return true;		
		}
		else if(command.equalsIgnoreCase("withdraw")) {
			if(cmd.size() >= 2)
			{
				String amount = Print.vectorToString(cmd, " ", 1);
				String type = "gold";
				if(cmd.size() >= 3)
					type = Print.vectorToString(cmd, " ", 2);
				withdrawCash(p, amount, type);
				return true;
			}
			who.notice("Withdraw what?");
			return true;		
		}
		else if(command.equalsIgnoreCase("transfer")) {
			if(cmd.size() >= 3)
			{
				String targetPlayer = Print.vectorToString(cmd, " ", 1);

				String amount = Print.vectorToString(cmd, " ", 2);
				String type = "gold";
				if(cmd.size() >= 4)
					type = Print.vectorToString(cmd, " ", 3);
				transferCash(p, targetPlayer, amount, type);
				return true;
			}
			who.notice("Transfer what?");
			return true;		
		}
		else if(command.equalsIgnoreCase("balance")) {
			balanceCheck(p);
			return true;
		}
		else if(command.equalsIgnoreCase("transactions")) {
			if(cmd.size() >= 2)
			{
				String count = Print.vectorToString(cmd, " ", 1);
				transactionCheck(p, count);
			}
			else
			{
				transactionCheck(p, "10");
			}
			return true;
		} else {
			return false;
		}
	}	

	protected String time() {
		GregorianCalendar gc = new GregorianCalendar();
		int h, m, d, mo, y;
		h = gc.get(Calendar.HOUR_OF_DAY);
		m = gc.get(Calendar.MINUTE);
      d = gc.get(Calendar.DAY_OF_MONTH);
      mo = gc.get(Calendar.MONTH) + 1;
      y = gc.get(Calendar.YEAR);

		return "["+d+"."+mo+"."+y+" "+h+":"+
         (m<10
          ? "0"+Integer.toString(m)
          : Integer.toString(m)) +
         "]";
	}

	public boolean depositCash(Player who, String amount, String type)
	{			
		World.log("Starting bank money deposit at time:"+System.currentTimeMillis());		
		if(!cb.hasAccount(who))
		{
			cb.createAccount(who);
			who.notice("You create a new account.");
		}
		
		Money playerCash = who.getMoney();

		CentralBank.Account acc = cb.getAccount(who);
		Money transferedCash = playerCash.move(amount, acc.getValue());

		if(transferedCash.getValue() == 0)
			who.notice("You don't have enough cash.");
		else
		{
			acc.transactions.add(new String(time() + ": Deposit of " + transferedCash.getWorth() + "."));
			who.notice("You deposit cash worth " + transferedCash.getWorth() + ".");
		}
		World.log("Bank money deposit ready at time:"+System.currentTimeMillis());		
		return true;
	}

	public boolean transferCash(Player who, String targetPlayer, String amount, String type)
	{
		if(!cb.hasAccount(targetPlayer))
		{
			who.notice("There is no account by that name.\n");
			return false;
		}
		targetPlayer = Print.capitalize(targetPlayer);

		CentralBank.Account source = cb.getAccount(who);
		CentralBank.Account target = cb.getAccount(targetPlayer);

		Money transferedCash = source.getValue().move(amount, target.getValue());

		if(transferedCash.getValue() == 0)
			who.notice("You don't have enough cash.");
		else
		{
			source.transactions.add(new String(time() + ": Transfer of " + transferedCash.getWorth() + " to " + targetPlayer + "."));
			target.transactions.add(new String(time() + ": Transfer of " + transferedCash.getWorth() + " from " + who.getName() + "."));
			who.notice("You transfer cash worth " + transferedCash.getWorth() + " to " + targetPlayer + ".");
		}
		return true;
	}

	public boolean withdrawCash(Player who, String amount, String type)
	{			
		if(!cb.hasAccount(who))
		{
			who.notice("You don't have an account.");
			return false;
		}

		Money playerCash = who.getMoney();

		CentralBank.Account acc = cb.getAccount(who);

		Money transferedCash = acc.getValue().move(amount, playerCash);

		if(transferedCash.getValue() == 0)
			who.notice("You don't have enough cash in your account.");
		else
		{
			acc.transactions.add(new String(time() + ": Withdraw of " + transferedCash.getWorth() + "."));
			who.notice("You withdraw cash worth " + transferedCash.getWorth() + ".");
		}

		if(acc.getValue().getValue() == 0)
		{
			who.notice("Your account has been removed.");
			cb.removeAccount(who);
		}
		return true;
	}

	public boolean balanceCheck(Player who)
	{
		if(!cb.hasAccount(who))
		{
			who.notice("You don't have an account.");
			return false;
		}	

		CentralBank.Account acc = cb.getAccount(who);
		who.notice("Your account balance is " + acc.getValue().getWorth() + ".");
		return true;
	}

	public boolean transactionCheck(Player who, String count)
	{

		int lines = 0;

		try
		{
			lines = Integer.parseInt(count);
		} catch(NumberFormatException nfe) {
			who.notice("Syntax: transactions <num>");
			return false;
		}
		
		if(!cb.hasAccount(who))
		{
			who.notice("You don't have an account.");
			return false;
		}	

		CentralBank.Account acc = cb.getAccount(who);

		if(lines >= acc.transactions.size())
			lines = acc.transactions.size();

		who.notice("Your last " + lines + " transactions.");
		for(int i=0; i < lines; i++)
		{
			String line = (String) acc.transactions.get(i);
			who.notice(line);
		}
		return true;
	}
}
