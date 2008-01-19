/*
 * Created on 1.9.2006
 */
package org.vermin.mudlib;

import org.vermin.util.DelayedAction;

/**
 * This class is alike to the RegexCommand class, but is used
 * to present players with one-time choices. After the player
 * has made the choice or a specified timeout occures, RegexChoice
 * automatically removes itself from the player.
 * 
 * @author Jaakko Pohjamo
 */
public abstract class RegexChoice implements ActionHandler<MObject>, RegexDispatchTarget {

	private RegexDispatch dispatch;
	private final Player chooser;
	private boolean chosen;
	
	/**
	 * Present target player with a choice defined by
	 * the regex dispatch configuration. Note that
	 * choices are always made with commad syntax
	 * 'choose [option]', so your configuration should
	 * be something like 'choose colour tv => colorTvChosen(actor)'.
	 * 
	 * @param chooser  the player presented with a choice
	 */
	public RegexChoice(Player chooser) {
		this(chooser, 0);
	}
	
	/**
	 * Present target player with a choice, with an additional
	 * timeout. After <code>timeout</code> regen ticks, the
	 * <code>timeout</code> method hook is invoked, and after
	 * that this choice is automatically removed from the player.
	 * You should use the hook to inform the player(s) involved
	 * that the choice has timed out.
	 *
	 * @param chooser  the player presented with a choice
	 * @param timeout  number of regen ticks until timeout
	 */
	public RegexChoice(final Player chooser, int timeout) {
		dispatch = new RegexDispatch(this);
		this.chooser = chooser;
		chooser.addHandler("choose", this);
		chosen = false;
		if(timeout > 0) {
			new DelayedAction(timeout, Tick.REGEN) {
				public void action() {
					if(!chosen) {
						timeout(chooser);
						chooser.removeHandler(RegexChoice.this);
					}
				}
			};
		}
	}
	
	public boolean action(MObject who, String params) {
		if(!dispatch.dispatch((Living) who, params)) {
			usage((Living) who);
		} else {
			chosen = true;
			chooser.removeHandler(this);
		}
        return true;
	}
	
	/**
	 * If the choice did not match the dispatch configuration,
	 * this hook is called. It should be overridden to notify
	 * the player about the syntax used for the choice.
	 * 
	 * @param who  the player to notify
	 */
	public void usage(Living who) {}
	
	/**
	 * This hook is invoked if the choice times out. If a
	 * timeout is specified, this method should be overridden
	 * to inform the player that the choice timed out.
	 *
	 * @param who  the player whose choice timed out
	 */
	public void timeout(Living who) {}
}
