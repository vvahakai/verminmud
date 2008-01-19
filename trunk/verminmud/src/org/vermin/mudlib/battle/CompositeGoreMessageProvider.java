package org.vermin.mudlib.battle;

import java.util.EnumSet;

import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Living;

public class CompositeGoreMessageProvider implements GoreMessageProvider {
   
   private GoreMessageProvider[] providers;
   private EnumSet<GoreProperty> properties;
   
   public CompositeGoreMessageProvider(GoreMessageProvider[] providers) {
      this.providers = providers;
      
      properties = EnumSet.noneOf(GoreProperty.class);
      
      for(GoreMessageProvider provider : providers) {
         properties.addAll(provider.getProperties());
      }
   }
   

   public boolean hasProperty(GoreProperty property) {
      return properties.contains(property);
   }
   
   public boolean hasProperties(EnumSet<GoreProperty> properties) {
      return properties.containsAll(this.properties);
   }
   
   public EnumSet<GoreProperty> getProperties() {
		return properties;
	}
   
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation) {
      return providers[Dice.rng.nextInt(providers.length)].getAttackerGoreMessage(who, attack, hitLocation);
   }
    
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation) {
      return providers[Dice.rng.nextInt(providers.length)].getSubjectGoreMessage(who, attack, hitLocation);
   }
}
