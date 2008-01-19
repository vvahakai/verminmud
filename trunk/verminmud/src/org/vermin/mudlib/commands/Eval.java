package org.vermin.mudlib.commands;


import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import jscheme.JScheme;

import org.vermin.mudlib.Living;
import org.vermin.mudlib.RegexCommand;
import org.vermin.mudlib.Wizard;

public class Eval extends RegexCommand {
	
	private HashMap<Living, JScheme> evaluators = new HashMap();
	
	public String[] getDispatchConfiguration() {
		return new String[] {
			"eval (.*) => schemeEval(actor, 1)"
		};
	}

	public void schemeEval(Living who, String txt) {

		if(!(who instanceof Wizard)) {
			who.notice("Only wizards can use this command.");
		} else {
			JScheme js = evaluators.get(who);
			if(js == null) {
				js = new JScheme();
				evaluators.put(who, js);
				
				/* Load user scheme */
				File f = new File("objects/players/"+who.getName().toLowerCase()+".user.scm");
				if(f.exists()) {
					who.notice("[eval] Found your user Scheme file, trying to load it now.");
					try {
						js.load(new FileReader(f));
						who.notice("[eval] Loading ok.");
					} catch(Exception e) {
						who.notice("[eval] Exception while loading user Scheme: "+e.getMessage());
					}
				}
			}
			
			Object res = null;
			
			if(txt.startsWith("toplevel "))
				res = js.eval(txt.substring("toplevel ".length()));
			else 
				res = js.call((jscheme.SchemeProcedure) js.eval("(lambda (this) "+ txt +")"), who);
			
			
			if(res != null)
				who.notice(res.toString());
		}
	}
}
