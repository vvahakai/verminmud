/**
 * TokenizedCommand.java
 * 21.9.2003 Tatu Tarvainen
 *
 * Base class to support old style (Vector params) commands.
 */
package org.vermin.mudlib;

import java.util.Vector;

public abstract class TokenizedCommand implements Command {
	
	public boolean action(Living who, String cmd) {
		String[] params = cmd.split(" +");
		Vector v = new Vector();
		for(int i=0; i<params.length; i++)
			v.add(params[i]);
		run(who, v);
        return false;
	}

	public abstract void run(Living who, Vector params);

}
