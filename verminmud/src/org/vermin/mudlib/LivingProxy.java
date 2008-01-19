/* BehaviourProxy.java
	14.1.2002	Tatu Tarvainen / Council 4
	
	Route method call to multiple Living objects.
*/
package org.vermin.mudlib;

import java.util.LinkedList;

public class LivingProxy implements Behaviour {
	
	private Iterable<Living> living;
	
	public LivingProxy(Iterable<Living> v) {
		living = v;
	}
	
	private Iterable<Living> living() {
		LinkedList<Living> l = new LinkedList();
		for(Living c : living) l.add(c);
		return l;
	}
	
	public void wields(final Living who, final Item what) {
		for(Living l : living()) {
			if(l == who) continue;
			l.wields(who,what);
		}
		who.wields(who, what);
	}

	public void unwields(final Living who, final Item what) {
		for(Living l : living()) {
			if(l == who) continue;
			l.unwields(who,what);
		}
		who.unwields(who, what);
	}
	
	public void arrives(final Living who) {
		for(Living l : living()) {
			if(l == who) continue;
			l.arrives(who);
		}
		who.arrives(who);
	}
		
	public void arrives(final Living who, final Exit from) {
		for(Living l : living()) {
			if(l == who) continue;
			l.arrives(who,from);
		}
		who.arrives(who, from);
	}
	
	public void afterArrives(final Living who) {
		for(Living l : living()) {
			if(l == who) continue;
			l.afterArrives(who);
		}
		who.afterArrives(who);
	}
	
	public void leaves(final Living who,final  Exit to) {
		for(Living l : living()) {
			if(l == who) continue;
			l.leaves(who,to);
		}
		who.leaves(who, to);
	}

	public void leaves(final Living who) {
		for(Living l : living()) {
			if(l == who) continue;
			l.leaves(who);
		}
		who.leaves(who);
	}
	
	/* Notify that who starts using a skill. */
	public void startsUsing(final Living who, final Skill skill) {
		for(Living l : living()) {
			if(l == who) continue;
			l.startsUsing(who, skill);
		}
		who.startsUsing(who, skill);
	}

	public void says(final Living who, final String what) {
		who.says(who, what);
		for(Living l : living()) {
			if(l == who) continue;
			l.says(who,what);
		}
	}
	
	/* Notice (say some text to) this Living */
	public void notice(final String text) {
		for(Living l : living()) {
			l.notice(text);
		}
	}

	
	/* Notify that someone takes something. */
	public void takes(final Living who, final Item what) {
		for(Living l : living()){
			if(l == who) continue;
			l.takes(who,what);
		}
		who.takes(who, what);
	}

	
	/* Notify that someone drops something. */
	public void drops(final Living who, final Item what) {
		for(Living l : living()) {
			if(l == who) continue;
			l.drops(who,what);
		}
		who.drops(who, what);
	}

	public void dies(final Living victim, final Living killer) {
		for(Living l : living()) {
			World.log("LivingProxy, calling dies for: "+l);
			if(l == victim) continue;
			l.dies(victim, killer);
		}
		victim.dies(victim, killer);
	}
	
	public void asks(final Living asker, Living target, final String subject) {
		for(Living l : living()) {
			if(l == asker) continue;
			l.asks(asker, target, subject);
		}
		asker.asks(asker, target, subject);
	}

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#onBattleTick(org.vermin.mudlib.Living)
     */
    public void onBattleTick(Living who) {
        // NOT APPLICABLE FOR MULTIPLE PROPAGATION
    }

    /* (non-Javadoc)
     * @see org.vermin.mudlib.Behaviour#onRegenTick(org.vermin.mudlib.Living)
     */
    public void onRegenTick(Living who) {
        // NOT APPLICABLE FOR MULTIPLE PROPAGATION
    }

	public void command(Object... args) {
		//	NOT APPLICABLE FOR MULTIPLE PROPAGATION
	}

    
}
