/* IgniteCommand.java
	2.8.2003		VV
	
*/

package org.vermin.world.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vermin.mudlib.Command;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.MObject;
import org.vermin.mudlib.Types;

public class IgniteCommand implements Command {

	private static final Pattern TORCH_REGEX = Pattern.compile("^\\s*(light|ignite|douse|extinguish)\\s+(.*)$");

	public boolean action(Living who, String command) {

		Matcher m = TORCH_REGEX.matcher(command);
		if(!m.find()) {
			who.notice("Ignite what?");
		} else {
 
			MObject torch;
			torch = who.findByNameAndType(m.group(2), Types.TYPE_ITEM);

			if(torch != null) {
				torch.action(who,command);
			} else {
				who.notice("Ignite what?");
			}
		}
        return false;
	}
}
