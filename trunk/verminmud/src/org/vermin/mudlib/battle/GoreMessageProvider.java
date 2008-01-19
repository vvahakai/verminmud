package org.vermin.mudlib.battle;

/**
 * GoreMessageProvider.java
 *
 * Implementations of <code>GoreMessageProvider</code>
 * will provide both violence describing messages and a
 * number of flags specifying what assumptions are made of
 * the suffering individual to produce more graphical
 * description.
 *
 * Clients of <code>GoreMessageProvider</code>s (usually
 * implementations of <code>Race</code>) should use these
 * flags to select the instances that produce appropriate
 * messages for them.
 *
 * Clients may install message providers independently,
 * or use the <code>GoreMessageProviderFactory</code>
 * to locate the set of message providers that fall
 * whithin the clients accepted flag set.
 *
 * The latter approach is recommended, as this will allow
 * the client to automatically access new appropriate providers
 * when somebody gets around to implement them.
 */
 
import java.util.EnumSet;

import org.vermin.mudlib.Living;
 
public interface GoreMessageProvider {

   public boolean hasProperty(GoreProperty property);
   
   public boolean hasProperties(EnumSet<GoreProperty> properties);
   
   public EnumSet<GoreProperty> getProperties();
   
   public String getAttackerGoreMessage(Living who, Attack attack, int hitLocation);
    
   public String getSubjectGoreMessage(Living who, Attack attack, int hitLocation);
}
