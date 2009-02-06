/*
 * Created on 21.4.2006
 */
package org.vermin.world.behaviours;

import org.vermin.driver.AbstractPropertyProvider;
import org.vermin.mudlib.Dice;
import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Player;
import org.vermin.mudlib.behaviour.BehaviourAdapter;
import org.vermin.mudlib.LivingProperty;
import org.vermin.util.Print;

/**
 * A monster behaviour that implements a basic ambush attempt.
 * Physical strength and dexterity are tested for attempts to 
 * escape the ambush. Ambushing also implies Aggressive.
 * 
 * Internally, this behaviour adds a PropertyProvider providing
 * LivingProperty.IMMOBILIZED to the victim. The provider stays
 * until either the ambusher or victim dies, either leaves the room
 * (by teleportation for example), or an escape check succeeds.
 * Escape checks are done on each battle tick.
 * 
 * @author Jaakko Pohjamo
 */
public class AmbushBehaviour extends BehaviourAdapter {
	
	private AmbushProvider ambushProvider = new AmbushProvider();
	private boolean ambushing = false;
	private Living ambushed;
	
	private static class AmbushProvider extends AbstractPropertyProvider<LivingProperty> {
		public boolean provides(LivingProperty lp) {
			return lp == LivingProperty.IMMOBILIZED;
		}
	}
	
	public AmbushBehaviour() {
	
	}
	
 
    public void afterArrives(Living who) {
        ambush(who);
    }
    
    private synchronized void ambush(Living who) {
    	// ambush only 1 at a time,
    	// don't ambush already ambushed players or otherwise immobile ones,
    	// ambush only players
    	if(ambushing ||
    			who.provides(LivingProperty.IMMOBILIZED) ||
    			!(who instanceof Player)) { 
    		return;
    	}
        owner.addAttacker(who);

    	if(who.provides(LivingProperty.FLIGHT) && !owner.provides(LivingProperty.FLIGHT)) {
    		who.notice(Print.capitalize(owner.getDescription())+" tries to ambush you, but "+owner.getPronoun()+" cannot fly.");
    		return;
    	}
    	
    	if(testEscapeDex(who)) {
    		who.notice(Print.capitalize(owner.getDescription())+" tries to ambush you, but you nimbly avoid getting trapped.");
    		return;
    	} else if(testEscapeStr(who)) {
    		who.notice(Print.capitalize(owner.getDescription())+" tries to ambush you, but you easilly brush "+owner.getObjective()+" aside.");
    		return;
    	}
    	
    	who.notice("&B2;"+Print.capitalize(owner.getDescription())+" suddenly appears from nowhere and attacks. You have been AMBUSHED!&;");
    	ambushing = true;
    	ambushed = who;
    	
    	who.addProvider(ambushProvider);
    }
    
    private synchronized void stopAmbush() {
    	if(!ambushing) { return; }
    	ambushing = false;
    	ambushed.removeProvider(ambushProvider);
    	ambushed = null;
    }
    
    public synchronized void onBattleTick(Living who) {
    	if(!ambushing) { return; }
    	if(testEscapeDex(ambushed)) {
    		ambushed.notice("&B2;Your nimbleness allows you to break free of "+Print.capitalize(owner.getName())+"'s ambush.&;");
    		stopAmbush();
    	} else if(testEscapeStr(ambushed)) {
    		ambushed.notice("&B2;Your great strength allows you to break free of "+Print.capitalize(owner.getName())+"'s ambush.&;");
    		stopAmbush();
    	} else if(Dice.random(20) == 20) {
    		ambushed.notice("&B2;By sheer luck, you manage to break free of "+Print.capitalize(owner.getName())+"'s ambush.&;");
    		stopAmbush();
    	}
    }
    
    // if ambusher or ambushed somehow manages to leave,
    // we have to stop. anything else is just silly.
    public synchronized void leaves(Living who) { 
    	if(who == ambushed || who == owner) {
    		stopAmbush();
    	}
    }
    public synchronized void leaves(Living who, Exit e) {
    	if(who == ambushed || who == owner) {
    		stopAmbush();
    	}
    }
    public synchronized void dies(Living victim, Living killer) {
    	if(victim == owner || victim == ambushed) {
    		stopAmbush();
    	}
    }
    
    private boolean testEscapeDex(Living who) {
    	return((who.getPhysicalDexterity()+Dice.random(80)) > (owner.getPhysicalDexterity()+Dice.random(80)));
    }
    private boolean testEscapeStr(Living who) {
    	return((who.getPhysicalStrength()+Dice.random(80)) > (owner.getPhysicalStrength()+Dice.random(80)));
    }    
}
