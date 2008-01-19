/*
 * Created on 29.1.2005
 *
 */
package org.vermin.mudlib;


/**
 * @author tadex
 *
 */
public interface Money {
    public enum Coin {
        
        GOLD(10000),
        SILVER(5000),
        BRONZE(500),
        COPPER(100),
        BRASS(10),
        TIN(1);
        
        // Value of one unit measured in tin coins
        int value;
        
        Coin(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    public static final Coin[] COIN_TYPES = { 
        Coin.GOLD, Coin.SILVER,
        Coin.BRONZE, Coin.COPPER,
        Coin.BRASS, Coin.TIN
    };
    
    /**
     * Check if the given string is a valid representation of money.
     * Money is represented in string form as "<amount> <currency>".
     * 
     * @param what the description
     * @return true if valid, false otherwise
     */
    public boolean isMoneyDescription(String what);
    
    
    /**
     * Add coins of a given currency.
     * 
     * @param type the currency 
     * @param amnt the amount to add
     */
    public void add(Coin type, long amnt);
    
    /**
     * Substract coins of a given currency.
     * 
     * @param type the currency 
     * @param amnt the amount to substract
     */
    public void substract(Coin type, long amnt);
    
    /**
     * Move coins of the given description to the target.
     * The description is of the form "<amount> <currency>".
     * 
     * @param what the description of what to move
     * @param target the target
     * @return a <code>Money</code> which has the amount moved
     */
    public Money move(String what, Money target);
    
    /**
     * Move all money to the target.
     * 
     * @param target the target
     * @return a <code>Money</code> which has the amount moved
     */
    public Money moveAll(Money target);
    
    /**
     * Returns the amount of coins of the given currency.
     * @param type
     * @return
     */
    public long getCoinsOf(Coin type);
    
    /**
     * Returns a human readable description of the
     * coins contained.
     * 
     * @return
     */
    public String getDescription();
    
    /**
     * Checks if the value is zero.
     * 
     * @return
     */
    public boolean isEmpty();
    
    /**
     * Returns the value in the given currency.
     * 
     * @param type the currency type
     * @return the worth in the given type
     */
    public long getValueAs(Coin type);
    
    /**
     * Describes the total worth in the largest
     * possible currency.
     * 
     * @return a human readable description of worth
     */
    public String getWorth();
    
    /**
     * Returns the value in tin coins.
     * @return the value
     */
    public long getValue();
    
    /**
     * Add to the total value in the largest currencies
     * possible.
     * 
     * @param amount the amount in tin coins
     */
    public void addValue(long value);
    
    /**
     * Add to the total value in the largest currencies
     * (up to a given type) possible.
     * 
     * @param amount the amount in tin coins
     * @param the largest currency type to try adding in
     * 
     */
    public void addValueUpToType(long value, Coin largestType);
	
    /**
     * Substract from the total value in smallest
     * currencies possible. If there is not enough small
     * currency a larger currency is taken and change is given
     * back in smaller currency.
     * 
     * @param amount the amount in tin coins
     */
    public void substractValue(long value);
}
