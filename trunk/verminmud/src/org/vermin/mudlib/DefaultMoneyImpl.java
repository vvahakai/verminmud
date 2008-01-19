/* MoneyItem.java
 * 18.5.2002 TTa & VV
 */
package org.vermin.mudlib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultMoneyImpl implements Money {
	
	protected long[] amount;
	
	public DefaultMoneyImpl() {
		amount = new long[COIN_TYPES.length];
	}
	
	public DefaultMoneyImpl(String money) {
		this.amount = new long[COIN_TYPES.length];
		
		if(money == null || money.length() == 0)
			return;
		
		money = money.trim();
		if(money.endsWith("coins"))
			money = money.substring(0, money.length()-5);
		
		String[] moneys = money.split(",");
		for(String m : moneys) {
			m = m.trim();
			
			Matcher match = MONEY_REGEX.matcher(m);
			if(!match.matches())
				throw new IllegalArgumentException("Money description is not valid: "+money);
			
			long howMuch;
			Coin type = getCoinType(match.group(2));
			
			if(match.group(1).equals("all"))
				throw new IllegalArgumentException("Amount 'all' is not valid.");
			else
				howMuch = Long.parseLong(match.group(1));
			
			add(type, howMuch);
		}
	}
	
	public boolean isMoneyDescription(String what) {
		return MONEY_REGEX.matcher(what).matches();
	}
	
	public void add(Coin type, long amnt) {
		amount[type.ordinal()] += amnt;
	}
	
	public void substract(Coin type, long amnt) {
		amount[type.ordinal()] -= amnt;
		if(amount[type.ordinal()] < 0) amount[type.ordinal()] = 0;
	}
	
	protected Coin getCoinType(String type) {
		return Coin.valueOf(type.trim().toUpperCase());
	}
	
	protected static final Pattern MONEY_REGEX =
		Pattern.compile("^\\s*(\\d+|all)\\s+(gold|silver|bronze|copper|brass|tin)\\s*$");
	
	public Money move(String what, Money target) {		
		
		Money result = new DefaultMoneyImpl();
		if(what.equalsIgnoreCase("coins") || what.equalsIgnoreCase("all"))
			return moveAll(target);
		
		Matcher m = MONEY_REGEX.matcher(what);
		if(!m.matches())
			return result;
		
		long howMuch;
		Coin type = getCoinType(m.group(2));
		
		if(m.group(1).equals("all"))
			howMuch = getCoinsOf(type);
		else
			howMuch = Long.parseLong(m.group(1));
		
		if(type == null) {
			System.out.println("No such coin type: "+m.group(2));
			return result;
		}
		
		if(howMuch > 0 && type != null) {
			if(amount[type.ordinal()] >= howMuch) {
				amount[type.ordinal()] -= howMuch;
				target.add(type, howMuch);
				result.add(type, howMuch);
			} else {
				target.add(type, amount[type.ordinal()]);
				result.add(type, amount[type.ordinal()]);
				amount[type.ordinal()] = 0;
			}
		}		
		return result;
	}
	
	public Money moveAll(Money target) {
		DefaultMoneyImpl result = new DefaultMoneyImpl();
		for(int i=0; i<COIN_TYPES.length; i++) {
			if(amount[i] > 0) {
				target.add(COIN_TYPES[i], amount[i]);
				result.add(COIN_TYPES[i], amount[i]);
				amount[i] = 0;
			}
		}
		return result;
	}
	
	public long getCoinsOf(Coin type) {
		return amount[type.ordinal()];
	}
	
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		boolean first=true;
		for(int i=0; i<COIN_TYPES.length; i++) {
			if(amount[i] > 0) {
				if(!first) sb.append(", ");
				sb.append(amount[i]+" "+COIN_TYPES[i].toString().toLowerCase());
				first = false;
			}
		}
		if(first)
			return "An empty pile of coins";
		
		sb.append(" coins");
		return sb.toString();
	}
	
	
	public boolean isEmpty() {
		for(int i=0; i<COIN_TYPES.length; i++) {
			if(amount[i] > 0) return false;
		}
		return true;
	}
	
	public long getValue() {
		long ret = 0;
		for(int i=0; i<COIN_TYPES.length; i++) {
			ret += COIN_TYPES[i].getValue() * amount[i];
		}
		return ret;
	}
	
	public long getValueAs(Coin type)
	{
		long ret = 0;
		for(int i=0; i<COIN_TYPES.length; i++) {
			ret += COIN_TYPES[i].getValue() * amount[i];
		}
		return (ret / COIN_TYPES[type.ordinal()].getValue());
	}
	
	/* Returns a string description of the total worth. */
	public String getWorth() {
		long value = getValue();
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for(int i=0; i<COIN_TYPES.length; i++) {
			long amount=0;
			
			amount = value / COIN_TYPES[i].getValue();
			value = value % COIN_TYPES[i].getValue();
			
			if(amount == 0)
				continue;
			
			if(!first)
				sb.append(", ");
			sb.append(amount + " "+COIN_TYPES[i].toString().toLowerCase());
			first = false;
		}
		return sb.toString();
	}
	
	public void substractValue(long value) {
		
		for(int i=COIN_TYPES.length-1; i > -1; i--) {
			
			if(value < amount[i] * COIN_TYPES[i].getValue()) {
				if(value % COIN_TYPES[i].getValue() == 0)
				{
					amount[i] -= value / COIN_TYPES[i].getValue();
				}
				else
				{
					amount[i] -= (value / COIN_TYPES[i].getValue()) + 1;
					addValue(value % COIN_TYPES[i].getValue());
				}
				break;
			} else {
				value -= amount[i] * COIN_TYPES[i].getValue();
				amount[i] = 0;
			}
		}
	}
	
	public void addValue(long value) {
		for(int i=0; i<COIN_TYPES.length; i++) {
			long acoins = value/COIN_TYPES[i].getValue();
			amount[i] += acoins;
			value -= acoins*COIN_TYPES[i].getValue();
			
			if(value < 1) break;
		}
	}
	
	public void addValueUpToType(long value, Coin maxType) {
		amount[maxType.ordinal()] = value / maxType.getValue();
		addValue(value % maxType.getValue());	   
	}	
	
	public static Money createMoneyFromDescription(String desc) {		
		String[] types = desc.split(",");
		DefaultMoneyImpl m = new DefaultMoneyImpl();
		for(String type : types) {
			type = type.trim();
			Matcher match = MONEY_REGEX.matcher(type);			
			
			if(!match.matches()) {				
				Pattern moneyRandom = Pattern.compile("^\\s*(\\d+)-(\\d+)\\s+(gold|silver|bronze|copper|brass|tin)\\s*$");
				Matcher randomMatch = moneyRandom.matcher(type);
				
				if(!randomMatch.matches()) {
					World.log("Invalid money description: "+type);
					continue;
				}
				Coin cType = m.getCoinType(randomMatch.group(3));
				long min = Long.parseLong(randomMatch.group(1));
				long max = Long.parseLong(randomMatch.group(2));
				long amount;
				if(min == max)
					amount = min;
				if(min > max) 
					amount = max+Dice.random((int) (min-max)); // Too lazy to swap
				else
					amount = min+Dice.random((int) (max-min));
				m.add(cType, amount);				
			} else {
				Coin cType = m.getCoinType(match.group(2));
				long howMuch = Long.parseLong(match.group(1));
				m.add(cType, howMuch);
			}
		}
		return m;
	}
}
