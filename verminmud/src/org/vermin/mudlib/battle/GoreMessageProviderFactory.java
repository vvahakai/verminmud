package org.vermin.mudlib.battle;

import java.util.EnumSet;
import java.util.Vector;

public class GoreMessageProviderFactory {
   
   private static GoreMessageProviderFactory _instance = new GoreMessageProviderFactory();
   
   private Vector<GoreMessageProvider> providers = new Vector<GoreMessageProvider>();
   
   private GoreMessageProviderFactory() {
      providers.add(BloodGoreMessageProvider.getInstance());
      providers.add(MammalGoreMessageProvider.getInstance());
      providers.add(InsectGoreMessageProvider.getInstance());
	  providers.add(SpeakGoreMessageProvider.getInstance());
	  providers.add(BoneGoreMessageProvider.getInstance());
	  providers.add(MechanicalGoreMessageProvider.getInstance());
	}
   
   public static GoreMessageProviderFactory getInstance() {
      return _instance;
   }
   
   /**
    * Searches and returns an <code>GoreMessageProvider</code> which matches
    * the given <code>EnumSet<GoreProperty></code>. See <code>GoreProperty</code>
    * for the available flag definintions. This method actually returns
    * several providers transparently wrapped in a <code>CompositeGoreMessageProvider</code>.
    * If you want greater control over the providers used, use <code>findProviders</code>,
    * or install each desired provider manually.
    *
    * @param properties an <code>EnumSet</code> specifying the set of flags to find a provider for
    * @return  a <code>GoreMessageProvider</code> appropriate for the flags 
    */
   public GoreMessageProvider findProvider(EnumSet<GoreProperty> properties) {
      GoreMessageProvider[] providerArray = findProviders(properties);
      if(providerArray.length == 0) {
    	  providerArray = new GoreMessageProvider[] { FallbackGoreMessageProvider.getInstance() };
      }
      return new CompositeGoreMessageProvider(providerArray);
   }
   
   /**
    * Searches and returns an array of <code>GoreMessageProvider</code>s
    * which match the given array of flags. See <code>GoreMessageProvider</code>
    * for the available flag definintions. Note: unlike with <code>findProvider</code>,
    * this method can return an empty array.
    *
    * @param properties an array specifying the set of flags to find providers for
    * @return  an array containing the <code>GoreMessageProvider</code>s found
    */
   public GoreMessageProvider[] findProviders(EnumSet<GoreProperty> properties) {
      Vector<GoreMessageProvider> qualifiedProviders = new Vector<GoreMessageProvider>();
      
      for(GoreMessageProvider provider : providers) {
         if(properties.containsAll(provider.getProperties())) {
            qualifiedProviders.add(provider);
         }
      }
      return qualifiedProviders.toArray(new GoreMessageProvider[qualifiedProviders.size()]);
   }
}
