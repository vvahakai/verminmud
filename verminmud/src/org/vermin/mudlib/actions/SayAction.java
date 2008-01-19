package org.vermin.mudlib.actions;

import java.util.Map;
import org.vermin.mudlib.*;

import java.util.regex.*;

public class SayAction implements ScriptAction {

	private static Pattern substitute = Pattern.compile("%[^%]+%");
	
	private String message;

    /**
     * Run the say action. 
     * Arguments for this action are:
     * <ul>
     *  <li>the target of the say (Living) or null</li>
     *  <li>the room in which the actor is in</li>
     * </ul>
     * This action always returns <code>null</code>.
     * 
     * @param self the object doing the saying
     * @param args the arguments as described above
     * @return null
     */
	public Object run(Object self, Object ... args) {
		Room room = ((Living) self).getRoom();
		room.say((Living) self, prepareMessage(args));
        return null;
	}
	
	private String prepareMessage(Object ... args) {
		
		Matcher m = substitute.matcher(message);
		StringBuffer sb = new StringBuffer();

		while(m.find()) {
			m.appendReplacement(sb, getReplacement(message.substring(m.start()+1,m.end()-1), args));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String getReplacement(String key, Object ... args) {

        if(key.equalsIgnoreCase("targetName")) {
            return ((MObject)args[0]).getName();
        } else return key;
    }

	public static void main(String[] args) {
		
		SayAction sa = new SayAction();
		sa.message = "Terve %nimi%, hyv‰‰ %aika%!";

		Map a = new java.util.HashMap();
		a.put("nimi", "Tatu");
		a.put("aika", "iltaa");
		System.out.println(sa.prepareMessage(a));

	}
}
