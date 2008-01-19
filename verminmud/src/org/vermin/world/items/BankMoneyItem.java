/* BankMoneyItem.java
 * 18.5.2002 TTa & VV
 */
package org.vermin.world.items;

import java.util.regex.Matcher;

import org.vermin.mudlib.*;

public class BankMoneyItem extends DefaultMoneyImpl {

   public void add(Coin type, long amnt) {
	   	addValue(amnt * COIN_TYPES[type.ordinal()].getValue());
   }

   public void addValue(long value) {
	   	value += getValue();
		emptyCoins();
		super.addValue(value);
   }   
   
   protected void emptyCoins() {
		for(int i=0; i<COIN_TYPES.length; i++) {
				amount[i] = 0;
		}
   }
  
   public Money move(String what, Money target) {
	   
	   Matcher m = MONEY_REGEX.matcher(what);
	   
	   if(m.matches()) {	//change all money to wanted type
		   Coin type = getCoinType(m.group(2));	
		   if (type != null) {
			   long value = getValue();
			   emptyCoins();
			   addValueUpToType(value, type);
		   }
	   }
	   
	   return super.move(what, target);	   	   
   }

}
