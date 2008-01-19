package org.vermin.mudlib;



/**
 * Class that dispatches commands to subclasses
 * by matching regular expressions and automatically
 * extracting parameters.
 *
 */
public abstract class RegexCommand implements Command, RegexDispatchTarget {

	private RegexDispatch dispatch;

	public RegexCommand() {
		dispatch = new RegexDispatch(this);
	}
	

	public boolean action(Living who, String command) {
		if(!dispatch.dispatch(who, command))
			usage(who);
        return false;
	}

	public void usage(Living who) {}
	
}

