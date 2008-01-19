/*
 * Created on 2.9.2006
 */
package org.vermin.mudlib.battle;

import java.util.EnumSet;

import org.vermin.mudlib.Damage;
import org.vermin.mudlib.Living;

public class FallbackGoreMessageProvider extends AbstractGoreMessageProvider {

	   private static FallbackGoreMessageProvider _instance = new FallbackGoreMessageProvider();
	   
	   protected FallbackGoreMessageProvider() {
	      super(EnumSet.noneOf(GoreProperty.class));
	   }
	   
	   public static FallbackGoreMessageProvider getInstance() {
		      return _instance;
	   }

	   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
		  Damage damage = attack.getMaxDamage();
		  int maxHp = Math.max(who.getMaxHp(), 1);
		   int percentage = (int) ((damage.damage * 100) / maxHp);			

		   return "No wounding description available. About "+percentage+"% of max hp was removed.";
	}

	public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
	      Damage damage = attack.getMaxDamage();
	      int maxHp = Math.max(who.getMaxHp(), 1);
		   int percentage = (int) ((damage.damage * 100) / maxHp);
		   return "No wounding description available. About "+percentage+"% of max hp was removed.";
	}

}
